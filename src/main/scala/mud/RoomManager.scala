package mud
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import scala.collection.mutable

class RoomManager extends Actor {
  val directions = "N S W E U D".split(" ")
  import RoomManager._
  def receive = {
    case AddPlayerToRoom(player, keyword) =>
      player ! Player.TakeExit(rooms.get(keyword))
    case GetShortestPath(n1, n2) =>
      sender ! Player.PrintMessage(shortestPath(n1, n2).mkString(" "))
      println("shortest called")
    case PrintRooms =>
      sender ! Player.PrintMessage(context.children.map(_.path.name).mkString(","))
    case m => println("Unhandled message in RoomManager: " + m)
  }

  var allExits: Map[String, Array[String]] = Map.empty
  val rooms = readRooms()
  for (child <- context.children) child ! Room.LinkExits(rooms)

  def readRooms(): Map[String, ActorRef] = {
    val xmlData = xml.XML.loadFile("map.xml")
    (xmlData \ "Room").map(readRoom).toMap
  }
  def readRoom(node: xml.Node): (String, ActorRef) = {
    val keyword = (node \ "@keyword").text
    val name = (node \ "@name").text
    val description = (node \ "description").text.trim
    val exits = (node \ "exits").text.split(",")
    allExits += (keyword -> exits)
    val item = (node \ "item")
      .map(
        n =>
          Item(
            (n \ "@name").text,
            (n \ "@speed").text.toInt,
            (n \ "@damage").text.toInt,
            n.text.trim
          )
      )
      .toList
    val npc = (node \ "NPC").map(n => (n \ "@name").text).toList

    for (i <- npc) {
      Main.npcMng ! NPCManager.MakeNewNPC(i, keyword)
    }
    keyword -> context.actorOf(
      Props(new Room(keyword, name, description, exits, item)),
      keyword
    )
  }
  def shortestPath(node1: String, node2: String): List[String] = {
    def helper(n1: String, visited: Set[String]): List[String] = {
      if (n1 == node2) List(n1)
      else {
        var ret: List[String] = Nil
        var newVisited = visited + n1
        for ((dest, dir) <- allExits(n1).zip(directions)) {
          if (!visited(dest) && allExits.contains(dest)) {
            val allVisited = helper(dest, newVisited)
            if (allVisited != Nil && (ret == Nil || allVisited.length + 2 < ret.length)) {
              ret = n1 :: dir :: allVisited
            }
          }
        }
        ret
      }
	  
	}
	helper(node1, Set.empty)
  }
}

object RoomManager {
  case class AddPlayerToRoom(player: ActorRef, keyword: String)
  case class GetShortestPath(node1: String, node2: String)
  case object PrintRooms
}
