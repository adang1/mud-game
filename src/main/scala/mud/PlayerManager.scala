package mud
import akka.actor.Actor
import java.net.Socket
import akka.actor.ActorRef
import akka.actor.Props
import java.io.PrintStream
import java.io.BufferedReader
import mud.RoomManager.AddPlayerToRoom


class PlayerManager extends Actor {

  import PlayerManager._
  def receive = {
    case CreatePlayer(nameOfNewPlayer, sock, out, in) => {
      
      val newPlayer = context.actorOf(Props(new Player(nameOfNewPlayer, out, in)), nameOfNewPlayer)
      Main.roomMng ! AddPlayerToRoom(newPlayer, "c")
    }
    case CheckAllInput =>
    for (child <- context.children) child ! Player.CheckAllInput
    case SendToAll(msg) =>  
    for (child <- context.children) child ! Player.PrintMessage(msg)
    case SayMsg(msg, room) => 
    for (child <- context.children) child ! Player.SayMsg(msg, room) 
    case TellMsg(msg, player) =>
      var check = false
      for (child <- context.children) {
        if (child.path.name == player && !check)
        { 
          child ! Player.PrintMessage(msg)
          check = true
        }
      }
      if (!check) sender ! Player.PrintMessage("player not found")
    
    case m => println("Unhandled message in PlayerManager: " + m)
  }
}

object PlayerManager {
  case class CreatePlayer(name: String, sock: Socket, out: PrintStream, in: BufferedReader)
  case object CheckAllInput
  case class SendToAll(msg: String)
  case class SayMsg(msg: String, room: ActorRef)
  case class TellMsg(msg: String, player: String)
}
