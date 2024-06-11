import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithCapacity = new Queue(5);
 }

 @Test
 public void testIsEmpty() {
 // test empty queue
 assertTrue(queue.isEmpty());
 // test non-empty queue
 queue.enqueue("apple");
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 // test enqueue null object
 assertNull(queue.dequeue());
 queue.enqueue(null);
 assertNull(queue.dequeue());
 // test enqueue with valid objects
 queue.enqueue("apple");
 queue.enqueue("banana");
 assertEquals("apple", queue.dequeue().toString());
 assertEquals("banana", queue.dequeue().toString());
 // test enqueue beyond capacity
 for (int i = 0; i < 6; i++) {
 queueWithCapacity.enqueue("item" + i);
 }
 assertFalse(queueWithCapacity.enqueue("item6"));
 assertEquals(5, queueWithCapacity.getNumberItems());
 }

 @Test
 public void testDequeue() {
 // test dequeue empty queue
 assertNull(queue.dequeue());
 // test dequeue non-empty queue
 queue.enqueue("apple");
 queue.enqueue("banana");
 assertEquals("apple", queue.dequeue().toString());
 assertEquals("banana", queue.dequeue().toString());
 // test dequeue beyond capacity
 for (int i = 0; i < 6; i++) {
 queueWithCapacity.enqueue("item" + i);
 }
 assertEquals("item0", queueWithCapacity.dequeue().toString());
 assertEquals("item1", queueWithCapacity.dequeue().toString());
 assertNull(queueWithCapacity.dequeue());
 }

 @Test
 public void testRemove() {
 // test remove null object
 queue.remove(null);
 // test remove valid object from empty queue
 assertEquals(0, queue.remove(new Object()));
 // test remove valid object from non-empty queue
 queue.enqueue("apple");
 queue.enqueue("banana");
 assertEquals(1, queue.remove("apple"));
 assertEquals("banana", queue.dequeue().toString());
 // test remove valid object beyond capacity
 for (int i = 0; i < 6; i++) {
 queueWithCapacity.enqueue("item" + i);
 }
 assertEquals(1, queueWithCapacity.remove("item0"));
 assertEquals("item1", queueWithCapacity.dequeue().toString());
 assertEquals(4, queueWithCapacity.getNumberItems());
 }

 @Test
 public void testRefreshElement() {
 // test refresh element with null object
 queue.refreshElement(null);
 // test refresh element with valid object
 queue.enqueue("apple");
 queue.refreshElement("apple");
 assertEquals("apple", queue.dequeue().toString());
 }

 @Test
 public void testGetNumberItems() {
 // test get number of items in empty queue
 assertEquals(0, queue.getNumberItems());
 // test get number of items in non-empty queue
 queue.enqueue("apple");
 queue.enqueue("banana");
 assertEquals(2, queue.getNumberItems());
 // test get number of items beyond capacity
 for (int i = 0; i < 6; i++) {
 queueWithCapacity.enqueue("item" + i);
 }
 assertEquals(5, queueWithCapacity.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 // test get peak number of items in empty queue
 assertEquals(-1, queue.getPeakNumberItems());
 // test get peak number of items in non-empty queue
 queue.enqueue("apple");
 queue.enqueue("banana");
 assertEquals(2, queue.getPeakNumberItems());
 // test get peak number of items beyond capacity
 for (int i = 0; i < 6; i++) {
 queueWithCapacity.enqueue("item" + i);
 }
 assertEquals(5, queueWithCapacity.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 // test get objects from empty queue
 assertEquals(0, queue.getObjects().size());
 // test get objects from non-empty queue
 queue.enqueue("apple");
 queue.enqueue("banana");
 Vector expected = new Vector(2, 10);
 expected.add("apple");
 expected.add("banana");
 assertEquals(expected, queue.getObjects());
 // test get objects beyond capacity
 for (int i = 0; i < 6; i++) {
 queueWithCapacity.enqueue("item" + i);
 }
 Vector expectedCapacity = new Vector(5, 10);
 for (int i = 0; i < 5; i++) {
 expectedCapacity.add("item" + i);
 }
 assertEquals(expectedCapacity, queueWithCapacity.getObjects());
 }

 @Test
 public void testMaxCapacityExceeded() {
 // test max capacity not exceeded
 assertFalse(queue.maxCapacityExceeded());
 // test max capacity exceeded
 for (int i = 0; i < 6; i++) {
 queueWithCapacity.enqueue("item" + i);
 }
 assertTrue(queueWithCapacity.maxCapacityExceeded());
 }
}