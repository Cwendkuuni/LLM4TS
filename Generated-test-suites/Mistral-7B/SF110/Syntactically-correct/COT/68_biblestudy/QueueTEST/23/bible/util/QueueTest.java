import org.junit.Test;
import org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
        assertEquals(0, queue.maxCapacity);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testQueueConstructorWithCapacity() {
        Queue queue = new Queue(5);
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(5, queue.maxCapacity);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        Vector vector = queue.getObjects();
        assertEquals("Test1", vector.get(0));
        assertEquals("Test2", vector.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertTrue(queue.maxCapacityExceeded());

        queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        Object dequeuedObject = queue.dequeue();
        assertEquals("Test1", dequeuedObject);
        assertEquals(1, queue.getNumberItems());

        dequeuedObject = queue.dequeue();
        assertEquals("Test2", dequeuedObject);
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");

        int removedItems = queue.remove("Test2");
        assertEquals(1, removedItems);
        assertEquals(2, queue.getNumberItems());
        Vector vector = queue.getObjects();
        assertEquals("Test1", vector.get(0));
        assertEquals("Test3", vector.get(1));
    }

    @Test
    public void testRemoveWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        int removedItems = queue.remove(null);
        assertEquals(0, removedItems);
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testRemoveWithNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        int removedItems = queue.remove("Test3");
        assertEquals(0, removedItems);
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        queue.refreshElement("Test1");
        assertEquals(2, queue.getNumberItems());
        Vector vector = queue.getObjects();
        assertEquals("Test1", vector.get(0));
        assertEquals("Test2", vector.get(1));

        queue.refreshElement("Test2");
        assertEquals(2, queue.getNumberItems());
        vector = queue.getObjects();
        assertEquals("Test1", vector.get(0));
        assertEquals("Test1", vector.get(1));
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        String toString = queue.toString();
        assertTrue(toString.contains("Queue:[numItems=2, maxNumItems=2, maxCapacity=0, getObjects()=["));
        assertTrue(toString.contains("Test1"));
        assertTrue(toString.contains("Test2"));
    }

}