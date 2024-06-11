import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testConstructor() {
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

        queue.enqueue("object1");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");

        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals("object2", queue.dequeue().toString());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
        Assert.assertEquals("object1", queue.dequeue().toString());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");

        queue.remove("object2");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals("object3", queue.dequeue().toString());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
        Assert.assertEquals("object1", queue.dequeue().toString());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getPeakNumberItems());

        queue.remove("object1");
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.remove("object2");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");

        queue.refreshElement("object1");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals("object1", queue.dequeue().toString());
        Assert.assertEquals("object1", queue.dequeue().toString());
        Assert.assertEquals("object2", queue.dequeue().toString());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());

        queue.enqueue("object1");
        Assert.assertEquals(1, queue.getNumberItems());

        queue.enqueue("object2");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("object1");
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("object2");
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");

        Vector objects = queue.getObjects();
        Assert.assertEquals(2, objects.size());
        Assert.assertEquals("object1", objects.get(0));
        Assert.assertEquals("object2", objects.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("object1");
        queue.enqueue("object2");

        Assert.assertTrue(queue.maxCapacityExceeded());

        queue = new Queue(-1);
        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        Assert.assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());

        queue.enqueue("object1");
        Assert.assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=-1, getObjects()=[object1]]", queue.toString());

        queue.enqueue("object2");
        Assert.assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[object1, object2]]", queue.toString());
    }
}