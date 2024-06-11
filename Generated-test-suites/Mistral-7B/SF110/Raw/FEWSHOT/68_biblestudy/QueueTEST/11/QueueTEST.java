package bible.util;

import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {

    @Test
    public void testQueueEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.dequeue());
        assertNull(queue.getObjects());
    }

    @Test
    public void testQueueEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("apple", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.dequeue());
        assertNull(queue.getObjects());

        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.enqueue("date");

        assertEquals(3, queue.getNumberItems());
        assertEquals("banana", queue.dequeue());
        assertEquals(2, queue.getNumberItems());
        assertEquals("cherry", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("date", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.getObjects());
    }

    @Test
    public void testQueueEnqueueAndRemove() {
        Queue queue = new Queue();

        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");

        queue.refreshElement("apple");
        assertEquals("apple", queue.dequeue());
        assertEquals(2, queue.getNumberItems());
        assertEquals("[class bible.util.Queue:[numItems=2, maxNumItems=3, maxCapacity=-1, getObjects()=[banana, cherry]\r\n]", queue.toString());

        queue.remove("banana");
        assertEquals(1, queue.getNumberItems());
        assertEquals("[class bible.util.Queue:[numItems=1, maxNumItems=3, maxCapacity=-1, getObjects()=[cherry]\r\n]", queue.toString());

        queue.remove("cherry");
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.getObjects());
    }

    @Test(expected = NullPointerException.class)
    public void testQueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
        queue.dequeue();
    }

    @Test
    public void testQueueMaxCapacity() {
        Queue queue = new Queue(3);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.enqueue("date");

        assertTrue(queue.maxCapacityExceeded());
        assertEquals(4, queue.maxPeakNumberItems());
        queue.refreshElement("date");
        assertEquals(3, queue.maxPeakNumberItems());
        assertEquals("date", queue.dequeue());
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.maxPeakNumberItems());
    }
}