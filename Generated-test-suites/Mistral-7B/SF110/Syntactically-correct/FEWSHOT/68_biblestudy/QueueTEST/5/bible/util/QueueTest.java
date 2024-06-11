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
    public void testQueueEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueueCreationWithCapacity() {
        Queue queue = new Queue(5);
        assertFalse(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
        assertEquals(5, queue.getMaxCapacity());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.enqueue("bar");
        assertEquals("foo", queue.dequeue());
        assertEquals("bar", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testEnqueueAndGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");
        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("foo", objects.get(0));
        assertEquals("bar", objects.get(1));
        assertEquals("baz", objects.get(2));
    }

    @Test
    public void testEnqueueAndMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");
        assertTrue(queue.maxCapacityExceeded());
        queue.refreshElement("foo");
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testRemoveObjectExists() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.enqueue("bar");
        int removed = queue.remove("foo");
        assertEquals(1, removed);
        assertEquals("bar", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemoveObjectDoesNotExist() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        int removed = queue.remove("bar");
        assertEquals(0, removed);
        assertEquals("foo", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.refreshElement("foo");
        assertEquals("foo", queue.dequeue());
        queue.enqueue("bar");
        queue.refreshElement("foo");
        assertEquals("foo", queue.dequeue());
        assertEquals("bar", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue(3);
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");
        assertEquals("Queue:[numItems=3, maxNumItems=3, maxCapacity=3, getObjects()=[foo, bar, baz]\r\n]", queue.toString());
    }

}