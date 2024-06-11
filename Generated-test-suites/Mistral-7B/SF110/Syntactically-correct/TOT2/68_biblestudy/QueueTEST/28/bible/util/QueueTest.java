package bible.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testIsNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        assertFalse(queue.isEmpty());
    }

    // isEmpty()
    @Test
    public void testIsEmptyWithSingleElement() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        assertFalse(queue.isEmpty());
    }

    // enqueue()
    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testEnqueueWithFullQueue() {
        Queue queue = new Queue(1);
        for (int i = 0; i < 2; i++) {
            queue.enqueue(new Object());
        }
    }

    // dequeue()
    @Test(expected = IllegalStateException.class)
    public void testDequeueWithEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testDequeueWithSingleElement() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        Object obj = queue.dequeue();
        assertEquals(obj, queue.getObjects().get(0));
    }

    @Test
    public void testDequeueWithMultipleElements() {
        Queue queue = new Queue();
        queue.enqueue(new Object1());
        queue.enqueue(new Object2());
        Object obj1 = queue.dequeue();
        Object obj2 = queue.dequeue();
        assertEquals(obj1, queue.getObjects().get(0));
        assertEquals(obj2, queue.getObjects().get(1));
    }

    // remove()
    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullObject() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveWithEmptyQueue() {
        Queue queue = new Queue();
        queue.remove(new Object());
    }

    @Test
    public void testRemoveWithSingleElement() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        assertFalse(queue.isEmpty());
        queue.remove(new Object());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemoveWithMultipleElements() {
        Queue queue = new Queue();
        queue.enqueue(new Object1());
        queue.enqueue(new Object2());
        assertEquals(2, queue.getNumberItems());
        queue.remove(new Object1());
        assertEquals(1, queue.getNumberItems());
        assertEquals(new Object2(), queue.getObjects().get(0));
    }

    // maxCapacityExceeded()
    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue(new Object());
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        for (int i = 0; i < 2; i++) {
            queue.enqueue(new Object());
        }
        assertTrue(queue.maxCapacityExceeded());
    }

    // Other methods
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        assertEquals(2, queue.getNumberItems());
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
        queue.enqueue(new Object1());
        queue.enqueue(new Object2());
        Vector vector = queue.getObjects();
        assertEquals(2, vector.size());
        assertEquals(new Object1(), vector.get(0));
        assertEquals(new Object2(), vector.get(1));
    }

    @Test
    public void testMaxCapacityExceededWithNoMaxCapacity() {
        Queue queue = new Queue();
        for (int i = 0; i < 100; i++) {
            queue.enqueue(new Object());
        }
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        queue.enqueue(new Object());
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Ljava.lang.Object;@656c36e4]", queue.toString());
    }
}