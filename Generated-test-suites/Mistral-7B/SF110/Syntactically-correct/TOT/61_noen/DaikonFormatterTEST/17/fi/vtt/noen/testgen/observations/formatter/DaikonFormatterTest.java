import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterConstructorStringBoolean() {
        DaikonFormatter formatter = new DaikonFormatter("test.dtrace", true);
        assertNotNull(formatter);
    }
}