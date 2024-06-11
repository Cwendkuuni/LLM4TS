import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

 private Queue queue;
 private Queue queueWithCapacity;

 @Test
 public void testQueueEmptyConstructor() {
 queue = new Queue();
 Assert.assertTrue("Queue should be empty", queue.isEmpty());
 Assert.assertEquals("Number of items should be zero", 0, queue.getNumberItems());
 }

 @Test
 public void testQueueWithCapacityConstructor() {
 queueWithCapacity = new Queue(5);
 Assert.assertTrue("Queue should be empty", queueWithCapacity.isEmpty());
 Assert.assertEquals("Number of items should be zero", 0, queueWithCapacity.getNumberItems());
 Assert.assertEquals("Maximum capacity should be 5", 5, queueWithCapacity.getPeakNumberItems());
 }

 @Test
 public void testQueueEnqueue() {
 queue = new Queue();
 queue.enqueue("item1");
 Assert.assertFalse("Queue should not be empty", queue.isEmpty());
 Assert.assertEquals("Number of items should be one", 1, queue.getNumberItems());
 queue.enqueue("item2");
 Assert.assertEquals("Number of items should be two", 2, queue.getNumberItems());
 }

 @Test(expected = NullPointerException.class)
 public void testQueueNullEnqueue() {
 queue = new Queue();
 queue.enqueue(null);
 }

 @Test
 public void testQueueDequeue() {
 queue = new Queue();
 queue.enqueue("item1");
 queue.enqueue("item2");
 Object dequeuedItem = queue.dequeue();
 Assert.assertEquals("Dequeued item should be item1", "item1", dequeuedItem);
 Assert.assertEquals("Number of items should be one after dequeue", 1, queue.getNumberItems());
 }

 @Test(expected = IllegalStateException.class)
 public void testQueueDequeueWhenEmpty() {
 queue = new Queue();
 queue.dequeue();
 }

 @Test
 public void testQueueRemove() {
 queue = new Queue();
 queue.enqueue("item1");
 queue.enqueue("item2");
 queue.enqueue("item3");
 queue.remove("item2");
 Assert.assertEquals("Number of items should be two after removing item2", 2, queue.getNumberItems());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testQueueRemoveNull() {
 queue = new Queue();
 queue.remove(null);
 }

 @Test
 public void testQueueRefreshElement() {
 queue = new Queue();
 queue.enqueue("item1");
 queue.enqueue("item2");
 queue.refreshElement("item2");
 Assert.assertEquals("Number of items should be two after refreshing element", 2, queue.getNumberItems());
 }

 @Test
 public void testQueueMaxCapacityExceeded() {
 queueWithCapacity = new Queue(4);
 queueWithCapacity.enqueue("item1");
 queueWithCapacity.enqueue("item2");
 queueWithCapacity.enqueue("item3");
 queueWithCapacity.enqueue("item4");
 Assert.assertTrue("Queue should indicate maximum capacity exceeded", queueWithCapacity.maxCapacityExceeded());
 }

 @Test
 public void testQueueToString() {
 queue = new Queue();
 queue.enqueue("item1");
 queue.enqueue("item2");
 String queueString = queue.toString();
 String expectedQueueString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[item1, item2]\r\n]";
 Assert.assertEquals("ToString method should match expected string", expectedQueueString, queueString);
 }

 @Test
 public void testQueueGetObjects() {
 queue = new Queue();
 queue.enqueue("item1");
 queue.enqueue("item2");
 Vector queueObjects = queue.getObjects();
 Assert.assertEquals("Size of obtained Vector should match number of items", 2, queueObjects.size());
 Assert.assertEquals("First item in obtained Vector should match first item in queue", "item1", queueObjects.get(0));
 Assert.assertEquals("Second item in obtained Vector should match second item in queue", "item2", queueObjects.get(1));
 }
}