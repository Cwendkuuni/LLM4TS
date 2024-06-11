package bible.util;

import static org.junit.Assert.*;
import java.util.*;
import org.junit.Test;

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
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueueNegativeCapacity() {
        Queue queue = new Queue(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueueZeroCapacity() {
        Queue queue = new Queue(0);
    }

    @Test
    public void testIsEmptyWhenNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
    }

    @Test
    public void testDequeueWithOneElement() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        assertEquals("apple", queue.dequeue());
    }

    @Test
    public void testRemoveWithNoMatchingObject() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertFalse(queue.remove("orange"));
    }

    @Test
    public void testRefreshElementWithNoMatchingObject() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertFalse(queue.refreshElement("orange"));
    }

    @Test
    public void testMaxCapacityExceededWhenCapacity() {
        Queue queue = new Queue(1);
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityExceededWhenNoCapacity() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertFalse(queue.maxCapacityExceeded());
    }
}