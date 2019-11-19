package mud
import akka.actor.Actor
import akka.actor.ActorRef
import akka.actor.Props
import mud.Room.GetDescription
import Player._
import mud.NPC.Move
class NPC(val name: String) extends Actor {
  var loc: ActorRef = null
  private var health = 50

  //Main.actMng ! ActivityManager.ScheduleActivity(5000, self, Move)
  def receive = {

    case Player.TakeExit(oroom) => {
      if (loc != null) {
        loc ! Room.RemovePlayer()
        }
        oroom match {
          case None       => 
          case Some(room) => loc = room
        }
        loc ! Room.AddPlayer()
        Main.actMng ! ActivityManager.ScheduleActivity(100, self, NPC.Move)
      }
      
      case Move => {
        println("moved")
        loc ! Room.GetExit(util.Random.nextInt(6)) 
        Main.playMng ! PlayerManager.SayMsg(name + " joined the room.", loc)
      }
    case m => println("Unhandled msg in NPC:" + m)
  }
}

object NPC {
    case object Move
}