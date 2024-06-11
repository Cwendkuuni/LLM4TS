import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    @Test
    public void testDiff_sameStrings() {
        String a = "hello";
        String b = "hello";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_emptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_emptyStringAndNonEmptyString() {
        String a = "";
        String b = "hello";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_nonEmptyStringsWithCommonSubstring() {
        String a = "hello world";
        String b = "world hello";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(4, 0, 6));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_nullString() {
        String a = null;
        String b = "hello";
        try {
            StringIncrementor.diff(a, b);
        } catch (IllegalArgumentException e) {
            assertEquals("a or b is null", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_nullString2() {
        String a = "hello";
        String b = null;
        try {
            StringIncrementor.diff(a, b);
        } catch (IllegalArgumentException e) {
            assertEquals("a or b is null", e.getMessage());
            throw e;
        }
    }
}