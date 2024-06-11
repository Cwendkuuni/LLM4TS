import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterConstructorWithStringArg() {
        String fileName = "test.dtrace";
        DaikonFormatter formatter = new DaikonFormatter(fileName, "method1");
        Set<String> expectedDeclarations = new HashSet<String>() {{
            add("method1");
        }};

        assertEquals("inputs:[method1]", System.out.getProperty("console")); // Verify the console output
        assertNotNull(formatter.declarations);
        assertEquals(expectedDeclarations, formatter.declarations);
    }
}