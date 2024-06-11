import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;
 private Object object1;
 private Object object2;
 private Object object3;

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithCapacity = new Queue(3);
 object1 = new Object();
 object2 = new Object();
 object3 = new Object();
 }

 @Test
 public void testIsEmpty() {
 // Test when queue is empty
 assertEquals(true, queue.isEmpty());
 // Test when queue is not empty
 queue.enqueue(object1);
 assertEquals(false, queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 // Test enqueueing with null object
 assertNull(queue.enqueue(null));
 // Test enqueueing valid objects
 queue.enqueue(object1);
 queue.enqueue(object2);
 // Test enqueueing with maximum capacity reached
 for (int i = 0; i < 3; i++) {
 queueWithCapacity.enqueue(new Object());
 }
 queueWithCapacity.enqueue(new Object());
 // Test throwing exception with maximum capacity reached
 try {
 queueWithCapacity.enqueue(new Object());
 fail("Expected IllegalStateException");
 } catch (IllegalStateException e) {
 // Pass
 }
 }

 @Test
 public void testDequeue() {
 // Test dequeueing with empty queue
 assertNull(queue.dequeue());
 // Test dequeueing valid objects
 queue.enqueue(object1);
 queue.enqueue(object2);
 assertEquals(object1, queue.dequeue());
 assertEquals(object2, queue.dequeue());
 // Test dequeueing with empty queue again
 assertNull(queue.dequeue());
 // Test dequeueing with maximum capacity queue
 for (int i = 0; i < 3; i++) {
 queueWithCapacity.enqueue(new Object());
 }
 assertEquals(queueWithCapacity.dequeue(), queueWithCapacity.dequeue());
 assertEquals(queueWithCapacity.dequeue(), queueWithCapacity.dequeue());
 assertNull(queueWithCapacity.dequeue());
 }

 @Test
 public void testRemove() {
 // Test removing with null object
 assertFalse(queue.remove(null));
 // Test removing valid object
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.remove(object1);
 assertFalse(queue.remove(object1));
 // Test removing multiple occurrences of valid object
 queue.enqueue(object1);
 queue.enqueue(object1);
 queue.enqueue(object2);
 int removedCount = queue.remove(object1);
 assertEquals(2, removedCount);
 // Test removing valid object from empty queue
 assertFalse(queue.remove(object1));
 }

 @Test
 public void testRefreshElement() {
 // Test refreshing element with null object
 assertFalse(queue.refreshElement(null));
 // Test refreshing valid element
 queue.enqueue(object1);
 queue.refreshElement(object1);
 assertEquals(object1, queue.getObjects().lastElement());
 }

 @Test
 public void testGetNumberItems() {
 // Test getting number of items in empty queue
 assertEquals(0, queue.getNumberItems());
 // Test getting number of items in queue with multiple objects
 queue.enqueue(object1);
 queue.enqueue(object2);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 // Test getting peak number of items in empty queue
 assertEquals(-1, queue.getPeakNumberItems());
 // Test getting peak number of items in queue with multiple objects
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 // Test getting objects from empty queue
 assertEquals(0, queue.getObjects().size());
 // Test getting objects from queue with multiple objects
 queue.enqueue(object1);
 queue.enqueue(object2);
 Vector expected = new Vector();
 expected.add(object1);
 expected.add(object2);
 assertEquals(expected, queue.getObjects());
 }

 @Test
 public void testMaxCapacityExceeded() {
 // Test max capacity not exceeded
 assertFalse(queue.maxCapacityExceeded());
 // Test max capacity exceeded
 queue.enqueue(new Object[4]);
 assertTrue(queue.maxCapacityExceeded());
 }
}