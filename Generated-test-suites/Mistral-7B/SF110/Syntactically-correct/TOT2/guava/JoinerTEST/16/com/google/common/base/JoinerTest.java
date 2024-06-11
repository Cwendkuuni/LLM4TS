import org.junit.Test;
import static org.junit.Assert.*;

public class JoinerTest {
    @Test
    public void testJoinerConstructor() {
        // Test with String separator
        Joiner joinerString = Joiner.on("|");
        assertEquals("|", joinerString.separator);

        // Test with char separator
        Joiner joinerChar = Joiner.on('|');
        assertEquals("|", joinerChar.separator);
    }
}