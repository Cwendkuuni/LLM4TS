import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Object obj1;
 private Object obj2;
 private Object obj3;

 @Before
 public void setUp() {
 queue = new Queue(3);
 obj1 = new Integer(1);
 obj2 = new Integer(2);
 obj3 = new Integer(3);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(queue.isEmpty());
 queue.enqueue(obj1);
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(3, queue.getNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testDequeue() {
 Object dequeuedObj = queue.dequeue();
 assertEquals(obj1, dequeuedObj);
 assertEquals(2, queue.getNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testDequeueEmptyQueue() {
 queue.dequeue();
 queue.dequeue();
 queue.dequeue();
 queue.dequeue();
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(3, queue.getNumberItems());
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
 queue.refreshElement(obj1);
 assertEquals(1, queue.getNumberItems());
 queue.refreshElement(obj1);
 assertEquals(1, queue.getNumberItems());
 }

 @Test
 public void testGetObjects() {
 Vector vector = queue.getObjects();
 assertEquals(0, vector.size());
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Vector expectedVector = new Vector(3, 10);
 expectedVector.addElement(obj1);
 expectedVector.addElement(obj2);
 expectedVector.addElement(obj3);
 assertEquals(expectedVector, queue.getObjects());
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
 String toString = queue.toString();
 String expectedToString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[1, 2, 3]\r\n]";
 assertEquals(expectedToString, toString);
 }
}