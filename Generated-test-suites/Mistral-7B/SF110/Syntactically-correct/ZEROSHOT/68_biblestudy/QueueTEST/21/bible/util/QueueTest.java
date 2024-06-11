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

    @After
    public void tearDown() {
        queue = null;
    }

    @Test
    public void testQueueEmpty() {
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueCreation() {
        queue = new Queue(5);
        assertFalse(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(5, queue.getMaxCapacity());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueObject() {
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals("apple", queue.getObjects().get(0));
        assertEquals("apple", queue.getObjects().lastElement());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testEnqueueMultipleObjects() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertEquals("cherry", queue.getObjects().lastElement());
        Vector expectedVector = new Vector(3, 10);
        expectedVector.addElement("cherry");
        expectedVector.addElement("banana");
        expectedVector.addElement("apple");
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testDequeueObject() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        Object dequeuedObject = queue.dequeue();
        assertEquals("apple", dequeuedObject);
        assertEquals(2, queue.getNumberItems());
        assertEquals("banana", queue.getObjects().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmptyQueue() {
        queue.dequeue();
    }

    @Test
    public void testRemoveObject() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.remove("cherry");
        assertEquals(2, queue.getNumberItems());
        assertFalse(queue.getObjects().contains("cherry"));
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveNonExistingObject() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.remove("orange");
    }

    @Test
    public void testRefreshElementObject() {
        queue.enqueue("apple");
        queue.refreshElement("apple");
        assertEquals("apple", queue.getObjects().get(0));
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(3);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        String expectedString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[cherry, banana, apple]]";
        assertEquals(expectedString, queue.toString());
    }
}