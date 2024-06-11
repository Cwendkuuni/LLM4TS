import org.junit.Test;
import static org.junit.Assert.*;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        char[] table = new char[]{'a', 'b', 'c'};
        long filter = 0b1111011110111111L;
        boolean containsZero = false;
        CharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, "Test CharMatcher");

        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('d'));
    }
}