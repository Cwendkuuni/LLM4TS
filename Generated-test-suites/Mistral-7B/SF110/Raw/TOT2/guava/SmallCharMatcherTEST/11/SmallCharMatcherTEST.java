import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        char[] table = new char[]{'a', 'b', 'c'};
        long filter = 0b11110101L;
        boolean containsZero = false;
        CharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, "Test CharMatcher");
        char c = 'a';
        boolean result = charMatcher.matches(c);
        assertTrue(result);

        c = 'b';
        result = charMatcher.matches(c);
        assertTrue(result);

        c = 'c';
        result = charMatcher.matches(c);
        assertTrue(result);

        c = '\u0000'; // null character
        result = charMatcher.matches(c);
        assertFalse(result); // should not match null character if containsZero is false

        if (containsZero) {
            result = charMatcher.matches('\u0000'); // null character
            assertTrue(result); // should match null character if containsZero is true
        }

        c = 'd';
        result = charMatcher.matches(c);
        assertFalse(result); // should not match character 'd' which is not in the table
    }
}