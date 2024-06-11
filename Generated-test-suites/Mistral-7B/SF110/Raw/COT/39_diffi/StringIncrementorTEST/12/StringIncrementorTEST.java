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
    public void testDiff_EmptyStrings()
    {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_IdenticalStrings()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertNull(result.getAbsolute());
        assertNull(result.linksCount());
    }

    @Test
    public void testDiff_DifferentStrings()
    {
        String a = "abcdabcdefghijklmnopqrstuvwxyz";
        String b = "abceabfghijklmnopqrstuvwxyz";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(3, 2, 3));
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_NullInput()
    {
        String a = null;
        String b = "abcdefghijklmnopqrstuvwxyz";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_NullInputB()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = null;
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiff_InvalidCharacterIndex()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz";
        int posB = b.length() + 1;
        RelativeString result = StringIncrementor.diff(a, b, true);
    }

    @Test
    public void testDiff_VerboseMode()
    {
        String a = "abcdabcdefghijklmnopqrstuvwxyz";
        String b = "abceabfghijklmnopqrstuvwxyz";
        StringIncrementor.diff(a, b, true);
    }

    @Test
    public void testPatch_EmptyStrings()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_IdenticalStrings()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        String expected = "abcdefghijklmnopqrstuvwxyz";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_DifferentStrings()
    {
        String a = "abcdabcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 2, 3));
        String expected = "abceabfghijklmnopqrstuvwxyz";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_NullInputA()
    {
        String a = null;
        RelativeString r = new RelativeString();
        StringIncrementor.patch(a, r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_NullInputR()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = null;
        StringIncrementor.patch(a, r);
    }

    @Test(expected = NullPointerException.class)
    public void testPatch_NullResultStringBuilder()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        StringIncrementor.patch(a, r, () -> null);
    }

    @Test
    public void testPatch_VerboseMode()
    {
        String a = "abcdabcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 2, 3));
        StringIncrementor.patch(a, r, (StringBuilder sb) -> {
            System.out.println("Creating patch StringBuilder...");
            return sb;
        });
    }
}