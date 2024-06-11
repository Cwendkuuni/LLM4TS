import static org.junit.Assert.*;
import bible.util.Queue;

public class QueueTest {
    

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        assertFalse(queue.isEmpty());
        queue.enqueue("test2");
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Object obj = queue.dequeue();
        assertEquals("test1", obj);
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test1");
        queue.remove("test1");
        assertFalse(queue.contains("test1"));
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
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Vector vector = queue.getObjects();
        assertEquals(2, vector.size());
        assertEquals("test1", vector.get(0));
        assertEquals("test2", vector.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        String str = queue.toString();
        assertNotNull(str);
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDequeueNullQueue() {
        Queue queue = null;
        queue.dequeue();
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test(expected = NullPointerException.class)
    public void testRefreshElementNullObject() {
        Queue queue = new Queue();
        queue.refreshElement(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveEmptyQueue() {
        Queue queue = new Queue();
        queue.remove("test1");
    }

    @Test(expected = IllegalStateException.class)
    public void testRefreshElementEmptyQueue() {
        Queue queue = new Queue();
        queue.refreshElement("test1");
    }

    @Test(expected = IllegalStateException.class)
    public void testMaxCapacityExceededNotFullQueue() {
        Queue queue = new Queue(2);
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertFalse(queue.maxCapacityExceeded());
    }

    
}