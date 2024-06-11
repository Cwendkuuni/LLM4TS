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
    public void testQueueEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.dequeue());
        assertNull(queue.remove("test"));
    }

    @Test
    public void testQueueNonEmpty() {
        Queue queue = new Queue();
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("test", queue.dequeue());
        assertEquals("test", queue.remove("test"));
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueMaxCapacity() {
        Queue queue = new Queue(5);
        for (int i = 1; i <= 5; i++) {
            queue.enqueue(Integer.toString(i));
        }
        assertEquals(5, queue.getNumberItems());
        assertEquals(5, queue.getPeakNumberItems());
        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("test");
        assertTrue(queue.maxCapacityExceeded());
        assertEquals(5, queue.getPeakNumberItems());
        assertEquals(5, queue.getNumberItems());
    }

    @Test
    public void testQueueMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        for (int i = 1; i <= 10; i++) {
            queue.enqueue(Integer.toString(i));
        }
        assertTrue(queue.maxCapacityExceeded());
        assertEquals(10, queue.getPeakNumberItems());
        assertEquals(10, queue.getNumberItems());
    }

    @Test
    public void testQueueRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.refreshElement("test");
        assertTrue(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("test", queue.dequeue());
    }

    @Test
    public void testQueueGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("test1", objects.get(0));
        assertEquals("test2", objects.get(1));
        assertEquals("test3", objects.get(2));
    }
}