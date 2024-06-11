package bible.util;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import java.util.Vector;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object");
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        Object dequeuedObj = queue.dequeue();
        assertEquals("Test Object 1", dequeuedObj);
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        queue.remove("Test Object 1");
        assertFalse(queue.contains("Test Object 1"));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.refreshElement("Test Object 2");
        assertEquals("Test Object 2", queue.getObjects().get(0));
        assertEquals(1, queue.getNumberItems());
    }
    
    @Test
    public void testGetNumberItemsAndPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }
    
    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        Vector result = queue.getObjects();
        assertEquals(2, result.size());
        assertEquals("Test Object 1", result.get(0));
        assertEquals("Test Object 2", result.get(1));
    }
    
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        for (int i = 0; i < 6; i++) {
            queue.enqueue(String.valueOf(i));
        }
        assertTrue(queue.maxCapacityExceeded());
    }
    
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test Object 1");
        queue.enqueue("Test Object 2");
        assertEquals("Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test Object 1, Test Object 2],]\r\n", queue.toString());
    }
}