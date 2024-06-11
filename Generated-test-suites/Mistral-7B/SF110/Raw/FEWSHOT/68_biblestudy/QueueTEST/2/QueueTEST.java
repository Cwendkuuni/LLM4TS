package bible.util;

import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {

    @Test
    public void testQueueCreation() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
        assertEquals(0, queueWithCapacity.getNumberItems());
        assertEquals(-1, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        assertEquals("test1", queue.dequeue());
        assertEquals("test2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        queue.remove("test1");
        assertEquals("test2", queue.dequeue().toString());

        queue.remove("test3");
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        queue.refreshElement("test1");
        assertEquals("test1", queue.dequeue().toString());
        assertEquals("test2", queue.dequeue().toString());
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        List expectedList = Arrays.asList("test1", "test2");
        assertEquals(expectedList, queue.getObjects());
    }

    @Test(expected = ConcurrentModificationException.class)
    public void testGetObjectsConcurrentModification() {
        Queue queue = new Queue();
        queue.enqueue("test1");

        List list = queue.getObjects();
        list.remove(0);
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]\r\n]";
        assertEquals(expectedString, queue.toString());
    }
}