package bible.util;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import bible.util.Queue;

public class QueueTest {
    @Test
    public void testQueueConstructor() {
        Queue queue = new Queue();
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=-1, getObjects()=[]]", queue.toString());

        Queue queue2 = new Queue(5);
        assertEquals("Queue:[numItems=0, maxNumItems=0, maxCapacity=5, getObjects()=[]]", queue2.toString());
    }

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());

        Queue queue2 = new Queue();
        queue2.enqueue("test");
        assertFalse(queue2.isEmpty());
    }

    // Test cases for enqueue, dequeue, remove, and refreshElement methods follow...
}