import static org.junit.Assert.*;
import static com.google.common.base.Charsets.*;
import static com.google.common.collect.Lists.*;
import static com.google.common.collect.Maps.*;
import static com.google.common.collect.Sets.*;
import static com.google.common.primitives.Chars.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import com.google.common.base.BitSet;
import com.google.common.base.CharMatcher;
import com.google.common.base.FastMatcher;
import com.google.common.base.SmallerSortedMap;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SmallCharMatcherTest {

    private CharMatcher charMatcher;

    @Before
    public void setup() {
        BitSet charSet = new BitSet();
        charSet.set('a');
        charSet.set('b');
        charSet.set('c');
        charMatcher = SmallCharMatcher.from(charSet, "matches 'ab' and 'c'");
    }

    @Test
    public void testMatches() {
        // Matches expected characters
        for (char c : new char[]{'a', 'b', 'c'}) {
            assertTrue(charMatcher.matches(c));
        }

        // Matches expected characters in various combinations
        for (char c1 : new char[]{'a', 'b'}) {
            for (char c2 : new char[]{'a', 'b'}) {
                assertTrue(charMatcher.matches(String.valueOf(c1) + String.valueOf(c2)));
            }
        }

        // Does not match unexpected characters
        for (char c : new char[]{'d', '\0', 'e', 'f'}) {
            assertFalse(charMatcher.matches(c));
        }

        // Does not match expected characters if in wrong order
        for (char c : new char[]{'c', 'b', 'a'}) {
            assertFalse(charMatcher.matches(String.valueOf(c) + String.valueOf(c < 0 ? '\0' : c)));
        }

        // Does not match multi-character strings containing unexpected characters
        for (String s : new String[]{"ad", "abd", "\u0000ab", "abcde"}) {
            assertFalse(charMatcher.matches(s));
        }

        // Does not match empty string
        assertFalse(charMatcher.matches(""));
    }

    @Test
    public void testSetBits() {
        // Sets expected bits
        BitSet expected = new BitSet();
        expected.set('a');
        expected.set('b');
        expected.set('c');

        BitSet actual = new BitSet();
        charMatcher.setBits(actual);

        assertEquals(expected, actual);

        // Sets no bits if no characters are expected to match
        BitSet empty = new BitSet();
        SmallCharMatcher emptyMatcher = SmallCharMatcher.from(Collections.emptySet(), "matches no characters");
        emptyMatcher.setBits(actual);

        assertTrue(actual.isEmpty());

        // Sets all bits if all characters are expected to match
        BitSet all = new BitSet(Character.MAX_VALUE);
        SmallCharMatcher allMatcher = SmallCharMatcher.from(all, "matches all characters");
        allMatcher.setBits(actual);

        assertEquals(all, actual);
    }

    @Test
    public void testPerformance() {
        // Creates charMatcher with 1 million unique characters
        BitSet charSet = new BitSet(1_000_000);
        for (int i = 0; i < 1_000_000; i++) {
            charSet.set((char) i);
        }
        CharMatcher charMatcher = SmallCharMatcher.from(charSet, "matches 1 million unique characters");

        // Measures performance of matches() method
        long start = System.currentTimeMillis();
        for (int i = 0; i < 10_000_000; i++) {
            charMatcher.matches((char) i);
        }
        long end = System.currentTimeMillis();

        // Logs performance results
        System.out.printf("Matched 10 million random characters in %.2f milliseconds%n", (end - start) / 1000.0);
    }
}