import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

 private Queue<Object> queue;

 @Before
 public void setUp() {
 queue = new Queue<>();
 }

 @Test
 public void testConstructor() {
 Queue queueWithNoCapacity = new Queue();
 Queue queueWithCapacity = new Queue(5);
 assertEquals(-1, queueWithNoCapacity.getMaxCapacity());
 assertEquals(5, queueWithCapacity.getMaxCapacity());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 queue.enqueue(new Object());
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 Object obj = new Object();
 queue.enqueue(obj);
 Node expectedFirst = new Node(obj);
 Node expectedLast = new Node(obj);
 Node actualFirst = queue.first;
 Node actualLast = queue.last;
 assertEquals(expectedFirst, actualFirst);
 assertEquals(expectedLast, actualLast);
 queue.enqueue(new Object());
 assertEquals(2, queue.getNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 fail("Expected NullPointerException and test did not fail");
 }

 @Test
 public void testDequeue() {
 Object obj = new Object();
 queue.enqueue(obj);
 Object dequeuedObj = queue.dequeue();
 assertEquals(obj, dequeuedObj);
 assertNull(queue.first);
 assertNull(queue.last);
 assertEquals(0, queue.getNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueWhenEmpty() {
 queue.dequeue();
 fail("Expected IllegalStateException and test did not fail");
 }

 @Test
 public void testRemove() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 int removed = queue.remove(obj1);
 assertEquals(1, removed);
 assertFalse(queue.contains(obj1));
 int remaining = queue.getNumberItems();
 assertEquals(1, remaining);
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullObject() {
 queue.remove(null);
 fail("Expected NullPointerException and test did not fail");
 }

 @Test
 public void testRefreshElement() {
 Object obj = new Object();
 queue.enqueue(obj);
 queue.refreshElement(obj);
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 Object obj1 = new Object();
 Object obj2 = new Object();
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 Vector expectedVector = new Vector();
 expectedVector.add(obj1);
 expectedVector.add(obj2);
 Vector actualVector = queue.getObjects();
 assertEquals(expectedVector, actualVector);
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue queueWithMaxCapacity = new Queue(4);
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 Queue queueWithObjects = new Queue();
 queueWithObjects.enqueue(new Object());
 queueWithObjects.enqueue(new Object());
 String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Ljava.lang.Object;@1b7d3d, \r\n]";
 String actualString = queueWithObjects.toString();
 assertEquals(expectedString, actualString);
 }

}