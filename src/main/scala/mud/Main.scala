package mud
import akka.actor.ActorSystem
import akka.actor.Props
import java.net.ServerSocket
import java.io.PrintStream
import java.io.BufferedReader
import java.io.InputStreamReader
import scala.concurrent.Future
import scala.concurrent.duration._
/**
This is a stub for the main class for your MUD.
*/
object Main extends App {
	
		println("Welcome to my MUD.")
		
		
		 val system = ActorSystem("MUD")

  

  implicit val ec = system.dispatcher

  val playMng = system.actorOf(Props[PlayerManager], "playMng")
  val roomMng = system.actorOf(Props[RoomManager], "roomMng")
  
  system.scheduler.schedule(1.second, 0.1.second, playMng, PlayerManager.CheckAllInput)

    
    // Future {
      val out = Console.out//new PrintStream(sock.getOutputStream())
      val in = Console.in//new BufferedReader(new InputStreamReader(sock.getInputStream()))
      out.println("What is your name?")
      val name = in.readLine()
      println(name + " has connected.")
      playMng ! PlayerManager.CreatePlayer(name, out, in)
  //   }
  // }

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