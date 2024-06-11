package bible.util;

import java.util.Vector;
import org.junit.Test;
import static org.junit.Assert.*;

public class QueueTest {
    
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertFalse(queue.isEmpty());
        queue.dequeue();
        assertTrue(queue.isEmpty());
        queue.enqueue("testObject");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        Node node1 = queue.first;
        Node node2 = queue.last;
        assertEquals("testObject1", node1.value);
        assertEquals("testObject2", node2.value);
        assertNotNull(node1.next);
        assertNotNull(node2.next);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        Object dequeuedObject = queue.dequeue();
        assertEquals("testObject1", dequeuedObject);
        assertNull(queue.dequeue());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        Object removedObject = queue.remove("testObject1");
        assertEquals("testObject1", removedObject);
        assertNotSame(queue.first, removedObject);
        Object removedObject2 = queue.remove("testObject2");
        assertEquals("testObject2", removedObject2);
        assertNull(queue.remove("testObject3"));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        for (int i = 0; i < 4; i++) {
            queue.enqueue("testObject");
        }
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.refreshElement("testObject1");
        assertEquals("testObject1", queue.getObjects().get(0));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        Vector objects = queue.getObjects();
        assertEquals("testObject1", objects.get(0));
        assertEquals("testObject2", objects.get(1));
    }
}