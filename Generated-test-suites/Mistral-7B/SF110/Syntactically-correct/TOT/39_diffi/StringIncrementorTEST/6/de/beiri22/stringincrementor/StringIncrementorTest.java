import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {
    @Test
    public void testDiff_EqualStrings() {
        String a = "abcd";
        String b = "abcd";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDiff_EmptyStringAndNonEmptyString() {
        String a = "";
        String b = "abcd";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals("@ 0%", result.toString());
    }

    @Test
    public void testDiff_EmptyStringAndEmptyString() {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertTrue(result.isEmpty());
    }

    @Test
    public void testDiff_DifferentStrings() {
        String a = "abcd";
        String b = "efgh";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        String expected = new StringLink(4, 0, 3).toString() + "@ 0%";
        assertEquals(expected, result.toString());
    }

    @Test
    public void testDiff_VerboseMode() {
        String a = "abcd";
        String b = "efgh";
        String verboseOutput = "Link: @ 0% (3 Characters): abc -> efgh\n";
        System.setOut(new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String s) {
                super.println(s);
                verboseOutput += s;
            }
        });
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertNotNull(result);
        assertEquals(expected + "Link: @ 100% (0 Characters): d", verboseOutput);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_NullString() {
        String a = null;
        String b = "abcd";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_NullStringB() {
        String a = "abcd";
        String b = null;
        StringIncrementor.diff(a, b);
    }

    @Test
    public void testPatch_EmptyString() {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }

    @Test
    public void testPatch_EmptyStringAndEmptyRelativeString() {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }

    @Test
    public void testPatch_NonEmptyStringAndEmptyRelativeString() {
        String a = "abcd";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals(a, result);
    }

    @Test
    public void testPatch_NonEmptyStringAndNonEmptyRelativeString() {
        String a = "abcd";
        RelativeString r = new StringLink(4, 0, 3).toString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("efgh", result);
    }

    @Test
    public void testPatch_VerboseMode() {
        String a = "abcd";
        RelativeString r = new StringLink(4, 0, 3).toString();
        System.setOut(new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String s) {
                super.println(s);
            }
        });
        String result = StringIncrementor.patch(a, r, true);
        assertEquals("efgh", result);
    }
}