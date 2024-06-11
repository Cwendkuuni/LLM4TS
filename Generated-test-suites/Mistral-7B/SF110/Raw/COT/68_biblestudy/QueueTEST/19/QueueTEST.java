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
    public void testConstructorWithCapacity() {
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
    public void testEnqueueNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueueObject() {
        Queue queue = new Queue();
        queue.enqueue("test");
        Node node = queue.first;
        Assert.assertEquals("test", node.value);

        queue.enqueue(new Integer(1));
        node = queue.first;
        Assert.assertEquals("test", node.next.value);
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("test");
        Object obj = queue.dequeue();
        Assert.assertEquals("test", obj);
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);
        Assert.assertEquals(0, queue.numItems);

        queue.enqueue("test1");
        queue.enqueue("test2");
        obj = queue.dequeue();
        Assert.assertEquals("test1", obj);
        Node node = queue.first;
        Assert.assertEquals("test2", node.value);
    }

    @Test(expected = IllegalStateException.class)
    public void testDequeueEmptyQueue() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testRemoveObject() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        int removedCount = queue.remove("test2");
        Assert.assertEquals(1, removedCount);
        Assert.assertEquals(2, queue.numItems);
        Node node = queue.first;
        Assert.assertEquals("test1", node.value);

        removedCount = queue.remove("test3");
        Assert.assertEquals(1, removedCount);
        Assert.assertEquals(1, queue.numItems);
        Assert.assertNull(queue.last);

        removedCount = queue.remove("test1");
        Assert.assertEquals(1, removedCount);
        Assert.assertEquals(0, queue.numItems);
        Assert.assertNull(queue.first);
        Assert.assertNull(queue.last);
    }

    @Test(expected = IllegalStateException.class)
    public void testRemoveNonExistingObject() {
        Queue queue = new Queue();
        queue.enqueue("test");
        queue.remove("test2");
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");

        queue.refreshElement("test2");
        Assert.assertEquals(3, queue.numItems);
        Node node = queue.first;
        Assert.assertEquals("test1", node.value);
        Assert.assertEquals("test2", node.next.value);
        Assert.assertEquals("test2", node.next.next.value);
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());

        queue.enqueue("test1");
        Assert.assertEquals(1, queue.getNumberItems());

        queue.enqueue("test2");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(-1, queue.getPeakNumberItems());

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
        queue.enqueue("test3");

        Vector vector = queue.getObjects();
        Assert.assertEquals(3, vector.size());
        Assert.assertEquals("test1", vector.get(0));
        Assert.assertEquals("test2", vector.get(1));
        Assert.assertEquals("test3", vector.get(2));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(2);
        queue.enqueue("test1");
        queue.enqueue("test2");
        Assert.assertTrue(queue.maxCapacityExceeded());

        queue = new Queue(3);
        queue.enqueue("test1");
        queue.enqueue("test2");
        queue.enqueue("test3");
        Assert.assertFalse(queue.maxCapacityExceeded());
    }

    @Test(expected = IllegalStateException.class)
    public void testMaxCapacityExceededWithNegativeMaxCapacity() {
        Queue queue = new Queue(-1);
        queue.enqueue("test");
    }
}