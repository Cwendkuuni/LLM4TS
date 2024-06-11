package bible.util;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

 private Queue queue;

 @Before
 public void setUp() {
 queue = new Queue(5);
 }

 @Test
 public void testIsEmpty() {
 Queue emptyQueue = new Queue();
 assertTrue(emptyQueue.isEmpty());

 Queue nonEmptyQueue = new Queue();
 nonEmptyQueue.enqueue("apple");
 assertFalse(emptyQueue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");

 assertEquals(3, queue.getNumberItems());

 Vector expectedVector = new Vector(3, 10);
 expectedVector.add("apple");
 expectedVector.add("banana");
 expectedVector.add("orange");

 Vector actualVector = queue.getObjects();
 assertEquals(expectedVector, actualVector);
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testMaxCapacity() {
 Queue maxCapacityQueue = new Queue(3);
 maxCapacityQueue.enqueue("apple");
 maxCapacityQueue.enqueue("banana");
 maxCapacityQueue.enqueue("orange");
 maxCapacityQueue.enqueue("grape"); // this should throw an exception
 fail("Expected exception: java.lang.IllegalStateException: maxCapacityExceeded");
 }

 @Test
 public void testDequeue() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");

 Object dequeuedObject = queue.dequeue();
 assertEquals("apple", dequeuedObject);

 queue.dequeue();
 queue.dequeue();

 int numberOfItemsAfterDequeue = queue.getNumberItems();
 assertEquals(0, numberOfItemsAfterDequeue);
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueEmptyQueue() {
 queue.dequeue(); // this should throw an exception
 fail("Expected exception: java.lang.IllegalStateException: Cannot remove when queue is empty");
 }

 @Test
 public void testRemove() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");
 queue.enqueue("grape");

 queue.remove("banana");
 assertEquals(3, queue.getNumberItems());

 Vector expectedVector = new Vector(3, 10);
 expectedVector.add("apple");
 expectedVector.add("orange");
 expectedVector.add("grape");

 Vector actualVector = queue.getObjects();
 assertEquals(expectedVector, actualVector);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullObject() {
 queue.remove(null);
 fail("Expected IllegalArgumentException");
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");
 queue.refreshElement("banana");

 int numberOfItemsAfterRefreshElement = queue.getNumberItems();
 assertEquals(3, numberOfItemsAfterRefreshElement);

 Vector expectedVector = new Vector(3, 10);
 expectedVector.add("apple");
 expectedVector.add("banana");
 expectedVector.add("orange");

 Vector actualVector = queue.getObjects();
 assertEquals(expectedVector, actualVector);
 }

 @Test
 public void testToString() {
 String expectedString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=5, getObjects()=[apple, orange, Queue@6f0b2a5d]]";
 String actualString = queue.toString();
 assertEquals(expectedString, actualString);
}

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");

 assertEquals(3, queue.getPeakNumberItems());
}
}