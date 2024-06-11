import static org.junit.Assert.*; // for assertions
import static com.pmdesigns.jvc.tools.TokenMgrError.*; // for the class under test

public class TokenMgrErrorTest {
    @Test
    public void testAddEscapes() {
        String input = "Hello\tWorld! @#$%&/()_=+[]{}|;:,.<>\n\r\\ \"'\\\\";
        String expected = "Hello\\tWorld! @#$%&/()_=+[]{}\\|;:,.<>\\n\\r\\ \\ \"'\\\\";
        String result = addEscapes(input);
        assertEquals(expected, result);
    }
}