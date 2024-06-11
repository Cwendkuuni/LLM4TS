package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueueConstruction() {
        Queue queue1 = new Queue();
        Queue queue2 = new Queue(5);
        Queue queue3 = new Queue(-1);

        assertEquals(0, queue1.getNumberItems());
        assertEquals(5, queue2.getMaxCapacity());
        assertEquals(-1, queue3.getMaxCapacity());
    }

    @Test
    public void testQueueIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("Item1");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");

        Vector vectors = queue.getObjects();
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, vectors.size());

        Object[] expected = {"Item1", "Item2", "Item3"};
        for (int i = 0; i < vectors.size(); i++) {
            assertEquals(expected[i], vectors.get(i));
        }
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");

        Object dequeued = queue.dequeue();
        assertEquals("Item1", dequeued);
        assertEquals(2, queue.getNumberItems());

        dequeued = queue.dequeue();
        assertEquals("Item2", dequeued);
        assertEquals(1, queue.getNumberItems());

        dequeued = queue.dequeue();
        assertEquals("Item3", dequeued);
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");

        int sizeBeforeRemove = queue.getNumberItems();
        queue.remove("Item2");
        int sizeAfterRemove = queue.getNumberItems();

        assertEquals(sizeBeforeRemove - 1, sizeAfterRemove);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.enqueue("Item1");
        queue.remove(null);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Item1");

        queue.refreshElement("Item1");

        assertEquals(1, queue.getNumberItems());
        assertTrue(queue.contains("Item1"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("Item1");
        queue.enqueue("Item2");

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(4);
        queue.enqueue("Item1");
        queue.enqueue("Item2");
        queue.enqueue("Item3");
        queue.enqueue("Item4");

        assertFalse(queue.maxCapacityExceeded());
    }

    @Test(expected = NullPointerException.class)
    public void testToStringNullQueue() {
        Queue queue = null;
        String string = queue.toString();
    }
}