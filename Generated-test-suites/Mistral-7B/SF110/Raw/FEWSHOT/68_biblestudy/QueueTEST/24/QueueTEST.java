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
    public void testQueue() {
        Queue queue = new Queue();
        assertEquals(Queue.NO_MAXIMUM, queue.getPeakNumberItems());
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueueWithCapacity() {
        Queue queue = new Queue(5);
        assertEquals(Queue.NO_MAXIMUM, queue.getPeakNumberItems());
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        queue.enqueue("5");
        assertEquals(5, queue.getNumberItems());
        assertEquals(5, queue.getPeakNumberItems());
        assertEquals("1", queue.dequeue());
        assertEquals("2", queue.dequeue());
        assertEquals("3", queue.dequeue());
        assertEquals("4", queue.dequeue());
        assertEquals("5", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        queue.enqueue("4");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testDeQueueWithEmptyQueue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
    }

    @Test
    public void testDeQueueWithSingleElement() {
        Queue queue = new Queue();
        queue.enqueue("1");
        assertEquals("1", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        assertEquals(3, queue.getNumberItems());
        queue.remove("1");
        assertEquals(2, queue.getNumberItems());
        queue.remove("3");
        assertEquals(1, queue.getNumberItems());
        queue.remove("2");
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.refreshElement("1");
        assertEquals("1", queue.dequeue());
        assertEquals("2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        Vector vector = queue.getObjects();
        assertEquals(3, vector.size());
        assertSame("1", vector.get(0));
        assertSame("2", vector.get(1));
        assertSame("3", vector.get(2));
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        assertEquals("bible.util.Queue:[numItems=0, maxNumItems=-1, maxCapacity=-1, getObjects()=[]]", queue.toString());
        queue.enqueue("1");
        queue.enqueue("2");
        queue.enqueue("3");
        assertEquals("bible.util.Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[1, 2, 3]]", queue.toString());
    }
}