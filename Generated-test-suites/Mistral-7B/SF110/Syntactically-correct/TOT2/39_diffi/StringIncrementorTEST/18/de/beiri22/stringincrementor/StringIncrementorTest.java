package de.beiri22.stringincrementor.test;

import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    @Test
    public void testDiff() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz123";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(2, 2, 3));
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiffVerbose() {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz123";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(2, 2, 3));
        RelativeString result = StringIncrementor.diff(a, b, true);
        // Add assertions or checks for verbose output here if needed
        assertEquals(expected, result);
    }

    @Test
    public void testDiffWithNullA() {
        assertNull(StringIncrementor.diff(null, "abcdef"));
    }

    @Test
    public void testDiffWithNullB() {
        String a = "abcdef";
        assertNull(StringIncrementor.diff("abcdef", null));
    }

    @Test
    public void testDiffWithEmptyStrings() {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiffWithSpecialCharacters() {
        String a = "abc#def";
        String b = "abcde#f";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(3, 3, 1));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiffWithLongStrings() {
        String a = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz123";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(26, 26, 3));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiffWithOverlappingLinks() {
        String a = "abcdef";
        String b = "abcdefg";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithNonMatchingCharacters() {
        String a = "abcdef";
        String b = "123456";
        StringIncrementor.diff(a, b);
    }

    // ... other test cases for patch method here ...
}