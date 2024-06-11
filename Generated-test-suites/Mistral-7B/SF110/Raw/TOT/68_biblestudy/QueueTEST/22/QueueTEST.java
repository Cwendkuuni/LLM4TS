package bible.util;
import org.junit.Test;
import static org.junit.Assert.*;
import java.util.Vector;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue(); // empty queue
        assertTrue(queue.isEmpty());

        queue.enqueue("test"); // add an element
        assertFalse(queue.isEmpty());

        queue = new Queue(); // create a new empty queue
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test");
        assertEquals(1, queue.getNumberItems());

        queue.enqueue("anotherTest");
        assertEquals(2, queue.getNumberItems());

        queue.enqueue(null); // test null value
        assertEquals(3, queue.getNumberItems());

        queue = new Queue(2); // test max capacity
        queue.enqueue("test");
        queue.enqueue("anotherTest"); // capacity reached, should not add another element
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.enqueue("anotherTest");

        Object obj = queue.dequeue();
        assertEquals("test", obj);
        assertEquals(1, queue.getNumberItems());

        obj = queue.dequeue();
        assertEquals("anotherTest", obj);
        assertEquals(0, queue.getNumberItems());

        queue.dequeue(); // empty queue, should return null
        assertNull(queue.dequeue());

        queue = new Queue(1); // test max capacity
        queue.enqueue("test");
        obj = queue.dequeue();
        assertEquals("test", obj);
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.enqueue("anotherTest");

        int removed = queue.remove("test");
        assertEquals(1, removed);
        assertEquals(1, queue.getNumberItems());

        removed = queue.remove("anotherTest");
        assertEquals(1, removed);
        assertEquals(0, queue.getNumberItems());

        removed = queue.remove("test"); // remove an already removed element
        assertEquals(0, removed);
        assertEquals(0, queue.getNumberItems());

        queue.remove("null"); // test removing null
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        queue.refreshElement("test1");
        assertEquals("test1", queue.getObjects().get(0));

        queue.refreshElement("test2");
        assertEquals("test2", queue.getObjects().get(1));

        queue.refreshElement("test1");
        assertEquals("test1", queue.getObjects().get(0));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        Vector objects = queue.getObjects();
        assertEquals("test1", objects.get(0));
        assertEquals("test2", objects.get(1));
        assertEquals("test3", objects.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3"); // max capacity reached

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[test1, test2, test3]\r\n]";
        assertEquals(expected, queue.toString());
    }

    @Test
    public void testEmptyConstructor() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testCapacityConstructor() {
        Queue queue = new Queue(5);
        assertFalse(queue.isEmpty());
    }
}