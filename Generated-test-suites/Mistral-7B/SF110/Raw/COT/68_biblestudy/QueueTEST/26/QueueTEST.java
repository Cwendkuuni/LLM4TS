import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);

        Queue queueWithCapacity = new Queue(5);
        Assert.assertTrue(queueWithCapacity.isEmpty());
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
        Assert.assertEquals(0, queueWithCapacity.getPeakNumberItems());
        Assert.assertNull(queueWithCapacity.first);
        Assert.assertNull(queueWithCapacity.last);
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());

        queue.enqueue("Item 1");
        Assert.assertFalse(queue.isEmpty());

        queue.dequeue();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("Item 1", queue.getObjects().get(0));
        Assert.assertEquals("Item 2", queue.getObjects().get(1));
        Assert.assertEquals("Item 1", queue.toString());

        Queue queueWithMaxCapacity = new Queue(1);
        queueWithMaxCapacity.enqueue("Item 1");
        queueWithMaxCapacity.enqueue("Item 2");
        queueWithMaxCapacity.enqueue("Item 3");
        Assert.assertEquals(2, queueWithMaxCapacity.getNumberItems());
        Assert.assertEquals(2, queueWithMaxCapacity.getPeakNumberItems());
        Assert.assertTrue(queueWithMaxCapacity.maxCapacityExceeded());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("Item 1", dequeuedObject);
        Assert.assertEquals("Item 2", queue.getObjects().get(0));
        Assert.assertEquals(1, queue.getNumberItems());

        queue.dequeue();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        queue.remove("Item 2");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertFalse(queue.contains("Item 2"));

        queue.remove("Item 1");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("Item 3", queue.getObjects().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullObject() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        queue.refreshElement("Item 1");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("Item 1", queue.getObjects().get(0));
        Assert.assertEquals("Item 2", queue.getObjects().get(1));

        queue.refreshElement("Item 3");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("Item 3", queue.getObjects().get(0));
        Assert.assertEquals("Item 2", queue.getObjects().get(1));
    }

    @Test(expected = RuntimeException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveWhenEmpty() {
        Queue queue = new Queue();
        queue.remove("Item 1");
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Assert.assertEquals(3, queue.getObjects().size());
        for (int i = 0; i < queue.getObjects().size(); i++) {
            Assert.assertEquals(queue.getObjects().get(i), queue.getObjects().get(i));
        }
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Assert.assertTrue(queue.maxCapacityExceeded());

        queue = new Queue(-1);
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        String output = queue.toString();
        String expected = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Item 1, Item 2]\r\n]";
        Assert.assertEquals(expected, output);
    }
}