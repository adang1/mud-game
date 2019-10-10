package mud

/**
This is a stub for the main class for your MUD.
*/
object Main extends App {
	
		println("Welcome to my MUD.")
		println("Enter your name: ")
		 val name = readLine()
		 val system = ActorSystem("MUD")

  val manager = system.actorOf(Props[PlayerManager], "player")

  implicit val ec = system.dispatcher
  
  system.scheduler.schedule(1.second, 0.1.second, manager, PlayerManager.CheckAllInput)

  
  while (true) {
    
    Future {
      val out = new PrintStream(sock.getOutputStream())
      out.println("What is your name?")
      val in = new BufferedReader(new InputStreamReader(sock.getInputStream()))
      val name = in.readLine()
      println(name + " has connected.")
      manager ! PlayerManager.NewPlayer(name, out, in)
    }
  }
}
// 		val player = new Player()
		
// 		/* description of the room
// 		 */
// 	   var command = readLine
// // 	   while (command != "exit") {
// // 		 player.processCommand(command)
// // 		 command = readLine   
// // 	}
// // 	}
// // }
// 	}

}