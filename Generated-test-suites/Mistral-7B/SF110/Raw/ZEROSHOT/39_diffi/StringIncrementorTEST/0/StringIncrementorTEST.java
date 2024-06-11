package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import org.junit.Test;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

public class StringIncrementorTest
{
    @Test
    public void testDiff_whenStringsAreIdentical_thenRelativeStringIsEmpty()
    {
        final String a = "IdenticalStrings";
        final RelativeString expected = new RelativeString();
        final RelativeString result = StringIncrementor.diff(a, a, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_whenStringsDifferBySingleCharacter_thenRelativeStringContainSingleLink()
    {
        final String a = "DifferentStrings";
        final String b = "DifferByASingleCharacter";
        final RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(6, 6, 1));
        final RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_whenStringsAreLongerThan13Characters_thenCorrectlyFindDifferences()
    {
        final String a = "LongString123456789012345678901234567890";
        final String b = "LongString123456789012345678901234567891";
        final RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(25, 25, 1));
        final RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_whenStringsAreLongerThan13Characters_verboseModeEnabled_thenCorrectlyFindDifferencesAndPrintToConsole()
    {
        final String a = "LongString123456789012345678901234567890";
        final String b = "LongString123456789012345678901234567891";
        final RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(25, 25, 1));
        final RelativeString result = StringIncrementor.diff(a, b, true);
        assertEquals(expected, result);
        // Test the console output
        final String expectedConsoleOutput = "@ 66.67%\nAdding Link: StringLink[posOrig=25, len=1, posNew=26]\n";
        String consoleOutput = "";
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final PrintStream originalOut = System.out;
        System.setOut(new PrintStream(outputStream));
        StringIncrementor.diff(a, b, true);
        System.setOut(originalOut);
        consoleOutput = outputStream.toString();
        assertEquals(expectedConsoleOutput, consoleOutput);
    }

    @Test
    public void testPatch_whenRelativeStringIsEmpty_thenOriginalStringIsReturned()
    {
        final String a = "OriginalString";
        final RelativeString r = new RelativeString();
        final String expected = a;
        final String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_whenRelativeStringContainsSingleLink_thenCorrectlyPatchedStringIsReturned()
    {
        final String a = "OriginalString";
        final RelativeString r = new RelativeString();
        r.addLink(new StringLink(6, 6, 1));
        final String expected = "OriginalString....PatchedCharacter";
        final String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_whenRelativeStringContainsMultipleLinks_thenCorrectlyPatchedStringIsReturned()
    {
        final String a = "OriginalString...";
        final RelativeString r = new RelativeString();
        r.addLink(new StringLink(6, 6, 1));
        r.addLink(new StringLink(13, 13, 1));
        final String expected = "OriginalString....PatchedCharacter1...PatchedCharacter2";
        final String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);
    }
}