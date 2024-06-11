import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {
    @Test
    public void testStripLeadingHyphens() {
        String input = "--test";
        String expectedOutput = "test";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }
}