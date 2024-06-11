import org.junit.Test;
import static org.junit.Assert.*;
import fi.vtt.noen.testgen.observations.formatter.*;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterWithSimpleParameter() {
        String fileName = "test.dtrace";
        boolean simple = true;

        try {
            DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
            assertTrue(formatter instanceof DaikonFormatter);
        } catch (IOException e) {
            fail("IOException should not be thrown in this test case.");
        }
    }
}