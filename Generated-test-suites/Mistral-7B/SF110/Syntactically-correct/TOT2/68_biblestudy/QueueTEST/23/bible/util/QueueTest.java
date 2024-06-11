package bible.util;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");

        assertEquals("apple", queue.dequeue());
        assertEquals("banana", queue.dequeue());
        assertEquals("orange", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");

        queue.remove("banana");
        assertEquals(2, queue.getNumberItems());
        assertEquals("apple", queue.getObjects().get(0));
        assertEquals("orange", queue.getObjects().get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");
        queue.enqueue("grape");
        queue.enqueue("pear");

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testGetNumberAndPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");

        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");

        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("apple", objects.get(0));
        assertEquals("banana", objects.get(1));
        assertEquals("orange", objects.get(2));
    }
}