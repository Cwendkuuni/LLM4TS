import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Queue;
import java.util.Vector;
import java.util.concurrent.BlockingQueue;

public class QueueTest {

 private Queue<Object> queue;

 @Before
 public void setUp() {
 queue = new Queue<>();
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testEnqueueAndDequeue() {
 queue.enqueue("apple");
 assertFalse(queue.isEmpty());
 assertEquals("apple", queue.dequeue());
 }

 @Test
 public void testEnqueueAndDequeueMultipleElements() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 assertEquals("apple", queue.dequeue());
 assertEquals("banana", queue.dequeue());
 }

 @Test(expected = NullPointerException.class)
 public void testNullElementInEnqueue() {
 queue.enqueue(null);
 fail("Expected NullPointerException");
 }

 @Test(expected = NullPointerException.class)
 public void testNullElementInDequeue() {
 queue.enqueue("apple");
 queue.enqueue(null);
 queue.dequeue();
 queue.dequeue();
 fail("Expected NullPointerException");
 }

 @Test
 public void testMaxCapacity() {
 Queue<Object> queueMax = new Queue<>(2);
 queueMax.enqueue("apple");
 queueMax.enqueue("banana");
 queueMax.enqueue(null); // this should fail
 assertFalse(queueMax.enqueue("pear"));
 assertEquals(2, queueMax.getNumberItems());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.refreshElement("apple");
 assertEquals("apple", queue.getObjects().get(0));
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("pear");
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("pear");
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("pear");
 Vector<Object> expected = new Vector<>(3, 10);
 expected.add("apple");
 expected.add("banana");
 expected.add("pear");
 assertEquals(expected, queue.getObjects());
 }
}