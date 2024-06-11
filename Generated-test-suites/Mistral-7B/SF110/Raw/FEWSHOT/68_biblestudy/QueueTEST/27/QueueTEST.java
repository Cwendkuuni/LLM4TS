package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNotEquals;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {

    Queue queue;

    @Before
    public void setup() {
        queue = new Queue();
    }

    @Test
    public void testQueueCreation() {
        Queue q1 = new Queue();
        Queue q2 = new Queue(10);
        assertEquals(0, q1.getNumberItems());
        assertEquals(0, q2.getNumberItems());
        assertEquals(-1, q1.getPeakNumberItems());
        assertEquals(0, q2.getPeakNumberItems());
    }

    @Test
    public void testIsEmptyQueue() {
        assertTrue(queue.isEmpty());
        queue.enqueue(new Object());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        Object obj = queue.dequeue();
        assertEquals(new Object(), obj);
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.remove(new Object());
        assertEquals(2, queue.getNumberItems());
        queue.remove(null);
        assertEquals(2, queue.getNumberItems());
        queue.remove(new Object());
        assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.refreshElement(new Object());
        assertEquals(new Object(), queue.getObjects().get(0));
        assertEquals(new Object(), queue.getObjects().get(1));
    }

    @Test
    public void testGetObjects() {
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        Vector objects = queue.getObjects();
        assertEquals(new Object(), objects.get(0));
        assertEquals(new Object(), objects.get(1));
        assertEquals(new Object(), objects.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        queue = new Queue(3);
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        String toString = queue.toString();
        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Ljava.lang.Object;@2c56397b]";
        assertNotEquals(toString, expected);
    }
}