package mud

import akka.actor.Actor
import akka.actor.ActorRef
import java.io.PrintStream
import java.io.BufferedReader
import mud.Room.GetDescription
import akka.actor.PoisonPill

class Player(name: String, out: PrintStream, in: BufferedReader) extends Actor {
  import Player._
  def receive = {
    case ProcessCommand(command) => {
      processCommand(command)
    }
    case CheckAllInput =>
      if (in.ready()) {
        val command = in.readLine()

        processCommand(command)
      }

    case PrintMessage(msg) => out.println(msg)
    case TakeItem(oitem) => {
      oitem match {
        case Some(item) =>
          out.println(s"${item.name} acquired")
          addToInventory(item)
        case None => out.println("Item not found in room")
      }
    }
    case TakeExit(oroom) => {
      if (loc != null) {
      loc ! Room.RemovePlayer()
      }
      oroom match {
        case None       => out.println("no exit in this direction")
        case Some(room) => loc = room
      }
      loc ! Room.AddPlayer()  
      loc ! GetDescription 
    }

    case SayMsg(msg, room) => {
      if (loc == room) out.println(msg)
    }
    //case BeAttacked
    //case EnemyAlive 
    //case FoundVictim
    //case Attack
    case m => out.println("Unhandled msg in Player: " + m)
  }
  var loc: ActorRef = null
  var inv: List[Item] = List()

  def processCommand(command: String): Unit = {
    if (command == "help") {
      out.println("N,S,E,W,U,D - for movements (north,south,east,west,up,down)")
      out.println("look - reprints the description of the current room")
      out.println("inv - list the contents in the inventory")
      out.println("get item - get an item and add to inventory")
      out.println("drop item - drop an item into the room")
      out.println("exit - leave the game")
      out.println("help - open the help menu")
    } else if (command == "look") {
      loc ! GetDescription

    } else if (command == "inv") {
      out.println(inventoryListing())

    } else if (command.startsWith("get")) {
      val itemName = command.substring(4)
      loc ! Room.GetItem(itemName)

    } else if (command.startsWith("drop")) {
      val itemName = command.substring(5)
      out.println(itemName)
      getFromInventory(itemName) match {
        case Some(item) =>
          loc ! Room.DropItem(item)
          out.println(s"$itemName dropped")
        case None => out.println(s"$itemName not found in inventory")
      }
    } else if (command == "N") {
      move("N")
    } else if (command == "E") {
      move("E")
    } else if (command == "W") {
      move("W")
    } else if (command == "S") {
      move("S")
    } else if (command == "U") {
      move("U")
    } else if (command == "D") {
      move("D")
    } else if (command.startsWith("say")) {
      Main.playMng ! PlayerManager.SayMsg(command.drop(4), loc)
    } else if (command.startsWith("tell")) {
      var str = ""
      val cmd = command.split(" ")
      val msg = cmd.drop(2)
      msg.foreach(a => str += a + " ")
      val pl = cmd(1)
      Main.playMng ! PlayerManager.TellMsg(str, pl)
    } else if (command == "exit") {
      out.println("you left")
      in.close()
      out.close()
      self ! PoisonPill
    }
  }
    //else if (command == "flee") loc ! Room.GetExit(util.Random.nextInt(6))
    //else if (command == "equip") if (!inv.isEmpty) 

  def getFromInventory(itemName: String): Option[Item] = {
    val foundItem = inv.find(x => itemName == x.name)
    inv = inv.filterNot(x => itemName == x.name)
    foundItem
  }
  def addToInventory(item: Item): Unit = {
    inv = item :: inv
  }
  def inventoryListing(): String = {
    var invlist: String = ""
    for (x <- inv) {
      invlist += x.name + ","
    }
    invlist
  }

  def move(dir: String): Unit = {

    var dirInd = 0
    if (dir == "N") dirInd = 0
    if (dir == "S") dirInd = 1
    if (dir == "E") dirInd = 2
    if (dir == "W") dirInd = 3
    if (dir == "U") dirInd = 4
    if (dir == "D") dirInd = 5
    loc ! Room.GetExit(dirInd)
  }
//def kill()
}

object Player {
  case class ProcessCommand(command: String)
  case object CheckAllInput
  case class PrintMessage(msg: String)
  case class TakeItem(oitem: Option[Item])
  case class TakeExit(oroom: Option[ActorRef])
  case class SayMsg(msg: String, room: ActorRef)
  case class TellMsg(msg: String, player: ActorRef)
}

