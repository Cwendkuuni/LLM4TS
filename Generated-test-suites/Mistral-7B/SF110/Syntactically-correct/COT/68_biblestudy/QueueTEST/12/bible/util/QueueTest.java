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

        Queue nonEmptyQueue = new Queue();
        nonEmptyQueue.enqueue("test");
        Assert.assertFalse(nonEmptyQueue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueAndGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");
        queue.enqueue("three");

        Vector vector = queue.getObjects();
        Assert.assertEquals(3, vector.size());
        Assert.assertEquals("one", vector.get(0));
        Assert.assertEquals("two", vector.get(1));
        Assert.assertEquals("three", vector.get(2));
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");

        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("one", dequeuedObject);
        Assert.assertEquals(1, queue.getNumberItems());

        dequeuedObject = queue.dequeue();
        Assert.assertEquals("two", dequeuedObject);
        Assert.assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueFromEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("one");
        queue.enqueue("two");

        Assert.assertEquals(3, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());

        queue.remove("one");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());

        queue.remove("two");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistingObject() {
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
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("two");
        queue.refreshElement("one");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());

        queue.enqueue("one");
        Assert.assertEquals(1, queue.getNumberItems());

        queue.enqueue("two");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(-1, queue.getPeakNumberItems());

        queue.enqueue("one");
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("two");
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("one");
        queue.enqueue("two");
        queue.enqueue("three");

        Vector vector = queue.getObjects();
        Assert.assertEquals(3, vector.size());
        Assert.assertEquals("one", vector.get(0));
        Assert.assertEquals("two", vector.get(1));
        Assert.assertEquals("three", vector.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(-1);
        Assert.assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(1);
        Assert.assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(0);
        Assert.assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(5);
        Assert.assertFalse(queue.maxCapacityExceeded());

        queue = new Queue(6);
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        Assert.assertEquals("Queue:[numItems=0, maxNumItems=-1, maxCapacity=0, getObjects()=[]]", queue.toString());

        queue = new Queue(5);
        Assert.assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=5, getObjects()=[]]", queue.toString());

        queue.enqueue("one");
        Assert.assertEquals("Queue:[numItems=1, maxNumItems=1, maxCapacity=5, getObjects()=[one]],", queue.toString());

        queue.enqueue("two");
        Assert.assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=5, getObjects()=[one, two]],", queue.toString());

        queue.enqueue("three");
        Assert.assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=5, getObjects()=[one, two, three]],", queue.toString());

        queue.dequeue();
        Assert.assertEquals("Queue:[numItems=2, maxNumItems=3, maxCapacity=5, getObjects()=[two, three]],", queue.toString());

        queue.refreshElement("two");
        Assert.assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=5, getObjects()=[two, three, two]],", queue.toString());
    }
}