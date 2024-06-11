import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        String input = "\0\b\t\n\f\r\"'\\\n";
        String expected = "\\0\\b\\t\\n\\f\\r\\\"'\\\\";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }
}