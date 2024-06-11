package bible.util;

import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {

    @Test
    public void testQueueEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueBasicFunctionality() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        assertFalse(queue.isEmpty());
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        Object dequeuedObject = queue.dequeue();
        assertEquals("apple", dequeuedObject);
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        Object dequeuedObject2 = queue.dequeue();
        assertEquals("banana", dequeuedObject2);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testMaxCapacity() {
        Queue queue = new Queue(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue("test" + i);
        }
        assertTrue(queue.maxCapacityExceeded());
        assertEquals(5, queue.getNumberItems());
        assertEquals(5, queue.getPeakNumberItems());
        queue.refreshElement("test5");
        assertEquals(6, queue.getNumberItems());
        assertEquals(6, queue.getPeakNumberItems());
    }

    @Test
    public void testMaxCapacityNegative() {
        Queue queue = new Queue(-1);
        queue.enqueue("test");
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testNullDequeue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");
        Object dequeuedObject = queue.dequeue();
        assertEquals("apple", dequeuedObject);
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
        int removedCount = queue.remove("banana");
        assertEquals(1, removedCount);
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        Object dequeuedObject2 = queue.dequeue();
        assertEquals("orange", dequeuedObject2);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testRemoveNonExistingObject() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        int removedCount = queue.remove("orange");
        assertEquals(0, removedCount);
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.refreshElement("banana");
        Object dequeuedObject = queue.dequeue();
        assertEquals("banana", dequeuedObject);
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
        Object dequeuedObject2 = queue.dequeue();
        assertEquals("apple", dequeuedObject2);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");
        List<Object> list = queue.getObjects();
        assertEquals(3, list.size());
        assertEquals("apple", list.get(0));
        assertEquals("banana", list.get(1));
        assertEquals("orange", list.get(2));
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("orange");
        String queueString = queue.toString();
        assertTrue(queueString.contains("Queue:[numItems=3, maxNumItems=2, maxCapacity=-1, getObjects()=[apple, banana, orange]\r\n]"));
    }
}