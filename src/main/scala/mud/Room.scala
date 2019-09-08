package mud

import scala.io._
class Room(
  val name: String,
  val desc: String,
  private var items: List[Item],
  private val exits: Array[Int]) {
  
    def printexits(): String = {
    var exitString: String = ""
    if (exits(0) != -1) exitString += "North, " 
    if (exits(1) != -1) exitString += "South, " 
    if (exits(2) != -1) exitString += "East, " 
    if (exits(3) != -1) exitString += "West, " 
    if (exits(4) != -1) exitString += "Up, " 
    if (exits(5) != -1) exitString += "Down, " 
    "Exits: " + exitString // needs reformatting
    } 
    
    def printitems(): String = {
      var itemString: String = ""
      for (x <- items) {
        itemString += x.name + x.desc + "\n" 
      }
      "Items: " + itemString
    }
    
    def description(): String = name + "\n" + desc + "\n" + printexits + "\n" + printitems
   
    def getExit(dir: Int): Option[Room] = { 
      if (exits(dir) == -1) None
      else Some(Room.roomList(exits(dir)))
    }
    
    def getItem(itemName: String): Option[Item] = {
      val finditem = items.find(x => itemName == x.name)
      finditem match {
        case None => 
        case Some(item) => items = items.filter(x => item.name != x.name)
      }
      finditem
    }
    def dropItem(item: Item): Unit = items ::= item
 }

object Room {
  val file = Source.fromFile("map.txt")
  var lines = file.getLines.toList
  def itemsplit(item:String): Item = {
    val index = item.indexOf(",")
    new Item(item.substring(0,index), item.substring(index))
  }
  def readMap(lines: Array[String]): Room = {
    val items = lines(3).split(";").map(itemsplit).toList
    val exits = lines(1).split(",").map(_.toInt)
    new Room(lines(0),lines(2),items,exits)
    
  }
  val roomList: Array[Room] = {
    (for (i <- 1 to lines.length/4) yield {
      val room = readMap(lines.take(4).toArray)
      lines = lines.drop(4)
      room
    }).toArray
  }
 }