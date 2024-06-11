import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testConstructor() {
        Queue queue = new Queue();
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);
        Assert.assertEquals(0, queue.numItems);
        Assert.assertEquals(-1, queue.maxNumItems);
        Assert.assertEquals(0, queue.maxCapacity);
    }

    @Test
    public void testConstructWithCapacity() {
        Queue queue = new Queue(5);
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);
        Assert.assertEquals(0, queue.numItems);
        Assert.assertEquals(0, queue.maxNumItems);
        Assert.assertEquals(5, queue.maxCapacity);
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
        queue.enqueue(new Object());
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue(new Object());
        Queue queueWithMultipleItems = new Queue();
        queueWithMultipleItems.enqueue(new Object());
        queueWithMultipleItems.enqueue(new Object());

        Assert.assertNotNull(queue.first);
        Assert.assertNotNull(queue.last);
        Assert.assertEquals(1, queue.numItems);
        Assert.assertEquals(1, queue.maxNumItems);
        Assert.assertEquals(1, queue.maxCapacity);

        Assert.assertEquals(queue.first.value, queue.dequeue());
        Assert.assertEquals(0, queue.numItems);
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);

        Node firstNode = queueWithMultipleItems.first;
        Node lastNode = queueWithMultipleItems.last;
        Assert.assertEquals(queueWithMultipleItems.getNumberItems(), 2);
        Assert.assertEquals(queueWithMultipleItems.getPeakNumberItems(), 2);
        Assert.assertNotNull(firstNode);
        Assert.assertNotNull(lastNode);
        Assert.assertEquals(firstNode.value, queueWithMultipleItems.dequeue());
        Assert.assertEquals(queueWithMultipleItems.getNumberItems(), 1);
        Assert.assertEquals(queueWithMultipleItems.getPeakNumberItems(), 1);
    }

    @Test(expected = NoSuchElementException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        Object objectToRemove = new Object();
        queue.enqueue(objectToRemove);
        queue.enqueue(new Object());

        Assert.assertEquals(queue.remove(objectToRemove).intValue(), 1);
        Assert.assertEquals(queue.getNumberItems(), 1);

        Assert.assertEquals(queue.remove(new Object()).intValue(), -1);
        Assert.assertEquals(queue.getNumberItems(), 1);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        Object objectToRefresh = new Object();
        queue.enqueue(objectToRefresh);
        queue.refreshElement(objectToRefresh);
        Assert.assertEquals(queue.getNumberItems(), 1);
        Assert.assertEquals(queue.getPeakNumberItems(), 1);
    }

    @Test
    public void testGetNumberItems() {
        Queue queueWithSingleItem = new Queue();
        queueWithSingleItem.enqueue(new Object());
        Queue queueWithMultipleItems = new Queue();
        queueWithMultipleItems.enqueue(new Object());
        queueWithMultipleItems.enqueue(new Object());

        Assert.assertEquals(queueWithSingleItem.getNumberItems(), 1);
        Assert.assertEquals(queueWithMultipleItems.getNumberItems(), 3);
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queueWithSingleItem = new Queue();
        queueWithSingleItem.enqueue(new Object());
        Queue queueWithMultipleItems = new Queue();
        queueWithMultipleItems.enqueue(new Object());
        queueWithMultipleItems.enqueue(new Object());

        Assert.assertEquals(queueWithSingleItem.getPeakNumberItems(), 1);
        Assert.assertEquals(queueWithMultipleItems.getPeakNumberItems(), 3);
    }

    @Test
    public void testGetObjects() {
        Queue queueWithObjects = new Queue();
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = new Object();

        queueWithObjects.enqueue(object1);
        queueWithObjects.enqueue(object2);
        queueWithObjects.enqueue(object3);

        Vector objects = queueWithObjects.getObjects();
        Assert.assertEquals(objects.size(), 3);
        Assert.assertEquals(objects.get(0), object1);
        Assert.assertEquals(objects.get(1), object2);
        Assert.assertEquals(objects.get(2), object3);
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        Object object1 = new Object();
        Object object2 = new Object();
        Object object3 = new Object();

        queue.enqueue(object1);
        queue.enqueue(object2);
        queue.enqueue(object3);

        Assert.assertFalse(queue.maxCapacityExceeded());

        queue.enqueue(new Object());

        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNegativeCapacity() {
        new Queue(-1);
    }
}