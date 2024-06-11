import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue emptyQueue;
 private Node node1;
 private Node node2;
 private Node node3;
 private Object object1;
 private Object object2;
 private Object object3;

 @Before
 public void setUp() {
 queue = new Queue();
 emptyQueue = new Queue();
 object1 = new Object();
 object2 = new Object();
 object3 = new Object();
 node1 = new Node(object1);
 node2 = new Node(object2);
 node3 = new Node(object3);
 }

 @Test
 public void testIsEmpty() {
 assertTrue(emptyQueue.isEmpty());
 assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueue() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);

 assertEquals(3, queue.getNumberItems());
 assertEquals(3, queue.getPeakNumberItems());

 Node firstNode = queue.first;
 Node lastNode = queue.last;

 assertEquals(object1, firstNode.value);
 assertEquals(object3, lastNode.value);
 }

 @Test
 public void testDequeue() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);

 Object dequeuedObject = queue.dequeue();
 assertEquals(object1, dequeuedObject);

 assertEquals(2, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testDequeueNullObject() {
 queue.enqueue(object1);
 queue.dequeue();
 queue.dequeue(null);
 }

 @Test
 public void testRemove() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);

 queue.remove(object2);
 assertEquals(2, queue.getNumberItems());
 assertEquals(2, queue.getPeakNumberItems());
 }

 @Test
 public void testRemoveObjectNotInQueue() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);

 queue.remove(new Object());
 assertEquals(3, queue.getNumberItems());
 assertEquals(3, queue.getPeakNumberItems());
 }

 @Test
 public void testRefreshElement() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);

 queue.refreshElement(object2);
 assertEquals(3, queue.getNumberItems());
 assertEquals(3, queue.getPeakNumberItems());

 Node firstNode = queue.first;
 Node lastNode = queue.last;

 assertEquals(object1, firstNode.value);
 assertEquals(object3, lastNode.value);

 Node object2Node = null;
 for (int i = 0; i < queue.getNumberItems(); i++) {
 Object currentObject = queue.dequeue();
 if (currentObject.equals(object2)) {
 object2Node = queue.last;
 break;
 }
 queue.enqueue(currentObject);
 }

 assertEquals(object2, object2Node.value);
 }

 @Test
 public void testGetObjects() {
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);

 Vector expectedVector = new Vector(3);
 expectedVector.addElement(object1);
 expectedVector.addElement(object2);
 expectedVector.addElement(object3);

 Vector actualVector = queue.getObjects();
 assertEquals(expectedVector, actualVector);
 }

 @Test
 public void testMaxCapacityExceeded() {
 queue = new Queue(1);
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);

 assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testMaxCapacityNotExceeded() {
 queue = new Queue(3);
 queue.enqueue(object1);
 queue.enqueue(object2);
 queue.enqueue(object3);
 queue.enqueue(object4);

 assertFalse(queue.maxCapacityExceeded());
 }
}