import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;
 private Object obj1;
 private Object obj2;
 private Object obj3;

 @Before
 public void setUp() {
 obj1 = new Object();
 obj2 = new Object();
 obj3 = new Object();
 queue = new Queue();
 queueWithCapacity = new Queue(3);
 }

 @Test
 public void testConstructor() {
 Queue q = new Queue();
 assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", q.toString());
 }

 @Test
 public void testConstructorWithCapacity() {
 assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=3, getObjects()=[]]", queueWithCapacity.toString());
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 assertFalse(queueWithCapacity.isEmpty());
 queue.enqueue(obj1);
 assertFalse(queue.isEmpty());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(obj1);
 Node expectedFirst = new Node(obj1);
 Node expectedLast = new Node(obj1);
 Node actualFirst = queue.first;
 Node actualLast = queue.last;
 assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()[0]=" + obj1 + "]", queue.toString());
 assertEquals(expectedFirst, actualFirst);
 assertEquals(expectedLast, actualLast);
 queue.enqueue(obj2);
 Node expectedSecond = new Node(obj2);
 Node actualSecond = queue.last.next;
 assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()[0]=" + obj1 + ", getObjects()[1]=" + obj2 + "]", queue.toString());
 assertEquals(expectedSecond, actualSecond);
 queueWithCapacity.enqueue(obj3);
 assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()[0]=" + obj1 + ", getObjects()[1]=" + obj2 + ", getObjects()[2]=" + obj3 + "]", queueWithCapacity.toString());
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueWhenEmpty() {
 queue.dequeue();
 }

 @Test
 public void testDequeue() {
 Object expectedObj1 = obj1;
 Object actualObj1 = queue.dequeue();
 assertEquals(expectedObj1, actualObj1);
 assertEquals("Queue:[numItems=0, maxNumItems=2, maxCapacity=-1, getObjects()=[]]", queue.toString());
 Object expectedObj2 = obj2;
 Object actualObj2 = queue.dequeue();
 assertEquals(expectedObj2, actualObj2);
 assertEquals("Queue:[numItems=0, maxNumItems=2, maxCapacity=-1, getObjects()=[]]", queue.toString());
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.remove(obj1);
 assertEquals("Queue:[numItems=1, maxNumItems=2, maxCapacity=-1, getObjects()[0]=" + obj2 + "]", queue.toString());
 queue.remove(obj2);
 assertEquals("Queue:[numItems=0, maxNumItems=2, maxCapacity=-1, getObjects()=[]]", queue.toString());
 queueWithCapacity.enqueue(obj1);
 queueWithCapacity.enqueue(obj2);
 queueWithCapacity.enqueue(obj3);
 queueWithCapacity.remove(obj2);
 assertEquals("Queue:[numItems=2, maxNumItems=3, maxCapacity=3, getObjects()[0]=" + obj1 + ", getObjects()[1]=" + obj3 + "]", queueWithCapacity.toString());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.refreshElement(obj1);
 assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()[0]=" + obj1 + "]", queue.toString());
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()[0]=" + obj1 + "]", queue.toString());
 queueWithCapacity.enqueue(obj1);
 queueWithCapacity.enqueue(obj2);
 queueWithCapacity.enqueue(obj3);
 queueWithCapacity.refreshElement(obj2);
 assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()[0]=" + obj1 + ", getObjects()[1]=" + obj3 + ", getObjects()[2]=" + obj2 + "]", queueWithCapacity.toString());
 }

 @Test
 public void testGetNumberItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 assertEquals(2, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 Vector expectedVector = new Vector(){
 {
 add(obj1);
 add(obj2);
 }
 };
 Vector actualVector = queue.getObjects();
 assertEquals(expectedVector, actualVector);
 }

 @Test
 public void testMaxCapacityExceeded() {
 queueWithCapacity.enqueue(obj1);
 queueWithCapacity.enqueue(obj2);
 queueWithCapacity.enqueue(obj3);
 assertTrue(queueWithCapacity.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 Queue q = new Queue();
 assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", q.toString());
 queue.enqueue(obj1);
 assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()[0]=" + obj1 + "]", queue.toString());
 queue.enqueue(obj2);
 assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()[0]=" + obj1 + ", getObjects()[1]=" + obj2 + "]", queue.toString());
 queueWithCapacity.enqueue(obj1);
 queueWithCapacity.enqueue(obj2);
 queueWithCapacity.enqueue(obj3);
 assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()[0]=" + obj1 + ", getObjects()[1]=" + obj2 + ", getObjects()[2]=" + obj3 + "]", queueWithCapacity.toString());
 }
}