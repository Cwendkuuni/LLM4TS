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
        Queue<Object> queue = new Queue<>();
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertNull(queue.dequeue());
    }

    @Test
    public void testQueue() {
        Queue<Object> queue = new Queue<>();
        queue.enqueue("item1");
        assertEquals("item1", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item2");
        queue.enqueue("item3");
        assertEquals("item2", queue.dequeue());
        assertEquals("item3", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item4");
        queue.enqueue("item5");
        assertEquals("item4", queue.dequeue());
        assertEquals("item5", queue.dequeue());
        assertEquals("item4", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(3, queue.getNumberItems()); // last item is still in queue

        queue.refreshElement("refreshed");
        assertEquals("refreshed", queue.dequeue());
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testQueueMaxCapacity() {
        Queue<Object> queue = new Queue<>(1);
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item3"); // should throw exception
        assertFalse(queue.isEmpty());
        assertEquals(1, queue.getNumberItems());
        assertNull(queue.dequeue()); // exception should have been thrown before this

        Queue<Object> queue2 = new Queue<>(3);
        queue2.enqueue("item1");
        queue2.enqueue("item2");
        queue2.enqueue("item3");
        queue2.enqueue("item4"); // should not throw exception
        assertEquals("item1", queue2.dequeue());
        assertEquals("item2", queue2.dequeue());
        assertEquals("item3", queue2.dequeue());
        assertEquals("item4", queue2.dequeue());
        assertTrue(queue2.isEmpty());
        assertEquals(0, queue2.getNumberItems());
    }

    @Test
    public void testQueueVector() {
        Queue<Object> queue = new Queue<>();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        Vector<Object> vector = queue.getObjects();
        assertEquals("item1", vector.get(0));
        assertEquals("item2", vector.get(1));
        assertEquals("item3", vector.get(2));
    }
}