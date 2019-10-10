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

  private var exits: Array[Option[ActorRef]] = null
  import Room._

  def receive = {
    case LinkExits(rooms) =>
      exits = exitKeys.map(key => rooms.get(key))
    case GetItem(itemName) =>
      sender ! Player.TakeItem(getItem(itemName))
    case DropItem(item) => ???
    case GetExit(dir)   => ???
    case GetDescription => 
      sender ! Player.PrintMessage(description())
    case m => println("Unhandled msg in Room: " + m)

  }

  def printExits(): String = {
    var x = "Exits: "
    if (exits(0).nonEmpty) x += "North "
    if (exits(1).nonEmpty) x += "South "
    if (exits(2).nonEmpty) x += "East "
    if (exits(3).nonEmpty) x += "West "
    if (exits(4).nonEmpty) x += "Up "
    if (exits(5).nonEmpty) x += "Down "
    x
  }

  def printItems(): String = {
    var y = "Items: "
    for (item <- items) {
      y += item.name + " "
    }
    y
  }

  def description(): String =
    name + "\n" + desc + "\n" + printExits() + "\n" + printItems()

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
}

object Room {
  case class GetItem(itemName: String)
  case class DropItem(item: Item)
  case class GetExit(dir: Int)
	case object GetDescription
	case class LinkExits(rooms: Map[String, ActorRef])
}
