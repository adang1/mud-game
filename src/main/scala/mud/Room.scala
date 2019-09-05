package mud

import scala.io.Source._

class Room(
  val name: String,
  val desc: String,
  private var items: List[Item],
  private val exits: Array[Int]) {
  def description(): String = name + "," + desc + "," + exits
    def getExit(dir: Int): Option[Room] = ???
    def getItem(itemName: String): Option[Item] = ???
    def dropItem(item: Item): Unit = items ::= item
 }

object Room {
  val file = fromFile("map.txt")
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