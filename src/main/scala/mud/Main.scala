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

    val ss = ServerSocket(4041) 
    while(true) {
      val sock = ss.accept()
     Future {
      val out = new PrintStream(sock.getOutputStream())
      out.println("What is your name?")
      val in = new BufferedReader(new InputStreamReader(sock.getInputStream()))
      val name = in.readLine()
      println(name + " has connected.")
      playMng ! PlayerManager.CreatePlayer(name, sock, out, in)
     }
    }
  }
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

