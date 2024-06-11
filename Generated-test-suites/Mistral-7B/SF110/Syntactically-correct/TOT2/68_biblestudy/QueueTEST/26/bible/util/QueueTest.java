import static org.junit.Assert.*;
import java.util.Vector;
import bible.util.Queue;
import java.util.concurrent.locks.ReentrantLock;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("testObject");
        assertFalse(queue.isEmpty());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testQueueNegativeCapacity() {
        new Queue(-1);
    }
    
    @Test
    public void testQueueZeroCapacity() {
        Queue queue = new Queue(0);
        assertTrue(queue.isEmpty());
        queue.enqueue("testObject");
        assertFalse(queue.isEmpty());
    }
    
    @Test
    public void testQueuePositiveCapacity() {
        Queue queue = new Queue(5);
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        queue.enqueue("testObject3");
        assertFalse(queue.isEmpty());
        assertEquals(3, queue.getNumberItems());
    }
    
    @Test(expected = NullPointerException.class)
    public void testQueueNullElement() {
        Queue queue = new Queue();
        queue.enqueue(null);
    }
    
    @Test
    public void testQueueMultipleElements() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        queue.enqueue("testObject3");
        assertFalse(queue.isEmpty());
        assertEquals("testObject1", queue.dequeue().toString());
        assertEquals("testObject2", queue.dequeue().toString());
        assertEquals("testObject3", queue.dequeue().toString());
        assertTrue(queue.isEmpty());
    }
    
    @Test
    public void testQueueRemoveNonExistentElement() {
        Queue queue = new Queue();
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        Object removed = queue.remove("testObject3");
        assertNull(removed);
    }
    
    @Test(expected = IllegalStateException.class)
    public void testQueueFull() {
        Queue queue = new Queue(1);
        queue.enqueue("testObject1");
        queue.enqueue("testObject2");
        queue.enqueue("testObject3");
    }
    
    @Test
    public void testQueueSynchronization() {
        Queue queue = new Queue();
        ReentrantLock lock = new ReentrantLock();
        Thread thread1 = new Thread(() -> {
            for (int i = 0; i < 1001; ++i) {
                lock.lock();
                queue.enqueue("testObject");
                lock.unlock();
            }
        });
        Thread thread2 = new Thread(() -> {
            for (int i = 0; i < 1001; ++i) {
                lock.lock();
                queue.dequeue();
                lock.unlock();
            }
        });
        thread1.start();
        thread2.start();
        try {
            thread1.join();
            thread2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        assertTrue(queue.isEmpty());
    }
}