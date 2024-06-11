import static org.junit.Assert.assertEquals;
import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest {
    @Test
    public void testDiff() {
        String a = "abcd";
        String b = "efghabcd";
        RelativeString expected = new RelativeString("abcd->efghabcd[6]");
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffVerbose() {
        String a = "abcd";
        String b = "efghabcd";
        RelativeString expected = new RelativeString("abcd->efghabcd[6]");
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffNullStrings() {
        String a = null;
        String b = null;
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffSpecialCharacters() {
        String a = "abcd#efghabcd";
        String b = "efghabcd#abcd";
        RelativeString expected = new RelativeString("abcd#<-efghabcd#>[11]");
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    // You can continue writing test cases for other edge cases

    @Test(expected = NullPointerException.class)
    public void testDiffWithNullStringA() {
        String b = "efghabcd";
        String a = null;
        StringIncrementor.diff(a, b);
    }

    // You can continue writing test cases for other edge cases and exceptions

    @Test
    public void testPatch() {
        String a = "abcd";
        String patch = "efgh";
        String expected = "efghabcd";
        String result = StringIncrementor.patch(a, new RelativeString("abcd->efghabcd[6]"));
        assertEquals(expected, result);
    }

    // You can continue writing test cases for other methods and edge cases

    @AfterClass
    public static void cleanup() {
        // ...
    }
}