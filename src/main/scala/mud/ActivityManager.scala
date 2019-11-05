package mud

import akka.actor.Actor
import java.net.Socket
import akka.actor.ActorRef
import akka.actor.Props
import java.io.PrintStream
import java.io.BufferedReader


class ActivityManager extends Actor {
    val pq = new PriorityQueue[Activity](_.time < _.time)
    def receive = {
        case ScheduleActivity(delay, actor, msg) =>
        pq.enqueue(Activity(curTime + delay, actor, msg))
        case CheckQueue => {
            curTime + 1 // where to declare curTime ???
            while (pq.peek.time <= curTime) {
                val activity = pq.dequeue()
                activity.actor ! activity.msg
            }
        }
    }
}

object PlayerManager {
    case class Activity(time: Int, actor: ActorRef, msg: Any)
    case class ScheduleActivity(delay: Int, actor: ActorRef, msg: Any)
    case object CheckQueue 
}