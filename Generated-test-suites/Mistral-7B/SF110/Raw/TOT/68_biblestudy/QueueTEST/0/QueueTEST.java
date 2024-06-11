import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;

public class QueueTest {

 private Queue<Integer> queue;

 @Test
 public void testQueueEmpty() {
 Queue<Integer> emptyQueue = new Queue<>();
 Assert.assertTrue(emptyQueue.isEmpty());
 Assert.assertEquals(0, emptyQueue.getNumberItems());
 }

 @Test
 public void testQueueInitialization() {
 Queue<Integer> initializedQueue = new Queue(5);
 Assert.assertFalse(initializedQueue.isEmpty());
 Assert.assertEquals(0, initializedQueue.getNumberItems());
 Assert.assertEquals(5, initializedQueue.getPeakNumberItems());
 }

 @Test
 public void testQueueEnqueue() {
 queue = new Queue<>();
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 Assert.assertFalse(queue.isEmpty());
 Assert.assertEquals(3, queue.getNumberItems());
 Assert.assertEquals(3, queue.getPeakNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testQueueEnqueueNullObject() {
 queue = new Queue<>();
 queue.enqueue(null);
 }

 @Test
 public void testQueueDequeue() {
 queue = new Queue<>();
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 Object dequeuedObject = queue.dequeue();
 Assert.assertEquals(1, dequeuedObject.intValue());

 Assert.assertEquals(2, queue.getNumberItems());
 Assert.assertEquals(3, queue.getPeakNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testQueueDequeueEmptyQueue() {
 queue = new Queue<>();
 queue.dequeue();
 }

 @Test
 public void testQueueRemove() {
 queue = new Queue<>();
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 queue.remove(2);

 Assert.assertEquals(2, queue.getNumberItems());
 Assert.assertEquals(3, queue.getPeakNumberItems());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueueRemoveNullObject() {
 queue = new Queue<>();
 queue.enqueue(1);
 queue.remove(null);
 }

 @Test
 public void testQueueRefreshElement() {
 queue = new Queue<>();
 queue.enqueue(1);
 queue.enqueue(2);
 queue.refreshElement(1);

 Assert.assertEquals(2, queue.getNumberItems());
 Assert.assertEquals(2, queue.getPeakNumberItems());
 }

 @Test
 public void testQueueMaxCapacityExceeded() {
 Queue<Integer> queueWithMaxCapacity = new Queue(4);
 queueWithMaxCapacity.enqueue(1);
 queueWithMaxCapacity.enqueue(2);
 queueWithMaxCapacity.enqueue(3);
 queueWithMaxCapacity.enqueue(4);
 queueWithMaxCapacity.enqueue(5);

 Assert.assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
 }

 @Test
 public void testQueueToString() {
 queue = new Queue<>();
 queue.enqueue(1);
 queue.enqueue(2);
 queue.enqueue(3);

 Assert.assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[1, 2, 3],]", queue.toString());
 }

}