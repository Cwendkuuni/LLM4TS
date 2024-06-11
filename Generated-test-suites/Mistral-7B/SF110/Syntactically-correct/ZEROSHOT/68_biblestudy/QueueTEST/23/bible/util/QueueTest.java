package bible.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueCreation() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);
        Assert.assertEquals(5, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("Test Object", queue.getObjects().get(0));
        queue.enqueue(new Integer(1));
        Assert.assertEquals(2, queue.getNumberItems());
        queue.enqueue(new Double(3.14));
        Assert.assertEquals(3, queue.getNumberItems());
        queue.enqueue(null);
        Assert.assertEquals(4, queue.getNumberItems());
        queue.enqueue(new String("Five"));
        Assert.assertEquals(5, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        Object dequeuedObj1 = queue.dequeue();
        Assert.assertEquals("Test Object 1", dequeuedObj1);
        Assert.assertEquals(2, queue.getNumberItems());
        Object dequeuedObj2 = queue.dequeue();
        Assert.assertEquals("Test Object 2", dequeuedObj2);
        Assert.assertEquals(1, queue.getNumberItems());
        Object dequeuedObj3 = queue.dequeue();
        Assert.assertEquals("Test Object 3", dequeuedObj3);
        Assert.assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        queue.remove("Test Object 2");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("Test Object 1", queue.getObjects().get(0));
        Assert.assertEquals("Test Object 3", queue.getObjects().get(1));
        queue.remove("Test Object 1");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("Test Object 3", queue.getObjects().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveWhenEmpty() {
        Queue queue = new Queue();
        queue.remove("Test Object");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        queue.refreshElement("Test Object 1");
        Assert.assertEquals(3, queue.getNumberItems());
        Assert.assertEquals("Test Object 1", queue.getObjects().get(0));
        Assert.assertEquals("Test Object 2", queue.getObjects().get(1));
        Assert.assertEquals("Test Object 3", queue.getObjects().get(2));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());
        queue.enqueue("Test Object 1");
        Assert.assertEquals(1, queue.getNumberItems());
        queue.enqueue("Test Object 2");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        queue.enqueue("Test Object 4");
        Assert.assertEquals(4, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        Assert.assertEquals(3, queue.getObjects().size());
        Assert.assertEquals("Test Object 1", queue.getObjects().get(0));
        Assert.assertEquals("Test Object 2", queue.getObjects().get(1));
        Assert.assertEquals("Test Object 3", queue.getObjects().get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        Assert.assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Test Object 1, Test Object 2, Test Object 3]]", queue.toString());
    }
}