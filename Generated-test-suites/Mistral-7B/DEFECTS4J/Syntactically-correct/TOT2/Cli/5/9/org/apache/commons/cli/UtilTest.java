import org.junit.Test;
import static org.junit.Assert.*;

public class UtilTest {

    @Test
    public void testStripLeadingHyphens() {
        String str = "--test-string";
        String expected = "test-string";
        String actual = Util.stripLeadingHyphens(str);
        assertEquals(expected, actual);
    }
}