package bible.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.*;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;
 private Object obj1;
 private Object obj2;
 private Object obj3;

 @Before
 public void setUp() {
 queue = new Queue();
 queueWithCapacity = new Queue(3);
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
 public void testQueueIsNotEmpty() {
 queue.enqueue(obj1);
 assertFalse(queue.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullObject() {
 queue.enqueue(null);
 }

 @Test
 public void testEnqueueObjects() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 assertEquals(3, queue.getNumberItems());
 }

 @Test
 public void testEnqueueExceedsMaxCapacity() {
 queueWithCapacity.enqueue(obj1);
 queueWithCapacity.enqueue(obj2);
 queueWithCapacity.enqueue(obj3);
 queueWithCapacity.enqueue(obj4); // this will fail
 assertFalse(queueWithCapacity.isEmpty());
 assertTrue(queueWithCapacity.maxCapacityExceeded());
 }

 @Test
 public void testDequeue() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 Object obj1Ret = queue.dequeue();
 assertEquals(obj1, obj1Ret);
 Object obj2Ret = queue.dequeue();
 assertEquals(obj2, obj2Ret);
 assertNull(queue.dequeue());
 }

 @Test
 public void testDequeueEmpty() {
 queue.dequeue();
 queue.dequeue();
 assertNull(queue.dequeue());
 }

 @Test
 public void testRemove() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 queue.remove(obj1);
 assertEquals(2, queue.getNumberItems());
 queue.remove(obj2);
 assertEquals(1, queue.getNumberItems());
 queue.remove(obj3);
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testRemoveNullObject() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.remove(null);
 assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testRemoveNonExistingObject() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 int originalNumItems = queue.getNumberItems();
 queue.remove(obj3);
 assertEquals(originalNumItems, queue.getNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 Vector objects = queue.getObjects();
 assertEquals(3, objects.size());
 Object obj1Ret = (Object) objects.get(0);
 assertEquals(obj1, obj1Ret);
 Object obj2Ret = (Object) objects.get(1);
 assertEquals(obj2, obj2Ret);
 Object obj3Ret = (Object) objects.get(2);
 assertEquals(obj3, obj3Ret);
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
 public void testMaxCapacityExceeded() {
 queueWithCapacity.enqueue(obj1);
 queueWithCapacity.enqueue(obj2);
 queueWithCapacity.enqueue(obj3);
 queueWithCapacity.enqueue(obj4); // this will fail
 assertTrue(queueWithCapacity.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue.enqueue(obj1);
 queue.enqueue(obj2);
 queue.enqueue(obj3);
 String queueString = queue.toString();
 assertTrue(queueString.contains("Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[obj1, obj2, obj3]"));
 }

 @After
 public void tearDown() {
 queue = null;
 queueWithCapacity = null;
 obj1 = null;
 obj2 = null;
 obj3 = null;
 }
}