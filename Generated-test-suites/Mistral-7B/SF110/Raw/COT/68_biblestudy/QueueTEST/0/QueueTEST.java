import org.junit.Assert;
import org.junit.Test;
import java.util.Vector;

public class QueueTest {

    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);
        Assert.assertEquals(0, queue.numItems);
        Assert.assertEquals(-1, queue.maxNumItems);
        Assert.assertEquals(0, queue.maxCapacity);
    }

    @Test
    public void testQueueConstructorWithCapacity() {
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
        queue.enqueue("test");
        Assert.assertFalse(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        Node expectedFirst = new Node("test1");
        Node expectedLast = new Node("test1");
        expectedLast.next = null;
        queue.numItems = 1;
        queue.maxNumItems = 1;
        synchronized (queue) {
            Assert.assertEquals(expectedFirst, queue.first);
            Assert.assertEquals(expectedLast, queue.last);
        }
        queue.enqueue("test2");
        Node expectedFirst2 = new Node("test1");
        Node expectedLast2 = new Node("test2");
        expectedLast2.next = null;
        queue.numItems = 2;
        queue.maxNumItems = 2;
        synchronized (queue) {
            Assert.assertEquals(expectedFirst2, queue.first);
            Assert.assertEquals(expectedLast2, queue.last);
        }
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("test1", dequeuedObject);
        Assert.assertEquals(1, queue.numItems);
        Assert.assertNull(queue.first);
        Assert.assertEquals("test2", queue.getObjects().get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        queue.remove("test2");
        Assert.assertEquals(2, queue.numItems);
        Assert.assertEquals("test1", queue.getObjects().get(0));
        Assert.assertEquals("test3", queue.getObjects().get(1));
        queue.remove("test1");
        Assert.assertEquals(1, queue.numItems);
        Assert.assertEquals("test3", queue.getObjects().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testRemoveWithNullObject() {
        Queue queue = new Queue();
        queue.remove(null);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.refreshElement("test1");
        Assert.assertEquals(2, queue.numItems);
        Assert.assertEquals("test1", queue.getObjects().get(0));
        Assert.assertEquals("test2", queue.getObjects().get(1));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertEquals(2, queue.getObjects().size());
        Assert.assertEquals("test1", queue.getObjects().get(0));
        Assert.assertEquals("test2", queue.getObjects().get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testMaxCapacityNotExceeded() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=0, getObjects()=[test1, test2]]";
        Assert.assertEquals(expectedString, queue.toString());
    }
}