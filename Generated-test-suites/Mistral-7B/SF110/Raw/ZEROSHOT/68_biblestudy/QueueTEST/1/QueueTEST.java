import org.junit.Before;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class QueueTest {

 private Queue<Object> queue;

 @Before
 public void setUp() {
 queue = new Queue<>();
 }

 @Test
 public void testEmptyQueue() {
 assertTrue(queue.isEmpty());
 assertNull(queue.dequeue());
 }

 @Test
 public void testCreateQueueWithCapacity() {
 queue = new Queue<>(5);
 assertEquals(0, queue.getNumberItems());
 assertEquals(0, queue.getPeakNumberItems());
 }

 @Test
 public void testEnqueueAndDequeue() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 Object dequeued = queue.dequeue();
 assertEquals("apple", dequeued);
 assertEquals(1, queue.getNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testNullEnqueue() {
 queue.enqueue(null);
 queue.enqueue("orange");
 queue.enqueue(null); // this should throw an exception
 }

 @Test
 public void testMultipleEnqueueAndDequeue() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");
 Object dequeued = queue.dequeue();
 assertEquals("apple", dequeued);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.refreshElement("banana");
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testMaxCapacity() {
 queue = new Queue<>(2);
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");
 Vector<Object> objects = queue.getObjects();
 assertEquals(3, objects.size());
 }

 @Test
 public void testRemove() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");
 queue.remove("banana");
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testRemoveNonExistentElement() {
 queue.enqueue("apple");
 queue.enqueue("banana");
 queue.enqueue("orange");
 queue.remove("grape");
 assertEquals(3, queue.getNumberItems());
 }
}