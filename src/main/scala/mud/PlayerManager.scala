package mud
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import java.io.PrintStream
import java.io.BufferedReader
import mud.RoomManager.AddPlayerToRoom


class PlayerManager extends Actor {

  import PlayerManager._
  def receive = {
    case CreatePlayer(nameOfNewPlayer, out, in) => {
      
      val newPlayer = context.actorOf(Props(new Player(nameOfNewPlayer, out, in)), nameOfNewPlayer)
      Main.roomMng ! AddPlayerToRoom(newPlayer, "c")
    }
    case CheckAllInput =>
    for (child <- context.children) child ! Player.CheckAllInput
    case SendToAll(msg) =>  
    for (child <- context.children) child ! Player.PrintMessage(msg)
    case m => println("Unhandled message in PlayerManager: " + m)
  }
}

object PlayerManager {
  case class CreatePlayer(name: String, out: PrintStream, in: BufferedReader)
  case object CheckAllInput
  case class SendToAll(msg: String)
}
