package bible.util;

import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {

    @Test
    public void testQueueEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertNull(queue.dequeue());
        assertNull(queue.remove("Test Object"));
    }

    @Test
    public void testQueueBasicOperations() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertEquals("Test Object 1", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals("Test Object 1", queue.dequeue());
        assertEquals("Test Object 2", queue.dequeue());
        assertEquals("Test Object 3", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueWithMaxCapacity() {
        Queue queue = new Queue(2);
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");

        assertTrue(queue.maxCapacityExceeded());
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        queue.refreshElement("Test Object 1");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals("Test Object 3", queue.dequeue());
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");

        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        int numRemoved = queue.remove("Test Object 2");
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals(1, numRemoved);

        numRemoved = queue.remove("Test Object 1");
        assertEquals(1, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals(1, numRemoved);

        numRemoved = queue.remove("Test Object 3");
        assertEquals(0, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals(1, numRemoved);
    }

    @Test
    public void testQueueToString() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.enqueue("Test Object 3");

        String queueString = queue.toString();
        assertTrue(queueString.contains("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Test Object 1, Test Object 2, Test Object 3]\r\n]"));
    }
}