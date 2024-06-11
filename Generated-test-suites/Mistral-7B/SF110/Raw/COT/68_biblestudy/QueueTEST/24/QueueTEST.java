import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstruction() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(10);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(10, queueWithCapacity.getNumberItems());
        Assert.assertEquals(10, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());

        queue.enqueue(new Object());
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
        queue.enqueue(new Object());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testEnqueueMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        Assert.assertEquals(3, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testDequeueSingleObject() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        Object obj = queue.dequeue();
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
        Assert.assertEquals(obj, queue.dequeue()); // Queue is empty, should return null
        Assert.assertNull(queue.dequeue());
    }

    @Test
    public void testDequeueMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        Object obj1 = queue.dequeue();
        Object obj2 = queue.dequeue();
        Object obj3 = queue.dequeue();

        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
        Assert.assertNotEquals(obj3, obj1);
        Assert.assertNotEquals(obj3, obj2);
        Assert.assertNotEquals(obj1, obj2);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        queue.refreshElement(new Object());
        Assert.assertEquals(3, queue.getNumberItems());

        int removed = queue.remove(new Object());
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals(1, removed);
    }

    @Test
    public void testRemoveMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        queue.refreshElement(new Object());
        queue.refreshElement(new Object());

        int removed = queue.remove(new Object());
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
        Assert.assertEquals(2, removed);

        removed = queue.remove(new Object());
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
        Assert.assertEquals(1, removed);

        removed = queue.remove(new Object());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
        Assert.assertEquals(0, removed);
    }

    @Test(expected = NoSuchElementException.class)
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        queue.remove(new Object());
        queue.remove(new Object());
        queue.remove(new Object());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        queue.refreshElement(new Object());
        Assert.assertEquals(3, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());

        queue.enqueue(new Object());
        Assert.assertEquals(1, queue.getNumberItems());

        queue.enqueue(new Object());
        Assert.assertEquals(2, queue.getNumberItems());

        queue.enqueue(new Object());
        Assert.assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(-1, queue.getPeakNumberItems());

        queue.enqueue(new Object());
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue(new Object());
        Assert.assertEquals(2, queue.getPeakNumberItems());

        queue.enqueue(new Object());
        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        Vector vector = queue.getObjects();
        Assert.assertEquals(3, vector.size());
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
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        String expectedOutput = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Ljava.lang.Object;@571d84,]\r\n]";
        Assert.assertEquals(expectedOutput, queue.toString());
    }
}