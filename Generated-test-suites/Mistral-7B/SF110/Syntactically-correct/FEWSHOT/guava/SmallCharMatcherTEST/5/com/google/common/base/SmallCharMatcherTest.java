package com.google.common.base;

import static com.google.common.base.Preconditions.checkArgument;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.base.BitSet;
import com.google.common.base.FastMatcher;
import org.junit.Test;

public class SmallCharMatcherTest {

    @Test
    public void testSmallCharMatcher_validConstructorCalls() {
        char[] table = new char[SmallCharMatcher.chooseTableSize(5)];
        long filter = 0b1111_0001_0111_1101L;
        boolean containsZero = true;
        String description = "Test Matcher";

        SmallCharMatcher charMatcher = new SmallCharMatcher(table, filter, containsZero, description);

        checkArgument(charMatcher.table != null, "table should not be null");
        checkArgument(charMatcher.filter != filter, "filter should match constructor arguments");
        checkArgument(charMatcher.containsZero == containsZero, "containsZero should match constructor arguments");
        checkArgument(charMatcher.description.equals(description), "description should match constructor arguments");
    }

    @Test
    public void testSmallCharMatcher_matchesCharacterInTable() {
        char[] table = new char[8];
        long filter = 0b1011_0110_1010_1101L;
        boolean containsZero = false;
        String description = "Test Matcher";

        SmallCharMatcher charMatcher = SmallCharMatcher.from(BitSet.create(Arrays.asList('a', 'e', 'i')), description);

        for (char c : new String("these are the characters in the matcher: a, e, i").toCharArray()) {
            if (c == '\0') {
                continue;
            }
            assertTrue(charMatcher.matches(c));
            for (char tableChar : table) {
                if (tableChar != '\0' && tableChar == c) {
                    break;
                }
                if (tableChar == '\0' && c == ' ') {
                    break;
                }
            }
        }
    }

    @Test
    public void testSmallCharMatcher_matchesCharacterNotInTable() {
        char[] table = new char[8];
        long filter = 0b1011_0110_1010_1101L;
        boolean containsZero = false;
        String description = "Test Matcher";

        SmallCharMatcher charMatcher = SmallCharMatcher.from(BitSet.create(Arrays.asList('a', 'e', 'i')), description);

        for (char c : "some other characters: b, f, z".toCharArray()) {
            assertFalse(charMatcher.matches(c));
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSmallCharMatcher_illegalArgumentExceptionThrownWhenEmptyBitSet() {
        BitSet bitSet = BitSet.create();
        String description = "Test Matcher";
        SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, description);
    }

    @Test
    @VisibleForTesting
    public void testSmear() {
        int hashCode = 1234567890;
        int expected = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        int actual = SmallCharMatcher.smear(hashCode);
        assertEquals(expected, actual);
    }

    @Test
    public void testChooseTableSize() {
        int tableSize = SmallCharMatcher.chooseTableSize(5);
        assertEquals(16, tableSize);
        int tableSize2 = SmallCharMatcher.chooseTableSize(10);
        assertEquals(32, tableSize2);
    }

    @Test
    public void testFrom_validBitSet() {
        BitSet bitSet = BitSet.create(Arrays.asList('a', 'b', 'c', 'd', 'e'));
        String description = "Test Matcher";
        SmallCharMatcher charMatcher = SmallCharMatcher.from(bitSet, description);

        char[] table = charMatcher.table;
        long filter = charMatcher.filter;
        boolean containsZero = charMatcher.containsZero;

        assertEquals(16, table.length);
        assertEquals(bitSet.cardinality(), Long.bitCount(filter));
        assertFalse(containsZero);
    }

    @Test
    public void testFrom_emptyBitSet() {
        BitSet emptyBitSet = BitSet.create();
        String description = "Test Matcher";
        SmallCharMatcher charMatcher = SmallCharMatcher.from(emptyBitSet, description);

        assertNull(charMatcher.table);
        assertEquals(0, charMatcher.filter);
        assertTrue(charMatcher.containsZero);
    }
}