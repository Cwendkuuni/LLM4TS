import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import bible.util.Queue;
import bible.util.Queue.Node;

public class QueueTest {

 private Queue<Integer> queue;

 @Before
 public void setUp() {
 queue = new Queue<>(5);
 }

 @Test
 public void testIsEmpty() {
 // Test empty queue
 assertTrue(queue.isEmpty());
 // Test non-empty queue
 queue.enqueue(1);
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 // Test enqueue with null object
 assertNull(queue.first);
 assertThrows(NullPointerException.class, () -> queue.enqueue(null));
 // Test enqueue with valid object
 Node node = new Node(1);
 queue.enqueue(1);
 assertEquals(node, queue.first);
 assertEquals(node, queue.last);
 assertEquals(1, queue.getNumberItems());
 // Test enqueue beyond max capacity
 for (int i = 0; i < Queue.NO_MAXIMUM; i++) {
 queue.enqueue(i);
 }
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testDequeue() {
 // Test dequeue from empty queue
 assertNull(queue.dequeue());
 // Test dequeue from non-empty queue
 queue.enqueue(1);
 assertEquals(1, queue.dequeue().intValue());
 // Test dequeue multiple times
 queue.enqueue(1);
 queue.enqueue(2);
 assertEquals(1, queue.dequeue().intValue());
 assertEquals(2, queue.dequeue().intValue());
 // Test dequeue beyond max capacity
 for (int i = 0; i < Queue.NO_MAXIMUM; i++) {
 queue.enqueue(i);
 }
 assertThrows(RuntimeException.class, () -> queue.dequeue());
 }

 @Test
 public void testRemove() {
 // Test remove with null object
 assertThrows(NullPointerException.class, () -> queue.remove(null));
 // Test remove with non-existent object
 queue.enqueue(1);
 assertThrows(NoSuchElementException.class, () -> queue.remove(2));
 // Test remove with existing object
 queue.enqueue(1);
 queue.enqueue(2);
 queue.remove(1);
 assertNull(queue.first);
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testRefreshElement() {
 // Test refresh element with null object
 assertThrows(NullPointerException.class, () -> queue.refreshElement(null));
 // Test refresh element with non-existent object
 queue.enqueue(1);
 assertThrows(NoSuchElementException.class, () -> queue.refreshElement(2));
 // Test refresh element with existing object
 queue.enqueue(1);
 queue.enqueue(2);
 queue.refreshElement(1);
 assertEquals(2, queue.getNumberItems());
 assertEquals(1, queue.getPeakNumberItems());
 }

 @Test
 public void testGetNumberItems() {
 assertEquals(0, queue.getNumberItems());
 queue.enqueue(1);
 assertEquals(1, queue.getNumberItems());
 queue.enqueue(2);
 queue.enqueue(3);
 assertEquals(4, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 assertEquals(-1, queue.getPeakNumberItems());
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 // Test getObjects with empty queue
 assertNotNull(queue.getObjects());
 assertEquals(0, queue.getObjects().size());
 // Test getObjects with non-empty queue
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);
 Object[] expected = {1, 2, 3};
 assertArrayEquals(expected, queue.getObjects().toArray());
 }

 @Test
 public void testMaxCapacityExceeded() {
 assertFalse(queue.maxCapacityExceeded());
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);
 queue.enqueue(4);
 queue.enqueue(5);
 assertTrue(queue.maxCapacityExceeded());
 }

 @After
 public void tearDown() {
 queue = null;
 }
}