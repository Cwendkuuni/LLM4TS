package bible.util;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue(new Object());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        assertEquals(2, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        Object firstObject = queue.dequeue();
        Object secondObject = queue.dequeue();

        assertNotNull(firstObject);
        assertNotNull(secondObject);
        assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        queue.remove(new Object());
        assertEquals(2, queue.getNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.remove(null);
    }

    @Test
    public void testRemoveMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        queue.remove(new Object());
        queue.remove(new Object());
        queue.remove(new Object());

        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        queue.refreshElement(new Object());

        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        Vector objects = queue.getObjects();

        assertEquals(3, objects.size());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());

        String expectedToString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[], maxCapacityExceeded=false]\r\n]";
        assertEquals(expectedToString, queue.toString());
    }
}