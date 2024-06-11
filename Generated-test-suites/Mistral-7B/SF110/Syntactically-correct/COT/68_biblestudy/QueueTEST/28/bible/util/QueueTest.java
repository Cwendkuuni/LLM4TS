import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;

public class QueueTest {

 private Queue queue;

 @Test
 public void testQueueConstructor() {
 queue = new Queue();
 Assert.assertEquals(0, queue.getNumberItems());
 }

 @Test
 public void testQueueWithCapacityConstructor() {
 queue = new Queue(5);
 Assert.assertEquals(0, queue.getNumberItems());
 }

 @Test
 public void testIsEmpty() {
 queue = new Queue();
 queue.isEmpty();
 Assert.assertTrue(queue.isEmpty());
 }

 @Test
 public void testEnqueueNullObject() {
 queue = new Queue();
 queue.enqueue(null);
 queue.isEmpty();
 Assert.assertFalse(queue.isEmpty());
 }

 @Test(expected = NullPointerException.class)
 public void testEnqueueNullMethod() {
 queue = new Queue();
 queue.enqueue(null);
 }

 @Test
 public void testEnqueueOneObject() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.isEmpty();
 Assert.assertFalse(queue.isEmpty());
 }

 @Test
 public void testEnqueueMultipleObjects() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.isEmpty();
 Assert.assertFalse(queue.isEmpty());
 }

 @Test(expected = IllegalStateException.class)
 public void testEnqueueWhenFull() {
 queue = new Queue(1);
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 }

 @Test
 public void testDequeueEmptyQueue() {
 queue = new Queue();
 queue.dequeue();
 queue.isEmpty();
 Assert.assertTrue(queue.isEmpty());
 }

 @Test
 public void testDequeueOneObject() {
 queue = new Queue();
 queue.enqueue(new Object());
 Object dequeuedObject = queue.dequeue();
 queue.isEmpty();
 Assert.assertFalse(queue.isEmpty());
 Assert.assertEquals(dequeuedObject, queue.peek());
 }

 @Test
 public void testDequeueMultipleObjects() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 Object firstDequeuedObject = queue.dequeue();
 Object secondDequeuedObject = queue.dequeue();
 queue.isEmpty();
 Assert.assertFalse(queue.isEmpty());
 Assert.assertEquals(firstDequeuedObject, queue.peek());
 }

 @Test(expected = NoSuchElementException.class)
 public void testDequeueEmptyQueueException() {
 queue = new Queue();
 queue.dequeue();
 }

 @Test
 public void testRemoveObjectFromEmptyQueue() {
 queue = new Queue();
 queue.remove(new Object());
 queue.isEmpty();
 Assert.assertTrue(queue.isEmpty());
 }

 @Test
 public void testRemoveObjectFromQueue() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.remove(new Object());
 queue.isEmpty();
 Assert.assertFalse(queue.isEmpty());
 }

 @Test(expected = NoSuchElementException.class)
 public void testRemoveNonExistentObjectFromQueue() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.remove(new Object());
 queue.remove(new Object());
 }

 @Test
 public void testRefreshElement() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.refreshElement(new Object());
 queue.isEmpty();
 Assert.assertFalse(queue.isEmpty());
 }

 @Test
 public void testGetNumberItems() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.isEmpty();
 Assert.assertEquals(2, queue.getNumberItems());
 }

 @Test
 public void testGetPeakNumberItems() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.isEmpty();
 Assert.assertEquals(2, queue.getPeakNumberItems());
 }

 @Test
 public void testGetObjects() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.isEmpty();
 Vector objects = queue.getObjects();
 Assert.assertEquals(2, objects.size());
 }

 @Test
 public void testMaxCapacityExceeded() {
 queue = new Queue(1);
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.isEmpty();
 Assert.assertTrue(queue.maxCapacityExceeded());
 }

 @Test
 public void testToString() {
 queue = new Queue();
 queue.enqueue(new Object());
 queue.enqueue(new Object());
 queue.toString();
 }
}