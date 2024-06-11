package bible.util;

import java.util.Vector;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class QueueTest {

    Queue queue;

    @Before
    public void setUp() {
        queue = new Queue(5);
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
    public void testQueueNotEmpty() {
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        Vector objects = queue.getObjects();
        assertEquals("apple", objects.get(0));
        assertEquals("banana", objects.get(1));
        assertEquals("cherry", objects.get(2));
    }

    @Test(expected = NullPointerException.class)
    public void testNullObjectEnqueue() {
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        queue.enqueue("apple");
        queue.enqueue("banana");

        Object dequeuedObject = queue.dequeue();
        assertEquals("apple", dequeuedObject);
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());

        dequeuedObject = queue.dequeue();
        assertEquals("banana", dequeuedObject);
        assertEquals(0, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("cherry");
        queue.enqueue("orange");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        dequeuedObject = queue.dequeue();
        assertEquals("cherry", dequeuedObject);
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        dequeuedObject = queue.dequeue();
        assertEquals("orange", dequeuedObject);
        assertEquals(0, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenEmpty() {
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");

        int removedObjects = queue.remove("banana");
        assertEquals(1, removedObjects);
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        Vector objects = queue.getObjects();
        assertEquals("apple", objects.get(0));
        assertEquals("cherry", objects.get(1));

        removedObjects = queue.remove("cherry");
        assertEquals(1, removedObjects);
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        removedObjects = queue.remove("apple");
        assertEquals(1, removedObjects);
        assertEquals(0, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());

        removedObjects = queue.remove("orange");
        assertEquals(0, removedObjects);
        assertEquals(0, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveWhenEmpty() {
        queue.remove("apple");
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("apple");
        queue.enqueue("banana");

        queue.refreshElement("banana");
        Vector objects = queue.getObjects();
        assertEquals("apple", objects.get(0));
        assertEquals("apple", objects.get(1));

        queue.refreshElement("cherry");
        objects = queue.getObjects();
        assertEquals("apple", objects.get(0));
        assertEquals("apple", objects.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(2);
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceded() {
        queue = new Queue(5);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.enqueue("orange");
        queue.enqueue("grape");
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetObjectsConcurrentModification() {
        queue.enqueue("apple");
        queue.enqueue("banana");

        Vector objects = queue.getObjects();
        objects.add("orange");
    }
}