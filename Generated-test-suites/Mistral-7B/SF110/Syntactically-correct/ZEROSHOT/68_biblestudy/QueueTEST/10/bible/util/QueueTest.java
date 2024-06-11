import org.junit.Test;
import org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(5, queueWithCapacity.getNumberItems());
        Assert.assertEquals(-1, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue(new Object());
        assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueObject() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        Queue queueWithCapacity = new Queue(5);
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertEquals(5, queueWithCapacity.getNumberItems());
        Assert.assertEquals(5, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        Object object = queue.dequeue();
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
        Assert.assertEquals(object, queue.dequeue());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
    }

    @Test(expected = RuntimeException.class)
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.remove(new Object());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        queue.remove(new Object());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.remove(new Object());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.refreshElement(new Object());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());
        queue.enqueue(new Object());
        Assert.assertEquals(1, queue.getNumberItems());
        Queue queueWithCapacity = new Queue(5);
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        Assert.assertEquals(5, queueWithCapacity.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        queue.enqueue(new Object());
        Assert.assertEquals(1, queue.getPeakNumberItems());
        Queue queueWithCapacity = new Queue(5);
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        Assert.assertEquals(5, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        Vector objects = queue.getObjects();
        Assert.assertEquals(2, objects.size());
        Assert.assertEquals(queue.get(0), objects.get(0));
        Assert.assertEquals(queue.get(1), objects.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        Assert.assertEquals("Queue:[numItems=0, maxNumItems=-1, maxCapacity=-1, getObjects()=[],]", queue.toString());
        queue.enqueue(new Object());
        Assert.assertEquals("Queue:[numItems=1, maxNumItems=-1, maxCapacity=-1, getObjects()=[],]", queue.toString());
        Queue queueWithCapacity = new Queue(5);
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        queueWithCapacity.enqueue(new Object());
        Assert.assertEquals("Queue:[numItems=5, maxNumItems=5, maxCapacity=5, getObjects()=[Object@1b6d358, Object@64b4a1d3, Object@1b6d358, Object@2e95c6d7, Object@64b4a1d3],]", queueWithCapacity.toString());
    }

}