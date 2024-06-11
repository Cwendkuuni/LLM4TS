import org.junit.Test;
import static org.junit.Assert.*;
import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.BitSets;

public class SmallCharMatcherTest {

    @Test
    public void testMatches() {
        BitSet chars = BitSets.newBitSet();
        chars.set(65); // A
        chars.set(66); // B
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "matches characters A and B");
        assertTrue(charMatcher.matches('A'));
        assertTrue(charMatcher.matches('B'));
        assertFalse(charMatcher.matches('C'));
    }

    @Test
    public void testEmptyMatcher() {
        BitSet emptySet = BitSets.newBitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(emptySet, "empty matcher");
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('A'));
    }

    @Test
    public void testContainsZero() {
        BitSet chars = BitSets.newBitSet();
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "contains zero");
        assertTrue(charMatcher.matches('\0'));
    }

    @Test
    public void testWrongDescription() {
        BitSet chars = BitSets.newBitSet();
        chars.set(65); // A
        CharMatcher charMatcher = SmallCharMatcher.from(chars, "this matcher doesn't make sense");
        assertFalse(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('A'));
    }
}