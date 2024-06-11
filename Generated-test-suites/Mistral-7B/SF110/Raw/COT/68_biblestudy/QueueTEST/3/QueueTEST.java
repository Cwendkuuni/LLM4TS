package bible.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

public class QueueTest {

 private Queue queue;

 @Before
 public void setUp() {
 queue = new Queue();
 }

 @Test
 public void testConstructor() {
 Queue q = new Queue();
 assertEquals(0, q.getNumberItems());
 assertEquals(-1, q.getPeakNumberItems());
 assertEquals(0, q.getNumberOfCapacity());
 }

 @Test
 public void testQueueWithCapacity() {
 Queue q = new Queue(5);
 assertEquals(0, q.getNumberItems());
 assertEquals(0, q.getPeakNumberItems());
 assertEquals(5, q.getNumberOfCapacity());
 }

 @Test
 public void testIsEmpty() {
 Queue q = new Queue();
 assertTrue(q.isEmpty());
 
 queue.enqueue("test");
 assertFalse(q.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testNullEnqueue() {
 queue.enqueue(null);
 fail("Expected NullPointerException");
 }

 @Test
 public void testEnqueue() {
 queue.enqueue("test1");
 queue.enqueue("test2");
 Node node1 = queue.first;
 Node node2 = queue.last;
 assertEquals("test1", node1.value);
 assertEquals("test2", node2.value);
 assertEquals(2, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 }

 @Test(expected = NoSuchElementException.class)
 public void testDequeueWhenEmpty() {
 queue.dequeue();
 fail("Expected NoSuchElementException");
 }

 @Test
 public void testDequeue() {
 queue.enqueue("test1");
 queue.enqueue("test2");
 Object obj = queue.dequeue();
 assertEquals("test1", obj);
 assertEquals(1, queue.getNumberItems());
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveWhenEmpty() {
 queue.remove("test");
 fail("Expected NoSuchElementException");
 }

 @Test
 public void testRemove() {
 queue.enqueue("test1");
 queue.enqueue("test2");
 queue.remove("test1");
 Node node1 = queue.first;
 Node node2 = queue.last;
 assertNotEquals("test1", node1.value);
 assertEquals("test2", node2.value);
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue("test1");
 queue.enqueue("test2");
 queue.refreshElement("test1");
 Node node1 = queue.first;
 Node node2 = queue.last;
 assertEquals("test1", node1.value);
 assertEquals("test2", node2.value);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue("test1");
 queue.enqueue("test2");
 queue.enqueue("test3");
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue("test1");
 queue.enqueue("test2");
 queue.enqueue("test3");
 queue.remove("test1");
 queue.remove("test2");
 assertEquals(0, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue("test1");
 queue.enqueue("test2");
 queue.enqueue("test3");
 Vector vector = queue.getObjects();
 assertEquals(3, vector.size());
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue q = new Queue(2);
 q.enqueue("test1");
 q.enqueue("test2");
 q.enqueue("test3");
 assertTrue(q.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue.enqueue("test1");
 queue.enqueue("test2");
 queue.enqueue("test3");
 String toString = queue.toString();
 assertTrue(toString.contains("Queue:[numItems=3, maxNumItems=3,"));
 }

}