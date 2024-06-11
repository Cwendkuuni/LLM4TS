import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.*;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;
 private Node node;
 private Object someObject = new Object();

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithCapacity = new Queue(5);
 }

 @Test
 public void testConstructor() {
 Queue queue = new Queue();
 Queue queueWithCapacity = new Queue(5);
 assertNotNull(queue);
 assertNotNull(queueWithCapacity);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 assertFalse(queueWithCapacity.isEmpty());
 queue.enqueue(someObject);
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(someObject);
 Node expectedNode = new Node(someObject);
 Node actualFirst = queue.first;
 Node actualLast = queue.last;
 assertEquals(expectedNode.value, actualFirst.value);
 assertEquals(expectedNode.value, actualLast.value);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 assertFalse(queueWithCapacity.isEmpty());
 assertFalse(queueWithCapacity.maxCapacityExceeded());
 }

 @Test
 public void testDequeue() {
 queue.enqueue(someObject);
 Object dequeuedObject = queue.dequeue();
 assertEquals(someObject, dequeuedObject);
 assertNull(queue.first);
 assertNull(queue.last);
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 Object dequeuedObject1 = queue.dequeue();
 Object dequeuedObject2 = queue.dequeue();
 assertEquals(someObject, dequeuedObject1);
 assertEquals(someObject, dequeuedObject2);
 assertNotNull(queue.last);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 Object dequeuedObject3 = queueWithCapacity.dequeue();
 Object dequeuedObject4 = queueWithCapacity.dequeue();
 Object dequeuedObject5 = queueWithCapacity.dequeue();
 Object dequeuedObject6 = queueWithCapacity.dequeue();
 Object dequeuedObject7 = queueWithCapacity.dequeue();
 assertNotEquals(someObject, dequeuedObject3); // edge case: dequeue when queue is not empty
 assertNull(queueWithCapacity.last);
 assertFalse(queueWithCapacity.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullObjectEnqueue() {
 queue.enqueue(null); // test enqueueing null object
 }

 @Test(expected = NullPointerException.class)
 public void testNullObjectDequeue() {
 queue.enqueue(someObject);
 queue.enqueue(null);
 Object dequeuedObject = queue.dequeue();
 assertNotEquals(null, dequeuedObject); // test dequeuing null object after enqueuing an object
 queue.dequeue();
 queue.dequeue();
 Queue emptyQueue = new Queue();
 Object dequeuedObject1 = emptyQueue.dequeue(); // test dequeuing from empty queue
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testMaxCapacityExceeded() {
 Queue queueWithMaxCapacity = new Queue(1);
 for (int i = 0; i < Queue.NO_MAXIMUM + 1; ++i) {
 queueWithMaxCapacity.enqueue(someObject);
 }
 }

 @Test
 public void testRemove() {
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 queue.remove(someObject);
 assertEquals(2, queue.numItems);
 queue.remove(someObject);
 assertEquals(1, queue.numItems);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.remove(someObject);
 queueWithCapacity.remove(someObject);
 queueWithCapacity.remove(someObject);
 queueWithCapacity.remove(someObject);
 queueWithCapacity.remove(someObject);
 assertEquals(0, queueWithCapacity.numItems);
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(someObject);
 queue.refreshElement(someObject);
 assertEquals(1, queue.numItems);
 queue.refreshElement(someObject);
 assertEquals(1, queue.numItems);
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 assertEquals(3, queue.getNumberItems());
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 assertEquals(5, queueWithCapacity.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 assertEquals(3, queue.getPeakNumberItems());
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 assertEquals(5, queueWithCapacity.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 Vector expectedVector = new Vector();
 expectedVector.add(someObject);
 expectedVector.add(someObject);
 expectedVector.add(someObject);
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 queue.enqueue(someObject);
 Vector actualVector = queue.getObjects();
 assertEquals(expectedVector, actualVector);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 Vector actualVector1 = queueWithCapacity.getObjects();
 assertEquals(expectedVector, actualVector1);
 }

 @Test
 public void testMaxCapacityExceededMessage() {
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 queueWithCapacity.enqueue(someObject);
 String actualMessage = queueWithCapacity.toString();
 String expectedMessage = "Queue:[numItems=6, maxNumItems=5, maxCapacity=5, getObjects()=[Ljava.lang.Object;@1328c2a, " +
                          "r\n]";
 assertEquals(expectedMessage, actualMessage);
 }

 @After
 public void tearDown() {
 queue = null;
 queueWithCapacity = null;
 }
}