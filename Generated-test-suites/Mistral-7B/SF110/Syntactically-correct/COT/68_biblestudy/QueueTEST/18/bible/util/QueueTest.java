import org.junit.Test;
import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;
 private Object someObject;
 private Object anotherObject;

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithCapacity = new Queue(5);
 someObject = new Object();
 anotherObject = new Object();
 }

 @Test
 public void testConstructor() {
 Queue q = new Queue();
 Queue qWithCapacity = new Queue(10);
 assertEquals(0, q.getNumberItems());
 assertEquals(0, qWithCapacity.getNumberItems());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 assertFalse(queue.enqueue(someObject).isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testEnqueueObject() {
 queue.enqueue(someObject);
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testEnqueueReachMaxCapacity() {
 for (int i = 0; i < Queue.NO_MAXIMUM; i++) {
 queue.enqueue(someObject);
 }
 assertEquals(Queue.NO_MAXIMUM, queue.getNumberItems());
 }

 @Test
 public void testMaxCapacityExceeded() {
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(anotherObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(anotherObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(anotherObject);
 assertTrue(queueWithCapacity.maxCapacityExceeded());
 }

 @Test
 public void testDequeueEmptyQueue() {
 queue = new Queue();
 assertNull(queue.dequeue());
 }

 @Test
 public void testDequeueSingleObject() {
 queue.enqueue(someObject);
 assertEquals(someObject, queue.dequeue());
 assertEquals(0, queue.getNumberItems());
 }

 @Test
 public void testDequeueMultipleObjects() {
 queue.enqueue(someObject);
 queue.enqueue(anotherObject);
 assertEquals(someObject, queue.dequeue());
 assertEquals(anotherObject, queue.dequeue());
 assertEquals(0, queue.getNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveNullObject() {
 queue.remove(null);
 }

 @Test
 public void testRemoveSingleObject() {
 queue.enqueue(someObject);
 queue.enqueue(anotherObject);
 assertEquals(1, queue.remove(someObject).getValue());
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testRemoveMultipleObjects() {
 queue.enqueue(someObject);
 queue.enqueue(anotherObject);
 queue.enqueue(someObject);
 queue.enqueue(anotherObject);
 assertEquals(2, queue.remove(someObject).getValue());
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(someObject);
 queue.refreshElement(someObject);
 assertEquals(1, queue.getNumberItems());
 assertEquals(someObject, queue.dequeue());
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue(someObject);
 queue.enqueue(anotherObject);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(someObject);
 queue.enqueue(anotherObject);
 assertEquals(2, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(someObject);
 queue.enqueue(anotherObject);
 Vector v = queue.getObjects();
 assertEquals(2, v.size());
 assertEquals(someObject, v.get(0));
 assertEquals(anotherObject, v.get(1));
 }

 @Test
 public void testToString() {
 queue.enqueue(someObject);
 queue.enqueue(anotherObject);
 String toString = queue.toString();
 assertTrue(toString.contains("numItems=2, maxNumItems=2, maxCapacity="));
 }

}