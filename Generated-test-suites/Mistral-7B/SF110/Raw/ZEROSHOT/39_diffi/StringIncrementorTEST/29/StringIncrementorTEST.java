package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest
{
    @Test
    public void testDiff()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        String b = "The quick red fox jumps over the lazy dog";

        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(7, 0, 3)); // red
        expected.addLink(new StringLink(12, 7, 1)); // quick -> red
        expected.setAbsolute("the 1 red fox jumps over the lazy dog");

        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(expected.toString(), result.toString());

        // test verbose output
        String output = new StringBuffer();
        PrintStream origOut = System.out;
        System.setOut(new PrintStream(output));
        RelativeString resultVerbose = StringIncrementor.diff(a, b, true);
        System.setOut(origOut);
        String expectedVerbose = "Creating index...\nSearching links...\n@ 0%\nLink: [7, 0, 3]\nAdding 3 absolute Bytes from pos#0\nthe 1 red fox jumps over the lazy dog @ 0%\n";
        assertEquals(expectedVerbose, output.toString());
    }

    @Test
    public void testDiffFile() throws IOException
    {
        String a = new String(new byte[0], StandardCharsets.UTF_8);
        String b = "The quick brown fox jumps over the lazy dog";

        RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(7, 0, 3)); // red
        expected.addLink(new StringLink(12, 7, 1)); // quick -> red
        expected.setAbsolute("the 1 red fox jumps over the lazy dog");

        RelativeString result = StringIncrementor.diff(a, b, false);
        assertEquals(expected.toString(), result.toString());

        // test verbose output
        String output = new StringBuffer();
        PrintStream origOut = System.out;
        System.setOut(new PrintStream(output));
        RelativeString resultVerbose = StringIncrementor.diff(a, b, true);
        System.setOut(origOut);
        String expectedVerbose = "Creating index...\nSearching links...\n@ 0%\nLink: [7, 0, 3]\nAdding 3 absolute Bytes from pos#0\nthe 1 red fox jumps over the lazy dog @ 0%\n";
        assertEquals(expectedVerbose, output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffNull()
    {
        StringIncrementor.diff(null, "The quick brown fox jumps over the lazy dog");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDiffEmpty()
    {
        StringIncrementor.diff("", "The quick brown fox jumps over the lazy dog");
    }

    @Test
    public void testPatch()
    {
        String a = "The quick brown fox jumps over the lazy dog";
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(7, 0, 3)); // red
        r.addLink(new StringLink(12, 7, 1)); // quick -> red
        r.setAbsolute("the 1 red fox jumps over the lazy dog");

        String expected = "The quick red fox jumps over the lazy dog";
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);

        // test verbose output
        String output = new StringBuffer();
        PrintStream origOut = System.out;
        System.setOut(new PrintStream(output));
        String resultVerbose = StringIncrementor.patch(a, r, true);
        System.setOut(origOut);
        String expectedVerbose = "Adding 3 absolute Bytes from pos#0: the 1 red fox jumps over the lazy dog\nThe quick red fox jumps over the lazy dog";
        assertEquals(expectedVerbose, output.toString());
    }

    @Test
    public void testPatchFile() throws IOException
    {
        String a = new String(new byte[0], StandardCharsets.UTF_8);
        RelativeString r = new RelativeString();
        r.addLink(new StringLink(7, 0, 3)); // red
        r.addLink(new StringLink(12, 7, 1)); // quick -> red
        r.setAbsolute("the 1 red fox jumps over the lazy dog");

        String expected = "The quick red fox jumps over the lazy dog";
        String result = StringIncrementor.patch(a, r, false);
        assertEquals(expected, result);

        // test verbose output
        String output = new StringBuffer();
        PrintStream origOut = System.out;
        System.setOut(new PrintStream(output));
        String resultVerbose = StringIncrementor.patch(a, r, true);
        System.setOut(origOut);
        String expectedVerbose = "Adding 3 absolute Bytes from pos#0: the 1 red fox jumps over the lazy dog\nThe quick red fox jumps over the lazy dog";
        assertEquals(expectedVerbose, output.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchNull()
    {
        StringIncrementor.patch(null, new RelativeString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testPatchEmpty()
    {
        StringIncrementor.patch("", new RelativeString());
    }
}