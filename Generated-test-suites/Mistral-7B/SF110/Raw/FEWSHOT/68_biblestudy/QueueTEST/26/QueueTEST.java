package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueueConstructorWithNoCapacity() {
        Queue queue = new Queue();
        assertEquals(Queue.class.getName() + ":[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());
        assertFalse(queue.maxCapacityExceeded());
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testQueueConstructorWithCapacity() {
        Queue queue = new Queue(5);
        assertEquals(Queue.class.getName() + ":[numItems=0, maxNumItems=0, maxCapacity=5, getObjects()=[]]", queue.toString());
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        queue.enqueue("foo");
        assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        assertEquals("foo", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testEnqueueAndDequeueMultipleObjects() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");

        assertEquals("foo", queue.dequeue());
        assertEquals("bar", queue.dequeue());
        assertEquals("baz", queue.dequeue());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");

        int removedObjects = queue.remove("bar");
        assertEquals(1, removedObjects);

        assertEquals("foo", queue.dequeue().toString());
        assertEquals("baz", queue.dequeue().toString());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.refreshElement("foo");
        assertEquals("foo", queue.dequeue().toString());
        assertTrue(queue.isEmpty());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");

        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");

        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("foo");
        queue.enqueue("bar");
        queue.enqueue("baz");

        Vector objects = queue.getObjects();
        assertEquals(3, objects.size());
        assertEquals("foo", objects.get(0).toString());
        assertEquals("bar", objects.get(1).toString());
        assertEquals("baz", objects.get(2).toString());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("foo");
        queue.enqueue("bar");
        assertTrue(queue.maxCapacityExceeded());
    }
}