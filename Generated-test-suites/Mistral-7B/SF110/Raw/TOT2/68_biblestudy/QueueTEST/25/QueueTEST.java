package bible.util;

import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.experimental.annotations.EdgeCases;
import org.junit.experimental.annotations.ExpectedException;
import java.util.*;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test");
        assertEquals("test", queue.getObjects().get(0));
        queue.enqueue(123);
        assertEquals(123, queue.getObjects().get(1));
        queue.enqueue(new Object());
        assertEquals(3, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueMaxCapacity() {
        Queue queue = new Queue(3);
        for (int i = 0; i < 3; i++) {
            queue.enqueue("test" + i);
        }
        try {
            queue.enqueue("test3");
            fail("Expected QueueIsFullException");
        } catch (QueueIsFullException e) {
            // Expected
        }
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test");
        assertEquals("test", queue.dequeue());
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test(expected = NoSuchElementException.class)
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test(expected = NoSuchElementException.class)
    public void testDequeueSingleElementQueue() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.dequeue();
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        queue.refreshElement("test2");
        assertEquals(2, queue.remove("test2").getValue());
        assertEquals(2, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test
    public void testRemoveNotPresentObject() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        int removedItems = queue.remove("test4");
        assertEquals(0, removedItems);
    }

    @Test
    public void testRemoveMultipleOccurrences() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test1");
        queue.enqueue("test1");
        int removedItems = queue.remove("test1");
        assertEquals(3, removedItems);
    }

    @Test(expected = QueueIsFullException.class)
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        queue.enqueue("test4");
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        String expectedString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[test1, test2, test3], maxCapacityExceeded=false]";
        assertEquals(expectedString, queue.toString());
    }
}

class QueueIsFullException extends RuntimeException {}