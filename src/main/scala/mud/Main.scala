package mud

/**
This is a stub for the main class for your MUD.
*/
object Main {
	def main(args: Array[String]): Unit = {
		//Room.roomList.foreach(x => println(x.description()))
		println("Welcome to my MUD.")
		println("Enter your name: ")
		val name = readLine()
		val player = new Player(name)
		
		/* description of the room
		 */
	   var command = readLine
	   while (command != "exit") {
		 player.processCommand(command)
		 command = readLine   
	}
	}
}
