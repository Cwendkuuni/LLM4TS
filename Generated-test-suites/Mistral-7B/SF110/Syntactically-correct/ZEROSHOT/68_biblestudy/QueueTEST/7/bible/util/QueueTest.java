import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;
 private Object obj1;
 private Object obj2;
 private Object obj3;

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithCapacity = new Queue(5);
 obj1 = new Object();
 obj2 = new Object();
 obj3 = new Object();
 }

 @Test
 public void testQueueEmpty() {
 Queue emptyQueue = new Queue();
 assertTrue(emptyQueue.isEmpty());
 }

 @Test
 public void testConstructNoCapacity() {
 Queue emptyQueue = new Queue();
 assertEquals(0, emptyQueue.getNumberItems());
 }

 @Test
 public void testConstructWithCapacity() {
 assertEquals(0, queueWithCapacity.getNumberItems());
 assertEquals(5, queueWithCapacity.getPeakNumberItems());
 }

 @Test
 public void testEnqueueAndDequeue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(obj1, queue.dequeue());
 assertEquals(obj2, queue.dequeue());
 assertEquals(obj3, queue.dequeue());
 assertTrue(queue.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testDequeueWhenEmpty() {
 Queue emptyQueue = new Queue();
 emptyQueue.dequeue();
 assertTrue(emptyQueue.isEmpty());
 }

 @Test
 public void testRemoveObject() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj2);
 assertFalse(queue.contains(obj2));
 assertEquals(2, queue.getNumberItems());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullObject() {
 queue.remove(null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 assertTrue(queue.contains(obj1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRefreshElementNullObject() {
 queue.refreshElement(null);
 fail("Should throw an IllegalArgumentException");
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.enqueue(obj3);
 queue.enqueue(obj3);
 assertEquals(5, queue.getPeakNumberItems());
 }

 @Test
 public void testMaxCapacityExceeded() {
 queueWithCapacity.enqueue(obj1);
 queueWithCapacity.enqueue(obj2);
 queueWithCapacity.enqueue(obj3);
 queueWithCapacity.enqueue(obj3);
 queueWithCapacity.enqueue(obj3);
 queueWithCapacity.enqueue(obj3);
 assertTrue(queueWithCapacity.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 String queueString = queue.toString();
 String expectedQueueString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[obj1, obj2, obj3]\r\n]";
 assertEquals(expectedQueueString, queueString);
 }

 @Test
 public void testGetObjectsAsVector() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Vector vector = queue.getObjects();
 Object[] expectedObjects = {obj1, obj2, obj3};
 assertArrayEquals(expectedObjects, vector.toArray());
 }
}