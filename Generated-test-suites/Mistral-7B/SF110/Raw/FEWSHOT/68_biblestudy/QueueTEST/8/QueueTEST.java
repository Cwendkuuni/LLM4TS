package bible.util;

import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {

    @Test
    public void testQueueEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("apple", queue.dequeue().toString());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueEnqueueMultiple() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertEquals(3, queue.getNumberItems());
        assertEquals("apple", queue.dequeue().toString());
        assertEquals("banana", queue.dequeue().toString());
        assertEquals("cherry", queue.dequeue().toString());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueEnqueueAndDequeueMultiple() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.dequeue();
        queue.dequeue();
        queue.enqueue("orange");
        queue.enqueue("grape");
        assertEquals(3, queue.getNumberItems());
        assertEquals("cherry", queue.dequeue().toString());
        assertEquals("orange", queue.dequeue().toString());
        assertEquals("grape", queue.dequeue().toString());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueMaxCapacity() {
        Queue queue = new Queue(5);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.enqueue("date");
        queue.enqueue("elderberry");
        queue.enqueue("fig");
        queue.enqueue("grape");
        queue.enqueue("honeydew");
        queue.enqueue("improbable"); // will exceed maximum capacity
        assertEquals(5, queue.getPeakNumberItems());
        assertEquals(6, queue.getNumberItems());
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testQueueRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.refreshElement("cherry");
        assertEquals(3, queue.getNumberItems());
        assertEquals("cherry", queue.dequeue().toString());
        assertEquals("cherry", queue.dequeue().toString());
        assertEquals("apple", queue.dequeue().toString());
        assertEquals(1, queue.getNumberItems());
        assertEquals("banana", queue.dequeue().toString());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        Vector vector = queue.getObjects();
        assertEquals(3, vector.size());
        assertEquals("apple", vector.get(0).toString());
        assertEquals("banana", vector.get(1).toString());
        assertEquals("cherry", vector.get(2).toString());
    }
}