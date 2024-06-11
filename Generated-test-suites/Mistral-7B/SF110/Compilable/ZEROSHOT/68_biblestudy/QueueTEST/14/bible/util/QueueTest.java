package bible.util;
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

 @After
 public void tearDown() {
 queue = null;
 obj1 = null;
 obj2 = null;
 obj3 = null;
 }

 @Test
 public void testQueueEmpty() {
 assertFalse(queue.isEmpty());
 queue.isEmpty();
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testQueueConstructor() {
 queue = new Queue(3);
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testIsEmpty() {
 queue.enqueue(obj1);
 assertFalse(queue.isEmpty());
 queue.dequeue();
 assertTrue(queue.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testEnqueueObject() {
 queue.enqueue(obj1);
 assertEquals(1, queue.getNumberItems());
 queue.enqueue(obj2);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testDequeue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Object obj = queue.dequeue();
 assertEquals(obj1, obj);
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testDequeueEmptyQueue() {
 queue.dequeue();
 queue.dequeue();
 queue.dequeue();
 assertNull(queue.dequeue());
 }

 @Test
 public void testRemoveObject() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj2);
 assertEquals(2, queue.getNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testRemoveNullObject() {
 queue.remove(null);
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 assertEquals(2, queue.getNumberItems());
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
 assertEquals(3, queue.getPeakNumberItems());
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
 queue = new Queue(2);
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 String str = queue.toString();
 assertTrue(str.contains("numItems=3"));
 assertTrue(str.contains("maxNumItems=3"));
 assertTrue(str.contains("maxCapacity=-1"));
 }
}