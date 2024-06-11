import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertEquals(0, queue.maxCapacity);
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);

        Queue queueWithCapacity = new Queue(5);
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
        Assert.assertEquals(0, queueWithCapacity.getPeakNumberItems());
        Assert.assertEquals(5, queueWithCapacity.maxCapacity);
        Assert.assertNull(queueWithCapacity.first);
        Assert.assertNull(queueWithCapacity.last);
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());

        queue.enqueue("test");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueObject() {
        Queue queue = new Queue();
        queue.enqueue("test");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("test", queue.getObjects().get(0));
    }

    @Test
    public void testEnqueueMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("test1", queue.getObjects().get(0));
        Assert.assertEquals("test2", queue.getObjects().get(1));
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("test1", dequeuedObject);
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("test2", queue.getObjects().get(0));

        queue.dequeue();
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenQueueIsEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.remove("test1");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("test2", queue.getObjects().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.remove("test2");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.refreshElement("test1");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("test1", queue.getObjects().get(0));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());
        queue.enqueue("test");
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Vector objects = queue.getObjects();
        Assert.assertEquals(2, objects.size());
        Assert.assertEquals("test1", objects.get(0));
        Assert.assertEquals("test2", objects.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue(5);
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=5, getObjects()=[test1, test2],]\r\n", queue.toString());
    }
}