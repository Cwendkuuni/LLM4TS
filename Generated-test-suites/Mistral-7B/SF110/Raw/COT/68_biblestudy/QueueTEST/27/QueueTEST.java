package bible.util;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        assertEquals(0, queue.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
        assertEquals(0, queue.maxCapacity);
    }

    @Test
    public void testQueueConstructorWithCapacity() {
        Queue queue = new Queue(5);
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(5, queue.maxCapacity);
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        
        queue.enqueue(new Object());
        assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        Object dequeuedObject = queue.dequeue();
        assertEquals(new Object(), dequeuedObject);
        
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        queue.remove(new Object());
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        
        queue.remove(new Object());
        assertEquals(1, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        queue.remove(new Object());
        queue.remove(new Object());
        queue.remove(new Object());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        Object refreshedObject = new Object();
        queue.refreshElement(refreshedObject);
        
        assertEquals(3, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
        
        Object dequeuedObject = queue.dequeue();
        assertEquals(refreshedObject, dequeuedObject);
        
        assertEquals(2, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        
        queue.enqueue(new Object());
        assertEquals(1, queue.getNumberItems());
        
        queue.enqueue(new Object());
        assertEquals(2, queue.getNumberItems());
        
        queue.enqueue(new Object());
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        Vector objects = queue.getObjects();
        
        assertEquals(3, objects.size());
        for (int i = 0; i < 3; i++) {
            assertEquals(queue.dequeue(), objects.get(i));
        }
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        assertTrue(queue.maxCapacityExceeded());
        
        queue.dequeue();
        
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(5);
        
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToString() {
        Queue queue = new Queue();
        queue.toString();
    }
}