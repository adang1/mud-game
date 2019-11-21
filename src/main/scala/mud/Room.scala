package mud

import akka.actor.Actor
import akka.actor.ActorRef

class Room(
    val keyword: String,
    name: String,
    desc: String,
    private val exitKeys: Array[String],
    private var items: List[Item]
) extends Actor {
  private var players: List[ActorRef] = List.empty
  private var exits: Array[Option[ActorRef]] = null
  import Room._

  def receive = {
    case LinkExits(rooms) =>
      exits = exitKeys.map(key => rooms.get(key))
    case GetItem(itemName) =>
      sender ! Player.TakeItem(getItem(itemName))
    case DropItem(item) => 
    dropItem(item)
    case GetExit(dir)   => 
    sender ! Player.TakeExit(getExit(dir))
    case GetDescription => 
      sender ! Player.PrintMessage(description())
    case AddPlayer() =>
    addPlayer(sender) 
    case RemovePlayer(player) =>
    removePlayer(sender)
    case FindVictim(victim) => 
    sender ! Player.FoundVictim(players.find(_.path.name == victim))
   
    case m => println("Unhandled msg in Room: " + m)

  }

  def printExits(): String = {
    var x = "Exits: "
    if (exits(0).nonEmpty) x += "North, "
    if (exits(1).nonEmpty) x += "South, "
    if (exits(2).nonEmpty) x += "East, "
    if (exits(3).nonEmpty) x += "West, "
    if (exits(4).nonEmpty) x += "Up, "
    if (exits(5).nonEmpty) x += "Down, "
    x
  }

  def printItems(): String = {
    var y = "Items: "
    for (item <- items) {
      y += item.name + ", "
    }
    y
  }

  def description(): String =
    name + "\n" + desc + "\n" + printExits() + "\n" + printItems() + "\n" + printPlayer()

  def getExit(dir: Int): Option[ActorRef] = {
    exits(dir)
  }

  def getItem(itemName: String): Option[Item] = {
    var y: Boolean = false
    var z: Int = 0
    for (x <- items) {
      if (!y) z = z + 1
      if (x.name == itemName) y = true
    }
    if (y) {
      val gotItem = Some(items(z - 1))
      items = items.filter(x => x != gotItem.get)
      gotItem
    } else {
      None
    }
  }
  def dropItem(item: Item): Unit = items ::= item
  def addPlayer(player: ActorRef): Unit = {
    players = player :: players 
  }
  def removePlayer(player: ActorRef): Unit = {
    players = players.filter(p => player.path.name != p.path.name)
  }
  def printPlayer(): String = {
  var playerlist: String = ""
  for (x <- players) {
    playerlist += x.path.name + ","
  }
  playerlist
}
}
object Room {
  case class GetItem(itemName: String)
  case class DropItem(item: Item)
  case class GetExit(dir: Int)
	case object GetDescription
  case class LinkExits(rooms: Map[String, ActorRef])
  case class AddPlayer()
  case class RemovePlayer(player: ActorRef)
  case class FindVictim(victim: String)
}
