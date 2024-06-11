import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

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
 public void testQueueCreation() {
 Queue emptyQueue = new Queue();
 Queue capacityQueue = new Queue(3);
 Assert.assertEquals(0, emptyQueue.getNumberItems());
 Assert.assertEquals(3, capacityQueue.getPeakNumberItems());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(obj1);
 Assert.assertEquals(1, queue.getNumberItems());
 queue.enqueue(obj2);
 Assert.assertEquals(2, queue.getNumberItems());
 queue.enqueue(obj3);
 Assert.assertEquals(3, queue.getNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Vector objects = queue.getObjects();
 Assert.assertEquals(3, objects.size());
 Assert.assertEquals(obj1, objects.get(0));
 Assert.assertEquals(obj2, objects.get(1));
 Assert.assertEquals(obj3, objects.get(2));
 }

 @Test
 public void testDequeue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Object obj = queue.dequeue();
 Assert.assertEquals(obj1, obj);
 Assert.assertEquals(2, queue.getNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueEmptyQueue() {
 queue.dequeue();
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj2);
 Assert.assertEquals(2, queue.getNumberItems());
 Assert.assertFalse(queue.contains(obj2));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testRemoveNullObject() {
 queue.remove(null);
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 Assert.assertEquals(2, queue.getNumberItems());
 Assert.assertFalse(queue.contains(obj1));
 Assert.assertTrue(queue.contains(obj2));
 queue.refreshElement(obj1);
 Assert.assertEquals(3, queue.getNumberItems());
 Assert.assertTrue(queue.contains(obj1));
 }

 @Test
 public void testIsEmpty() {
 Queue emptyQueue = new Queue();
 Queue notEmptyQueue = new Queue();
 notEmptyQueue.enqueue(obj1);
 Assert.assertTrue(emptyQueue.isEmpty());
 Assert.assertFalse(notEmptyQueue.isEmpty());
 }

 @Test
 public void testMaxCapacityExceeded() {
 Queue emptyQueue = new Queue();
 Queue capacityQueue = new Queue(3);
 Queue maxCapacityQueue = new Queue(2);
 Assert.assertFalse(emptyQueue.maxCapacityExceeded());
 Assert.assertFalse(capacityQueue.maxCapacityExceeded());
 maxCapacityQueue.enqueue(obj1);
 maxCapacityQueue.enqueue(obj2);
 Assert.assertTrue(maxCapacityQueue.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 String toString = queue.toString();
 String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Ljava.lang.Object;@22d935e, Ljava.lang.Object;@562d33b, Ljava.lang.Object;@78b9a2f]";
 Assert.assertEquals(expected, toString);
 }
}