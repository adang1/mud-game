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
    else if (command == "look") {
      println(loc.description)
  
    }
    else if (command == "inv") {
      println(inventoryListing())
  
    }
    else if (command.startsWith("get")) {
      val itemName = command.substring(4)
      loc.getItem(itemName) match {
        case Some(item) =>
        println(s"$itemName acquired")
        addToInventory(item)
        case None => println(s"$itemName not found in room")
      }
    }
    else if (command.startsWith("drop")) {
      val itemName = command.substring(5)
      println(itemName)
      getFromInventory(itemName) match {
        case Some(item) =>
        loc.dropItem(item)
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
  loc.getExit(dirInd) match {
    case None => println("no exit in this direction")
    case Some(room) => loc = room
  }
  loc.description()
}

}