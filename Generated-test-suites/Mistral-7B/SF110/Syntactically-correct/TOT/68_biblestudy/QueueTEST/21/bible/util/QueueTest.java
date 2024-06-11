package bible.util;

import org.junit.Test;
import org.junit.Assert.*;
import java.util.*;
import bible.util.Queue;
import bible.util.Queue.Node;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("item1");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.getObjects().get(0));
        assertEquals("item2", queue.getObjects().get(1));
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());

        assertTrue(queue.isEmpty());
    }

    @Test(expected = RuntimeException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);

        for (int i = 0; i < 6; i++) {
            queue.enqueue(String.valueOf(i));
        }

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.refreshElement("item1");

        assertEquals("item1", queue.getObjects().get(0));
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        Queue queue = new Queue(-1);
    }
}