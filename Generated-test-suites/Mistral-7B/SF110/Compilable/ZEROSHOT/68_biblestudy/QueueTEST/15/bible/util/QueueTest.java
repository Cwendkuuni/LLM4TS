package bible.util;

import java.util.Vector;

import org.junit.Assert;
import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueueCreation() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("Test Object 1", dequeuedObject);
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
    }

    @Test
    public void testMaxCapacityEnqueue() {
        Queue queue = new Queue(3);
        for (int i = 0; i < 4; i++) {
            queue.enqueue(Integer.toString(i));
        }
        Assert.assertEquals(3, queue.getNumberItems());
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        int removedObjects = queue.remove("Test Object 2");
        Assert.assertEquals(1, removedObjects);
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        queue.remove(null);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.refreshElement("Test Object 1");
        Assert.assertEquals("Test Object 1", queue.getObjects().get(0));
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        Vector objects = queue.getObjects();
        Assert.assertEquals(3, objects.size());
        Assert.assertEquals("Test Object 1", objects.get(0));
        Assert.assertEquals("Test Object 2", objects.get(1));
        Assert.assertEquals("Test Object 3", objects.get(2));
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
        queue.enqueue("Test Object");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Test Object 1, Test Object 2, Test Object 3]\r\n]";
        Assert.assertEquals(expected, queue.toString());
    }
}