// JUnit test file for the Queue class
package bible.util;

import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;

public class QueueTest {
    private static Queue queue;

    @BeforeClass
    public static void setup() {
        queue = new Queue();
    }

    @AfterClass
    public static void teardown() {
        queue = null;
    }

    @Before
    public void beforeEachTest() {
        queue = new Queue();
    }

    @After
    public void afterEachTest() {
        queue = null;
    }

    // Test for isEmpty() method
    @Test
    public void testIsEmpty() {
        assertTrue(queue.isEmpty());

        queue.enqueue("Apple");
        assertFalse(queue.isEmpty());

        queue = new Queue();
        assertTrue(queue.isEmpty());
    }

    // Test for enqueue() method
    @Test
    public void testEnqueue() {
        Queue queue = new Queue();
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Cherry");

        assertEquals(3, queue.getNumberItems());

        queue.enqueue(null); // null object test

        try {
            queue.enqueue("Durian"); // max capacity test
            fail("Expected IllegalStateException: Max capacity exceeded!");
        } catch (IllegalStateException e) {
            // expected exception
        }

        queue = new Queue(2); // test with max capacity
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Cherry");
        queue.enqueue("Durian"); // should throw an exception
    }

    // Test for dequeue() method
    @Test
    public void testDequeue() {
        Queue queue = new Queue();
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Cherry");

        assertEquals("Apple", queue.dequeue());
        assertEquals("Banana", queue.dequeue());
        assertEquals("Cherry", queue.dequeue());
        assertTrue(queue.isEmpty());

        queue = new Queue();
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.dequeue();
        assertEquals("Banana", queue.dequeue());

        queue = new Queue();
        queue.enqueue("Apple");
        queue.enqueue(null); // null object test
        queue.dequeue();
        assertTrue(queue.isEmpty());

        queue = new Queue();
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Cherry");
        queue.dequeue();
        queue.dequeue();
        queue.dequeue();
        queue.dequeue(); // empty queue test
    }

    // Test for remove() method
    @Test
    public void testRemove() {
        Queue queue = new Queue();
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Cherry");

        assertEquals(3, queue.getNumberItems());

        queue.remove("Apple");
        assertEquals(2, queue.getNumberItems());

        queue.remove("Banana");
        assertEquals(1, queue.getNumberItems());

        queue.remove("Cherry");
        assertEquals(0, queue.getNumberItems());

        queue.remove("Orange"); // non-existent object test
        assertEquals(0, queue.getNumberItems());
    }

    // Test for refreshElement() method
    @Test
    public void testRefreshElement() {
        Queue queue = new Queue();
        queue.enqueue("Apple");
        queue.refreshElement("Apple");
        assertEquals("Apple", queue.getObjects().get(0));

        queue.enqueue("Banana");
        queue.refreshElement("Banana");
        assertEquals("Banana", queue.getObjects().get(1));

        queue.enqueue("Cherry");
        queue.refreshElement("Cherry");
        assertEquals("Cherry", queue.getObjects().get(2));
    }

    // Test for getNumberItems() method
    @Test
    public void testGetNumberItems() {
        Queue queue = new Queue();

        assertEquals(0, queue.getNumberItems());

        queue.enqueue("Apple");
        assertEquals(1, queue.getNumberItems());

        queue.enqueue("Banana");
        assertEquals(2, queue.getNumberItems());

        queue.enqueue("Cherry");
        assertEquals(3, queue.getNumberItems());
    }

    // Test for getPeakNumberItems() method
    @Test
    public void testGetPeakNumberItems() {
        Queue queue = new Queue();

        assertEquals(-1, queue.getPeakNumberItems());

        queue.enqueue("Apple");
        assertEquals(1, queue.getPeakNumberItems());

        queue.enqueue("Banana");
        assertEquals(2, queue.getPeakNumberItems());

        queue.enqueue("Cherry");
        assertEquals(3, queue.getPeakNumberItems());
    }

    // Test for toString() method
    @Test
    public void testToString() {
        Queue queue = new Queue();
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Cherry");

        String expected = "Queue:[numItems=3, maxNumItems=3, maxCapacity=-1, getObjects()=[Apple, Banana, Cherry]\r\n]";
        assertEquals(expected, queue.toString());
    }

    // Test for maxCapacityExceeded() method
    @Test
    public void testMaxCapacityExceeded() {
        Queue queue = new Queue();
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Cherry");
        queue.enqueue("Durian"); // max capacity test
        assertTrue(queue.maxCapacityExceeded());

        queue = new Queue(5); // test with max capacity
        queue.enqueue("Apple");
        queue.enqueue("Banana");
        queue.enqueue("Cherry");
        queue.enqueue("Durian");
        queue.enqueue("Orange"); // should not throw an exception
        assertFalse(queue.maxCapacityExceeded());
    }
}

//