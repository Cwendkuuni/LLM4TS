package bible.util;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {

 private Queue<Object> queue;

 @Test
 public void testQueueEmpty() {
 Queue<Object> emptyQueue = new Queue<>();
 assertTrue(emptyQueue.isEmpty());
 assertEquals(0, emptyQueue.getNumberItems());
 }

 @Test
 public void testQueueInitialization() {
 Queue<Object> initializationQueue = new Queue<>(5);
 assertFalse(initializationQueue.isEmpty());
 assertEquals(0, initializationQueue.getNumberItems());
 assertEquals(5, initializationQueue.getPeakNumberItems());
 }

 @Test
 public void testEnqueue() {
 queue = new Queue<Object>();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");
 assertEquals(3, queue.getNumberItems());
 Vector<Object> objects = queue.getObjects();
 assertEquals(3, objects.size());
 assertEquals("A", objects.get(0));
 assertEquals("B", objects.get(1));
 assertEquals("C", objects.get(2));
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue = new Queue<Object>();
 queue.enqueue(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testMaxCapacity() {
 queue = new Queue<>(3);
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");
 queue.enqueue("D");
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testDequeue() {
 queue = new Queue<Object>();
 queue.enqueue("A");
 queue.enqueue("B");
 Object dequeuedObject = queue.dequeue();
 assertEquals("A", dequeuedObject);
 assertEquals(1, queue.getNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueEmptyQueue() {
 queue = new Queue<Object>();
 queue.dequeue();
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testRemove() {
 queue = new Queue<Object>();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");
 queue.remove("B");
 assertEquals(2, queue.getNumberItems());
 Vector<Object> objects = queue.getObjects();
 assertEquals("A", objects.get(0));
 assertEquals("C", objects.get(1));
 }

 @Test(expected = IllegalStateException.class)
 public void testRemoveNullObject() {
 queue = new Queue<Object>();
 queue.enqueue("A");
 queue.remove(null);
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testRefreshElement() {
 queue = new Queue<Object>();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.refreshElement("A");
 Vector<Object> objects = queue.getObjects();
 assertEquals(2, objects.size());
 assertEquals("B", objects.get(0));
 assertEquals("A", objects.get(1));
 }

 @Test
 public void testToString() {
 queue = new Queue<Object>();
 queue.enqueue("A");
 queue.enqueue("B");
 queue.enqueue("C");
 String toString = queue.toString();
 assertTrue(toString.contains("Queue"));
 assertTrue(toString.contains("numItems=" + 3));
 assertTrue(toString.contains("maxNumItems=" + 3));
 assertTrue(toString.contains("maxCapacity=" + 3));
 assertTrue(toString.contains("getObjects()="));
 }

}