package bible.util;
import org.junit.Assert;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstruction() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);

        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertEquals(5, queueWithCapacity.getPeakNumberItems());
    }

    @Test
    public void testQueueIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testQueueIsNotEmpty() {
        Queue queue = new Queue();
        queue.enqueue("Test");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testNullEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        Vector expectedVector = new Vector(2, 10);
        expectedVector.add("Test1");
        expectedVector.add("Test2");

        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals(expectedVector, queue.getObjects());
    }

    @Test(expected = RuntimeException.class)
    public void testMaxCapacityExceededEnqueue() {
        Queue queue = new Queue(2);
        for (int i = 0; i < 3; i++) {
            queue.enqueue("Test" + i);
        }
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        Assert.assertEquals("Test1", queue.dequeue());

        Vector expectedVector = new Vector(1, 10);
        expectedVector.add("Test2");

        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals(expectedVector, queue.getObjects());
    }

    @Test(expected = RuntimeException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveWhenNull() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.remove(null);
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        Assert.assertEquals(2, queue.getNumberItems());
        queue.remove("Test1");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("Test2", queue.dequeue().toString());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        Assert.assertEquals(2, queue.getNumberItems());
        queue.refreshElement("Test1");
        Assert.assertEquals(2, queue.getNumberItems());

        Vector expectedVector = new Vector(1, 10);
        expectedVector.add("Test2");

        Assert.assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Test1");
        queue.enqueue("Test2");

        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=-1, getObjects()=[Test2],]\r\n";
        Assert.assertEquals(expectedString, queue.toString());
    }
}