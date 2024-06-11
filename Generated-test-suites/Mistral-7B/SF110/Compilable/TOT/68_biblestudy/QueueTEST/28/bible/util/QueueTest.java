package bible.util;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.util.Queue;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("apple");
        queue.enqueue("banana");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertEquals(3, queue.getNumberItems());

        Queue maxQueue = new Queue(3);
        maxQueue.enqueue("apple");
        maxQueue.enqueue("banana");
        maxQueue.enqueue("cherry");
        maxQueue.enqueue("durian"); // This should throw an exception
        assertEquals(3, maxQueue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());

        queue.enqueue("apple");
        queue.enqueue("banana");
        assertEquals("apple", queue.dequeue());
        assertEquals("banana", queue.dequeue());
        assertNull(queue.dequeue());

        queue.dequeue();
        queue.dequeue();
        assertNull(queue.dequeue());
    }

    // Add more test cases for other methods as needed

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.enqueue("durian"); // This should throw an exception
        assertTrue(queue.maxCapacityExceeded());
    }

    // Add more test cases for other methods as needed

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.refreshElement("apple");
        assertEquals(2, queue.getNumberItems());
        assertEquals("apple", queue.getObjects().get(0));
        assertEquals("banana", queue.getObjects().get(1));
    }

    // Add more test cases for other methods as needed

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertEquals("Queue:[numItems=2, maxNumItems=0, maxCapacity=-1, getObjects()=[apple, banana],]\r\n", queue.toString());
    }

    // Add more test cases for other methods as needed

}