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
    }

    @Test
    public void testQueueSingleElement() {
        Queue queue = new Queue();
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
        assertEquals("test", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueMultipleElements() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        assertEquals(3, queue.getNumberItems());
        assertEquals("test1", queue.dequeue());
        assertEquals("test2", queue.dequeue());
        assertEquals("test3", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueMaxCapacity() {
        Queue queue = new Queue(3);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        queue.enqueue("test4");
        queue.enqueue("test5");
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals("test1", queue.dequeue());
        assertEquals("test2", queue.dequeue());
        assertEquals("test3", queue.dequeue());
        assertEquals("test4", queue.dequeue());
        assertEquals("test5", queue.dequeue());
        assertFalse(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test(expected = NullPointerException.class)
    public void testQueueNullElement() {
        Queue queue = new Queue();
        queue.enqueue(null);
        queue.dequeue();
        queue.enqueue(null);
        queue.dequeue();
        queue.enqueue("test");
        queue.dequeue();
        queue.remove(null);
    }

    @Test
    public void testQueueRemove() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        assertEquals(3, queue.getNumberItems());
        assertEquals(2, queue.remove("test2"));
        assertEquals("test1", queue.dequeue());
        assertEquals("test3", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        queue.refreshElement("test2");
        assertEquals(3, queue.getNumberItems());
        assertEquals("test1", queue.dequeue());
        assertEquals("test2", queue.dequeue());
        assertEquals("test3", queue.dequeue());
        assertEquals("test2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[test1, test2, test3],]", queue.toString());
        queue.dequeue();
        queue.dequeue();
        assertEquals("Queue:[numItems=1, maxNumItems=3, maxCapacity=-1, getObjects()=[test3],]", queue.toString());
    }
}