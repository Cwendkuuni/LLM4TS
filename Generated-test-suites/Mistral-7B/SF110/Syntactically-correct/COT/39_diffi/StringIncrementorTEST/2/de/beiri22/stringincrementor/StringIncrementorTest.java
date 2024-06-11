package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest
{
    @Test
    public void testDiff_sameStrings()
    {
        String a = "abcdef";
        String b = "abcdef";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_emptyString()
    {
        String a = "";
        String b = "abcdef";
        RelativeString expected = new RelativeString();
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_oneCharacterDifference()
    {
        String a = "abcdef";
        String b = "abcdff";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(5, 5, 1));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test
    public void testDiff_multipleCharacterDifferences()
    {
        String a = "abcdef";
        String b = "acgdeh";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(1, 1, 1));
        expected.addLink(new StringLink(4, 4, 1));
        assertEquals(expected, StringIncrementor.diff(a, b));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_nullStrings()
    {
        String a = null;
        String b = "abcdef";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDiff_targetStringTooShort()
    {
        String a = "abcdef";
        String b = "abcd";
        StringIncrementor.diff(a, b);
    }

    @Test
    public void testDiff_verboseMode()
    {
        String a = "abcdef";
        String b = "acgdeh";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(1, 1, 1));
        expected.addLink(new StringLink(4, 4, 1));
        StringIncrementor.diff(a, b, true);
        assertNull(System.out.err); // Verbose mode shouldn't print errors
    }

    @Test
    public void testPatch_sameString()
    {
        String a = "abcdef";
        RelativeString r = new RelativeString();
        String expected = "abcdef";
        assertEquals(expected, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatch_emptyString()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        assertEquals(expected, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatch_oneCharacterDeletion()
    {
        String a = "abcdef";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 1, 1));
        String expected = "bcded";
        assertEquals(expected, StringIncrementor.patch(a, r));
    }

    @Test
    public void testPatch_multipleCharacterDeletions()
    {
        String a = "abcdef";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 1, 1));
        r.addLink(new StringLink(4, 4, 1));
        String expected = "cde";
        assertEquals(expected, StringIncrementor.patch(a, r));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_nullStringOrRelativeString()
    {
        String a = "abcdef";
        RelativeString r = null;
        StringIncrementor.patch(a, r);
    }

    @Test
    public void testPatch_verboseMode()
    {
        String a = "abcdef";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(1, 1, 1));
        r.addLink(new StringLink(4, 4, 1));
        StringIncrementor.patch(a, r, true);
        assertNull(System.out.err); // Verbose mode shouldn't print errors
    }
}