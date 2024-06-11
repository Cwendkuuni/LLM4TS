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
    public void testQueueInstance() {
        Queue<Integer> queue = new Queue<>();
        Queue<Integer> capacityQueue = new Queue<>(5);
        Queue<Integer> emptyQueue = new Queue<>(0);

        assertEquals(Queue.class, queue.getClass());
        assertEquals(5, capacityQueue.getPeakNumberItems());
        assertTrue(emptyQueue.isEmpty());
    }

    @Test
    public void testQueueIsEmpty() {
        Queue<Integer> queue = new Queue<>();

        assertTrue(queue.isEmpty());
    }

    @Test
    public void testQueueIsNotEmpty() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);

        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueueSingleElement() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);

        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.dequeue().intValue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueueMultipleElements() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.enqueue(2);

        assertEquals(2, queue.getNumberItems());
        assertEquals(1, queue.dequeue().intValue());
        assertEquals(2, queue.dequeue().intValue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testMaxCapacityReached() {
        Queue<Integer> queue = new Queue<>(5);
        for (int i = 0; i < 5; i++) {
            queue.enqueue(i);
        }

        queue.enqueue(6); // should throw exception

        assertEquals(5, queue.getNumberItems());
        assertEquals(5, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue<Integer> queue = new Queue<>(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue(i);
        }

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testRemoveSingleElement() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);

        Object removed = queue.remove(1);

        assertSame(1, removed);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemoveMultipleElements() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.enqueue(2);

        Object removed1 = queue.remove(1);
        Object removed2 = queue.remove(2);

        assertSame(1, removed1);
        assertSame(2, removed2);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.refreshElement(1);

        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.dequeue().intValue());
        assertEquals(1, queue.dequeue().intValue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testGetObjects() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);
        queue.enqueue(2);

        Vector<Integer> objects = queue.getObjects();

        assertEquals(2, objects.size());
        assertEquals(1, ((Integer) objects.elementAt(0)).intValue());
        assertEquals(2, ((Integer) objects.elementAt(1)).intValue());
    }

    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        Queue<String> queue = new Queue<>();
        queue.enqueue(new String("foo"));
        queue.remove(null);
    }

    @Test
    public void testRemoveNonExistentObject() {
        Queue<Integer> queue = new Queue<>();
        queue.enqueue(1);

        int removed = queue.remove(2);

        assertEquals(-1, removed);
        assertEquals(1, queue.getNumberItems());
    }
}