import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

 private Queue<Integer> queue;
 private Node node;
 private final int capacity = 5;

 @Before
 public void setup() {
 queue = new Queue<>(capacity);
 node = new Node(1);
 }

 @Test
 public void testQueueEmpty() {
 Queue<Integer> emptyQueue = new Queue<>();
 assertTrue(emptyQueue.isEmpty());
 }

 @Test
 public void testQueueConstruction() {
 Queue<Integer> newQueue = new Queue<>();
 assertEquals(0, newQueue.getNumberItems());
 assertEquals(-1, newQueue.getPeakNumberItems());
 Queue<Integer> newQueueWithCapacity = new Queue<>(3);
 assertEquals(0, newQueueWithCapacity.getNumberItems());
 assertEquals(-1, newQueueWithCapacity.getPeakNumberItems());
 }

 @Test
 public void testQueueIsEmpty() {
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testEnqueueOneItem() {
 queue.enqueue(node.value);
 assertEquals(1, queue.getNumberItems());
 assertEquals(1, queue.getPeakNumberItems());
 }

 @Test
 public void testEnqueueMultipleItems() {
 queue.enqueue(node.value);
 queue.enqueue(2);
 queue.enqueue(3);
 assertEquals(3, queue.getNumberItems());
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testDequeue() {
 queue.enqueue(node.value);
 queue.enqueue(2);
 queue.enqueue(3);
 assertEquals(1, queue.dequeue().intValue());
 assertEquals(2, queue.dequeue().intValue());
 assertEquals(3, queue.dequeue().intValue());
 assertEquals(0, queue.getNumberItems());
 assertEquals(-1, queue.getPeakNumberItems());
 }

 @Test
 public void testRemoveObject() {
 queue.enqueue(node.value);
 queue.enqueue(2);
 queue.enqueue(node.value);
 queue.enqueue(3);
 queue.remove(node.value);
 assertEquals(3, queue.getNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullObject() {
 queue.remove(null);
 fail("NullPointerException expected but not thrown");
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(node.value);
 queue.enqueue(2);
 queue.refreshElement(node.value);
 assertEquals(1, queue.getNumberItems());
 assertEquals(1, queue.getPeakNumberItems());
 assertEquals(node.value, queue.dequeue().intValue());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);
 Vector<Integer> vector = queue.getObjects();
 assertEquals(3, vector.size());
 assertEquals(1, vector.get(0).intValue());
 assertEquals(2, vector.get(1).intValue());
 assertEquals(3, vector.get(2).intValue());
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue<Integer> queueWithMaxCapacity = new Queue<>(1);
 queueWithMaxCapacity.enqueue(1);
 queueWithMaxCapacity.enqueue(2);
 assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[1, 2, 3]\r\n]", queue.toString());
 }
}