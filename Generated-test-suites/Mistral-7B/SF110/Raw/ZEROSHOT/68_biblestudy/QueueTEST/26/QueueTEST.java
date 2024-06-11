package bible.util;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testQueueEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueCreation() {
        Queue queue = new Queue(10);
        assertFalse(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(10, queue.getMaxCapacity());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueueNegativeCapacity() {
        new Queue(-1);
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("apple", queue.getObjects().get(0));

        queue.enqueue("banana");
        queue.enqueue("orange");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals("apple", queue.getObjects().get(0));
        assertEquals("banana", queue.getObjects().get(1));
        assertEquals("orange", queue.getObjects().get(2));
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        queue.enqueue("apple");
        queue.enqueue("banana");

        assertEquals("apple", queue.dequeue());
        assertEquals("banana", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmptyQueue() {
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        queue.enqueue("apple");
        queue.enqueue("banana");

        queue.remove("apple");
        assertEquals("banana", queue.getObjects().get(0));

        queue.remove("orange"); // should not compile

        queue.refreshElement("apple");
        assertEquals("apple", queue.getObjects().get(0));
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(5);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");
        queue.enqueue("grape");
        queue.enqueue("kiwi");

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");

        assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=10, getObjects()=[apple, banana, orange]\r\n]", queue.toString());
    }
}