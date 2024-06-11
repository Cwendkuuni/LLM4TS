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
    public void testQueueCreation() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(10);
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[],]", queue.toString());
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=10, getObjects()=[],]", queueWithCapacity.toString());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("Test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        assertNull(queue.dequeue());
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.remove("Test1");
        assertFalse(queue.contains("Test1"));
        assertEquals("Test2", queue.dequeue());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.refreshElement("Test1");
        assertEquals("Test1", queue.dequeue());
        assertEquals("Test2", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        assertEquals(0, queue.getPeakNumberItems());

        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        queue.enqueue("Test3");

        Vector expectedVector = new Vector(3, 10);
        expectedVector.addElement("Test1");
        expectedVector.addElement("Test2");
        expectedVector.addElement("Test3");

        Vector actualVector = queue.getObjects();
        assertEquals(expectedVector, actualVector);
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("Test1");
        queue.enqueue("Test2");
        assertTrue(queue.maxCapacityExceeded());
    }
}