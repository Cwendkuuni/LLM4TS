package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;
import org.junit.Test;

public class StringIncrementorTest
{
    @Test
    public void testDiff_SameStrings()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals("The result should be an empty RelativeString.", result, new RelativeString());
    }

    @Test
    public void testDiff_EmptyString()
    {
        String a = "";
        String b = "abcdefghijklmnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b);
        String expected = "0..12";
        assertEquals("The result should be the absolute string from b[0] to b[12].", result.getAbsolute(), expected);
        assertEquals("The links count should be one.", result.linksCount(), 1);
        assertEquals("The first link should cover the whole difference.", result.getLink(0), new StringLink(0, 0, 13));
    }

    @Test
    public void testDiff_SingleCharacterDifference()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklnopqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b);
        String expected = "3..4";
        assertEquals("The result should be the absolute string from b[3] to b[4].", result.getAbsolute(), expected);
        assertEquals("The links count should be one.", result.linksCount(), 1);
        assertEquals("The first link should cover the difference.", result.getLink(0), new StringLink(2, 3, 2));
    }

    @Test
    public void testDiff_MultipleCharacterDifferences()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdelkpqrstuvwxyz";
        RelativeString result = StringIncrementor.diff(a, b);
        String expected = "1..2, 5..6, 10..11";
        assertEquals("The result should be the absolute strings from b[1] to b[2], b[5] to b[6], and b[10] to b[11].", result.getAbsolute(), expected);
        assertEquals("The links count should be three.", result.linksCount(), 3);
        assertEquals("The first link should cover the difference from a[1] to a[2].", result.getLink(0), new StringLink(0, 1, 2));
        assertEquals("The second link should cover the difference from a[5] to a[6].", result.getLink(1), new StringLink(4, 5, 2));
        assertEquals("The third link should cover the difference from a[10] to a[11].", result.getLink(2), new StringLink(8, 10, 2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testDiff_TargetStringTooShort()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvw";
        StringIncrementor.diff(a, b);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiff_VerboseModeWithNegativeVerbosityLevel()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz";
        try {
            StringIncrementor.diff(a, b, -1);
        } catch (IllegalArgumentException e) {
            assertEquals("The verbosity level should not be negative.", e.getMessage(), "Verbosity level must not be negative.");
            throw e;
        }
    }

    @Test
    public void testPatch_EmptyString()
    {
        String a = "";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("The result should be an empty string.", result, "");
    }

    @Test
    public void testPatch_NoDifferences()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        String result = StringIncrementor.patch(a, r);
        assertEquals("The result should be the same as the original string.", result, a);
    }

    @Test
    public void testPatch_SingleCharacterDifference()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new StringLink(2, 3, 2);
        String result = StringIncrementor.patch(a, r);
        assertEquals("The result should be the string 'bcdefghijklmnopqrstuvwxyz'.", result, "bcdefghijklmnopqrstuvwxyz");
    }

    @Test
    public void testPatch_MultipleCharacterDifferences()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new StringLink(0, 1, 3)
                .concat(new StringLink(4, 5, 2))
                .concat(new StringLink(8, 10, 2));
        String result = StringIncrementor.patch(a, r);
        assertEquals("The result should be the string 'abcdelkpqrstuvwxyz'.", result, "abcdelkpqrstuvwxyz");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatch_InvalidLink()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new StringLink(-1, -1, -1);
        try {
            StringIncrementor.patch(a, r);
        } catch (IllegalArgumentException e) {
            assertEquals("The position of the start and end of the link must not be negative.", e.getMessage(), "Position of start or end of link must not be negative.");
            throw e;
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPatch_OutOfBoundsStartPosition()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new StringLink(13, 13, 13);
        try {
            StringIncrementor.patch(a, r);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("The start position should not be out of bounds.", e.getMessage(), "Start position is out of bounds.");
            throw e;
        }
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testPatch_OutOfBoundsEndPosition()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new StringLink(0, 13, 13);
        try {
            StringIncrementor.patch(a, r);
        } catch (IndexOutOfBoundsException e) {
            assertEquals("The end position should not be out of bounds.", e.getMessage(), "End position is out of bounds.");
            throw e;
        }
    }
}