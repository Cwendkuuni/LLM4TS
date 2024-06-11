import org.junit.Test;
import static org.junit.Assert.*;;

public class Utf8Test {
    @Test
    public void testEncodedLength() {
        CharSequence sequence = "abcdefghijklmnopqrst"; // Basic use case
        int expected = 11;
        int actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);

        sequence = "\u0123\u0456\uF001\uD800\uDC00"; // Edge case: Surrogate pairs
        expected = 9;
        actual = Utf8.encodedLength(sequence);
        assertEquals(expected, actual);

        sequence = "\uD800"; // Error case: Invalid UTF-16 code point
        try {
            Utf8.encodedLength(sequence);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}