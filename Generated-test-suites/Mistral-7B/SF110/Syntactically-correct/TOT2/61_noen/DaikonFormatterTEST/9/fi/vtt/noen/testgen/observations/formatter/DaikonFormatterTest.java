import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    @Test
    public void testDaikonFormatterConstructorWithFileNameAndInputs() throws Exception {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", Arrays.asList("method1", "method2"));
        assertNotNull(daikonFormatter);
        assertEquals(Arrays.asList("method1", "method2"), daikonFormatter.getInputs());
    }
}