package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

public class StringIncrementorTest
{
    @Test
    public void testDiff_emptyStrings()
    {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertTrue(result.linksCount() == 0);
        assertEquals("", result.getAbsolute());
    }

    @Test
    public void testDiff_identicalStrings()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b);
        assertTrue(result.linksCount() == 0);
        assertEquals("", result.getAbsolute());
    }

    @Test
    public void testDiff_singleCharacterDifference()
    {
        String a = "abc";
        String b = "abd";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(3, link.getPosOrig());
        assertEquals(3, link.getPosNew());
        assertEquals(1, link.getLen());
        assertEquals("d", link.getReplaced());
        assertEquals("", result.getAbsolute());
    }

    @Test
    public void testDiff_multipleCharacterDifferences()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdxyzefghijklmnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(2, result.linksCount());
        StringLink link1 = result.getLink(0);
        assertEquals(4, link1.getPosOrig());
        assertEquals(4, link1.getPosNew());
        assertEquals(1, link1.getLen());
        assertEquals("x", link1.getReplaced());
        StringLink link2 = result.getLink(1);
        assertEquals(25, link2.getPosOrig());
        assertEquals(25, link2.getPosNew());
        assertEquals(1, link2.getLen());
        assertEquals("y", link2.getReplaced());
        assertEquals("", result.getAbsolute());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_nullStrings()
    {
        String a = null;
        String b = "abcdefghijklmnopqrstuvwxyz";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_nullFirstString()
    {
        String a = null;
        String b = "abcdefghijklmnopqrstuvwxyz";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_nullSecondString()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = null;
        StringIncrementor.diff(a, b);
    }

    @Test
    public void testDiff_verboseTrue()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdxyzefghijklmnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(2, result.linksCount());
        StringLink link1 = result.getLink(0);
        assertEquals(4, link1.getPosOrig());
        assertEquals(4, link1.getPosNew());
        assertEquals(1, link1.getLen());
        assertEquals("x", link1.getReplaced());
        StringLink link2 = result.getLink(1);
        assertEquals(25, link2.getPosOrig());
        assertEquals(25, link2.getPosNew());
        assertEquals(1, link2.getLen());
        assertEquals("y", link2.getReplaced());
        System.out.println(result);
    }

    @Test
    public void testPatch_emptyStringAndEmptyRelativeString()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String patched = StringIncrementor.patch(a, r);
        assertEquals("", patched);
    }

    @Test
    public void testPatch_emptyStringAndNonEmptyRelativeString()
    {
        String a = "";
        RelativeString r = new RelativeString("a", 0, 1);
        String patched = StringIncrementor.patch(a, r);
        assertEquals("a", patched);
    }

    @Test
    public void testPatch_nonEmptyStringAndEmptyRelativeString()
    {
        String a = "abc";
        RelativeString r = new RelativeString();
        String patched = StringIncrementor.patch(a, r);
        assertEquals("abc", patched);
    }

    @Test
    public void testPatch_nonEmptyStringAndNonEmptyRelativeString()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString("xyz", 4, 3);
        String patched = StringIncrementor.patch(a, r);
        assertEquals("abcxysdefghijklmnopqrstuvwxyz", patched);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_nullString()
    {
        String a = null;
        RelativeString r = new RelativeString("xyz", 4, 3);
        StringIncrementor.patch(a, r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_nullRelativeString()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = null;
        StringIncrementor.patch(a, r);
    }
}