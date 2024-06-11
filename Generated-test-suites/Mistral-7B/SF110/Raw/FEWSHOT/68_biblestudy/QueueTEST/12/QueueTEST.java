package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertThat;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueEmptyConstructor() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(-1, queue.maxCapacity());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueueWithCapacityConstructor() {
        Queue queue = new Queue(5);
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(5, queue.maxCapacity());
    }

    @Test
    public void testQueueIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testQueueIsNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        assertEquals("apple", queue.dequeue());
        assertEquals("banana", queue.dequeue());
        assertEquals("cherry", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.enqueue("durian");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test(expected = NullPointerException.class)
    public void testNullObjectEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.refreshElement("apple");
        assertEquals("apple", queue.getObjects().get(0));
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        Vector expected = new Vector(3, 10);
        expected.add("apple");
        expected.add("banana");
        expected.add("cherry");
        Vector actual = queue.getObjects();
        assertEquals(expected, actual);
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.remove("cherry");
        assertEquals(2, queue.getNumberItems());
        Vector actual = queue.getObjects();
        Vector expected = new Vector(2, 10);
        expected.add("apple");
        expected.add("banana");
        assertSame(expected, actual);
    }

    @Test
    public void testRemoveObjectNotPresent() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.remove("cherry");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testRemoveMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.enqueue("durian");
        int removedCount = queue.remove("banana");
        assertEquals(2, removedCount);
        Vector actual = queue.getObjects();
        Vector expected = new Vector(2, 10);
        expected.add("apple");
        expected.add("durian");
        assertSame(expected, actual);
    }

    @Test
    public void testRemoveAllObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        queue.removeAll(new Vector<Object>() {{
            add("apple");
            add("banana");
            add("cherry");
        }});
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");
        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[apple, banana, cherry]\r\n]";
        String actual = queue.toString();
        assertEquals(expected, actual);
    }
}