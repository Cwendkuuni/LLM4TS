import org.junit.Test;
import static org.junit.Assert.*; // Import Assert class for assertions
import bible.util.Queue;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue(); // Empty queue
        assertTrue(queue.isEmpty());

        queue.enqueue(new Object()); // Add an element
        assertFalse(queue.isEmpty());
    }
}