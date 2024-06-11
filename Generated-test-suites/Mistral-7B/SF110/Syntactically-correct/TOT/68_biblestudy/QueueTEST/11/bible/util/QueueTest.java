import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

 private Queue queue;
 private Object obj1, obj2, obj3;

 @Before
 public void setUp() {
 queue = new Queue();
 obj1 = new Object();
 obj2 = new Object();
 obj3 = new Object();
 }

 @Test
 public void testIsEmpty() {
 Queue emptyQueue = new Queue();
 assertTrue(emptyQueue.isEmpty());
 }

 @Test
 public void testEnqueueAndDequeue() {
 queue.enqueue(obj1);
 assertEquals(obj1, queue.dequeue());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueWithNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testEnqueueAndGetNumberOfItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testDequeueMultipleTimes() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(obj1, queue.dequeue());
 assertEquals(obj2, queue.dequeue());
 assertEquals(obj3, queue.dequeue());
 }

 @Test
 public void testDequeueWhenEmpty() {
 queue.dequeue();
 assertNull(queue.dequeue());
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj2);
 assertEquals(2, queue.getNumberItems());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWithNullObject() {
 queue.enqueue(obj1);
 queue.remove(null);
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 assertEquals(2, queue.getNumberItems());
 assertEquals(obj1, queue.getObjects().get(0));
 assertEquals(obj2, queue.getObjects().get(1));
 }

 @Test(expected = NoSuchElementException.class)
 public void testGetObjectsWhenEmpty() {
 Queue emptyQueue = new Queue();
 assertNull(emptyQueue.getObjects());
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue queueWithMaxCapacity = new Queue(1);
 queueWithMaxCapacity.enqueue(obj1);
 queueWithMaxCapacity.enqueue(obj2);
 queueWithMaxCapacity.enqueue(obj3);
 assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
 }
}