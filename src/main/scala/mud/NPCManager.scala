package mud
import akka.actor.Actor
import mud.RoomManager.AddPlayerToRoom
import akka.actor.Props

class NPCManager extends Actor {
    import NPCManager._
    def receive = {
        case MakeNewNPC(name, key) => {
            val newNPC = context.actorOf(Props(new NPC(name)), name)
            Main.roomMng ! AddPlayerToRoom(newNPC, key)
        }
        case m => println ("Unhandled msg in NPCMng:" + m)
    }
}

object NPCManager {
    case class MakeNewNPC(name: String, key: String)
}