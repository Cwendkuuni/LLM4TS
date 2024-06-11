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
    }

    @Test
    public void testQueueCreation() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(-1, queue.maxCapacity);

        Queue queueWithCapacity = new Queue(10);
        assertEquals(0, queueWithCapacity.getNumberItems());
        assertEquals(0, queueWithCapacity.getPeakNumberItems());
        assertEquals(10, queueWithCapacity.maxCapacity);
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();

        queue.enqueue("item1");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertEquals("item1", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals("item1", queue.dequeue());
        assertEquals(2, queue.getNumberItems());
        assertEquals("item2", queue.dequeue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("item3", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testEnqueueAndRemove() {
        Queue queue = new Queue();

        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.enqueue("item4");

        assertEquals(4, queue.getNumberItems());
        assertEquals(4, queue.getPeakNumberItems());

        queue.remove("item3");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        queue.refreshElement("item3");
        assertEquals(4, queue.getNumberItems());
        assertEquals(4, queue.getPeakNumberItems());
        assertEquals("item3", queue.dequeue());

        queue.remove("item1");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
        queue.dequeue();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        for (int i = 0; i < 4; i++) {
            queue.enqueue(String.valueOf(i));
        }
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[item1, item2, item3]\r\n]";
        assertEquals(expected, queue.toString());
    }
}