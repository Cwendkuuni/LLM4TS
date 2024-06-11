import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest {

    @Test
    public void diffTest1() {
        String a = "abcd";
        String b = "abcd";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void diffTest2() {
        String a = "abcd";
        String b = "acde";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(4, 0, 1));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    // Test cases for edge cases and exception handling go here

    @Test
    public void patchTest1() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        String expected = "abcd";
        assertEquals(expected, StringIncrementor.patch(a, r));
    }

    @Test
    public void patchTest2() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(4, 0, 1));
        String expected = "acde";
        assertEquals(expected, StringIncrementor.patch(a, r));
    }

    // Test cases for edge cases and exception handling go here
}