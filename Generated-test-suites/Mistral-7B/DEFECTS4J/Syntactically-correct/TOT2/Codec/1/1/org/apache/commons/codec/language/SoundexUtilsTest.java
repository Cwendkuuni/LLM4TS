import org.apache.commons.codec.DecoderException;
import org.junit.Test;
import static org.junit.Assert.*;

public class SoundexUtilsTest {

    @Test
    public void testCleanMethod() {
        String input = "helloWorld";
        String expected = "HELLO";
        String result = SoundexUtils.clean(input);
        assertEquals(expected, result);
    }

    // Add test cases for edge cases and exceptions, e.g. null input, empty string, non-alphanumeric characters
}