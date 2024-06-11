package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import de.beiri22.stringincrementor.*;
import de.beiri22.stringincrementor.helper.*;
import de.beiri22.stringincrementor.relativestring.*;

import org.junit.Before;
import org.junit.Test;

import java.io.*;

public class StringIncrementorTest
{
    private StringIncrementor stringIncrementor;
    private Helper helper;

    @Before
    public void setUp()
    {
        stringIncrementor = new StringIncrementor();
        helper = new Helper();
    }

    @Test
    public void testDiff_sameStrings_returnsEmptyRelativeString()
    {
        final String a = "Hello World";
        final String b = "Hello World";
        final RelativeString expected = new RelativeString();

        final RelativeString result = stringIncrementor.diff(a, b, false);

        assertEquals(expected, result);
    }

    @Test
    public void testDiff_identicalStrings_verbose_printsCorrectly()
    {
        final String a = "Hello World";
        final String b = "Hello World";
        final boolean verbose = true;

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        final String result = stringIncrementor.diff(a, b, verbose).getAbsolute();

        final String expected = "";
        assertEquals(expected, result);

        // Check that no output was printed when verbose is true
        assertTrue(outputStream.toString().isEmpty());
    }

    @Test
    public void testDiff_identicalStrings_nonVerbose_doesNotPrintAnything()
    {
        final String a = "Hello World";
        final String b = "Hello World";

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        stringIncrementor.diff(a, b, false);

        // Check that no output was printed
        assertTrue(outputStream.toString().isEmpty());
    }

    @Test
    public void testDiff_oneCharacterDifference_verbose_printsCorrectly()
    {
        final String a = "Hello World";
        final String b = "Hello World";
        final boolean verbose = true;

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        final RelativeString result = stringIncrementor.diff(a, b, verbose);

        final StringLink expectedLink = new StringLink(5, 5, 1);
        final RelativeString expected = new RelativeString();
        expected.addLink(expectedLink);

        assertEquals(expected, result);

        // Check that the correct output was printed
        final String expectedOutput = "@ 50.0%: LengthDiff(1)\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testDiff_multipleCharacterDifferences_verbose_printsCorrectly()
    {
        final String a = "Hello World";
        final String b = "Hello World! Hi There";
        final boolean verbose = true;

        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));

        final RelativeString result = stringIncrementor.diff(a, b, verbose);

        final StringLink link1 = new StringLink(5, 5, 1);
        final StringLink link2 = new StringLink(12, 12, 2);
        final RelativeString expected = new RelativeString();
        expected.addLink(link1);
        expected.addLink(link2);

        assertEquals(expected, result);

        // Check that the correct output was printed
        final String expectedOutput = "@ 50.0%: LengthDiff(1)\n" +
                "@ 66.7%: Delete(2): Hi There\n";
        assertEquals(expectedOutput, outputStream.toString());
    }

    @Test
    public void testPatch_emptyRelativeString_returnsOriginalString()
    {
        final String a = "Hello World";
        final RelativeString r = new RelativeString();

        final String result = stringIncrementor.patch(a, r, false);

        assertEquals("Hello World", result);
    }

    @Test
    public void testPatch_singleCharacterInsertion_returnsCorrectlyPatchedString()
    {
        final String a = "Hello World";
        final String b = "Hello World";
        final RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));

        final String result = stringIncrementor.patch(a, r, false);

        assertEquals("Hello World", result);
    }

    @Test
    public void testPatch_multipleCharacterDeletionsAndInsertions_returnsCorrectlyPatchedString()
    {
        final String a = "Hello World";
        final String b = "Hello World! Hi There";
        final RelativeString r = new RelativeString();
        r.addLink(new StringLink(5, 5, 1));
        r.addLink(new StringLink(12, 12, 2));

        final String result = stringIncrementor.patch(a, r, false);

        assertEquals("Hello World! Hi There", result);
    }
}