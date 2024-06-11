import static org.junit.jupiter.api.Assertions.*;
import bible.util.Queue;

public class QueueTest {
    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
}