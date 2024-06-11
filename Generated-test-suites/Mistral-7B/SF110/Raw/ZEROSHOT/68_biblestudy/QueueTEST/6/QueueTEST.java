import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class QueueTest {

 private Queue queue;
 private Object obj1, obj2, obj3;

 @Before
 public void setUp() {
 queue = new Queue();
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
 public void testQueueConstructor() {
 Queue queue = new Queue(10);
 assertEquals(10, queue.getMaxCapacity());
 }

 @Test
 public void testIsEmpty() {
 queue.enqueue(obj1);
 assertFalse(queue.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 fail("Should not be able to enqueue null object");
 }

 @Test
 public void testEnqueue() {
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
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testDequeue() {
 Object obj = null;
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 obj = queue.dequeue();
 assertEquals(obj1, obj);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj2);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Vector vector = queue.getObjects();
 assertEquals(3, vector.size());
 }

 @Test
 public void testMaxCapacityExceeded() {
 queue.enqueue(new Object[11]);
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 String string = queue.toString();
 assertTrue(string.contains("numItems=" + 3));
 assertTrue(string.contains("getObjects()=" + "[Ljava.lang.Object;@"));
 }

 @After
 public void tearDown() {
 queue = null;
 obj1 = null;
 obj2 = null;
 obj3 = null;
 }
}