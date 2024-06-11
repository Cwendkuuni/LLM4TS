import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertNull(queue.dequeue());
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("apple", queue.dequeue());
        Assert.assertEquals("banana", queue.dequeue());
        Assert.assertTrue(queue.isEmpty());
        Assert.assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testMaxCapacity() {
        Queue queue = new Queue(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue(new Integer(i));
        }
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.refreshElement("apple");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("apple", queue.getObjects().firstElement());
        Assert.assertEquals("banana", queue.getObjects().elementAt(1));
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        Vector vector = queue.getObjects();
        Assert.assertEquals(2, vector.size());
        Assert.assertEquals("apple", vector.elementAt(0));
        Assert.assertEquals("banana", vector.elementAt(1));
    }
}