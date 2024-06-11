import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testQueueCreateWithCapacity() {
        Queue queue = new Queue(5);
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(5, queue.getMaxCapacity());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueDequeue() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("A", dequeuedObject);
        Object dequeuedObject2 = queue.dequeue();
        Assert.assertEquals("B", dequeuedObject2);
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.remove("B");
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("C", dequeuedObject);
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.refreshElement("B");
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("B", dequeuedObject);
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        Assert.assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        Vector objects = queue.getObjects();
        Assert.assertEquals(3, objects.size());
        Assert.assertEquals("A", objects.get(0));
        Assert.assertEquals("B", objects.get(1));
        Assert.assertEquals("C", objects.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        Assert.assertTrue(queue.maxCapacityExceeded());
        queue.dequeue();
        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        String queueString = queue.toString();
        System.out.println(queueString);
        Assert.assertTrue(queueString.contains("Queue:[numItems=3, maxNumItems=3, maxCapacity=5, getObjects()=[A, B, C], "));
    }
}