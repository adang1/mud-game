package mud

class Room(
  val name: String,
  val desc: String,
  private var items: List[Item],
  val exits: Array[Int]) {
  def description(): String = 
    name + "," + desc + "," + exits
    def getExit(dir: Int): Option[Room] = ???
    def getItem(itemName: String): Option[Item] = ???
    def dropItem(item: Item): Unit = ???
     
 }
