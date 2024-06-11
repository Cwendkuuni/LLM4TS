import static org.junit.Assert.*;
import com.google.javascript.jscomp.CodeConsumer;

public class CodeConsumerTest {
    CodeConsumer consumer = new CodeConsumer() {
        @Override
        char getLastChar() {
            return 'a';
        }
    };

    @Test
    public void testGetLastChar() {
        assertEquals('a', consumer.getLastChar());
    }
}