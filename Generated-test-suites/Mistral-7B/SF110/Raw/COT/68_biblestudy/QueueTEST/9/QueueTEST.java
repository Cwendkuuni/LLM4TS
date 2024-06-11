import org.junit.Test;
import org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testConstructor() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
        assertEquals(0, queue.getNumberItems(), "The number of items should be equal to the initial capacity.");
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.enqueue(new Integer(1));
        assertEquals(2, queue.getNumberItems());
        assertEquals("test", queue.getObjects().get(0));
        assertEquals(new Integer(1), queue.getObjects().get(1));
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullPointerException() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.enqueue(new Integer(1));
        Object obj = queue.dequeue();
        assertEquals("test", obj);
        assertEquals(1, queue.getNumberItems());
        obj = queue.dequeue();
        assertEquals(new Integer(1), obj);
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
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        queue.remove("test2");
        assertEquals(2, queue.getNumberItems());
        assertEquals("test1", queue.getObjects().get(0));
        assertEquals("test3", queue.getObjects().get(1));
    }

    @Test
    public void testRemoveNotExist() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.remove("test3");
        assertEquals(2, queue.getNumberItems());
        assertEquals("test1", queue.getObjects().get(0));
        assertEquals("test2", queue.getObjects().get(1));
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullPointerException() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.remove(null);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.refreshElement("test1");
        assertEquals(2, queue.getNumberItems());
        assertEquals("test1", queue.getObjects().get(0));
        assertEquals("test2", queue.getObjects().get(1));
    }

    @Test
    public void testRefreshElementSameElement() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.refreshElement("test");
        assertEquals(1, queue.getNumberItems());
        assertEquals("test", queue.getObjects().get(0));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals(2, queue.getNumberItems());
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
        assertEquals(3, objects.size());
        assertEquals("test1", objects.get(0));
        assertEquals("test2", objects.get(1));
        assertEquals("test3", objects.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        String str = queue.toString();
        assertTrue(str.contains("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]]"));
    }

}