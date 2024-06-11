package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;

public class PosixParserTest
{
    @Test
    public void testSimpleOptionNoArgument()
    {
        String[] args = {"java", "-o", "out.txt"};
        Options options = new Options();
        options.addOption("o", false, "output file");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"o", "out.txt"}, parsedArgs);
    }

    @Test
    public void testSimpleOptionWithArgument()
    {
        String[] args = {"java", "-o", "out.txt", "input.txt"};
        Options options = new Options();
        options.addOption("o", true, "output file");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"o", "out.txt", "input.txt"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testMissingOption()
    {
        String[] args = {"java", "-x", "input.txt"};
        Options options = new Options();
        options.addOption("o", true, "output file");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        fail("Expected ParseException when missing option '-x' is encountered");
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption()
    {
        String[] args = {"java", "-z", "input.txt"};
        Options options = new Options();
        options.addOption("o", true, "output file");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        fail("Expected ParseException when invalid option '-z' is encountered");
    }

    @Test
    public void testLongOption()
    {
        String[] args = {"java", "--output", "out.txt", "input.txt"};
        Options options = new Options();
        options.addOption("output", true, "output file");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"output", "out.txt", "input.txt"}, parsedArgs);
    }

    @Test
    public void testLongOptionWithArgument()
    {
        String[] args = {"java", "--output", "out.txt", "--input", "input.txt"};
        Options options = new Options();
        options.addOption("output", true, "output file");
        options.addOption("input", true, "input file");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"output", "out.txt", "input", "input.txt"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testMissingLongOption()
    {
        String[] args = {"java", "--foo", "input.txt"};
        Options options = new Options();
        options.addOption("output", true, "output file");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        fail("Expected ParseException when missing long option '--foo' is encountered");
    }

    @Test(expected = ParseException.class)
    public void testInvalidLongOption()
    {
        String[] args = {"java", "--zoo", "input.txt"};
        Options options = new Options();
        options.addOption("output", true, "output file");

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        fail("Expected ParseException when invalid long option '--zoo' is encountered");
    }

    @Test
    public void testBurstedOption()
    {
        String[] args = {"java", "-foo", "bar", "--baz", "qux"};
        Options options = new Options();
        options.addOption("foo", true, "option foo");
        options.addOption("baz", true, "option baz");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"foo", "bar", "baz", "qux"}, parsedArgs);
    }

    @Test
    public void testBurstedOptionWithArgument()
    {
        String[] args = {"java", "-foo", "bar", "--baz", "qux", "quux"};
        Options options = new Options();
        options.addOption("foo", true, "option foo");
        options.addOption("baz", true, "option baz");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"foo", "bar", "baz", "qux", "quux"}, parsedArgs);
    }

    @Test
    public void testMixedOptions()
    {
        String[] args = {"java", "--output", "output.txt", "-foo", "bar", "--baz", "qux"};
        Options options = new Options();
        options.addOption("output", true, "output file");
        options.addOption("foo", true, "option foo");
        options.addOption("baz", true, "option baz");

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[]{"output", "output.txt", "foo", "bar", "baz", "qux"}, parsedArgs);
    }

    @Test
    public void testEmptyOptions()
    {
        String[] args = {"java"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.flatten(options, args, false);

        assertArrayEquals(new String[0], parsedArgs);
    }
}