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
        // Test case 1: Basic functionality - empty strings
        String a = "";
        String b = "";
        RelativeString expected = new RelativeString();
        RelativeString actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);

        // Test case 2: Basic functionality - identical strings
        a = "hello";
        b = "hello";
        expected = new RelativeString();
        actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);

        // Test case 3: Basic functionality - different strings with common substring
        a = "abcdefghijklmno";
        b = "123abcdefghijklmno456";
        expected = new RelativeString("5-11(7)", "5-11(7)");
        actual = StringIncrementor.diff(a, b);
        assertEquals(expected, actual);

        // Test case 4: Edge case - empty string as second argument
        a = "hello";
        b = "";
        expected = new RelativeString("0-5(6)", "0-5(6)", true);
        actual = StringIncrementor.diff(a, b, true);
        assertEquals(expected, actual);

        // Test case 5: Edge case - negative index
        a = "abcdefghijk";
        b = "lkjabcdfghiji";
        expected = new RelativeString("0-11(12)", "0-11(12)", true);
        actual = StringIncrementor.diff(a, b, true);
        assertEquals(expected, actual);

        // Test case 6: Exception handling - null strings
        String nullA = null;
        try {
            StringIncrementor.diff(nullA, b);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        String nullB = null;
        try {
            StringIncrementor.diff(a, nullB);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // expected
        }

        // Test case 7: Exception handling - strings with different lengths
        a = "abc";
        b = "def";
        try {
            StringIncrementor.diff(a, b);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // expected
        }

        // Test case 8: Test patch method with simple test case
        String aPatch = "abcdefghijklmno";
        RelativeString r = new RelativeString("5-11(7)", "5-11(7)");
        String expectedPatch = "1234567890abcdefghijklmno";
        String actualPatch = StringIncrementor.patch(aPatch, r);
        assertEquals(expectedPatch, actualPatch);

        // Test case 9: Test patch method with edge cases
        String aPatchEdge = "abcdefghijk";
        RelativeString rEdge = new RelativeString("0-11(12)", "0-11(12)", true);
        String expectedPatchEdge = "lkjabcdfghijk1234567890";
        String actualPatchEdge = StringIncrementor.patch(aPatchEdge, rEdge);
        assertEquals(expectedPatchEdge, actualPatchEdge);
    }

    @Test
    public void testDiffWithVerbose()
    {
        // Test case: Test diff method with verbose flag
        String a = "abcdefghijklmno";
        String b = "123abcdefghijklmno456";

        System.out.println("Testing diff with verbose flag...");
        RelativeString expected = new RelativeString("5-11(7)", "5-11(7)");
        RelativeString actual = StringIncrementor.diff(a, b, true);
        assertEquals(expected, actual);

        System.out.println("Expected: " + expected);
        System.out.println("Actual: " + actual);
    }
}