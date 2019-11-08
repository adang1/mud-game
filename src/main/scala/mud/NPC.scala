package mud
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import mud.Room.GetDescription

class NPC(val name: String, var currentRoom: ActorRef) extends Actor {
  var loc: ActorRef = null
  def receive = {

    case Player.TakeExit(oroom) => 
      oroom match {
        case None       => //out.println("no exit in this direction")
        case Some(room) => loc = room
      }
    case m => println("Unhandled msg")
  }
}

object NPC {
    
}