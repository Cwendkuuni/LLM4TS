package bible.util;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.*;

import org.junit.Test;

public class QueueTest {

    @Test
    public void testQueueCreation() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=5, getObjects()=[]]", queueWithCapacity.toString());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
        queue.enqueue("test");
        assertFalse(queue.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Node expectedNode1 = new Node("test1");
        Node expectedNode2 = new Node("test2");
        synchronized (queue) {
            Node currentNode = queue.first;
            assertEquals("test1", currentNode.value);
            assertEquals("test2", currentNode.next.value);
            assertEquals(2, queue.numItems);
        }
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        synchronized (queue) {
            Object dequeuedObject = queue.dequeue();
            assertEquals("test1", dequeuedObject);
            assertEquals(1, queue.numItems);
        }
    }

    @Test(expected = NullPointerException.class)
    public void testDequeueNullObject() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        int removedItems = queue.remove("test1");
        assertEquals(1, removedItems);
        int remainingItems = queue.numItems;
        assertEquals(1, remainingItems);
        Node expectedNode = queue.first;
        assertEquals("test2", expectedNode.value);
    }

    @Test
    public void testRemoveNonExistingObject() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        int removedItems = queue.remove("test2");
        assertEquals(0, removedItems);
        int remainingItems = queue.numItems;
        assertEquals(1, remainingItems);
    }

    @Test(expected = NullPointerException.class)
    public void testRemoveNullObject() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.refreshElement("test1");
        Node expectedNode = queue.first;
        assertEquals("test1", expectedNode.value);
        int remainingItems = queue.numItems;
        assertEquals(1, remainingItems);
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        int numberItems = queue.getNumberItems();
        assertEquals(2, numberItems);
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        int peakNumberItems = queue.getPeakNumberItems();
        assertEquals(2, peakNumberItems);
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Vector objects = queue.getObjects();
        List expectedObjects = new ArrayList();
        expectedObjects.add("test1");
        expectedObjects.add("test2");
        assertEquals(expectedObjects, objects);
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(3);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue(5);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        queue.enqueue("test4");
        queue.enqueue("test5");
        assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[test1, test2]]";
        assertEquals(expectedString, queue.toString());
    }

}