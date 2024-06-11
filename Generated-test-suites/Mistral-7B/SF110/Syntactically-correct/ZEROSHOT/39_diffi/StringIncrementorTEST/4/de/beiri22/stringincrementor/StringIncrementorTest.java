package de.beiri22.stringincrementor.test;

import static org.junit.Assert.*;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

import de.beiri22.stringincrementor.helper.IndexedString;
import de.beiri22.stringincrementor.helper.StringBuilderExtensions;
import de.beiri22.stringincrementor.relativestring.RelativeString;
import de.beiri22.stringincrementor.relativestring.StringLink;
import de.beiri22.stringincrementor.StringIncrementor;

public class StringIncrementorTest
{
    @Test
    public void testDiff_emptyStrings()
    {
        final String inputA = "";
        final String inputB = "";
        final RelativeString expected = new RelativeString();
        final RelativeString result = StringIncrementor.diff(inputA, inputB, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_equalStrings()
    {
        final String inputA = "test";
        final String inputB = "test";
        final RelativeString expected = new RelativeString();
        final RelativeString result = StringIncrementor.diff(inputA, inputB, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_simpleDifference()
    {
        final String inputA = "test123";
        final String inputB = "test456";
        final RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(3, 3, 3));
        final RelativeString result = StringIncrementor.diff(inputA, inputB, false);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_verbose()
    {
        final String inputA = "test123";
        final String inputB = "test456";
        final RelativeString expected = new RelativeString();
        expected.addLink(new StringLink(3, 3, 3));
        System.out.print("Expected: ");
        System.out.println(expected);
        final RelativeString result = StringIncrementor.diff(inputA, inputB, true);
        System.out.print("Result: ");
        System.out.println(result);
        assertEquals(expected, result);
    }

    @Test
    public void testDiff_largeStrings() throws FileNotFoundException
    {
        final String inputA = new String(new File("inputA.txt").getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8.name());
        final String inputB = new String(new File("inputB.txt").getBytes(StandardCharsets.UTF_8), StandardCharsets.UTF_8.name());
        final RelativeString expected = new RelativeString();
        final RelativeString result = StringIncrementor.diff(inputA, inputB, false);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_emptyStrings()
    {
        final String input = "";
        final RelativeString relString = new RelativeString();
        final String expected = "";
        final String result = StringIncrementor.patch(input, relString, false);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_simpleDifference()
    {
        final String input = "test123test123test123";
        final RelativeString relString = new RelativeString();
        relString.addLink(new StringLink(6, 6, 3));
        final String expected = "test123test456test123";
        final String result = StringIncrementor.patch(input, relString, false);
        assertEquals(expected, result);
    }

    @Test
    public void testPatch_verbose()
    {
        final String input = "test123test123test123";
        final RelativeString relString = new RelativeString();
        relString.addLink(new StringLink(6, 6, 3));
        final StringBuilder expected = new StringBuilder("test123test");
        final StringBuilder result = new StringBuilder();
        StringIncrementor.patch(input, relString, true, expected);
        assertEquals(expected.toString(), result.toString());
    }
}