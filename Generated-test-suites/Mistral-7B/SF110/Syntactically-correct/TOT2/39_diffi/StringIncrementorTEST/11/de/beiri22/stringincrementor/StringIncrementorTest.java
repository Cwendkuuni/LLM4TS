import org.junit.Test;
import static org.junit.Assert.*;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {

    @Test
    public void testDiff_SameStrings() {
        String a = "hello";
        String b = "hello";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_DifferentStrings() {
        String a = "hello";
        String b = "world";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(5, 0, 5, "hello"));
        expected.addLink(new StringLink(10, 5, 5, "world"));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }
}