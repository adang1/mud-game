package mud
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props

class PlayerManager extends Actor {

	import PlayerManager._
  def receive = {
		case CreatePlayer(player) => ???
    case m => println("Unhandled message in PlayerManager: " + m)
  }
}

object PlayerManager { 
    case class CreatePlayer(player: ActorRef)
}
