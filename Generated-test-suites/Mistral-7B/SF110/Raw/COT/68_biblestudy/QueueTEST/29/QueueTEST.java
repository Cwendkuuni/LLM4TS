package bible.util; // make sure the test class is in the same package as the Queue class

import static org.junit.Assert.*; // import assert methods from JUnit
import java.util.*; // import Vector and NullPointerException

public class QueueTest {

 private Queue<Object> queue; // assuming generic Queue is used in test

 @Before // can be used instead of @BeforeEach for setting up the instance of Queue
 public void setup() {
 queue = new Queue<>();
 }

 @Test
 public void testQueueConstructor() {
 Queue queueWithNoCapacity = new Queue(); // test default constructor
 Queue queueWithCapacity = new Queue(5); // test constructor with capacity
 assertEquals(0, queueWithNoCapacity.getNumberItems());
 assertEquals(0, queueWithCapacity.getNumberItems());
 }

 @Test
 public void testIsEmpty() {
 Queue queue = new Queue(); // create an empty Queue
 assertTrue(queue.isEmpty());
 queue.enqueue(1); // add an element
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 Queue queue = new Queue(); // create an empty Queue
 Object obj = new Object();
 queue.enqueue(obj); // add an element
 assertEquals(1, queue.getNumberItems());
 queue.enqueue(2); // add another element
 assertEquals(2, queue.getNumberItems());
}

 @Test(expected = NullPointerException.class)
 public void testNullObjectInEnqueue() {
 Queue queue = new Queue();
 queue.enqueue(null); // try to add a null object
}

 @Test
 public void testDequeue() {
 Queue queue = new Queue(); // create an empty Queue
 Object obj = new Object();
 queue.enqueue(obj); // add an element
 Object dequeuedObj = queue.dequeue(); // dequeue an element
 assertEquals(obj, dequeuedObj);
 assertEquals(0, queue.getNumberItems());
}

 @Test(expected = NoSuchElementException.class)
 public void testDequeueFromEmptyQueue() {
 Queue queue = new Queue();
 queue.dequeue(); // try to dequeue from an empty Queue
}

 @Test
 public void testRemove() {
 Queue queue = new Queue(); // create an empty Queue
 Object obj = new Object();
 queue.enqueue(obj); // add an element
 queue.remove(obj); // remove an element
 assertEquals(0, queue.getNumberItems());
}

 @Test(expected = NoSuchElementException.class)
 public void testRemoveFromEmptyQueue() {
 Queue queue = new Queue();
 queue.remove(new Object()); // try to remove from an empty Queue
}

 @Test
 public void testRemoveObjectThatDoesNotExist() {
 Queue queue = new Queue(); // create an empty Queue
 Object obj = new Object();
 queue.enqueue(obj); // add an element
 queue.enqueue(new Object()); // add another element
 int result = queue.remove(new Object()); // remove an object that does not exist
 assertEquals(-1, result);
}

 @Test
 public void testRefreshElement() {
 Queue queue = new Queue(); // create an empty Queue
 Object obj = new Object();
 queue.enqueue(obj); // add an element
 queue.refreshElement(obj); // refresh an element
 assertEquals(1, queue.getNumberItems());
}

 @Test
 public void testMaxCapacityExceeded() {
 Queue queue = new Queue(1); // create a Queue with max capacity of 1
 queue.enqueue(new Object()); // add an element
 queue.enqueue(new Object()); // add another element
 assertTrue(queue.maxCapacityExceeded());
}

 @Test
 public void testGetNumberItems() {
 Queue queue = new Queue(); // create an empty Queue
 assertEquals(0, queue.getNumberItems());
 queue.enqueue(new Object()); // add an element
 assertEquals(1, queue.getNumberItems());
 queue.enqueue(new Object()); // add another element
 assertEquals(2, queue.getNumberItems());
}

 @Test
 public void testGetPeakNumberItems() {
 Queue queue = new Queue(); // create an empty Queue
 assertEquals(-1, queue.getPeakNumberItems());
 queue.enqueue(new Object()); // add an element
 assertEquals(1, queue.getPeakNumberItems());
 queue.enqueue(new Object()); // add another element
 assertEquals(2, queue.getPeakNumberItems());
}

 @Test
 public void testGetObjects() {
 Queue queue = new Queue(); // create an empty Queue
 Vector vector = queue.getObjects(); // get the objects in the Queue
 assertNotNull(vector);
 assertEquals(0, vector.size());
 queue.enqueue(new Object()); // add an element
 queue.enqueue(new Object()); // add another element
 vector = queue.getObjects(); // get the objects in the Queue
 Object obj = vector.get(0); // get the first object
 assertEquals(obj, queue.dequeue()); // dequeue the object and check if it is the same
}

 @Test
 public void testToString() {
 Queue queue = new Queue(); // create an empty Queue
 assertEquals("Queue:[numItems=0, maxNumItems=-1, maxCapacity=-1, getObjects()=[]]", queue.toString());
 queue.enqueue(new Object()); // add an element
 assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[], maxCapacityExceeded=false]", queue.toString());
 queue.enqueue(new Object()); // add another element
 assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[], maxCapacityExceeded=false]", queue.toString());
}

}