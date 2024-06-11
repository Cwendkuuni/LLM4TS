package bible.util;

import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {

    @Test
    public void testQueueCreation() {
        Queue q = new Queue();
        Queue qWithCapacity = new Queue(5);
        assertEquals(0, q.getNumberItems());
        assertEquals(-1, q.getPeakNumberItems());
        assertEquals(0, qWithCapacity.getNumberItems());
        assertEquals(-1, qWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testEnqueueAndDequeue() {
        Queue q = new Queue();
        q.enqueue("foo");
        q.enqueue("bar");
        assertEquals("foo", q.dequeue());
        assertEquals("bar", q.dequeue());
        assertEquals(0, q.getNumberItems());
        assertEquals(-1, q.getPeakNumberItems());
    }

    @Test
    public void testEnqueueAndDequeueMultipleObjects() {
        Queue q = new Queue();
        q.enqueue("foo");
        q.enqueue("bar");
        q.enqueue("baz");
        assertEquals("foo", q.dequeue());
        assertEquals("bar", q.dequeue());
        assertEquals("baz", q.dequeue());
        assertEquals(0, q.getNumberItems());
        assertEquals(-1, q.getPeakNumberItems());
    }

    @Test
    public void testMaxCapacity() {
        Queue q = new Queue(2);
        q.enqueue("foo");
        q.enqueue("bar");
        q.enqueue("baz");
        q.enqueue("qux");
        assertTrue(q.maxCapacityExceeded());
        assertEquals(3, q.getNumberItems());
        assertEquals(3, q.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testNullObjectAddedToQueue() {
        Queue q = new Queue();
        q.enqueue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testNullObjectRemovedFromQueue() {
        Queue q = new Queue();
        q.enqueue("foo");
        q.enqueue("bar");
        q.remove(null);
    }

    @Test
    public void testRefreshElement() {
        Queue q = new Queue();
        q.enqueue("foo");
        q.enqueue("bar");
        q.refreshElement("foo");
        assertEquals(2, q.getNumberItems());
        assertEquals(2, q.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue q = new Queue();
        q.enqueue("foo");
        q.enqueue("bar");
        Vector vec = q.getObjects();
        assertEquals(2, vec.size());
        assertEquals("foo", vec.get(0));
        assertEquals("bar", vec.get(1));
    }

    @Test
    public void testToString() {
        Queue q = new Queue();
        q.enqueue("foo");
        q.enqueue("bar");
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[foo, bar]\r\n]", q.toString());
    }
}