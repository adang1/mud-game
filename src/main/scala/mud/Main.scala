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
		println("Enter your name: ")
		 val name = readLine()
		 val system = ActorSystem("MUD")

  val manager = system.actorOf(Props[PlayerManager], "player")

  implicit val ec = system.dispatcher

  val playMng = system.actorOf(Props[PlayerManager], "playMng")
  val roomMng = system.actorOf(Props[RoomManager], "roomMng")
  
  system.scheduler.schedule(1.second, 0.1.second, manager, PlayerManager.CheckAllInput)

    
  //   Future {
  //     val out = new PrintStream(sock.getOutputStream())
  //     val in = new BufferedReader(new InputStreamReader(sock.getInputStream()))
  //     println("What is your name?")
  //     val name = in.readLine()
  //     println(name + " has connected.")
  //     playMng ! PlayerManager.CreatePlayer(name, out, in)
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