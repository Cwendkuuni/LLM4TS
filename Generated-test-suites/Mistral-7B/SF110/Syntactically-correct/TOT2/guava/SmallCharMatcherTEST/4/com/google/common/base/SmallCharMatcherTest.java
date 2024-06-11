import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        char[] table = new char[]{'a', 'e', 'i'};
        long filter = 0b1111101111101111L;
        boolean containsZero = false;
        CharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, "Test CharMatcher");

        char validChar = 'e';
        assertTrue(charMatcher.matches(validChar));

        char invalidChar1 = 'g';
        assertFalse(charMatcher.matches(invalidChar1));

        char invalidChar2 = '\u0000';
        if (charMatcher.containsZero()) {
            assertTrue(charMatcher.matches(invalidChar2));
        } else {
            assertFalse(charMatcher.matches(invalidChar2));
        }
    }
}