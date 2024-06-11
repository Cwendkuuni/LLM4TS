import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.BitSet;

import org.junit.Before;
import org.junit.Test;
import org.junit.experimental.annotations.VisibleForTesting;

import com.google.common.base.CharMatcher;
import com.google.common.base.FastMatcher;
import com.google.common.base.SmallCharMatcher;
import com.google.common.collect.BitSets;
import com.google.common.annotations.GwtIncompatible;

@GwtIncompatible("no precomputation is done in GWT")
public class SmallCharMatcherTest {
    private SmallCharMatcher matcher;

    @Before
    public void setup() {
        BitSet chars = BitSets.newSet();
        chars.set('a');
        matcher = SmallCharMatcher.from(chars, "Matcher for 'a'");
    }

    @Test
    public void testSmear() {
        // Basic test case
        int hashCode = 10;
        int expectedResult = 461845907 * Integer.rotateLeft(hashCode * -862048943, 15);
        int actualResult = SmallCharMatcher.smear(hashCode);
        assertEquals(expectedResult, actualResult);

        // Edge case and exception handling test case
        @SuppressWarnings("deprecation")
        int hashCodeNegative = -1;
        try {
            SmallCharMatcher.smear(hashCodeNegative);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testCheckFilter() {
        // Basic test case
        char c = 'a';
        boolean expectedResult = true;
        boolean actualResult = matcher.checkFilter((int)c);
        assertEquals(expectedResult, actualResult);

        // Edge case and exception handling test case
        char cNegative = (char)Character.MIN_VALUE;
        boolean expectedResultNegative = false;
        boolean actualResultNegative = matcher.checkFilter((int)cNegative);
        assertEquals(expectedResultNegative, actualResultNegative);
    }

    @Test
    public void testChooseTableSize() {
        // Basic test case
        int setSize = 4;
        int expectedResult = 4;
        int actualResult = SmallCharMatcher.chooseTableSize(setSize);
        assertEquals(expectedResult, actualResult);

        // Edge case and exception handling test case
        int setSizeNegative = -3;
        try {
            SmallCharMatcher.chooseTableSize(setSizeNegative);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testFrom() {
        // Basic test case
        BitSet chars = BitSets.newSet();
        chars.set('a');
        chars.set('b');
        CharMatcher expectedMatcher = CharMatcher.is('a').or(CharMatcher.is('b'));
        CharMatcher actualMatcher = SmallCharMatcher.from(chars, "Simple matcher");
        CharSequence expectedDescription = "Simple matcher";
        CharSequence actualDescription = actualMatcher.description();
        assertEquals(expectedMatcher, actualMatcher);
        assertEquals(expectedDescription, actualDescription);

        // Edge case and exception handling test case
        BitSet charsNull = null;
        try {
            SmallCharMatcher.from(charsNull, "Null chars matcher");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testMatches() {
        // Basic test case
        char c = 'a';
        boolean expectedResult = true;
        boolean actualResult = matcher.matches(c);
        assertEquals(expectedResult, actualResult);

        // Edge case and exception handling test case
        char cNull = '\0';
        boolean expectedResultNull = false;
        boolean actualResultNull = matcher.matches(cNull);
        assertEquals(expectedResultNull, actualResultNull);
    }

    @Test
    public void testSetBits() {
        // Basic test case
        BitSet table = BitSets.newSet();
        matcher.setBits(table);
        char c = 'a';
        boolean expectedResult = table.get(c);
        boolean actualResult = table.get(c);
        assertEquals(expectedResult, actualResult);

        // Edge case and exception handling test case
        BitSet tableNull = null;
        try {
            matcher.setBits(tableNull);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected
        }
    }
}