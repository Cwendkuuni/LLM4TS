import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterConstructorStringBoolean() {
        String fileName = "test.dtrace";
        boolean simple = false;
        DaikonFormatter formatter = new DaikonFormatter(fileName, simple);
        assertNotNull(formatter);
    }
}