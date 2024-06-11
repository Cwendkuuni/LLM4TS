import org.junit.Test;
import org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstruction() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertEquals(5, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        Queue queueWithOneElement = new Queue();
        queueWithOneElement.enqueue("one");
        assertFalse(queueWithOneElement.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");
        Queue queueWithMaxCapacity = new Queue(1);
        queueWithMaxCapacity.enqueue("one");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(1, queueWithMaxCapacity.getNumberItems());
        Assert.assertEquals("one", queue.getObjects().get(0));
        Assert.assertEquals("two", queue.getObjects().get(1));
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");
        Object dequeuedElement = queue.dequeue();
        Assert.assertEquals("one", dequeuedElement);
        Assert.assertEquals(1, queue.getNumberItems());
        Object emptyDequeue = queue.dequeue();
        Assert.assertNull(emptyDequeue);
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
        queue.enqueue("one");
        queue.enqueue("two");
        queue.remove("one");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("two", queue.getObjects().get(0));
        queue.remove("two");
        Assert.assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.remove("two");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.refreshElement("one");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("one", queue.getObjects().get(0));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");
        Assert.assertEquals(2, queue.getObjects().size());
        Assert.assertEquals("one", queue.getObjects().get(0));
        Assert.assertEquals("two", queue.getObjects().get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(0);
        queue.enqueue("one");
        queue.enqueue("two");
        Assert.assertFalse(queue.maxCapacityExceeded());
        Queue queueWithMaxCapacity = new Queue(1);
        queueWithMaxCapacity.enqueue("one");
        queueWithMaxCapacity.enqueue("two");
        Assert.assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");
        String expected = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[one, two]]";
        Assert.assertEquals(expected, queue.toString());
    }
}