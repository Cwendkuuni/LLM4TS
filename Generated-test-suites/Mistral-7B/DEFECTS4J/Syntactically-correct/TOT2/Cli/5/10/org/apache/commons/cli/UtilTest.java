import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String input = "--test-string";
        String expectedOutput = "test-string";
        String output = Util.stripLeadingHyphens(input);
        assertEquals(expectedOutput, output);
    }

    // Test case for empty string and null input
}