package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.util.Arrays;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.ExtBoolArray;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;

public class StringIncrementorTest
{
    @Test
    public void testDiff_EmptyStrings()
    {
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_IdenticalStrings()
    {
        String a = "abcdefghijklmno";
        String b = "abcdefghijklmno";
        RelativeString expected = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_DifferentStrings()
    {
        String a = "abcdefghijklmno";
        String b = "abcdefghijklmonpqrst";
        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(-7, 10, 3));
        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_ThrowsExceptionWithNullStrings()
    {
        String a = null;
        String b = "abcdefghijklmno";
        StringIncrementor.diff(a, b, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_ThrowsExceptionWithNullStringB()
    {
        String a = "abcdefghijklmno";
        String b = null;
        StringIncrementor.diff(a, b, false);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testDiff_ThrowsExceptionWithNegativeLinkLength()
    {
        String a = "abcdefghijklmno";
        String b = "abcdefghijklmonpqrst";
        StringIncrementor.diff(a, b, false);
        RelativeString result = StringIncrementor.diff(a, b, true);
        StringLink link = result.getLink(0);
        link.setLen(-1);
    }

    @Test
    public void testPatch_EmptyStrings()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String expected = "";
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_NoLinks()
    {
        String a = "abcdefghijklmno";
        RelativeString r = new RelativeString();
        String expected = "abcdefghijklmno";
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_SingleLink()
    {
        String a = "abcdefghijklmno";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 12, 3));
        String expected = "abcdedefghijklmno";
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_MultipleLinks()
    {
        String a = "abcdefghijklmno";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 12, 3));
        r.addLink(new StringLink(15, 21, 6));
        String expected = "abcdedefghijklmonpqrst";
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_ThrowsExceptionWithNullStringA()
    {
        String a = null;
        RelativeString r = new RelativeString();
        StringIncrementor.patch(a, r, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_ThrowsExceptionWithNullStringR()
    {
        String a = "abcdefghijklmno";
        RelativeString r = null;
        StringIncrementor.patch(a, r, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_ThrowsExceptionWithNullAbsoluteStringR()
    {
        String a = "abcdefghijklmno";
        RelativeString r = new RelativeString();
        r.setAbsolute(null);
        StringIncrementor.patch(a, r, false);
    }
}