package bible.util;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.*;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue(5);
    }

    @After
    public void tearDown() {
        queue = null;
    }

    @Test
    public void testQueueConstruction() {
        assertEquals("Expected default Queue instance", "Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());

        Queue queue1 = new Queue();
        assertEquals("Expected empty Queue instance", "Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue1.toString());

        Queue queue2 = new Queue(3);
        assertEquals("Expected Queue instance with maxCapacity=3", "Queue:[numItems=0, maxNumItems=0, maxCapacity=3, getObjects()=[]]", queue2.toString());
    }

    @Test
    public void testIsEmpty() {
        assertTrue("Expected empty Queue", queue.isEmpty());
        queue.enqueue("item1");
        assertFalse("Expected non-empty Queue", queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        queue.enqueue(null);
        fail("Expected NullPointerException");
    }

    @Test
    public void testEnqueueObject() {
        queue.enqueue("item1");
        assertEquals("Expected Queue instance with one item", "Queue:[numItems=1, maxNumItems=1, maxCapacity=5, getObjects()=[item1]]", queue.toString());

        queue.enqueue("item2");
        assertEquals("Expected Queue instance with two items", "Queue:[numItems=2, maxNumItems=2, maxCapacity=5, getObjects()=[item1, item2]]", queue.toString());

        queue.enqueue("item3");
        assertEquals("Expected Queue instance with three items", "Queue:[numItems=3, maxNumItems=3, maxCapacity=5, getObjects()=[item1, item2, item3]]", queue.toString());

        queue.enqueue("item4");
        assertEquals("Expected Queue instance with four items", "Queue:[numItems=4, maxNumItems=4, maxCapacity=5, getObjects()=[item1, item2, item3, item4]]", queue.toString());

        queue.enqueue("item5");
        assertEquals("Expected Queue instance with five items", "Queue:[numItems=5, maxNumItems=5, maxCapacity=5, getObjects()=[item1, item2, item3, item4, item5]]", queue.toString());

        queue.enqueue("item6");
        assertEquals("Expected Queue instance with maxCapacity exceeded", "Queue:[numItems=5, maxNumItems=5, maxCapacity=5, getObjects()=[item1, item2, item3, item4, item5], maxCapacityExceeded=true]", queue.toString());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenEmpty() {
        queue.dequeue();
        fail("Expected IllegalStateException");
    }

    @Test
    public void testDequeue() {
        queue.enqueue("item1");
        assertEquals("Expected item1 as dequeued", "item1", queue.dequeue());
        assertEquals("Expected Queue instance with one item", "Queue:[numItems=0, maxNumItems=1, maxCapacity=5, getObjects()=[]]", queue.toString());

        queue.enqueue("item2");
        queue.enqueue("item3");
        assertEquals("Expected item1 as dequeued", "item1", queue.dequeue());
        assertEquals("Expected item2 as dequeued", "item2", queue.dequeue());
        assertEquals("Expected item3 as dequeued", "item3", queue.dequeue());
        assertEquals("Expected empty Queue instance", "Queue:[numItems=0, maxNumItems=3, maxCapacity=5, getObjects()=[]]", queue.toString());
    }

    @Test
    public void testRemoveObject() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        queue.refreshElement("item1");
        queue.refreshElement("item2");

        assertEquals("Expected Queue instance with item1 removed", "Queue:[numItems=2, maxNumItems=3, maxCapacity=5, getObjects()=[item3]]", queue.toString());

        queue.remove("item3");
        assertEquals("Expected Queue instance with item3 removed", "Queue:[numItems=1, maxNumItems=3, maxCapacity=5, getObjects()=[]]", queue.toString());
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        queue.refreshElement("item1");
        queue.refreshElement("item2");

        assertEquals("Expected Queue instance with refreshed elements", "Queue:[numItems=2, maxNumItems=3, maxCapacity=5, getObjects()=[item1, item2]]", queue.toString());
    }

    @Test
    public void testGetNumberItems() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        assertEquals("Expected number of items in Queue", 3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        assertEquals("Expected peak number of items in Queue", 3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        Vector expectedObjects = new Vector<>(3, 10);
        expectedObjects.add("item1");
        expectedObjects.add("item2");
        expectedObjects.add("item3");

        assertEquals("Expected objects in Queue", expectedObjects, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceded() {
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.enqueue("item4");
        queue.enqueue("item5");
        queue.enqueue("item6");

        assertTrue("Expected maxCapacityExceeded=true", queue.maxCapacityExceeded());
    }
}