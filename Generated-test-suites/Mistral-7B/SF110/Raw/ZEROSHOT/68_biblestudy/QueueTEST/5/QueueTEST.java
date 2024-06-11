package bible.util;

import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueCreation() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
        Assert.assertEquals(-1, queue.getMaxCapacity());
        Assert.assertEquals(5, queueWithCapacity.getMaxCapacity());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());

        Queue queueWithElement = new Queue();
        queueWithElement.enqueue("Element");
        Assert.assertFalse(queueWithElement.isEmpty());
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Element1");
        queue.enqueue("Element2");
        Vector expectedVector = new Vector(2, 10);
        expectedVector.add("Element1");
        expectedVector.add("Element2");

        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Element1");
        queue.enqueue("Element2");

        Object dequeuedElement = queue.dequeue();
        Assert.assertEquals("Element1", dequeuedElement);
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());

        dequeuedElement = queue.dequeue();
        Assert.assertEquals("Element2", dequeuedElement);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());

        queue.enqueue("Element3");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Element1");
        queue.enqueue("Element2");

        int originalNumItems = queue.getNumberItems();
        queue.remove("Element1");

        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals(originalNumItems - 1, queue.remove("Element2").intValue());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Element1");
        queue.enqueue("Element2");
        queue.refreshElement("Element1");

        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals("Element1", queue.getObjects().get(0));
        Assert.assertEquals("Element2", queue.getObjects().get(1));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Element1");
        queue.enqueue("Element2");

        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("Element1");
        queue.enqueue("Element2");

        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("Element1");
        queue.enqueue("Element2");

        Vector expectedVector = new Vector(2, 10);
        expectedVector.add("Element1");
        expectedVector.add("Element2");

        Assert.assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(4);
        for (int i = 0; i < 5; i++) {
            queue.enqueue("Element" + i);
        }

        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Element1");
        queue.enqueue("Element2");

        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Element1, Element2]]";
        Assert.assertEquals(expectedString, queue.toString());
    }
}