package bible.util;

import static org.junit.Assert.*;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueueConstruction() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        assertEquals("Queue with no capacity.", 0, queue.getNumberItems());
        assertEquals("Queue with capacity.", 0, queueWithCapacity.getNumberItems());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue("New queue should be empty.", queue.isEmpty());

        queue.enqueue("Test Object");
        assertFalse("Enqueued object should not make queue empty.", queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueObjects() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");

        assertEquals("Number of items in the queue.", 3, queue.getNumberItems());
        Vector expectedVector = new Vector(3, 10);
        expectedVector.add("Object 1");
        expectedVector.add("Object 2");
        expectedVector.add("Object 3");

        Vector actualVector = queue.getObjects();
        assertEquals("Objects in the queue.", expectedVector, actualVector);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");

        assertEquals("Dequeued object 1.", "Object 1", queue.dequeue());
        assertEquals("Dequeued object 2.", "Object 2", queue.dequeue());
        assertEquals("Queue should be empty after dequeuing all objects.", true, queue.isEmpty());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");

        assertEquals("Number of items in the queue after removing Object 1.", 2, queue.getNumberItems());
        queue.remove("Object 1");

        Vector expectedVector = new Vector(2, 10);
        expectedVector.add("Object 2");
        expectedVector.add("Object 3");

        Vector actualVector = queue.getObjects();
        assertEquals("Objects in the queue after removing Object 1.", expectedVector, actualVector);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistingObject() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");

        queue.remove("Object 3");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.refreshElement("Object 1");

        Vector expectedVector = new Vector(2, 10);
        expectedVector.add("Object 1");
        expectedVector.add("Object 2");

        Vector actualVector = queue.getObjects();
        assertEquals("Objects in the queue after refreshing Object 1.", expectedVector, actualVector);
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");

        assertEquals("Number of items in the queue.", 3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");

        assertEquals("Maximum number of items in the queue.", 3, queue.getPeakNumberItems());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");

        assertTrue("Queue should not exceed its maximum capacity.", queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(5);
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");
        queue.enqueue("Object 4");

        assertFalse("Queue should not exceed its maximum capacity.", queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Object 1");
        queue.enqueue("Object 2");
        queue.enqueue("Object 3");

        String expectedString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Object 1, Object 2, Object 3]\r\n]";
        String actualString = queue.toString();

        assertEquals("ToString representation of the queue.", expectedString, actualString);
    }
}