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
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz";

        RelativeString result = StringIncrementor.diff(a, b);

        assertEquals("1 2-3 abcdefghijklmnopqrstuvwxyz 11-13 abcdefghijklmnopqrstuvwxyz", result.toString());

        // Test verbose mode
        result = StringIncrementor.diff(a, b, true);
        System.out.println(result.toString());
        String expectedVerbose = "@ 0%\n@ 50%\nAdding Link: 1 2-3 abcdefghijklmnopqrstuvwxyz @ 50%\nAdding Link: 11-13 abcdefghijklmnopqrstuvwxyz @ 100%";
        assertEquals(expectedVerbose, result.toString());

        // Test empty strings
        String anEmptyString = "";
        String bAnEmptyString = "";
        result = StringIncrementor.diff(anEmptyString, bAnEmptyString);
        assertEquals("", result.toString());

        // Test null strings
        a = null;
        result = StringIncrementor.diff(anEmptyString, a);
        assertEquals("", result.toString());
        result = StringIncrementor.diff(a, bAnEmptyString);
        assertEquals("", result.toString());
    }

    @Test
    public void testPatch()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString("1 2-3 abcdefghijklmnopqrstuvwxyz 11-13 abcdefghijklmnopqrstuvwxyz");

        String patchedString = StringIncrementor.patch(a, r);

        assertEquals("abcdefghijklmnopqrstuvwxyzabcdefghijklmnopqrstuvwxyz", patchedString);

        // Test verbose mode
        patchedString = StringIncrementor.patch(a, r, true);
        System.out.println(patchedString);
        String expectedVerbose = "Adding 11 absolute Bytes from pos#0\nAdding Link: 1 2-3 abcdefghijklmnopqrstuvwxyz @ 50%\nAdding Link: 11-13 abcdefghijklmnopqrstuvwxyz @ 100%";
        assertEquals(expectedVerbose, patchedString);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchNull()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = null;

        StringIncrementor.patch(a, r);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchEmptyRelativeString()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();

        StringIncrementor.patch(a, r);
    }
}