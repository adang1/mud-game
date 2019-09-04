package mud

class Player {
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
  
    }
    if (command == "inv") {
      println(inventoryListing())
  
    }
    if (command.startsWith("get")) {

    }

    if (command.startsWith("drop")) {

    }

    if (command == "north") {

    }
    if (command == "east") {
      
    }
    if (command == "west") {
      
    }
    if (command == "south") {
      
    }
  }
def getFromInventory(itemName: String): Option[Item] = {
val found = inv.find(item => itemName==item.name) 
inv = inv.filterNot(item => itemName==item.name)
found
}
def addToInventory(item: Item): Unit = {
inv = item :: inv
}
def inventoryListing(): String = {
  inv.map(_.name).mkString(",")
}
def move(dir: String): Unit = ???

}