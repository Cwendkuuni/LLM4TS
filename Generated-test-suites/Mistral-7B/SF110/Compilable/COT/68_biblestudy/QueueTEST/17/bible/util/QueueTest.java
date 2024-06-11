package bible.util;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testConstructor() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);

        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queueWithCapacity.getNumberItems());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Queue queueWithElement = new Queue();
        queueWithElement.enqueue("test");

        assertTrue(queue.isEmpty());
        assertFalse(queueWithElement.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        Vector expectedVector = new Vector(2, 10);
        expectedVector.add("test1");
        expectedVector.add("test2");

        assertEquals(expectedVector, queue.getObjects());
        assertEquals(2, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.dequeue();

        Vector expectedVector = new Vector(1, 10);
        expectedVector.add("test2");

        assertEquals(expectedVector, queue.getObjects());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();

        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.remove("test1");

        Vector expectedVector = new Vector(1, 10);
        expectedVector.add("test2");

        assertEquals(expectedVector, queue.getObjects());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testRemoveMultipleOccurrences() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.remove("test1");

        Vector expectedVector = new Vector(1, 10);
        expectedVector.add("test2");

        assertEquals(expectedVector, queue.getObjects());
        assertEquals(1, queue.getNumberItems());
        assertEquals(2, queue.getPeakNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.remove("test2");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.refreshElement("test1");

        Vector expectedVector = new Vector(1, 10);
        expectedVector.add("test1");

        assertEquals(expectedVector, queue.getObjects());
        assertEquals(1, queue.getNumberItems());
        assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        queue.enqueue("test4");
        queue.enqueue("test5");

        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2],\r\n]";
        assertEquals(expectedString, queue.toString());
    }
}