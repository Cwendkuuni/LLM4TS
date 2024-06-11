package bible.util;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Vector;

public class QueueTest {

    private Queue queue;

    @Before
    public void setup() {
        queue = new Queue();
    }

    @Test
    public void testQueueEmpty() {
        Assert.assertTrue(queue.isEmpty());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getPeakNumberItems());
    }

    @Test
    public void testQueueCreation() {
        queue = new Queue(5);
        Assert.assertFalse(queue.isEmpty());
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getPeakNumberItems());
        Assert.assertEquals(5, queue.getMaxCapacity());
    }

    @Test
    public void testEnqueue() {
        queue = new Queue();
        queue.enqueue("apple");
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("apple", queue.getObjects().get(0));
        Assert.assertEquals("apple", queue.toString().split(":")[7].split("=")[1].split(",")[0]);
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("banana");
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("banana", queue.getObjects().get(1));
        Assert.assertEquals("banana", queue.toString().split(":")[7].split("=")[1].split(",")[1]);

        queue.enqueue("cherry");
        Assert.assertEquals(3, queue.getNumberItems());
        Assert.assertEquals("cherry", queue.getObjects().get(2));
        Assert.assertEquals("cherry", queue.toString().split(":")[7].split("=")[1].split(",")[2]);
    }

    @Test
    public void testDequeue() {
        queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");
        queue.enqueue("cherry");

        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("apple", dequeuedObject);
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals("banana", queue.getObjects().get(0));
        Assert.assertEquals("banana", queue.toString().split(":")[7].split("=")[1].split(",")[0]);

        dequeuedObject = queue.dequeue();
        Assert.assertEquals("banana", dequeuedObject);
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals("cherry", queue.getObjects().get(0));
        Assert.assertEquals("cherry", queue.toString().split(":")[7].split("=")[1].split(",")[0]);

        dequeuedObject = queue.dequeue();
        Assert.assertNull(dequeuedObject);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertTrue(queue.isEmpty());
    }

    @Test
    public void testMaxCapacityExceeded() {
        queue = new Queue(1);
        for (int i = 0; i < 2; i++) {
            queue.enqueue("test");
        }
        Assert.assertTrue(queue.maxCapacityExceeded());
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        queue = new Queue();
        queue.enqueue(null);
    }

    @Test(expected = NullPointerException.class)
    public void testDequeueNullObject() {
        queue = new Queue();
        queue.enqueue("test");
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue(null);
    }
}