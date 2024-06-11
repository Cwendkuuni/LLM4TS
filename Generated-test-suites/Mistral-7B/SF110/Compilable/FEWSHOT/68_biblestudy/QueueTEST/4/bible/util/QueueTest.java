package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {

    @Test
    public void testConstructor() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);

        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=5, getObjects()=[]]", queueWithCapacity.toString());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Queue queueWithElements = new Queue();
        queueWithElements.enqueue("element1");

        assertTrue(queue.isEmpty());
        assertFalse(queueWithElements.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();

        queue.enqueue("element1");
        queue.enqueue("element2");

        Vector expectedVector = new Vector(2, 10);
        expectedVector.add("element1");
        expectedVector.add("element2");

        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[element1, element2]]", queue.toString());
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("element1");
        queue.enqueue("element2");

        Object dequeuedObject = queue.dequeue();
        assertEquals("element1", dequeuedObject);

        queue.dequeue();

        Vector expectedVector = new Vector(1, 10);
        expectedVector.add("element2");

        assertEquals("Queue:[numItems=0, maxNumItems=2, maxCapacity=-1, getObjects()=[element2]]", queue.toString());
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();

        Object dequeuedObject = queue.dequeue();
        assertNull(dequeuedObject);

        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("element1");
        queue.enqueue("element2");

        int removedElements = queue.remove("element1");
        assertEquals(1, removedElements);

        Vector expectedVector = new Vector(1, 10);
        expectedVector.add("element2");

        assertEquals("Queue:[numItems=1, maxNumItems=2, maxCapacity=-1, getObjects()=[element2]]", queue.toString());
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testRemoveDoesNotExist() {
        Queue queue = new Queue();
        queue.enqueue("element1");
        queue.enqueue("element2");

        int removedElements = queue.remove("element3");
        assertEquals(0, removedElements);

        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[element1, element2]]", queue.toString());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("element1");
        queue.enqueue("element2");

        queue.refreshElement("element1");

        Vector expectedVector = new Vector(2, 10);
        expectedVector.add("element1");
        expectedVector.add("element2");

        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[element1, element2]]", queue.toString());
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("element1");
        queue.enqueue("element2");

        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("element1");
        queue.enqueue("element2");

        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public
    void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("element1");
        queue.enqueue("element2");

        assertTrue(queue.maxCapacityExceeded());

        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=1, getObjects()=[element1, element2]]", queue.toString());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("element1");
        queue.enqueue("element2");
        queue.enqueue("element3");

        assertFalse(queue.maxCapacityExceeded());

        assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[element1, element2, element3]]", queue.toString());
    }

}