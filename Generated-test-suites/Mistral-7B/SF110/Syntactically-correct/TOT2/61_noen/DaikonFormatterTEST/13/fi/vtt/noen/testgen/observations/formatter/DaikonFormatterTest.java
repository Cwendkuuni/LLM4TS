import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatter() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("test.txt", false);
            assertTrue(formatter != null);
        } catch (IOException e) {
            fail("Expected no IOException");
        }
    }
}