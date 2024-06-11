import org.junit.Test;
import static org.junit.Assert.*; // for assertions
import de.beiri22.stringincrementor.*;

public class StringIncrementorTest {

    @Test
    public void testDiff_sameStrings() {
        String a = "hello";
        String b = "hello";
        RelativeString expected = new RelativeString();
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    @Test
    public void testDiff_emptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    @Test
    public void testDiff_emptyStringWithNonEmptyString() {
        String a = "";
        String b = "hello";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(0, 0, 5));
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);
    }

    // Add edge case and exception handling test cases here
}