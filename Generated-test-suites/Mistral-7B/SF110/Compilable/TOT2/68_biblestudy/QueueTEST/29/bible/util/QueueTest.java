package bible.util;
import org.junit.Test;
import static org.junit.Assert.*;
import bible.util.Queue;

public class QueueTest {

    @Test
    public void testIsEmpty() {
        Queue queue = new Queue();
        assertTrue(queue.isEmpty());
    }
}