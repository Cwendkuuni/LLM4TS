import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        String input = "Hello\nWorld";
        String expected = "Hello\\nWorld";
        String result = TokenMgrError.addEscapes(input);
        assertEquals(expected, result);
    }
}