import org.junit.Assert;
import org.junit.Test;
import bible.util.Queue;
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
        expectedFirst.next = expectedLast;
        Assert.assertEquals(expectedFirst, queue.first);
        Assert.assertEquals(expectedLast, queue.last);
        Assert.assertEquals(1, queue.numItems);
        Assert.assertEquals(1, queue.maxNumItems);

        queue.enqueue("test2");
        Node expectedFirst2 = new Node("test1");
        Node expectedLast2 = new Node("test2");
        expectedFirst2.next = expectedLast2;
        Assert.assertEquals(expectedFirst2, queue.first);
        Assert.assertEquals(expectedLast2, queue.last);
        Assert.assertEquals(2, queue.numItems);
        Assert.assertEquals(2, queue.maxNumItems);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("test1", dequeuedObject);
        Assert.assertEquals(0, queue.numItems);
        Assert.assertEquals(null, queue.first);
        Assert.assertEquals(null, queue.last);

        queue.enqueue("test1");
        queue.enqueue("test2");
        dequeuedObject = queue.dequeue();
        Assert.assertEquals("test1", dequeuedObject);
        Assert.assertEquals(1, queue.numItems);
        Assert.assertEquals("test2", queue.first.value);
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
        queue.remove("test1");
        Assert.assertEquals(1, queue.numItems);
        Assert.assertEquals("test2", queue.first.value);

        queue.remove("test2");
        Assert.assertEquals(0, queue.numItems);
        Assert.assertEquals(null, queue.first);
        Assert.assertEquals(null, queue.last);
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
        queue.refreshElement("test1");
        Assert.assertEquals(1, queue.numItems);
        Assert.assertEquals("test1", queue.first.value);

        queue.refreshElement("test2");
        Assert.assertEquals(1, queue.numItems);
        Assert.assertEquals("test2", queue.first.value);
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        Assert.assertEquals(1, queue.getNumberItems());

        queue.enqueue("test2");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("test2");
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        Vector expectedVector = new Vector();
        expectedVector.add("test1");
        expectedVector.add("test2");

        Assert.assertEquals(expectedVector, queue.getObjects());
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("test1");
        Assert.assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("test2");
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");

        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=0, getObjects()=[test1, test2]\r\n]";
        Assert.assertEquals(expectedString, queue.toString());
    }

}