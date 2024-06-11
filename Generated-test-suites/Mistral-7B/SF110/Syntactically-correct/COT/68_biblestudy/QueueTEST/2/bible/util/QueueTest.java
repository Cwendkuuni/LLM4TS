import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(-1, queue.getPeakNumberItems());
        Assert.assertEquals(0, queue.getNumberItems() - queue.getPeakNumberItems());
    }

    @Test
    public void testQueueConstructorWithCapacity() {
        Queue queue = new Queue(5);
        Assert.assertEquals(0, queue.getNumberItems());
        Assert.assertEquals(0, queue.getPeakNumberItems());
        Assert.assertEquals(5, queue.getMaxCapacity());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        Assert.assertTrue(queue.isEmpty());
        queue.enqueue(new Integer(1));
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
        queue.enqueue(new Integer(1));
        queue.enqueue(new Integer(2));
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
        Assert.assertEquals(true, queue.maxCapacityExceeded());
        Assert.assertEquals(true, queue.maxCapacityExceeded());
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue(new Integer(1));
        queue.enqueue(new Integer(2));
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals(new Integer(1), dequeuedObject);
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue(new Integer(1));
        queue.enqueue(new Integer(2));
        queue.enqueue(new Integer(3));
        int removedObjects = queue.remove(new Integer(2));
        Assert.assertEquals(1, removedObjects);
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(3, queue.getPeakNumberItems());
        removedObjects = queue.remove(new Integer(3));
        Assert.assertEquals(1, removedObjects);
        Assert.assertEquals(1, queue.getNumberItems());
        Assert.assertEquals(1, queue.getPeakNumberItems());
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue(new Integer(1));
        queue.enqueue(new Integer(2));
        queue.refreshElement(new Integer(1));
        Assert.assertEquals(2, queue.getNumberItems());
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());
        queue.enqueue(new Integer(1));
        Assert.assertEquals(1, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue(new Integer(1));
        queue.enqueue(new Integer(2));
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue(new Integer(1));
        queue.enqueue(new Integer(2));
        Vector objects = queue.getObjects();
        Assert.assertEquals(2, objects.size());
        Assert.assertEquals(new Integer(1), objects.get(0));
        Assert.assertEquals(new Integer(2), objects.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(5);
        queue.enqueue(new Integer(1));
        queue.enqueue(new Integer(2));
        queue.enqueue(new Integer(3));
        queue.enqueue(new Integer(4));
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue(new Integer(1));
        queue.enqueue(new Integer(2));
        String toString = queue.toString();
        Assert.assertTrue(toString.contains("bible.util.Queue:"));
        Assert.assertTrue(toString.contains("[numItems=" + 2));
        Assert.assertTrue(toString.contains("[maxNumItems="));
        Assert.assertTrue(toString.contains("[maxCapacity="));
        Assert.assertTrue(toString.contains("getObjects()="));
    }
}