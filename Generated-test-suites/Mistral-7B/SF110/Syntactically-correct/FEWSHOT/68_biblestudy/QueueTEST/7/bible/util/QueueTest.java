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
        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueSingleItem() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        assertFalse(queue.isEmpty());
        assertEquals("item1", queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueMultipleItems() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        assertFalse(queue.isEmpty());
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());

        queue.enqueue("item3");
        queue.enqueue("item4");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testQueueMaxCapacity() {
        Queue queue = new Queue(5);
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");
        queue.enqueue("item4");
        queue.enqueue("item5");
        queue.enqueue("item6"); // Should throw exception

        assertEquals(5, queue.getPeakNumberItems());
        assertEquals(5, queue.getNumberItems());

        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();

        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");

        assertEquals(2, queue.getNumberItems());

        queue.refreshElement("item1");
        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.dequeue());
        assertEquals("item1", queue.dequeue());
        assertEquals("item2", queue.dequeue());
        assertEquals(0, queue.getNumberItems());

        queue.refreshElement("item1");
        queue.enqueue("item3");
        assertEquals(1, queue.getNumberItems());
        assertEquals("item1", queue.getObjects().get(0));
        assertEquals("item3", queue.getObjects().get(1));
    }

    @Test
    public void testQueueRemove() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        queue.remove("item2");
        assertEquals(2, queue.getNumberItems());
        assertEquals("item1", queue.getObjects().get(0));
        assertEquals("item3", queue.getObjects().get(1));

        queue.remove("item1");
        assertEquals(1, queue.getNumberItems());
        assertEquals("item3", queue.getObjects().get(0));

        queue.remove("item3");
        assertEquals(0, queue.getNumberItems());

        queue.remove("item1");
        assertEquals(0, queue.getNumberItems());
    }

    @Test
    public void testQueueToString() {
        Queue queue = new Queue();
        queue.enqueue("item1");
        queue.enqueue("item2");
        queue.enqueue("item3");

        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[item1, item2, item3]\r\n]";
        assertEquals(expected, queue.toString());
    }
}