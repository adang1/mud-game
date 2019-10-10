package mud
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

class PlayerManager extends Actor {

  import PlayerManager._
  def receive = {
    case CreatePlayer(nameOfNewPlayer) => {
      context.actorOf(Props(new Player(nameOfNewPlayer)))
    }
    case CheckAllInput =>
    for (child <- context.children) child ! Player.CheckInput
    case SendToAll(msg) =>
    for (child <- context.children) child ! Player.PrintMessage(msg)
    case m => println("Unhandled message in PlayerManager: " + m)
  }
}

object PlayerManager {
  case class CreatePlayer(name: String)
  case object CheckAllInput
  case class SendToAll(msg: String)
}
