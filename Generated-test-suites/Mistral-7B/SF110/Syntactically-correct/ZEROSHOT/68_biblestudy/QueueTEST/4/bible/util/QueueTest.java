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
    public void testQueueCreation() {
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());
    }

    @Test
    public void testQueueCreationWithCapacity() {
        queue = new Queue(5);
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=5, getObjects()=[]]", queue.toString());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 1, Item 2, Item 3]]", queue.toString());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals("Item 1", queue.dequeue());
        assertEquals("Queue:[numItems=2, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 2, Item 3]]", queue.toString());

        assertEquals("Item 2", queue.dequeue());
        assertEquals("Queue:[numItems=1, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 3]]", queue.toString());

        assertEquals("Item 3", queue.dequeue());
        assertEquals("Queue:[numItems=0, maxNumItems=3, maxCapacity=-1, getObjects()=[]]", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmptyQueue() {
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals(3, queue.remove("Item 1").intValue());
        assertEquals("Queue:[numItems=2, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 2, Item 3]]", queue.toString());

        assertEquals(1, queue.remove("Item 2").intValue());
        assertEquals("Queue:[numItems=1, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 3]]", queue.toString());

        assertEquals(0, queue.remove("Item 3").intValue());
        assertEquals("Queue:[numItems=0, maxNumItems=3, maxCapacity=-1, getObjects()=[]]", queue.toString());

        assertEquals(0, queue.remove("Item 1").intValue());
        assertEquals("Queue:[numItems=0, maxNumItems=3, maxCapacity=-1, getObjects()=[]]", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveNonExistingObject() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        queue.remove("Item 3");
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.refreshElement("Item 1");

        assertEquals("Queue:[numItems=1, maxNumItems=3, maxCapacity=-1, getObjects()=[Item 1, Item 2]]", queue.toString());
    }

    @Test
    public void testGetNumberItems() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("Item 1");
        queue.enqueue("Item 2");
        queue.enqueue("Item 3");

        Vector expected = new Vector(3, 10);
        expected.add("Item 1");
        expected.add("Item 2");
        expected.add("Item 3");

        assertEquals(expected, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);

        queue.enqueue("Item 1");
        queue.enqueue("Item 2");

        assertTrue(queue.maxCapacityExceeded());
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=2, getObjects()=[Item 1, Item 2]]", queue.toString());

        queue.dequeue();

        assertFalse(queue.maxCapacityExceeded());
        assertEquals("Queue:[numItems=1, maxNumItems=2, maxCapacity=2, getObjects()=[Item 2]]", queue.toString());
    }

    @After
    public void tearDown() {
        queue = null;
    }
}