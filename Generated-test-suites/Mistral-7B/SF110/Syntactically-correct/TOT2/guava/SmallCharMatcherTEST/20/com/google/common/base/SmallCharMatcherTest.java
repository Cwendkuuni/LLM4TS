import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {
    @Test
    public void testMatchesCharInTable() {
        char[] table = new char[]{'a', 'e', 'i'};
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0, false, "CharMatcher for vowels");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('e'));
        assertTrue(charMatcher.matches('i'));
    }

    @Test
    public void testMatchesCharNotInTable() {
        char[] table = new char[]{'a', 'e', 'i'};
        SmallCharMatcher charMatcher = new SmallCharMatcher(table, 0, false, "CharMatcher for vowels");
        assertFalse(charMatcher.matches('o'));
    }

    @Test
    public void testMatchesZero() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, true, "CharMatcher for zero");
        assertTrue(charMatcher.matches('\0'));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testMatchesNegativeChar() {
        SmallCharMatcher charMatcher = new SmallCharMatcher(new char[0], 0, false, "CharMatcher for valid chars");
        char invalidChar = (char)Integer.MIN_VALUE;
        charMatcher.matches(invalidChar);
    }
}