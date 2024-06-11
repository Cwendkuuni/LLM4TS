import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class QueueTest {

 private Queue queue;
 private Object obj1, obj2, obj3;

 @Before
 public void setup() {
 queue = new Queue();
 obj1 = new Object();
 obj2 = new Object();
 obj3 = new Object();
 }

 @After
 public void teardown() {
 queue = null;
 obj1 = null;
 obj2 = null;
 obj3 = null;
 System.out.println("###Test case finished###");
 }

 @Test
 public void testConstructor() {
 Queue queue = new Queue();
 assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());
 }

 @Test
 public void testQueueWithCapacity() {
 Queue queue = new Queue(3);
 assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=3, getObjects()=[]]", queue.toString());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueWithNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testEnqueueAndDequeue() {
 queue.enqueue(obj1);
 assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[obj1]]", queue.toString());
 queue.dequeue();
 assertEquals("Queue:[numItems=0, maxNumItems=1, maxCapacity=-1, getObjects()=[]]", queue.toString());
 }

 @Test
 public void testEnqueueWithMaxCapacity() {
 Queue queue = new Queue(1);
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=1, getObjects()=[obj3, obj2, obj1]]", queue.toString());
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue queue = new Queue(0);
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testIsEmpty() {
 Queue queue = new Queue();
 assertTrue(queue.isEmpty());
 queue.enqueue(obj1);
 assertFalse(queue.isEmpty());
 queue.dequeue();
 assertTrue(queue.isEmpty());
 }

 @Test
 public void testGetNumberItems() {
 Queue queue = new Queue();
 assertEquals(0, queue.getNumberItems());
 queue.enqueue(obj1);
 assertEquals(1, queue.getNumberItems());
 queue.dequeue();
 assertEquals(0, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 Queue queue = new Queue();
 assertEquals(0, queue.getPeakNumberItems());
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.refreshElement(obj1);
 assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[obj1]]", queue.toString());
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj3);
 assertEquals("Queue:[numItems=2, maxNumItems=3, maxCapacity=-1, getObjects()=[obj1, obj2]]", queue.toString());
 }

 @Test
 public void testRemoveMultiple() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj1);
 queue.remove(obj2);
 assertNull(queue.getObjects().elementAt(0));
 assertNull(queue.getObjects().elementAt(1));
 assertEquals("Queue:[numItems=0, maxNumItems=3, maxCapacity=-1, getObjects()=[]]", queue.toString());
 }

 @Test
 public void testToString() {
 Queue queue = new Queue();
 assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());
 queue.enqueue(obj1);
 assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[obj1]]", queue.toString());
 }

 @Test
 public void testGetObjects() {
 Queue queue = new Queue();
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Vector vector = queue.getObjects();
 assertEquals(3, vector.size());
 assertEquals(obj3, vector.get(0));
 assertEquals(obj2, vector.get(1));
 assertEquals(obj1, vector.get(2));
 }
}