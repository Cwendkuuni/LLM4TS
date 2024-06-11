import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcdabcd";
        String b = "abcdabcdabcd";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(3, 3, 3));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    // Edge case test with empty strings
    @Test
    public void testDiffEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    // Error case test with null strings
    @Test
    public void testDiffNullStrings() {
        String a = null;
        String b = null;
        try {
            StringIncrementor.diff(a, b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Pass
        }
    }

    // Error case test with invalid input length
    @Test
    public void testDiffInvalidInputLength() {
        String a = "abcd";
        String b = "abcdabcdabcdabcd";
        try {
            StringIncrementor.diff(a, b);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Pass
        }
    }
}