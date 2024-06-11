import static org.junit.Assert.assertEquals;
import bible.util.Queue;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertEquals(true, queue.isEmpty());
    }

    // Add test cases for other methods here
}