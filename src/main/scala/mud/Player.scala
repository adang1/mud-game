package mud

import akka.actor.Actor
import akka.actor.ActorRef
import java.io.PrintStream
import java.io.BufferedReader
import mud.Room.GetDescription

class Player(name: String, out: PrintStream, in: BufferedReader) extends Actor {
  import Player._
  def receive = {
    case ProcessCommand(command) => {
      processCommand(command)
    }
    case CheckAllInput => 
    
    if(in.ready()) {
      val command = in.readLine()
      
      processCommand(command)
    }
    
    case PrintMessage(msg) => out.println(msg)
    case TakeItem(oitem) => {
      oitem match {
        case Some(item) => 
        println(s"${item.name} acquired")
        addToInventory(item)
        case None => println("Item not found in room")
      }
      // if there is any item:
    // items :: itemName
      // else println("$itemname does not exist")
    }
    case TakeExit(oroom) => {
      oroom match {
        case None => out.println("no exit in this direction")
        case Some(room) => loc = room
      }
      loc ! GetDescription
    }
    


    case m => println("Unhandled msg in Player: " + m)
  }
  var loc: ActorRef = null
    var inv: List[Item] = List()
    
    def processCommand(command:String): Unit = {
      if (command == "help") {
        out.println ("N,S,E,W,U,D - for movements (north,south,east,west,up,down)")
        out.println ("look - reprints the description of the current room")
        out.println ("inv - list the contents in the inventory")
        out.println ("get item - get an item and add to inventory")
        out.println ("drop item - drop an item into the room")
        out.println ("exit - leave the game")
        out.println ("help - open the help menu")
      }
      else if (command == "look") {
        loc ! GetDescription
    
      }
      else if (command == "inv") {
        out.println(inventoryListing())
    
      }
      else if (command.startsWith("get")) {
        val itemName = command.substring(4)
        loc ! Room.GetItem(itemName)
        
      }
      else if (command.startsWith("drop")) {
        val itemName = command.substring(5)
        println(itemName)
        getFromInventory(itemName) match {
          case Some(item) =>
          loc ! Room.DropItem(item)
          println(s"$itemName dropped")
          case None => println(s"$itemName not found in inventory")
        }
        }
      else if (command == "N") {
        move("N")
        }
      else if (command == "E") {
        move("E")
      }
      else if (command == "W") {
        move("W")
      }
      else if (command == "S") {
        move("S")
      }
      else if (command == "U") {
        move("U")
      }
      else if (command == "D") {
        move("D")
      }
    
    
    }
  
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
  
  }


object Player {
  case class ProcessCommand(command: String)
  case object CheckAllInput
  case class PrintMessage(msg: String)
  case class TakeItem(oitem: Option[Item])
  case class TakeExit(oroom: Option[ActorRef])
}
// 