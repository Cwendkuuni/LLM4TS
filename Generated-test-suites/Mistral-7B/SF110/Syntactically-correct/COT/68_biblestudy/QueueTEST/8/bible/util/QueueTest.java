package bible.util;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {

 private Queue queue;

 @Test
 public void testQueueConstructor() {
 queue = new Queue();
 assertEquals(0, queue.getNumberItems());
 assertEquals(-1, queue.getPeakNumberItems());
 assertEquals(0, queue.maxCapacity);
 }

 @Test
 public void testQueueConstructorWithCapacity() {
 queue = new Queue(5);
 assertEquals(0, queue.getNumberItems());
 assertEquals(0, queue.getPeakNumberItems());
 assertEquals(5, queue.maxCapacity);
 }

 @Test
 public void testIsEmpty() {
 queue = new Queue();
 assertTrue(queue.isEmpty());
 queue.enqueue("First");
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue = new Queue();
 queue.enqueue("First");
 queue.enqueue("Second");
 queue.enqueue("Third");
 assertEquals(3, queue.getNumberItems());
 assertEquals("First", queue.getObjects().get(0));
 assertEquals("Second", queue.getObjects().get(1));
 assertEquals("Third", queue.getObjects().get(2));
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue = new Queue();
 queue.enqueue(null);
 }

 @Test
 public void testDequeue() {
 queue = new Queue();
 queue.enqueue("First");
 queue.enqueue("Second");
 queue.enqueue("Third");
 Object obj = queue.dequeue();
 assertEquals("First", obj);
 assertEquals(2, queue.getNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueEmptyQueue() {
 queue = new Queue();
 queue.dequeue();
 }

 @Test
 public void testRemoveObject() {
 queue = new Queue();
 queue.enqueue("First");
 queue.enqueue("Second");
 queue.enqueue("Third");
 queue.remove("Second");
 assertEquals(2, queue.getNumberItems());
 assertFalse(queue.getObjects().contains("Second"));
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullObject() {
 queue = new Queue();
 queue.enqueue("First");
 queue.remove(null);
 }

 @Test
 public void testRefreshElement() {
 queue = new Queue();
 queue.enqueue("First");
 queue.enqueue("Second");
 queue.refreshElement("First");
 assertEquals("First", queue.getObjects().get(0));
 }

 @Test
 public void testGetNumberItems() {
 queue = new Queue();
 queue.enqueue("First");
 queue.enqueue("Second");
 queue.enqueue("Third");
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue = new Queue();
 queue.enqueue("First");
 queue.enqueue("Second");
 queue.enqueue("Third");
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue = new Queue();
 queue.enqueue("First");
 queue.enqueue("Second");
 queue.enqueue("Third");
 Vector expectedVector = new Vector(3, 10);
 expectedVector.add("First");
 expectedVector.add("Second");
 expectedVector.add("Third");
 assertEquals(expectedVector, queue.getObjects());
 }

 @Test
 public void testMaxCapacityExceeded() {
 queue = new Queue(2);
 queue.enqueue("First");
 queue.enqueue("Second");
 assertFalse(queue.maxCapacityExceeded());
 queue.enqueue("Third");
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue = new Queue();
 queue.enqueue("First");
 queue.enqueue("Second");
 queue.enqueue("Third");
 String expectedString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[First, Second, Third]\r\n]";
 assertEquals(expectedString, queue.toString());
 }

}