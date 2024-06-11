package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

import java.util.Arrays;

public class StringIncrementorTest
{
    @Test
    public void testDiffWithVerboseTrue()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        String b = "The quick red fox jumps over the lazy dog";

        RelativeString result = StringIncrementor.diff(a, b, true);

        String expected = "The quick red fox jumps over the lazy dog";
        String expectedLink = "The red ";
        String expectedLinkLength = 5;

        assertEquals(expected, result.getAbsolute());
        assertEquals(1, result.linksCount());
        StringLink link = result.getLink(0);
        assertEquals(expectedLink, a.substring(link.getPosOrig(), link.getPosOrig() + expectedLinkLength));
        assertEquals(13, link.getLen());
    }

    @Test
    public void testDiffWithVerboseFalse()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        String b = "The quick red fox jumps over the lazy dog";

        RelativeString result = StringIncrementor.diff(a, b);

        String expected = "The quick red fox jumps over the lazy dog";
        String expectedLink = "The red ";
        String expectedLinkLength = 5;

        assertEquals(expected, result.getAbsolute());
        assertTrue(result.linksCount() > 0);
        StringLink link = result.getLink(0);
        assertEquals(expectedLink, a.substring(link.getPosOrig(), link.getPosOrig() + expectedLinkLength));
        assertEquals(13, link.getLen());
    }

    @Test
    public void testDiffWithNoLinks()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        String b = "The quick brown fox jumps over the lazy dog";

        RelativeString result = StringIncrementor.diff(a, b);

        String expected = "";
        assertEquals(expected, result.getAbsolute());
        assertEquals(0, result.linksCount());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithNullString()
    {
        String a = null;
        String b = "The quick brown fox jumps over the lazy dog";

        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffWithNullStringB()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        String b = null;

        StringIncrementor.diff(a, b);
    }

    @Test
    public void testPatchWithVerboseTrue()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 5, "The red "));

        String result = StringIncrementor.patch(a, r, true);

        String expected = "The quick red fox jumps over the lazy dog";

        assertEquals(expected, result);
    }

    @Test
    public void testPatchWithVerboseFalse()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 5, "The red "));

        String result = StringIncrementor.patch(a, r);

        String expected = "The quick red fox jumps over the lazy dog";

        assertEquals(expected, result);
    }

    @Test
    public void testPatchWithEmptyStringResult()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        RelativeString r = new RelativeString();

        String result = StringIncrementor.patch(a, r);

        String expected = "";

        assertEquals(expected, result);
    }

    @Test
    public void testPatchWithEmptyStringRelative()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        RelativeString r = new RelativeString();

        String result = StringIncrementor.patch(a, r);

        String expected = "The quick brown fox jumps over the lazy dog";

        assertEquals(expected, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchWithNullString()
    {
        String a = null;
        RelativeString r = new RelativeString();

        StringIncrementor.patch(a, r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchWithNullStringRelative()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        RelativeString r = null;

        StringIncrementor.patch(a, r);
    }
}