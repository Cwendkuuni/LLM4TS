package bible.util;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
        assertEquals("apple", queue.dequeue().toString());
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertEquals("apple", queue.dequeue().toString());
        assertEquals("banana", queue.dequeue().toString());
        assertEquals("cherry", queue.dequeue().toString());
        assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testDequeueNull() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.refreshElement("banana");
        assertEquals(2, queue.getNumberItems());
        assertEquals("cherry", queue.dequeue().toString());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNull() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        Vector expected = new Vector(3, 10);
        expected.add("apple");
        expected.add("banana");
        expected.add("cherry");
        assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[apple, banana, cherry]\r\n]";
        assertEquals(expected, queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testRefreshElementNull() {
        Queue queue = new Queue();
        queue.refreshElement(null);
    }
}