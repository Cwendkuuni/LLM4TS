package bible.util;
import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstruction() {
        Queue queue = new Queue();
        Queue queueWithCapacity = new Queue(5);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queueWithCapacity.getNumberItems());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
        queue.enqueue(1);
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueObject() {
        Queue queue = new Queue();
        queue.enqueue(1);
        Queue queueWithCapacity = new Queue(3);
        queueWithCapacity.enqueue(1);
        queueWithCapacity.enqueue(2);
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(2, queueWithCapacity.getNumberItems());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals(1, dequeuedObject);
        Assert.assertEquals(1, queue.getNumberItems());
        Object dequeuedObjectFromEmptyQueue = queue.dequeue();
        Assert.assertNull(dequeuedObjectFromEmptyQueue);
        Assert.assertEquals(0, queue.getNumberItems());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveNonExistentObject() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.remove(2);
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        int numberOfRemovedObjects = queue.remove(1);
        Assert.assertEquals(1, numberOfRemovedObjects);
        Assert.assertEquals(1, queue.getNumberItems());
        int numberOfRemovedObjectsAfterEmptyQueue = queue.remove(1);
        Assert.assertEquals(0, numberOfRemovedObjectsAfterEmptyQueue);
        Assert.assertEquals(0, queue.getNumberItems());

        Queue queueWithMultipleObjects = new Queue();
        queueWithMultipleObjects.enqueue(1);
        queueWithMultipleObjects.enqueue(2);
        queueWithMultipleObjects.enqueue(3);
        int numberOfRemovedObjectsFromMiddle = queueWithMultipleObjects.remove(2);
        Assert.assertEquals(1, numberOfRemovedObjectsFromMiddle);
        Assert.assertEquals(2, queueWithMultipleObjects.getNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.refreshElement(1);
        Assert.assertEquals(1, queue.getNumberItems());
        Queue queueWithMultipleObjects = new Queue();
        queueWithMultipleObjects.enqueue(1);
        queueWithMultipleObjects.enqueue(2);
        queueWithMultipleObjects.refreshElement(2);
        Assert.assertEquals(2, queueWithMultipleObjects.getNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());
        queue.enqueue(1);
        Assert.assertEquals(1, queue.getNumberItems());
        Queue queueWithMultipleObjects = new Queue();
        queueWithMultipleObjects.enqueue(1);
        queueWithMultipleObjects.enqueue(2);
        queueWithMultipleObjects.enqueue(3);
        Assert.assertEquals(3, queueWithMultipleObjects.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        queue.enqueue(1);
        Assert.assertEquals(1, queue.getPeakNumberItems());
        Queue queueWithMultipleObjects = new Queue();
        queueWithMultipleObjects.enqueue(1);
        queueWithMultipleObjects.enqueue(2);
        queueWithMultipleObjects.enqueue(3);
        Assert.assertEquals(3, queueWithMultipleObjects.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(1);
        queue.enqueue(2);
        Vector objects = queue.getObjects();
        Assert.assertEquals(2, objects.size());
        Assert.assertEquals(1, objects.get(0));
        Assert.assertEquals(2, objects.get(1));

        Queue queueWithMultipleObjects = new Queue();
        queueWithMultipleObjects.enqueue(1);
        queueWithMultipleObjects.enqueue(2);
        queueWithMultipleObjects.enqueue(3);
        Vector objectsFromQueueWithMultipleObjects = queueWithMultipleObjects.getObjects();
        Assert.assertEquals(3, objectsFromQueueWithMultipleObjects.size());
        Assert.assertEquals(1, objectsFromQueueWithMultipleObjects.get(0));
        Assert.assertEquals(2, objectsFromQueueWithMultipleObjects.get(1));
        Assert.assertEquals(3, objectsFromQueueWithMultipleObjects.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue();
        Assert.assertFalse(queue.maxCapacityExceeded());

        Queue queueWithMaxCapacity = new Queue(1);
        Assert.assertFalse(queueWithMaxCapacity.maxCapacityExceeded());
        queueWithMaxCapacity.enqueue(1);
        Assert.assertTrue(queueWithMaxCapacity.maxCapacityExceeded());

        Queue queueWithMaxCapacityAndMultipleObjects = new Queue(1);
        queueWithMaxCapacityAndMultipleObjects.enqueue(1);
        queueWithMaxCapacityAndMultipleObjects.enqueue(2);
        Assert.assertTrue(queueWithMaxCapacityAndMultipleObjects.maxCapacityExceeded());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testToStringWithNullQueue() {
        Queue queue = null;
        queue.toString();
    }
}