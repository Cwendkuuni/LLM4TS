import static com.google.common.base.Charsets.UTF_8;
import static org.junit.Assert.*;

import com.google.common.base.BitSets;
import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

public class SmallCharMatcherTest {

    @Test
    public void testFromCharArray() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newMutableSet(), "Test CharMatcher from Char Array");
        char[] charArray = {'a', 'b', 'c'};
        CharMatcher expectedCharMatcher = SmallCharMatcher.from(BitSets.create(0, 1, 2), "Test CharMatcher from Char Array");
        assertEquals(expectedCharMatcher, charMatcher);
        CharMatcher charMatcherFromArray = SmallCharMatcher.from(charArray, "Test CharMatcher from Char Array");
        assertEquals(expectedCharMatcher, charMatcherFromArray);
    }

    @Test
    public void testMatches() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newMutableSet(), "Test Matches 'a'");
        char[] charArray = {'a', 'b', 'c'};
        CharMatcher expectedCharMatcher = SmallCharMatcher.from(BitSets.create(0), "Test Matches 'a'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(expectedCharMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(expectedCharMatcher.matches('b'));
        assertFalse(charMatcher.matches('d'));
        assertFalse(expectedCharMatcher.matches('d'));

        charMatcher = SmallCharMatcher.from(BitSets.newMutableSet(), "Test Matches '\\0'");
        assertTrue(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));

        charMatcher = SmallCharMatcher.from(charArray, "Test Matches 'a'");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('d'));

        charMatcher = SmallCharMatcher.from(charArray, "Test Matches 'b' or 'c'");
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('d'));

        charMatcher = SmallCharMatcher.from(charArray, "Test Matches '\\0' or 'b'");
        assertFalse(charMatcher.matches('\0'));
        assertFalse(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        charMatcher = SmallCharMatcher.from(charArray, "Test Matches 'a' and 'b'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        charMatcher = SmallCharMatcher.from(charArray, "Test Matches 'a' or 'b' but not 'c'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        charMatcher = SmallCharMatcher.from(charArray, "Test Matches 'a' and not 'b' or 'c'");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));

        charMatcher = SmallCharMatcher.from(charArray, "Test Matches 'a' and not 'b'");
        assertTrue(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
    }

    @Test
    public void testSetBits() {
        CharMatcher charMatcher = SmallCharMatcher.from(BitSets.newMutableSet(), "Test SetBits");
        BitSet expectedTable = BitSets.newMutableSet();
        expectedTable.set('a');
        expectedTable.set('b');
        expectedTable.set('c');

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        try (Writer writer = new BufferedWriter(new OutputStreamWriter(outputStream, UTF_8))) {
            charMatcher.setBits(expectedTable);
            CharMatcher expectedCharMatcher = SmallCharMatcher.from(outputStream.toByteArray(), "Test SetBits");
            BitSet actualTable = BitSets.newMutableSet();
            expectedCharMatcher.setBits(actualTable);
            byte[] expectedBytes = expectedTable.toByteArray();
            byte[] actualBytes = actualTable.toByteArray();
            assertEquals(expectedBytes.length, actualBytes.length);
            for (int i = 0; i < expectedBytes.length; i++) {
                assertEquals(expectedBytes[i], actualBytes[i]);
            }
        } catch (IOException e) {
            fail("Unexpected exception" + e);
        }
    }

    @Test
    public void testFromString() {
        CharMatcher charMatcher = SmallCharMatcher.from("[abc]", "Test FromString 'abc'");
        byte[] expectedBytes = "[abc]".getBytes(UTF_8);
        CharMatcher expectedCharMatcher = SmallCharMatcher.from(expectedBytes, "Test FromString 'abc'");
        assertTrue(charMatcher.matches('a'));
        assertTrue(charMatcher.matches('b'));
        assertTrue(charMatcher.matches('c'));
        assertFalse(charMatcher.matches('d'));
        assertEquals(expectedCharMatcher, charMatcher);

        charMatcher = SmallCharMatcher.from("[^abc]", "Test FromString '^abc'");
        assertFalse(charMatcher.matches('a'));
        assertFalse(charMatcher.matches('b'));
        assertFalse(charMatcher.matches('c'));
        assertTrue(charMatcher.matches('d'));
        assertEquals(SmallCharMatcher.not(SmallCharMatcher.from("[abc]", "Test FromString 'abc'")), charMatcher);
    }
}