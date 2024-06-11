package bible.util;

import org.junit.Assert;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        boolean result = queue.isEmpty();
        Assert.assertTrue(result);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        Assert.assertFalse(queue.isEmpty());

        queue = new Queue(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue(String.valueOf(i));
        }
        Assert.assertEquals(5, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testEnqueueMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue(String.valueOf(i));
        }
        queue.enqueue("Item6");
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        Object result = queue.dequeue();
        Assert.assertEquals("Item1", result);
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.remove("Item1");
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals("Item2", queue.getObjects().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.remove(null);
    }

    @Test
    public void testRemoveNotInQueue() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.remove("Item3");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.refreshElement("Item1");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("Item2", queue.getObjects().get(0));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue(String.valueOf(i));
        }
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test(expected = IllegalStateException.class)
    public void testMaxCapacityExceededEmptyQueue() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        String result = queue.toString();
        Assert.assertTrue(result.contains("numItems=2"));
    }
}