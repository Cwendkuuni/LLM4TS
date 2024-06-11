package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Test;

public class StringIncrementorTest
{
    // Test case for StringIncrementor.diff() with basic input
    @Test
    public void testDiffBasic()
    {
        String a = "abcd";
        String b = "abcdabcd";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 1));
        expectedResult.addLink(new StringLink(4, 5, 1));
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    // Test case for StringIncrementor.diff() with verbose flag
    @Test
    public void testDiffVerbose()
    {
        String a = "abcd";
        String b = "abcdabcd";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 1));
        expectedResult.addLink(new StringLink(4, 5, 1));

        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String x) {
                System.out.println("DiffTest: " + x);
            }
        };

        RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(expectedResult, result);
        // Check the printed messages
        assertTrue(System.out.toString().contains("Creating index..."));
        assertTrue(System.out.toString().contains("Searching links..."));
        assertTrue(System.out.toString().contains("@ 0%"));
        assertTrue(System.out.toString().contains("@ 50%"));
        assertTrue(System.out.toString().contains("Link: @ 0 [ 0, 1 ]"));
        assertTrue(System.out.toString().contains("Link: @ 50 [ 4, 5 ]"));
    }

    // Test case for StringIncrementor.diff() with empty input
    @Test
    public void testDiffEmpty()
    {
        String a = "";
        String b = "";
        RelativeString expectedResult = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    // Test case for StringIncrementor.diff() with input of different lengths
    @Test
    public void testDiffDifferentLengths()
    {
        String a = "abcd";
        String b = "efghijklmnop";
        RelativeString expectedResult = new RelativeString();
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
        assertTrue(result.getAbsolute().isEmpty());
    }

    // Test case for StringIncrementor.diff() with overlapping substrings
    @Test
    public void testDiffOverlapping()
    {
        String a = "abcdefghijklmnopqrstuvwxyz";
        String b = "abcdefghijklmnopqrstuvwxyzabcd";
        RelativeString expectedResult = new RelativeString();
        expectedResult.addLink(new StringLink(0, 0, 6));
        expectedResult.addLink(new StringLink(7, 6, 1));
        expectedResult.addLink(new StringLink(14, 11, 1));
        RelativeString result = StringIncrementor.diff(a, b);
        assertEquals(expectedResult, result);
    }

    // Test case for StringIncrementor.diff() with exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testDiffNullInputs()
    {
        String a = null;
        String b = "test";
        StringIncrementor.diff(a, b);
    }

    // Test case for StringIncrementor.patch() with basic input
    @Test
    public void testPatchBasic()
    {
        String a = "abcdabcd";
        String patchString = "1234";
        String expectedResult = "abcd1234abcd";
        String result = StringIncrementor.patch(a, new RelativeString(patchString));
        assertEquals(expectedResult, result);
    }

    // Test case for StringIncrementor.patch() with verbose flag
    @Test
    public void testPatchVerbose()
    {
        String a = "abcdabcd";
        String patchString = "1234";
        String expectedResult = "abcd1234abcd";

        System.out = new PrintStream(new ByteArrayOutputStream()) {
            @Override
            public void println(String x) {
                System.out.println("PatchTest: " + x);
            }
        };

        String result = StringIncrementor.patch(a, new RelativeString(patchString), true);
        assertEquals(expectedResult, result);
        // Check the printed messages
        assertTrue(System.out.toString().contains("Adding Link: @ 0 [ 0, 5 ]"));
        assertTrue(System.out.toString().contains("Adding Link: @ 12 [ 12, 16 ]"));
    }

    // Test case for StringIncrementor.patch() with empty input
    @Test
    public void testPatchEmpty()
    {
        String a = "";
        String patchString = "";
        String expectedResult = "";
        String result = StringIncrementor.patch(a, new RelativeString(patchString));
        assertEquals(expectedResult, result);
    }

    // Test case for StringIncrementor.patch() with exception handling
    @Test(expected = IllegalArgumentException.class)
    public void testPatchNullInputs()
    {
        String a = "test";
        String patchString = null;
        StringIncrementor.patch(a, new RelativeString(patchString));
    }
}