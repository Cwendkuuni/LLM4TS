import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue queueMax;
 private Object obj1;
 private Object obj2;
 private Object obj3;

 @Before
 public void setUp() {
 queue = new Queue();
 queueMax = new Queue(3);
 obj1 = new Integer(1);
 obj2 = new Integer(2);
 obj3 = new Integer(3);
 }

 @Test
 public void testQueueEmpty() {
 Queue emptyQueue = new Queue();
 assertTrue(emptyQueue.isEmpty());
 }

 @Test
 public void testQueueConstructor() {
 assertEquals(0, queue.getNumberItems());
 assertEquals(0, queue.getPeakNumberItems());
 assertNull(queue.first);
 assertNull(queue.last);
 }

 @Test
 public void testQueueConstructorMaxCapacity() {
 assertEquals(0, queueMax.getNumberItems());
 assertEquals(0, queueMax.getPeakNumberItems());
 assertNull(queueMax.first);
 assertNull(queueMax.last);
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(obj1);
 assertEquals(1, queue.getNumberItems());
 assertEquals(1, queue.getPeakNumberItems());
 assertNotNull(queue.first);
 assertSame(obj1, queue.first.value);
 assertNull(queue.last);

 queue.enqueue(obj2);
 assertEquals(2, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 assertSame(obj1, queue.first.value);
 assertSame(obj2, queue.last.value);

 queueMax.enqueue(obj3);
 assertEquals(1, queueMax.getNumberItems()); // max capacity exceeded
 assertSame(obj1, queueMax.first.value);
 assertSame(obj3, queueMax.last.value);
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNull() {
 queue.enqueue(null);
 fail("Should throw NullPointerException");
 }

 @Test
 public void testDequeue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 Object dequeued = queue.dequeue();
 assertEquals(obj1, dequeued);
 assertEquals(1, queue.getNumberItems());
 assertEquals(1, queue.getPeakNumberItems());
 assertSame(obj2, queue.first.value);

 queueMax.enqueue(obj1);
 queueMax.enqueue(obj2);
 queueMax.enqueue(obj3);
 Object dequeuedMax1 = queueMax.dequeue();
 assertEquals(obj1, dequeuedMax1);
 assertEquals(2, queueMax.getNumberItems());
 assertEquals(2, queueMax.getPeakNumberItems());
 assertSame(obj2, queueMax.first.value);

 Object dequeuedMax2 = queueMax.dequeue();
 assertEquals(obj2, dequeuedMax2);
 assertEquals(1, queueMax.getNumberItems());
 assertEquals(2, queueMax.getPeakNumberItems());
 assertNull(queueMax.last);
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueEmpty() {
 queue.dequeue();
 fail("Should throw IllegalStateException");
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.refreshElement(obj1);
 queue.refreshElement(obj2);
 queue.refreshElement(obj3);

 int numRemoved = queue.remove(obj3);
 assertEquals(1, numRemoved);
 assertEquals(2, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 assertSame(obj1, queue.first.value);
 assertSame(obj2, queue.last.value);

 numRemoved = queue.remove(obj1);
 assertEquals(1, numRemoved);
 assertEquals(1, queue.getNumberItems());
 assertEquals(1, queue.getPeakNumberItems());
 assertNull(queue.first);
 assertNull(queue.last);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNull() {
 queue.remove(null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 assertEquals(2, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 assertSame(obj1, queue.first.value);
 assertSame(obj2, queue.last.value);
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queueMax.enqueue(obj1);
 queueMax.enqueue(obj2);
 queueMax.enqueue(obj3);
 assertEquals(2, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 assertEquals(3, queueMax.getNumberItems());
 assertEquals(3, queueMax.getPeakNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queueMax.enqueue(obj1);
 queueMax.enqueue(obj2);
 queueMax.enqueue(obj3);
 assertEquals(2, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 assertEquals(3, queueMax.getNumberItems());
 assertEquals(3, queueMax.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 Vector objects = queue.getObjects();
 assertEquals(2, objects.size());
 assertEquals(obj1, objects.get(0));
 assertEquals(obj2, objects.get(1));
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue queueFull = new Queue(2);
 queueFull.enqueue(obj1);
 queueFull.enqueue(obj2);
 queueFull.enqueue(obj3);
 assertTrue(queueFull.maxCapacityExceeded());
 }

 @Test
 public void testMaxCapacityNotExceeded() {
 Queue queueNotFull = new Queue(5);
 queueNotFull.enqueue(obj1);
 queueNotFull.enqueue(obj2);
 queueNotFull.enqueue(obj3);
 assertFalse(queueNotFull.maxCapacityExceeded());
 }
}