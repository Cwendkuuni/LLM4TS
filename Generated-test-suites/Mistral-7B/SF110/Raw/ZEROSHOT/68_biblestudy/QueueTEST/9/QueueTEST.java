package bible.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class QueueTest {

 private Queue queue;

 @Test
 public void testQueueEmpty() {
 Queue queue = new Queue();
 assertTrue(queue.isEmpty());
 assertEquals(0, queue.getNumberItems());
 }

 @Test
 public void testQueueCreation() {
 Queue queue = new Queue();
 assertNull(queue.first);
 assertNull(queue.last);
 assertEquals(0, queue.getNumberItems());
 }

 @Test
 public void testQueueCreationWithCapacity() {
 Queue queue = new Queue(5);
 assertNull(queue.first);
 assertNull(queue.last);
 assertEquals(0, queue.getNumberItems());
 assertEquals(5, queue.maxCapacity);
 }

 @Test
 public void testEnqueue() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 assertFalse(queue.isEmpty());
 assertEquals(1, queue.getNumberItems());
 assertEquals("apple", queue.getObjects().get(0));
}

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 Queue queue = new Queue();
 queue.enqueue(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testEnqueueMultipleObjects() {
 Queue queue = new Queue(5);
queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("cherry");
 queue.enqueue("date");
 queue.enqueue("elderberry");
 assertEquals(5, queue.getNumberItems());
 assertEquals(5, queue.maxCapacity);
 assertEquals("apple", queue.getObjects().get(0));
 assertEquals("banana", queue.getObjects().get(1));
 assertEquals("cherry", queue.getObjects().get(2));
 assertEquals("date", queue.getObjects().get(3));
 assertEquals("elderberry", queue.getObjects().get(4));
}

 @Test
 public void testDequeue() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 queue.enqueue("banana");
 Object obj = queue.dequeue();
 assertEquals("apple", obj);
 assertEquals(1, queue.getNumberItems());
 assertEquals("banana", queue.getObjects().get(0));
 }

 @Test(expected = EmptyStackException.class)
 public void testDequeueEmptyQueue() {
 Queue queue = new Queue();
 queue.dequeue();
 fail("Should throw EmptyStackException");
 }

 @Test
 public void testRemove() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.remove("apple");
 assertEquals("banana", queue.getObjects().get(0));
 assertEquals(1, queue.getNumberItems());
}

 @Test(expected = NoSuchElementException.class)
 public void testRemoveNonExistentObject() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 queue.remove("banana");
 fail("Should throw NoSuchElementException");
 }

 @Test
 public void testRefreshElement() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.refreshElement("apple");
 assertEquals("apple", queue.getObjects().get(0));
 }

 @Test
 public void testGetNumberItems() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("cherry");
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("cherry");
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("cherry");
 assertEquals("[apple, banana, cherry]", queue.getObjects().toString());
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue queue = new Queue(2);
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("cherry");
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 Queue queue = new Queue();
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("cherry");
 assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[apple, banana, cherry]\r\n]", queue.toString());
 }

}