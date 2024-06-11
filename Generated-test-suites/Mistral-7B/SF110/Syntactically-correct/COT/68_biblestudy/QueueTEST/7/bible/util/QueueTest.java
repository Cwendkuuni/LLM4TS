package bible.util;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class QueueTest {

    private Queue queue;

    @Before
    public void setUp() {
        queue = new Queue();
    }

    @Test
    public void testConstructor() {
        Queue q = new Queue();
        Queue q2 = new Queue(10);
        assertEquals(-1, q.getMaxCapacity());
        assertEquals(0, q2.getMaxCapacity());
    }

    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        Node expectedNode1 = new Node("test1");
        Node expectedNode2 = new Node("test2");
        synchronized (queue) {
            Node currentNode = queue.first;
            assertEquals("test1", currentNode.value);
            assertSame(expectedNode1, currentNode);
            currentNode = currentNode.next;
            assertEquals("test2", currentNode.value);
            assertSame(expectedNode2, currentNode);
        }
        assertEquals(2, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        queue.enqueue(null);
    }

    @Test
    public void testDequeue() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        Object dequeuedObject = queue.dequeue();
        assertEquals("test1", dequeuedObject);
        dequeuedObject = queue.dequeue();
        assertEquals("test2", dequeuedObject);
        assertNull(queue.dequeue());
        assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenQueueIsEmpty() {
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.remove("test1");
        Node expectedNode1 = new Node("test2");
        synchronized (queue) {
            Node currentNode = queue.first;
            assertSame(expectedNode1, currentNode);
        }
        queue.remove("test3");
        assertEquals(1, queue.getNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveWithNullObject() {
        queue.remove(null);
    }

    @Test
    public void testRefreshElement() {
        queue.enqueue("test1");
        queue.refreshElement("test1");
        assertEquals("test1", queue.dequeue().value);
        queue.refreshElement("test2");
        assertEquals("test2", queue.dequeue().value);
        assertEquals("test1", queue.dequeue().value);
    }

    @Test
    public void testGetNumberItems() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        assertEquals(3, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        Vector expectedVector = new Vector(3, 10);
        expectedVector.add("test1");
        expectedVector.add("test2");
        expectedVector.add("test3");
        Vector actualVector = queue.getObjects();
        assertEquals(expectedVector, actualVector);
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("test1");
        queue.enqueue("test2");
        assertTrue(queue.maxCapacityExceeded());
        queue.refreshElement("test1");
        assertFalse(queue.maxCapacityExceeded());
    }

    @After
    public void tearDown() {
        queue = null;
    }
}