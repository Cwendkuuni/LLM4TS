import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "hello world";
        String b = "hello there world";
        RelativeString result = StringIncrementor.diff(a, b);
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(5, 5, 3));
        expected.addLink(new StringLink(10, 10, 3));
        expected.setAbsolute(" there ");
        assertEquals(expected, result);
    }

    @Test
    public void testDiffVerbose() {
        String a = "hello world";
        String b = "hello there world";
        RelativeString result = StringIncrementor.diff(a, b, true);
        // The output of the test is not relevant for JUnit, but it should show the links.
        // You can use a TestLogger to capture the output and check it.
        // For example in TestNG it can be done using:
        // assertLogContains("@ 50.0%", "there @ 50.0%");
    }
}