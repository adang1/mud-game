package mud

import org.junit._
import org.junit.Assert._

class TestPQ {

var queue: PriorityQueue[Int] = null
@Before def initQueue: Unit = {
queue = new PriorityQueue[Int](_ > _)
}
@Test def enqueue100Dequeue100: Unit = {
val nums = Array.fill(100)(util.Random.nextInt)
nums.foreach(queue.enqueue)
nums.sorted.reverse.foreach(assertEquals(_, queue.dequeue))
}
}