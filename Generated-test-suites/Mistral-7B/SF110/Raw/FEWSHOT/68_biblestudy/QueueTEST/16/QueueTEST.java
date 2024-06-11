package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    private Queue<Integer> queue;

    @Before
    public void setUp() {
        queue = new Queue<>();
    }

    @Test
    public void testQueueEmpty() {
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueEnqueueAndDequeue() {
        queue.enqueue(1);
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertEquals(1, queue.dequeue().intValue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueEnqueueMultipleElements() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals(1, queue.dequeue().intValue());
        assertEquals(2, queue.dequeue().intValue());
        assertEquals(1, queue.dequeue().intValue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueRemove() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        queue.remove(Integer.valueOf(2));
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals(1, queue.getObjects().elementAt(0).intValue());
        assertEquals(3, queue.getObjects().elementAt(1).intValue());
    }

    @Test(expected = NullPointerException.class)
    public void testQueueRemoveNull() {
        queue.remove(null);
    }

    @Test
    public void testQueueRefreshElement() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.refreshElement(Integer.valueOf(1));
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        assertEquals(1, queue.getObjects().elementAt(0).intValue());
        assertEquals(2, queue.getObjects().elementAt(1).intValue());
    }

    @Test
    public void testQueueMaxCapacityExceeded() {
        final int maxSize = 5;
        Queue<Integer> queue = new Queue<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            queue.enqueue(i);
        }
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testQueueMaxCapacityNotExceeded() {
        final int maxSize = 10;
        Queue<Integer> queue = new Queue<>(maxSize);
        for (int i = 0; i < maxSize; i++) {
            queue.enqueue(i);
        }
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testQueueToString() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);
        assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[1, 2, 3],]", queue.toString());
    }
}