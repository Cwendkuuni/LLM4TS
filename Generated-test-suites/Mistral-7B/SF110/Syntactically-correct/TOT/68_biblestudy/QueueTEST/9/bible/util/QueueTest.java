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
 public void testIsEmpty() {
 assertEquals(true, queue.isEmpty());
 queue.enqueue(obj1);
 assertEquals(false, queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(obj1, queue.dequeue());
 assertEquals(obj2, queue.dequeue());
 assertEquals(obj3, queue.dequeue());
 assertEquals(0, queue.getNumberItems());

 queueMax.enqueue(obj1);
 queueMax.enqueue(obj2);
 queueMax.enqueue(obj3);
 assertEquals(3, queueMax.getPeakNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 }

 @Test(expected = IllegalStateException.class)
 public void testMaxCapacityExceeded() {
 queueMax.enqueue(obj1);
 queueMax.enqueue(obj2);
 queueMax.enqueue(obj3);
 queueMax.enqueue(obj3); // should throw an exception
 }

 @Test
 public void testGetObjects() {
 Vector expectedVector = new Vector(3, 10);
 expectedVector.addElement(obj1);
 expectedVector.addElement(obj2);
 expectedVector.addElement(obj3);
 assertEquals(expectedVector, queueMax.getObjects());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.refreshElement(obj1);
 assertEquals(obj1, queue.getObjects().get(0));
 }

 @Test(expected = NoSuchElementException.class)
 public void testDequequeueEmptyQueue() {
 queue.dequeue();
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveNullObject() {
 queue.enqueue(obj1);
 queue.remove(null);
 }

 @Test
 public void testRemoveObject() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj3);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testRemoveObjectMultipleTimes() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj3);
 queue.remove(obj3);
 assertEquals(1, queue.getNumberItems());
 }
}