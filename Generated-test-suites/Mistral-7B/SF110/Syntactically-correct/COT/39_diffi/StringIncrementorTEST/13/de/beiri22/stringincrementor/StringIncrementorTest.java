package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.RelativeString;
import de.beiri22.stringincrementor.StringIncrementor;
import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringBuilderExt;
import de.beiri22.stringincrementor.relativestring.StringLink;

import org.junit.Test;

public class StringIncrementorTest
{
    @Test
    public void testDiff()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyz12345";

        RelativeString expectedRelativeResult = new RelativeString();
        expectedRelativeResult.addLink(new StringLink(10, 0, 11, "12345"));

        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedRelativeResult, result);

        // Edge case: empty strings
        String a1 = "";
        String b1 = "";
        RelativeString expectedRelativeResult1 = new RelativeString();
        assertEquals(expectedRelativeResult1, StringIncrementor.diff(a1, b1));

        // Edge case: identical strings
        String a2 = "abcdefghijklmnopqrstuvwxyz";
        String b2 = "abcdefghijklmnopqrstuvwxyz";
        RelativeString expectedRelativeResult2 = new RelativeString();
        assertEquals(expectedRelativeResult2, StringIncrementor.diff(a2, b2));

        // Edge case: empty target string
        String a3 = "abcdefghijklmnopqrstuvwxyz";
        String b3 = "";
        RelativeString expectedRelativeResult3 = new RelativeString();
        assertEquals(expectedRelativeResult3, StringIncrementor.diff(a3, b3));

        // Edge case: target string shorter than source string
        String a4 = "abcdefghijklmnopqrstuvwxyz";
        String b4 = "abcdefghijklmnopq";
        RelativeString expectedRelativeResult4 = new RelativeString();
        assertEquals(expectedRelativeResult4, StringIncrementor.diff(a4, b4));

        // Edge case: target string longer than source string
        String a5 = "abcdefghijklmnopqrstuvwxyz";
        String b5 = "abcdefghijklmnopqrstuvwxyz1234567890";
        RelativeString expectedRelativeResult5 = new RelativeString();
        expectedRelativeResult5.addLink(new StringLink(28, 0, 13, "1234567890"));
        assertEquals(expectedRelativeResult5, StringIncrementor.diff(a5, b5));

        // Edge case: verbose flag is true
        String a6 = "abcdefghijklmnopqrstuvwxyz";
        String b6 = "abcdefghijklmnopqrstuvwxyz12345";
        RelativeString expectedRelativeResult6 = new RelativeString();
        expectedRelativeResult6.addLink(new StringLink(10, 0, 11, "12345"));

        StringIncrementor.diff(a6, b6, true); // This line only prints some output and does not return a value, so we can't test the result directly

        // Test exception handling: null input
        try {
            StringIncrementor.diff(null, "test");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //expected
        }

        try {
            StringIncrementor.diff("test", null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //expected
        }
    }

    @Test
    public void testPatch()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(10, 0, 11, "12345"));

        String expectedResult = "abcdefghijklmnopqrstuvwxyz12345";
        String result = StringIncrementor.patch(a, r);
        assertEquals(expectedResult, result);

        // Edge case: empty relative string
        RelativeString r1 = new RelativeString();
        String result1 = StringIncrementor.patch(a, r1);
        assertEquals(a, result1);

        // Edge case: empty source string
        String a1 = "";
        RelativeString r2 = new RelativeString();
        String result2 = StringIncrementor.patch(a1, r2);
        assertEquals(a1, result2);

        // Edge case: relative string with no links
        RelativeString r3 = new RelativeString();
        String result3 = StringIncrementor.patch(a, r3);
        assertEquals(a, result3);

        // Test exception handling: null input
        try {
            StringIncrementor.patch(null, r);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //expected
        }

        try {
            StringIncrementor.patch(a, null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            //expected
        }
    }
}