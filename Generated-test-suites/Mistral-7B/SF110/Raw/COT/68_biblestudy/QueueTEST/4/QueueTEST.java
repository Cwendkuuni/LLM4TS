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
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);

        Queue queueWithCapacity = new Queue(5);
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
        Assert.assertEquals(0, queueWithCapacity.getPeakNumberItems());
        Assert.assertNull(queueWithCapacity.first);
        Assert.assertNull(queueWithCapacity.last);
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());

        queue.enqueue("test");
        Assert.assertFalse(queue.isEmpty());

        queue.dequeue();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());

        Vector objects = queue.getObjects();
        Assert.assertEquals("test1", objects.get(0));
        Assert.assertEquals("test2", objects.get(1));
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("test1", dequeuedObject);

        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());

        dequeuedObject = queue.dequeue();
        Assert.assertEquals("test2", dequeuedObject);

        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        queue.remove("test1");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());

        Vector objects = queue.getObjects();
        Assert.assertEquals("test2", objects.get(0));

        queue.remove("test2");
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveWhenEmpty() {
        Queue queue = new Queue();
        queue.remove("test");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        queue.refreshElement("test1");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());

        Vector objects = queue.getObjects();
        Assert.assertEquals("test1", objects.get(0));
        Assert.assertEquals("test2", objects.get(1));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        Assert.assertEquals(2, queue.getNumberItems());
        queue.dequeue();
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());

        queue.dequeue();
        Assert.assertEquals(1, queue.getNumberItems());
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

        Assert.assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("test3");

        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        String queueString = queue.toString();
        Assert.assertTrue(queueString.contains("[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]]"));

        Queue queueWithCapacity = new Queue(5);
        queueWithCapacity.enqueue("test1");
        queueWithCapacity.enqueue("test2");

        String queueStringWithCapacity = queueWithCapacity.toString();
        Assert.assertTrue(queueStringWithCapacity.contains("[numItems=2, maxNumItems=2, maxCapacity=5, getObjects()=[test1, test2]]"));
    }
}