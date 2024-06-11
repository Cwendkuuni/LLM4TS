import org.junit.Test;
import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

 @Test
 public void testConstructor() {
 Queue queue = new Queue();
 assertEquals(0, queue.getNumberItems());
 assertEquals(-1, queue.getPeakNumberItems());
 assertEquals(0, queue.getNumberItems() - queue.getPeakNumberItems());
 }

 @Test
 public void testQueueWithCapacity() {
 Queue queue = new Queue(5);
 assertEquals(0, queue.getNumberItems());
 assertEquals(0, queue.getPeakNumberItems());
 assertEquals(5, queue.getMaxCapacity());
 }

 @Test
 public void testIsEmpty() {
 Queue queue = new Queue();
 assertTrue(queue.isEmpty());
 Queue queue2 = new Queue(5);
 assertTrue(queue2.isEmpty());
 Queue queue3 = new Queue();
 enqueueItems(queue3, "A", "B", "C", "D", "E");
 assertFalse(queue3.isEmpty());
 }

 @Test
 public void testEnqueue() {
 Queue queue = new Queue();
 enqueueItems(queue, "A", "B", "C", "D", "E");
 assertEquals(5, queue.getNumberItems());
 assertEquals(5, queue.getPeakNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 Queue queue = new Queue();
 queue.enqueue(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testDequeue() {
 Queue queue = new Queue();
 enqueueItems(queue, "A", "B", "C", "D", "E");
 assertEquals("A", queue.dequeue());
 assertEquals(4, queue.getNumberItems());
 assertEquals(4, queue.getPeakNumberItems());
 }

 @Test
 public void testDequeueEmptyQueue() {
 Queue queue = new Queue();
 assertNull(queue.dequeue());
 assertEquals(0, queue.getNumberItems());
 }

 @Test
 public void testRemove() {
 Queue queue = new Queue();
 enqueueItems(queue, "A", "B", "C", "D", "E");
 queue.remove("C");
 assertEquals(4, queue.getNumberItems());
 }

 @Test
 public void testRemoveNonExistentObject() {
 Queue queue = new Queue();
 enqueueItems(queue, "A", "B", "C", "D", "E");
 assertEquals(0, queue.remove("F"));
 assertEquals(5, queue.getNumberItems());
 }

 @Test
 public void testRefreshElement() {
 Queue queue = new Queue();
 enqueueItems(queue, "A", "B", "C", "D", "E");
 queue.refreshElement("C");
 assertEquals("C", queue.getObjects().get(3));
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue queue = new Queue(5);
 enqueueItems(queue, "A", "B", "C", "D", "E");
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testMaxCapacityNotExceeded() {
 Queue queue = new Queue(10);
 enqueueItems(queue, "A", "B", "C", "D", "E");
 assertFalse(queue.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 Queue queue = new Queue();
 assertEquals("Queue:[numItems=0, maxNumItems=-1, maxCapacity=-1, getObjects()=[]]", queue.toString());
 Queue queue2 = new Queue(5);
 enqueueItems(queue2, "A", "B", "C", "D", "E");
 assertEquals("Queue:[numItems=5, maxNumItems=5, maxCapacity=5, getObjects=[A, B, C, D, E]]", queue2.toString());
 }

 private void enqueueItems(Queue queue, Object... items) {
 for (Object item : items) {
 queue.enqueue(item);
 }
}

 private Vector getVectorFromQueue(Queue queue) {
 return queue.getObjects();
 }

}