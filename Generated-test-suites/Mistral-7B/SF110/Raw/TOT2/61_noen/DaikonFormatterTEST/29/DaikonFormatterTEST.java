import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterWithFileNameAndInputs() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
        assertNotNull(daikonFormatter);
    }
}