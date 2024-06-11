import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstruction() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(10);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(10, queueWithCapacity.getMaxCapacity());
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
        Assert.assertEquals(-1, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
        queue.enqueue(new Object());
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        Object obj = queue.dequeue();
        Assert.assertEquals(new Object(), obj);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
    }

    @Test
    public void testEnqueueAndGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        Vector vector = queue.getObjects();
        Assert.assertEquals(3, vector.size());
        for (int i = 0; i < vector.size(); i++) {
            Assert.assertEquals(new Object(), vector.get(i));
        }
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        int removed = queue.remove(new Object());
        Assert.assertEquals(1, removed);
        Assert.assertEquals(2, queue.getNumberItems());
        Object obj = queue.dequeue();
        Assert.assertEquals(new Object(), obj);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.refreshElement(new Object());
        Assert.assertEquals(new Object(), queue.peek());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue(new Object());
        }
        Assert.assertTrue(queue.maxCapacityExceeded());
    }
}