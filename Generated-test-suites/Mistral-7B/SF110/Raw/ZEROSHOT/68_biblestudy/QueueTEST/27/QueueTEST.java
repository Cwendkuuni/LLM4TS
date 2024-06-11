import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

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
        assertEquals(-1, queue.getPeakNumberItems());
        assertNull(queue.dequeue());
        assertNull(queue.toString());
    }

    @Test
    public void testQueueCreation() {
        queue = new Queue(5);
        assertFalse(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(5, queue.getMaxCapacity());
        assertNull(queue.dequeue());
        assertNotNull(queue.toString());
    }

    @Test
    public void testQueueEnqueue() {
        queue = new Queue();

        queue.enqueue("Apple");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        assertEquals("Apple", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("Banana");
        queue.enqueue("Cherry");
        queue.enqueue("Durian");

        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertTrue(queue.maxCapacityExceeded());

        Object[] expectedObjects = {"Cherry", "Durian"};
        Vector actualObjects = queue.getObjects();
        assertArrayEquals(expectedObjects, actualObjects.toArray());

        queue.refreshElement("Cherry");
        queue.enqueue("Elderberry");

        Object[] expectedObjectsAfterRefresh = {"Elderberry", "Durian"};
        Vector actualObjectsAfterRefresh = queue.getObjects();
        assertArrayEquals(expectedObjectsAfterRefresh, actualObjectsAfterRefresh.toArray());
    }

    @Test(expected = NullPointerException.class)
    public void testQueueNullObject() {
        queue = new Queue();
        queue.enqueue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testQueueNullObjectDequeue() {
        queue = new Queue();
        queue.enqueue("Apple");
        queue.dequeue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testQueueNullObjectRemove() {
        queue = new Queue();
        queue.enqueue("Apple");
        queue.remove(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueueNegativeCapacity() {
        queue = new Queue(-1);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueueNegativeCapacityEnqueue() {
        queue = new Queue(-1);
        queue.enqueue("Apple");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testQueueNegativeCapacityDequeue() {
        queue = new Queue(-1);
        queue.enqueue("Apple");
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
    }

    @After
    public void tearDown() {
        queue = null;
    }
}