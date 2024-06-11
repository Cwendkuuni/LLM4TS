package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueueEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueue() {
        Queue queue = new Queue();
        queue.enqueue("A");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("A", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueWithMaxCapacity() {
        Queue queue = new Queue(5);
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.enqueue("D");
        queue.enqueue("E");
        queue.enqueue("F");
        assertTrue(queue.maxCapacityExceeded());
        assertEquals(5, queue.getPeakNumberItems());
        assertEquals("A", queue.dequeue());
        assertEquals(4, queue.getNumberItems());
        assertEquals("B", queue.dequeue());
        assertEquals("C", queue.dequeue());
        assertEquals("D", queue.dequeue());
        assertEquals("E", queue.dequeue());
        assertEquals("F", queue.dequeue());
        assertFalse(queue.isEmpty());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueueRemove() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        queue.refreshElement("B");
        queue.refreshElement("A");
        assertEquals(2, queue.getNumberItems());
        assertEquals("C", queue.dequeue().getValue());
        assertEquals(1, queue.getNumberItems());
        assertEquals("A", queue.dequeue().getValue());
        assertEquals("B", queue.dequeue().getValue());
        assertFalse(queue.isEmpty());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueueAddMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue("One");
        queue.enqueue("Two");
        queue.enqueue("Three");
        queue.enqueue("Four");
        queue.enqueue("Five");
        assertEquals(5, queue.getNumberItems());
        assertEquals("One", queue.dequeue().getValue());
        assertEquals("Two", queue.dequeue().getValue());
        assertEquals("Three", queue.dequeue().getValue());
        assertEquals("Four", queue.dequeue().getValue());
        assertEquals("Five", queue.dequeue().getValue());
        assertFalse(queue.isEmpty());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueueAddAndGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        assertEquals("A", queue.dequeue().getValue());
        queue.enqueue("C");
        queue.enqueue("D");
        assertEquals("B", queue.dequeue().getValue());
        assertEquals("C", queue.dequeue().getValue());
        assertEquals("D", queue.dequeue().getValue());
        assertFalse(queue.isEmpty());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueueToString() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        assertEquals("Queue:[numItems=3, maxNumItems=0, maxCapacity=-1, getObjects()=[A, B, C]\r\n]", queue.toString());
    }

    @Test
    public void testQueueGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("A");
        queue.enqueue("B");
        queue.enqueue("C");
        Vector expected = new Vector(Arrays.asList("A", "B", "C"));
        assertEquals(expected, queue.getObjects());
    }

}