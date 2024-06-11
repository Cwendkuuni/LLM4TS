package bible.util;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue(3);
        queue.enqueue("apple");
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
        assertEquals("apple", queue.dequeue());
        assertEquals("banana", queue.dequeue());
        assertEquals("cherry", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.refreshElement("banana");
        queue.refreshElement("cherry");
        queue.refreshElement("apple");
        queue.remove("cherry");
        assertEquals(2, queue.getNumberItems());
        queue.remove("banana");
        assertEquals(1, queue.getNumberItems());
        queue.remove("apple");
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("apple", objects.get(0));
        assertEquals("banana", objects.get(1));
        assertEquals("cherry", objects.get(2));
    }
}