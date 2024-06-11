package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest
{
    @Test
    public void testDiff_EmptyStrings()
    {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }

    @Test
    public void testDiff_IdenticalStrings()
    {
        String a = "hello";
        String b = "hello";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result);
    }

    @Test
    public void testDiff_SingleCharacterDifference()
    {
        String a = "hello";
        String b = "helo";
        RelativeString result = StringIncrementor.diff(a, b);
        StringLink expected = new StringLink(4, 3, 1);
        assertEquals(expected.toString(), result.getLink(0).toString());
    }

    @Test
    public void testDiff_MultipleCharacterDifferences()
    {
        String a = "hello world";
        String b = "hello earth";
        RelativeString result = StringIncrementor.diff(a, b);
        StringLink expected1 = new StringLink(6, 5, 1);
        StringLink expected2 = new StringLink(11, 10, 1);
        String[] links = {expected1.toString(), expected2.toString()};
        assertArrayEquals(links, result.getLinksArray());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_ThrowsException_WhenStringBShorterThanStringA()
    {
        String a = "abcdef";
        String b = "abc";
        StringIncrementor.diff(a, b);
    }

    @Test
    public void testDiff_VerboseMode()
    {
        String a = "hello world";
        String b = "hello earth";
        RelativeString result = StringIncrementor.diff(a, b, true);
        StringLink expected1 = new StringLink(6, 5, 1);
        StringLink expected2 = new StringLink(11, 10, 1);
        String[] links = {expected1.toString(), expected2.toString()};
        assertArrayEquals(links, result.getLinksArray());
        // Verbose output is not testable, but we can check the console output with a separate test
    }

    @Test
    public void testPatch_EmptyStrings()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }

    @Test
    public void testPatch_EmptyRelativeString()
    {
        String a = "hello";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals(a, result);
    }

    @Test
    public void testPatch_SingleCharacterDifference()
    {
        String a = "hello";
        String b = "helo";
        String result = StringIncrementor.patch(a, new StringLink(4, 3, 1));
        assertEquals("helo", result);
    }

    @Test
    public void testPatch_MultipleCharacterDifferences()
    {
        String a = "hello world";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(6, 5, 1));
        r.addLink(new StringLink(11, 10, 1));
        String result = StringIncrementor.patch(a, r);
        assertEquals("hello earth", result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_RelativeStringWithNegativeLength()
    {
        String a = "hello";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(-1, -1, 1));
        StringIncrementor.patch(a, r);
    }
}