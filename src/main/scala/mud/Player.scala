package mud

class Player(val name:String) {
  var loc = Room.roomList(0)
  var inv: List[Item] = List()
  
  def processCommand(command:String): Unit = {
    if (command == "help") {
      println ("N,S,E,W,U,D - for movements (north,south,east,west,up,down)")
      println ("look - reprints the description of the current room")
      println ("inv - list the contents in the inventory")
      println ("get item - get an item and add to inventory")
      println ("drop item - drop an item into the room")
      println ("exit - leave the game")
      println ("help - open the help menu")
    }
    if (command == "look") {
      println(loc.description)
  
    }
    if (command == "inv") {
      println(inventoryListing())
  
    }
    if (command.startsWith("get")) {
      /*val itemName = command.substring(5)
      addtoInventory(itemName) match {
        case Some(item) =>

        println("item acquired")
        loc.getItem(item)
        case None => println("item not found")*/
      }
      
    
    if (command.startsWith("drop")) {
      val itemName = command.substring(5)
      getFromInventory(itemName) match {
        case Some(item) => 

        println("item dropped")
        loc.dropItem(item)
        case None => println("item not found")
      }
    }


    if (command == "N") {
      move("N")

    }
    if (command == "E") {
      
    }
    if (command == "W") {
      
    }
    if (command == "S") {
      
    }
  }
def getFromInventory(itemName: String): Option[Item] = {

  val found = inv.find(x => itemName == x) 
inv = inv.filterNot(x => itemName == x)
found
}
def addToInventory(item: Item): Unit = {
inv = item :: inv
}
def inventoryListing(): String = {
  inv.mkString(",")
}
def move(dir: String): Unit = {
  var dirInd = 0
  if (dir == "N") dirInd = 0
  if (dir == "S") dirInd = 1
  if (dir == "E") dirInd = 2
  if (dir == "W") dirInd = 3
  if (dir == "U") dirInd = 4
  if (dir == "D") dirInd = 5

}

}