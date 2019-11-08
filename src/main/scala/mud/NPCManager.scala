package mud
import akka.actor.Actor

class NPCManager extends Actor {
    def receive = {
        case m => println ("Unhandled msg")
    }
}