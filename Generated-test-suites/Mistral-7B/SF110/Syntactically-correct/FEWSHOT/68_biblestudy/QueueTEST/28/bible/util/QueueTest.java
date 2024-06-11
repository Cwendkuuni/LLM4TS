package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThrows;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {

    private Queue<Integer> queue;

    @Before
    public void setup() {
        queue = new Queue<>();
    }

    @Test
    public void testConstructor() {
        // Default constructor
        Queue<Integer> queue1 = new Queue<>();

        // Constructor with capacity
        Queue<Integer> queue2 = new Queue<>(5);
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());

        queue.enqueue(1);

        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        Object[] expected = {1, 2, 3};
        assertArrayEquals(expected, queue.getObjects().toArray());
    }

    @Test
    public void testDequeue() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        Object obj = queue.dequeue();
        assertEquals(1, obj);

        Object[] expected = {2, 3};
        assertArrayEquals(expected, queue.getObjects().toArray());
    }

    @Test
    public void testRemove() {
        queue.enqueue(1);
        queue.enqueue(1);
        queue.enqueue(2);

        int numRemoved = queue.remove(1);
        assertEquals(2, numRemoved);
        assertEquals(1, queue.getNumberItems());
        assertArrayEquals(new Object[] {2}, queue.getObjects().toArray());

        numRemoved = queue.remove(2);
        assertEquals(0, numRemoved);
        assertArrayEquals(new Object[] {2}, queue.getObjects().toArray());

        numRemoved = queue.remove(1);
        assertEquals(1, numRemoved);
        assertNull(queue.getObjects().size());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue(1);
        queue.enqueue(2);

        queue.refreshElement(1);

        Object[] expected = {1, 2};
        assertArrayEquals(expected, queue.getObjects().toArray());
    }

    @Test
    public void testGetNumberItems() {
        assertEquals(0, queue.getNumberItems());

        queue.enqueue(1);

        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue(1);
        queue.enqueue(2);

        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue(1);
        queue.enqueue(2);

        Object[] expected = {1, 2};
        assertArrayEquals(expected, queue.getObjects().toArray());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue<>(2);

        queue.enqueue(1);
        queue.enqueue(2);

        assertFalse(queue.maxCapacityExceeded());

        queue.enqueue(3);

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue(1);
        queue.enqueue(2);
        queue.enqueue(3);

        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[1, 2, 3]\r\n]";
        assertEquals(expected, queue.toString());
    }

    @Test
    public void testNullArgument() {
        // dequeue
        assertThrows(NullPointerException.class, () -> queue.dequeue());

        // enqueue
        assertThrows(NullPointerException.class, () -> queue.enqueue(null));

        // remove
        assertThrows(NullPointerException.class, () -> queue.remove(null));

        // refreshElement
        assertThrows(NullPointerException.class, () -> queue.refreshElement(null));
    }

    @Test
    public void testConcurrentModificationException() {
        queue.enqueue(1);
        queue.enqueue(2);

        Object obj = queue.dequeue();
        assertEquals(1, obj);

        assertThrows(ConcurrentModificationException.class, () -> {
            queue.remove(2);
            queue.remove(1);
        });
    }
}