package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {

    Queue<Object> queue;

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
    public void testQueueNonEmpty() {
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
        assertEquals("apple", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueMultipleEnqueue() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertEquals("apple", queue.dequeue());
        assertEquals("banana", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnqueue() {
        queue.enqueue(null);
    }

    @Test
    public void testQueueRefreshElement() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.refreshElement("apple");
        assertEquals("apple", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueRemove() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.remove("apple");
        assertFalse(queue.contains("apple"));
        assertEquals("banana", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueMaxCapacity() {
        Queue<Object> queueMax = new Queue<>(0);
        queueMax.enqueue("apple");
        queueMax.enqueue("banana");
        queueMax.enqueue("cherry");
        queueMax.enqueue("date");
        queueMax.enqueue("elderberry");
        assertTrue(queueMax.maxCapacityExceeded());
        assertEquals(-1, queueMax.getMaxCapacity());
    }

    @Test
    public void testQueueGetObjects() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        Vector<Object> vector = queue.getObjects();
        assertEquals(2, vector.size());
        assertEquals("apple", vector.get(0));
        assertEquals("banana", vector.get(1));
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testQueueGetObjectsConcurrentModification() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        Vector<Object> vector = queue.getObjects();
        queue.dequeue();
        vector.remove(0);
    }

    @Test
    public void testQueueToString() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[apple, banana]\r\n]", queue.toString());
    }
}