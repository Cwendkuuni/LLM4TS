import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

 private Queue<Integer> queue;

 @Before
 public void setup() {
 queue = new Queue<>(3);
 }

 @Test
 public void testisEmpty() {
 // Test empty queue
 assertTrue(queue.isEmpty());

 // Test non-empty queue
 queue.enqueue(1);
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testenqueue() {
 // Test enqueue with valid objects
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);
 assertEquals(3, queue.getNumberItems());

 // Test enqueue with null object
 assertNull(queue.first);
 try {
 queue.enqueue(null);
 } catch (NullPointerException e) {
 // Expected
 }
 }

 @Test
 public void testdequeue() {
 // Test dequeue with empty queue
 assertNull(queue.dequeue());

 // Test dequeue with non-empty queue
 queue.enqueue(1);
 queue.enqueue(2);
 queue.dequeue();
 assertEquals(1, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 }

 @Test
 public void testremove() {
 // Test remove with empty queue
 assertEquals(0, queue.remove(1));

 // Test remove with single object in queue
 queue.enqueue(1);
 assertEquals(1, queue.remove(1));
 assertEquals(0, queue.getNumberItems());

 // Test remove with multiple objects in queue
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);
 assertEquals(1, queue.remove(1));
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testrefreshElement() {
 // Test refreshElement with empty queue
 queue.refreshElement(1);
 assertTrue(queue.isEmpty());

 // Test refreshElement with non-empty queue
 queue.enqueue(1);
 queue.enqueue(2);
 queue.refreshElement(1);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testgetObjects() {
 // Test getObjects with empty queue
 Vector<Integer> expected = new Vector<>();
 assertEquals(expected, queue.getObjects());

 // Test getObjects with populated queue
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);
 Vector<Integer> actual = queue.getObjects();
 assertEquals(actual, new Vector<>(asList(1, 2, 3)));
 }

 @Test
 public void testmaxCapacityExceeded() {
 // Test queue with no max capacity
 queue = new Queue<>();
 assertFalse(queue.maxCapacityExceeded());
 queue.enqueue(1);
 queue.enqueue(2);
 assertFalse(queue.maxCapacityExceeded());
 queue.enqueue(3);
 assertFalse(queue.maxCapacityExceeded());
 queue.enqueue(4); // Exceeds max capacity
 assertTrue(queue.maxCapacityExceeded());

 // Test queue with max capacity
 queue = new Queue<>(2);
 queue.enqueue(1);
 queue.enqueue(2);
 assertFalse(queue.maxCapacityExceeded());
 queue.enqueue(3); // Exceeds max capacity
 assertTrue(queue.maxCapacityExceeded());
 }
}