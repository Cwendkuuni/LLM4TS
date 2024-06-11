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
    public void testQueueConstructWithCapacity() {
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

        queue.enqueue("apple");
        Assert.assertFalse(queue.isEmpty());

        queue.dequeue();
        Assert.assertTrue(queue.isEmpty());
    }

    @Test(expected = NullPointerException.class)
    public void testEnqueueWithNullObject() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }

    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        Node expectedLastNode = new Node("apple");
        Assert.assertEquals(expectedLastNode, queue.last);

        queue.enqueue("banana");
        Node expectedSecondLastNode = new Node("apple");
        Node expectedLastNode2 = new Node("banana");
        Assert.assertEquals(expectedSecondLastNode.next, expectedLastNode2);
        Assert.assertEquals(expectedLastNode2, queue.last);

        queue.enqueue("cherry");
        Node expectedThirdLastNode = new Node("apple");
        Node expectedSecondLastNode2 = new Node("banana");
        Node expectedLastNode3 = new Node("cherry");
        Assert.assertEquals(expectedThirdLastNode.next, expectedSecondLastNode2);
        Assert.assertEquals(expectedSecondLastNode2.next, expectedLastNode3);
        Assert.assertEquals(expectedLastNode3, queue.last);

        Assert.assertEquals(3, queue.numItems);
        Assert.assertEquals(3, queue.maxNumItems);
    }

    @Test(expected = RuntimeException.class)
    public void testDequeueWhenEmpty() {
        Queue queue = new Queue();
        queue.dequeue();
    }

    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");

        Object dequeuedObject = queue.dequeue();
        Assert.assertEquals("apple", dequeuedObject);

        Assert.assertEquals(1, queue.numItems);

        dequeuedObject = queue.dequeue();
        Assert.assertEquals("banana", dequeuedObject);

        Assert.assertEquals(0, queue.numItems);
    }

    @Test(expected = RuntimeException.class)
    public void testRemoveWhenEmpty() {
        Queue queue = new Queue();
        queue.remove("apple");
    }

    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");

        int numObjectsRemoved = queue.remove("apple");
        Assert.assertEquals(1, numObjectsRemoved);
        Assert.assertEquals(1, queue.numItems);

        numObjectsRemoved = queue.remove("banana");
        Assert.assertEquals(-1, numObjectsRemoved);
        Assert.assertEquals(1, queue.numItems);

        queue.enqueue("cherry");
        numObjectsRemoved = queue.remove("apple");
        Assert.assertEquals(-1, numObjectsRemoved);
        Assert.assertEquals(2, queue.numItems);
    }

    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");

        queue.refreshElement("apple");
        Assert.assertEquals(2, queue.numItems);
        Assert.assertEquals("apple", queue.getObjects().get(0));
        Assert.assertEquals("banana", queue.getObjects().get(1));
    }

    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(0, queue.getNumberItems());

        queue.enqueue("apple");
        Assert.assertEquals(1, queue.getNumberItems());

        queue.enqueue("banana");
        Assert.assertEquals(2, queue.getNumberItems());
    }

    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();
        Assert.assertEquals(-1, queue.getPeakNumberItems());

        queue.enqueue("apple");
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("banana");
        Assert.assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("cherry");
        Assert.assertEquals(2, queue.getPeakNumberItems());
    }

    @Test
    public void testGetObjects() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");

        Vector returnedObjects = queue.getObjects();
        Assert.assertEquals(2, returnedObjects.size());
        Assert.assertEquals("apple", returnedObjects.get(0));
        Assert.assertEquals("banana", returnedObjects.get(1));
    }

    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue(1);
        queue.enqueue("apple");
        queue.enqueue("banana");

        Assert.assertFalse(queue.maxCapacityExceeded());

        queue.enqueue("cherry");
        Assert.assertTrue(queue.maxCapacityExceeded());
    }

    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("apple");
        queue.enqueue("banana");

        String expectedString = "Queue:[numItems=2, maxNumItems=2, maxCapacity=0, getObjects()=[apple, banana]\r\n]";
        Assert.assertEquals(expectedString, queue.toString());
    }
}