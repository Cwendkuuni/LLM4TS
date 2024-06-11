import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterConstructor() {
        String validFileName = "test.dtrace";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(validFileName, simple);
        assertTrue(formatter != null);

        String invalidFileName = null;
        try {
            new DaikonFormatter(invalidFileName, simple);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        String emptyFileName = "";
        try {
            new DaikonFormatter(emptyFileName, simple);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        simple = true;
        DaikonFormatter simpleFormatter = new DaikonFormatter(validFileName, simple);
        assertTrue(simpleFormatter != null);
    }
}