package de.beiri22.stringincrementor.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringBuilderExt;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest
{
    @Test
    public void testDiff()
    {
        String a = "abc";
        String b = "abcd";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(2, 0, 1));
        StringIncrementor.diff(a, b, true); // verbose mode
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiffEmptyStrings()
    {
        String a = "";
        String b = "";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testDiffEmptyStringAndNonEmptyString()
    {
        String a = "";
        String b = "abc";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 3));
        StringIncrementor.diff(a, b, true); // verbose mode
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test
    public void testDiffIdenticalStrings()
    {
        String a = "abc";
        String b = "abc";
        RelativeString result = StringIncrementor.diff(a, b);
        assertNotNull(result);
        assertEquals("", result.getAbsolute());
        assertEquals(0, result.linksCount());
    }

    @Test
    public void testDiffSubstringInBString()
    {
        String a = "abcdabcd";
        String b = "efghijlk";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(6, 0, 4));
        StringIncrementor.diff(a, b, true); // verbose mode
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    @Test(expected = RuntimeException.class)
    public void testDiffDifferentLengthsMoreThanOneCharacter()
    {
        String a = "abc";
        String b = "abcd";
        StringIncrementor.diff(a, b, false); // not in verbose mode
    }

    @Test
    public void testPatch()
    {
        String a = "abcdefghijklm";
        String b = "abcxyzdefghijklm";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 3, 3));
        String result = StringIncrementor.patch(a, r);
        assertEquals("abcxyzdefghijklm", result);
    }

    @Test
    public void testPatchEmptyStringAndNonEmptyString()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("", result);
    }

    @Test
    public void testPatchIdenticalStrings()
    {
        String a = "abc";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("abc", result);
    }

    @Test
    public void testPatchSubstringInAResult()
    {
        String a = "abcdabcd";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 0, 4));
        String result = StringIncrementor.patch(a, r);
        assertEquals("abcdabcd", result);
    }

    @Test
    public void testPatchSubstringInBString()
    {
        String a = "abcdabcd";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 3, 3));
        String result = StringIncrementor.patch(a, r);
        assertEquals("abcdxyzabcd", result);
    }

    @Test
    public void testPatchSubstringInBStringDifferentLengths()
    {
        String a = "abcdabcd";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(3, 3, 5));
        String result = StringIncrementor.patch(a, r);
        assertEquals("abcdxxxabcd", result);
    }
}