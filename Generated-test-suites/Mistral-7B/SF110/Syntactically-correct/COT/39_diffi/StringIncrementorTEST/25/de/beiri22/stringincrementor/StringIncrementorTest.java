package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest
{
    @Test
    public void testDiff()
    {
        String a = "hello world";
        String b = "hello hello world world";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(6, 6, 5, " world"));
        StringIncrementor.diff(a, b, true); // verbose testing
        assertEquals(expectedResult, StringIncrementor.diff(a, b));

        a = "ABCDEFG";
        b = "1234567890ABCDEFG";
        expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(10, 10, 2, "12"));
        expectedResult.addLink(new StringLink(13, 13, 1, "3"));
        expectedResult.addLink(new StringLink(15, 15, 2, "56"));
        expectedResult.addLink(new StringLink(18, 18, 1, "7"));
        expectedResult.addLink(new StringLink(20, 20, 1, "9"));
        expectedResult.addLink(new StringLink(22, 22, 1, "0"));
        expectedResult.addLink(new StringLink(23, 23, 1, "A"));
        expectedResult.addLink(new StringLink(24, 24, 1, "B"));
        expectedResult.addLink(new StringLink(25, 25, 1, "C"));
        expectedResult.addLink(new StringLink(26, 26, 1, "D"));
        expectedResult.addLink(new StringLink(27, 27, 1, "E"));
        expectedResult.addLink(new StringLink(28, 28, 1, "F"));
        expectedResult.addLink(new StringLink(29, 29, 1, "G"));

        RelativeString result = StringIncrementor.diff(a, b, true); // verbose testing
        assertEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithNullStrings()
    {
        String a = null;
        String b = "hello world";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithNullRelativeString()
    {
        String a = "hello world";
        RelativeString r = null;
        StringIncrementor.patch(a, r);
    }

    @Test
    public void testPatch()
    {
        String a = "hello world";
        RelativeString r = StringIncrementor.diff(a, "hello hello world world", true);
        String patchedString = StringIncrementor.patch(a, r);
        assertEquals("hello world world world", patchedString);
    }

    @Test
    public void testPatchWithNullString()
    {
        String a = null;
        RelativeString r = StringIncrementor.diff("hello world", "hello hello world world", true);
        String patchedString = StringIncrementor.patch(a, r);
        assertNull(patchedString);
    }
}