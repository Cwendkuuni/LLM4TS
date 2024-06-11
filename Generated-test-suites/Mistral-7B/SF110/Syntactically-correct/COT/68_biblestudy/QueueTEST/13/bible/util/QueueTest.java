import org.junit.Test;
import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstruction() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(10);
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queueWithCapacity.getNumberItems());
        assertEquals(-1, queue.getPeakNumberItems());
        assertEquals(-1, queueWithCapacity.getPeakNumberItems());
        assertNull(queue.first);
        assertNull(queueWithCapacity.first);
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Queue queueWithOneElement = new Queue();
        queueWithOneElement.enqueue("test");
        assertTrue(queue.isEmpty());
        assertFalse(queueWithOneElement.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentInEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertEquals(2, queue.getNumberItems());
        Vector expectedVector = new Vector(2, 10);
        expectedVector.addElement("test1");
        expectedVector.addElement("test2");
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test(expected = IllegalStateException.class)
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        for (int i = 0; i < 3; i++) {
            queue.enqueue("test" + i);
        }
    }

    @Test
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        assertNull(queue.dequeue());
    }

    @Test
    public void testDequeueWhenNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Object result = queue.dequeue();
        assertEquals("test1", result);
        assertEquals(1, queue.getNumberItems());
        Object result2 = queue.dequeue();
        assertEquals("test2", result2);
        assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveWhenEmpty() {
        Queue queue = new Queue();
        queue.remove("test");
    }

    @Test
    public void testRemoveOneElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.remove("test1");
        Vector expectedVector = new Vector(1, 10);
        expectedVector.addElement("test2");
        assertEquals(expectedVector, queue.getObjects());
        assertEquals(1, queue.getNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.remove("test2");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.refreshElement("test1");
        assertEquals(2, queue.getNumberItems());
        Vector expectedVector = new Vector(2, 10);
        expectedVector.addElement("test2");
        expectedVector.addElement("test1");
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test(expected = NullPointerException.class)
    public void testNullArgumentInRefreshElement() {
        Queue queue = new Queue();
        queue.refreshElement(null);
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        Vector expectedVector = new Vector(3, 10);
        expectedVector.addElement("test1");
        expectedVector.addElement("test2");
        expectedVector.addElement("test3");
        assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceededWhenMaxCapacityIsZero() {
        Queue queue = new Queue(0);
        queue.enqueue("test");
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        String expectedString = "Queue:[numItems=3, maxNumItems=3, maxCapacity=10, getObjects()=[test1, test2, test3]\r\n]";
        assertEquals(expectedString, queue.toString());
    }
}