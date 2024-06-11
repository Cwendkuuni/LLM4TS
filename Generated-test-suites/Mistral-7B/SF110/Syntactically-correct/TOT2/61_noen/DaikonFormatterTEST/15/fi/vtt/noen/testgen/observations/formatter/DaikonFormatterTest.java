import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterWithFileNameAndInputs() {
        DaikonFormatter formatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
        assertEquals("test.txt", formatter.getFileName());
        assertEquals(Arrays.asList("method1", "method2"), formatter.getInputs());
    }
}