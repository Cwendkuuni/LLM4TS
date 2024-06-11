import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue queueWithMaxCapacity;
 private Object object1;
 private Object object2;
 private Object object3;

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithMaxCapacity = new Queue(3);
 object1 = new Integer(1);
 object2 = new Integer(2);
 object3 = new Integer(3);
 }

 @Test
 public void testIsEmpty() {
 Queue emptyQueue = new Queue();
 assertTrue(emptyQueue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(object1);
 Node expectedFirst = new Node(object1);
 Node expectedLast = new Node(object1);
 assertEquals(expectedFirst, queue.first);
 assertEquals(expectedLast, queue.last);
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testEnqueueWithMaxCapacity() {
 queueWithMaxCapacity.enqueue(object1);
 queueWithMaxCapacity.enqueue(object2);
 queueWithMaxCapacity.enqueue(object3);
 assumeTrue(queueWithMaxCapacity.maxCapacityExceeded());
}

 @Test(expected = NullPointerException.class)
 public void testEnqueueWithNullObject() {
 queue.enqueue(null);
 fail("Expected a NullPointerException");
 }

 @Test
 public void testDequeue() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 Object dequeuedObject = queue.dequeue();
 assertEquals(object1, dequeuedObject);
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testDequeueWhenEmpty() {
 Queue emptyQueue = new Queue();
 try {
 emptyQueue.dequeue();
 fail("Expected an EmptyQueueException");
 } catch (IllegalStateException e) {
 // pass
 }
 }

 @Test
 public void testRemove() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.remove(object1);
 assertEquals(object2, queue.getPeak());
 }

 @Test
 public void testRemoveWithNullObject() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 try {
 queue.remove(null);
 fail("Expected a NullPointerException");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.refreshElement(object1);
 assertEquals(object1, queue.getPeak());
 }

 @Test
 public void testRefreshElementWithNullObject() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 try {
 queue.refreshElement(null);
 fail("Expected a NullPointerException");
 } catch (NullPointerException e) {
 // pass
 }
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);
 Vector expectedVector = new Vector();
 expectedVector.add(object1);
 expectedVector.add(object2);
 expectedVector.add(object3);
 assertEquals(expectedVector, queue.getObjects());
 }

 @Test
 public void testMaxCapacityExceeded() {
 queueWithMaxCapacity.enqueue(object1);
 queueWithMaxCapacity.enqueue(object2);
 queueWithMaxCapacity.enqueue(object3);
 assumeTrue(queueWithMaxCapacity.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);
 String expectedString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[1, 2, 3]\r\n]";
 assertEquals(expectedString, queue.toString());
 }
}