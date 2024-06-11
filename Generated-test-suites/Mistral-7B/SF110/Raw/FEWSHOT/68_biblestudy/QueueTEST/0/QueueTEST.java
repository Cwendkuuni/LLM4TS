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
    public void testQueueConstructor() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);

        assertEquals(Queue.class, queue.getClass());
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());

        assertEquals(Queue.class, queueWithCapacity.getClass());
        assertEquals(0, queueWithCapacity.getNumberItems());
        assertEquals(0, queueWithCapacity.getPeakNumberItems());
        assertEquals(5, queueWithCapacity.getMaxCapacity());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Queue queueWithOneElement = new Queue();
        queueWithOneElement.enqueue("test");

        assertTrue(queue.isEmpty());
        assertFalse(queueWithOneElement.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        assertEquals("test1", queue.dequeue());
        assertEquals("test2", queue.dequeue());
        assertTrue(queue.isEmpty());

        queue.enqueue("test3");
        assertEquals("test3", queue.dequeue());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        assertEquals("test1", queue.dequeue());
        assertEquals("test2", queue.dequeue());

        assertNull(queue.dequeue());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        assertEquals(3, queue.remove("test2"));

        assertEquals("test1", queue.dequeue());
        assertEquals("test3", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        queue.refreshElement("test2");

        assertEquals("test2", queue.dequeue());
        assertEquals("test1", queue.dequeue());
        assertTrue(queue.isEmpty());
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

        Vector expectedVector = new Vector(3, 10);
        expectedVector.addElement("test1");
        expectedVector.addElement("test2");
        expectedVector.addElement("test3");

        assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        assertTrue(queue.maxCapacityExceeded());

        queue.dequeue();
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]\r\n]";
        assertEquals(expectedString, queue.toString());
    }
}