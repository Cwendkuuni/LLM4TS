import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

 private Queue<Integer> queue;

 @Before
 public void setUp() {
 queue = new Queue<>(5);
 }

 @Test
 public void testIsEmpty() {
 Queue<Integer> emptyQueue = new Queue<>();
 assertTrue(emptyQueue.isEmpty());

 queue.isEmpty(); // test method call
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 assertEquals(3, queue.getNumberItems());

 Queue<Integer> fullQueue = new Queue<>(0);
 fullQueue.enqueue(1); // max capacity exceeded
 assertEquals(1, fullQueue.getNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNull() {
 queue.enqueue(null);
 fail("Expected NullPointerException but it didn't occur.");
 }

 @Test
 public void testDequeue() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 assertEquals(1, queue.dequeue().intValue());
 assertEquals(2, queue.dequeue().intValue());
 assertEquals(3, queue.dequeue().intValue());
 assertEquals(0, queue.getNumberItems());

 Queue<Integer> emptyQueue = new Queue<>();
 queue.dequeue(); // test method call
 assertEquals(0, queue.getNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueWhenEmpty() {
 queue.dequeue(); // test method call
 fail("Expected IllegalStateException but it didn't occur.");
 }

 @Test
 public void testRemove() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 assertEquals(2, queue.remove((Integer) 2).intValue());
 assertEquals(2, queue.getNumberItems());

 assertEquals(-1, queue.remove((Integer) 1).intValue());
 assertEquals(1, queue.getNumberItems());

 Queue<Integer> emptyQueue = new Queue<>();
 queue.remove((Integer) 1); // test method call
 assertEquals(0, queue.getNumberItems());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveWrongObject() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 queue.remove((Object) 4); // test method call
 fail("Expected IllegalArgumentException but it didn't occur.");
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 queue.refreshElement(2);
 assertEquals(3, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());

 assertArrayEquals(new Object[] {1, 2, 3}, queue.getObjects().toArray());
 }

 @Test
 public void testGetNumberItems() {
 Queue<Integer> emptyQueue = new Queue<>();
 assertEquals(0, emptyQueue.getNumberItems());

 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 Object[] expectedObjects = {1, 2, 3};
 assertEquals(expectedObjects, queue.getObjects().toArray());
 }

 @Test
 public void testMaxCapacityExceeded() {
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);
 queue.enqueue(4);
 queue.enqueue(5); // max capacity exceeded

 assertTrue(queue.maxCapacityExceeded());
 }
}