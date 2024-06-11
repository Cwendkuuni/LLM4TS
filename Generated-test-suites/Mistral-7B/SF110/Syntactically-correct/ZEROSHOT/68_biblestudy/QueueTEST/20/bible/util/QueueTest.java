import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class QueueTest {

    private Queue<Object> queue;

    @Before
    public void setUp() {
        queue = new Queue<>();
    }

    @Test
    public void testQueueEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueueEnqueue() {
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("apple", queue.getObjects().get(0));
    }

    @Test
    public void testQueueEnqueueMultiple() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");
        assertEquals(3, queue.getNumberItems());
        assertEquals("apple", queue.getObjects().get(0));
        assertEquals("banana", queue.getObjects().get(1));
        assertEquals("orange", queue.getObjects().get(2));
    }

    @Test(expected = NullPointerException.class)
    public void testQueueEnqueueNullObject() {
        queue.enqueue(null);
    }

    @Test
    public void testQueueDequeue() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        Object obj = queue.dequeue();
        assertEquals("apple", obj);
        assertEquals(1, queue.getNumberItems());
        assertEquals("banana", queue.getObjects().get(0));
    }

    @Test
    public void testQueueRemove() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        int numRemoved = queue.remove("banana");
        assertEquals(1, numRemoved);
        assertEquals("apple", queue.getObjects().get(0));
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueRemoveNonExistentObject() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        int numRemoved = queue.remove("orange");
        assertEquals(0, numRemoved);
        assertEquals("apple", queue.getObjects().get(0));
        assertEquals("banana", queue.getObjects().get(1));
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testQueueRefreshElement() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.refreshElement("banana");
        assertEquals("banana", queue.getObjects().get(0));
        assertEquals("apple", queue.getObjects().get(1));
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testQueueMaxCapacity() {
        queue = new Queue<>(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue(Integer.toString(i));
        }
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testQueueMaxCapacityNoLimit() {
        queue = new Queue<>();
        for (int i = 0; i < 100; i++) {
            queue.enqueue(Integer.toString(i));
        }
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testQueueToString() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");
        String queueString = queue.toString();
        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[apple, banana, orange]\r\n]";
        assertEquals(expected, queueString);
    }

    @After
    public void tearDown() {
        queue = null;
    }
}