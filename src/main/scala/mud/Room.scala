package mud

import scala.io._
class Room(
  val name: String,
  val desc: String,
  private var items: List[Item],
  private val exits: Array[String]) {
  
    def printexits(): String = {
    var exitString: String = ""
    if (exits(0) != "-1") exitString += "North, " 
    if (exits(1) != "-1") exitString += "South, " 
    if (exits(2) != "-1") exitString += "East, " 
    if (exits(3) != "-1") exitString += "West, " 
    if (exits(4) != "-1") exitString += "Up, " 
    if (exits(5) != "-1") exitString += "Down, " 
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
  /* val rooms = readRooms()
  def readRooms(): Map[String, Room] = {
    val xmlData = xml.XML.loadFile("map.xml")
    xmlData.map(readRoom).map(r => r.keyword -> r).toMap
  }

  def readRoom(node: xml.Node): Room = {
    val keyword = (node \ "@keyword").text
    val name = (node \ "@name").text
    val description = (node \ "description").text.trim
    val exits = (node \ "exits").text.split(",")
    val item = (node \ "item").map(n => Item((n \ "@name").text,n.text.trim)).toList
    new Room(keyword,name,description,exits,item)
  }
  */
  val file = Source.fromFile("map.txt")
  var lines = file.getLines.toArray
  def itemsplit(item:String): Item = {
    val index = item.indexOf(",")
    new Item(item.substring(0,index), item.substring(index))
  }
  def readMap(lines: Array[String]): Room = {
    val items = lines(3).split(";").map(itemsplit).toList
    val exits = lines(1).split(",")
    new Room(lines(0).split(",")(0),lines(2),items,exits)
    
  }
  val roomList: Map[String,Room] = {
    (for (i <- 1 to lines.length/4) yield {
      val room = readMap(lines.take(4))
      val key = lines(0).split(",")(1)
      lines = lines.drop(4)
      (key -> room)
    }).toMap[String,Room]
  }
}

  
 