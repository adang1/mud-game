package mud

import akka.actor.Actor
import java.net.Socket
import akka.actor.ActorRef
import akka.actor.Props
import java.io.PrintStream
import java.io.BufferedReader
import mud.ActivityManager._



class ActivityManager extends Actor {
    private var curTime = 0
    val pq = new PriorityQueue[Activity](_.time < _.time)
    def receive = {
        case ScheduleActivity(delay, actor, msg) =>
        pq.enqueue(Activity(curTime + delay, actor, msg))
        case CheckQueue => {
            curTime + 1 
            while ((!pq.isEmpty) && (pq.peek.time <= curTime)) {
                val activity = pq.dequeue()
                activity.actor ! activity.msg
            }
        }
    }
}

object ActivityManager {
    case class Activity(time: Int, actor: ActorRef, msg: Any)
    case class ScheduleActivity(delay: Int, actor: ActorRef, msg: Any)
    case object CheckQueue 
}