package bible.util;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueue() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
        assertEquals(0, queue.maxCapacity);
    }

    @Test
    public void testQueueWithCapacity() {
        Queue queue = new Queue(5);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(5, queue.maxCapacity);
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");
        Object dequeuedObject = queue.dequeue();
        assertEquals("object1", dequeuedObject);
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        Object dequeuedObject = queue.dequeue();
        assertNull(dequeuedObject);
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        int removedCount = queue.remove("object2");
        assertEquals(1, removedCount);
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        Object dequeuedObject = queue.dequeue();
        assertEquals("object1", dequeuedObject);
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");
        int removedCount = queue.remove("object3");
        assertEquals(0, removedCount);
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.refreshElement("object1");
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        Object dequeuedObject = queue.dequeue();
        assertEquals("object1", dequeuedObject);
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("object1", objects.get(0));
        assertEquals("object2", objects.get(1));
        assertEquals("object3", objects.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("object1");
        queue.enqueue("object2");
        queue.enqueue("object3");
        assertFalse(queue.maxCapacityExceeded());
    }
}