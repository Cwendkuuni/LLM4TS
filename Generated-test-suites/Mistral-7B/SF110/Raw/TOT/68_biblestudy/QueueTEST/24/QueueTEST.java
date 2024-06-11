import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;
 private Object obj1;
 private Object obj2;
 private Object obj3;

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithCapacity = new Queue(3);
 obj1 = new Integer(1);
 obj2 = new Integer(2);
 obj3 = new Integer(3);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 assertFalse(queueWithCapacity.isEmpty());
 queue.enqueue(obj1);
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Queue actualQueue = new Queue();
 actualQueue.enqueue(obj1);
 actualQueue.enqueue(obj2);
 actualQueue.enqueue(obj3);
 assertArrayEquals(new Object[]{obj1, obj2, obj3}, queue.getObjects().toArray());
 assertArrayEquals(new Object[]{obj1, obj2, obj3}, actualQueue.getObjects().toArray());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testDequeue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Object objDequeued = queue.dequeue();
 assertEquals(obj1, objDequeued);
 queue.dequeue();
 queue.dequeue();
 assertNull(queue.dequeue());
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj2);
 assertFalse(queue.contains(obj2));
 assertArrayEquals(new Object[]{obj1, obj3}, queue.getObjects().toArray());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullObject() {
 queue.remove(null);
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 assertArrayEquals(new Object[]{obj1, obj2}, queue.getObjects().toArray());
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testMaxCapacityExceded() {
 queueWithCapacity.enqueue(obj1);
 queueWithCapacity.enqueue(obj2);
 queueWithCapacity.enqueue(obj3);
 queueWithCapacity.enqueue(obj3);
 assertTrue(queueWithCapacity.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[1, 2, 3],]\r\n";
 assertEquals(expected, queueWithCapacity.toString());
 }
}