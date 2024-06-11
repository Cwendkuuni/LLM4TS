package bible.util;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.Vector;

import org.junit.Before;
import org.junit.Test;

public class QueueTest {

    Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testQueueEmpty() {
        assertTrue(queue.isEmpty());
        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());
        assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueBasicOperations() {
        queue.enqueue("one");
        queue.enqueue("two");
        queue.enqueue("three");

        assertEquals("one", queue.dequeue().toString());
        assertEquals("two", queue.dequeue().toString());
        assertEquals("three", queue.dequeue().toString());
        assertTrue(queue.isEmpty());
        assertEquals(0, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        queue.enqueue("four");
        queue.enqueue("five");
        queue.enqueue("six");

        assertEquals("four", queue.dequeue().toString());
        assertEquals("five", queue.dequeue().toString());
        assertEquals("six", queue.dequeue().toString());

        queue.refreshElement("four");
        queue.refreshElement("five");
        queue.refreshElement("six");

        assertEquals("four", queue.dequeue().toString());
        assertEquals("five", queue.dequeue().toString());
        assertEquals("six", queue.dequeue().toString());

        queue.enqueue("seven");
        queue.enqueue("eight");
        queue.enqueue("nine");

        assertEquals("seven", queue.dequeue().toString());
        assertEquals("eight", queue.dequeue().toString());
        assertEquals("nine", queue.dequeue().toString());

        assertFalse(queue.maxCapacityExceeded());
        queue.enqueue("ten");
        assertTrue(queue.maxCapacityExceeded());

        Vector vec = queue.getObjects();
        assertEquals(3, vec.size());
        assertEquals("four", vec.get(0));
        assertEquals("five", vec.get(1));
        assertEquals("six", vec.get(2));
    }

    @Test
    public void testQueueRemove() {
        queue.enqueue("one");
        queue.enqueue("two");
        queue.enqueue("three");

        assertEquals("one", queue.dequeue().toString());
        assertEquals("two", queue.dequeue().toString());
        assertEquals("three", queue.dequeue().toString());

        queue.enqueue("four");
        queue.enqueue("five");
        queue.enqueue("six");

        assertEquals(3, queue.remove("three").intValue());
        assertEquals("four", queue.dequeue().toString());
        assertEquals("five", queue.dequeue().toString());
        assertEquals("six", queue.dequeue().toString());
        assertEquals(0, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        queue.enqueue("seven");
        queue.enqueue("eight");
        queue.enqueue("nine");

        assertEquals(3, queue.remove("seven").intValue());
        assertEquals("eight", queue.dequeue().toString());
        assertEquals("nine", queue.dequeue().toString());
        assertEquals("", queue.dequeue().toString());
        assertEquals(1, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());

        queue.refreshElement("eight");
        assertEquals("eight", queue.dequeue().toString());
        assertEquals("nine", queue.dequeue().toString());
        assertEquals("", queue.dequeue().toString());
        assertEquals(0, queue.getNumberItems());
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testQueueNullObject() {
        queue.enqueue("one");
        queue.enqueue("two");
        queue.enqueue("three");

        queue.remove(null);
    }
}