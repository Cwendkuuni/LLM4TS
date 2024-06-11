package org.apache.commons.cli.test;

import org.apache.commons.cli.PosixParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test flattening with no options.
     */
    @Test
    public void testFlattenNoOptions() {
        String[] args = new String[]{"MyProgram", "--foo=bar"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "foo", "bar"}, flattenedArgs);
    }

    /**
     * Test flattening with single option.
     */
    @Test
    public void testFlattenSingleOption() {
        String[] args = new String[]{"MyProgram", "-f"};
        Options options = new Options();
        options.addRequiredOption("f", "file", true, "The file to process");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f"}, flattenedArgs);

        // test with value
        args = new String[]{"MyProgram", "-f", "file.txt"};
        options.addRequiredOption("f", "file", true, "The file to process");

        parser = new PosixParser();
        flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f", "file.txt"}, flattenedArgs);
    }

    /**
     * Test flattening with multiple options.
     */
    @Test
    public void testFlattenMultipleOptions() {
        String[] args = new String[]{"MyProgram", "-f", "-o", "out.txt"};
        Options options = new Options();
        options.addRequiredOption("f", "file", true, "The input file");
        options.addRequiredOption("o", "output", true, "The output file");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f", "file.txt", "--", "o", "out.txt"}, flattenedArgs);
    }

    /**
     * Test flattening with long option.
     */
    @Test
    public void testFlattenLongOption() {
        String[] args = new String[]{"MyProgram", "--long-option"};
        Options options = new Options();
        options.addOption("longOption", false, "A long option");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "longOption"}, flattenedArgs);
    }

    /**
     * Test flattening with multiple long options.
     */
    @Test
    public void testFlattenMultipleLongOptions() {
        String[] args = new String[]{"MyProgram", "--longOption1", "--longOption2"};
        Options options = new Options();
        options.addOption("longOption1", false, "A long option 1");
        options.addOption("longOption2", false, "A long option 2");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "longOption1", "--", "longOption2"}, flattenedArgs);
    }

    /**
     * Test flattening with options that have arguments.
     */
    @Test
    public void testFlattenOptionsWithArguments() {
        String[] args = new String[]{"MyProgram", "-d", "dir", "--file", "file.txt"};
        Options options = new Options();
        options.addOption("d", "dir", true, "The directory");
        options.addOption("file", true, "A file");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "d", "dir", "--", "file", "file.txt"}, flattenedArgs);
    }

    /**
     * Test flattening with short and long options.
     */
    @Test
    public void testFlatteningWithShortAndLongOptions() {
        String[] args = new String[]{"MyProgram", "-f", "-o", "out.txt", "--long-option"};
        Options options = new Options();
        options.addRequiredOption("f", "file", true, "The input file");
        options.addRequiredOption("o", "output", true, "The output file");
        options.addOption("longOption", false, "A long option");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f", "file", "--", "o", "out.txt", "--longOption"}, flattenedArgs);
    }

    /**
     * Test flattening with non-options.
     */
    @Test(expected = ParseException.class)
    public void testFlattenWithNonOptions() {
        String[] args = new String[]{"MyProgram", "arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        parser.flatten(options, args, true);
    }

    /**
     * Test flattening with stopAtNonOption.
     */
    @Test
    public void testFlatteningWithStopAtNonOption() {
        String[] args = new String[]{"MyProgram", "arg1", "arg2"};
        Options options = new Options();
        PosixParser parser = new PosixParser();

        String[] flattenedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"MyProgram", "arg1", "arg2"}, flattenedArgs);
    }

    /**
     * Test bursting.
     */
    @Test
    public void testBursting() {
        String[] args = new String[]{"MyProgram", "-foobar"};
        Options options = new Options();
        options.addRequiredOption("f", "file", true, "The input file");
        options.addOption("o", "output", true, "The output file");
        options.addOption("ob", "backup", false, "Create a backup");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f", "foobar"}, flattenedArgs);

        // test with value
        args = new String[]{"MyProgram", "-foobar=backup.txt"};
        parser = new PosixParser();
        flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f", "foobar", "backup.txt"}, flattenedArgs);

        // test without -- as last character
        args = new String[]{"MyProgram", "-foobar=backup.txt", "arg"};
        parser = new PosixParser();
        flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f", "foobar", "backup.txt", "arg"}, flattenedArgs);

        // test with stopAtNonOption
        args = new String[]{"MyProgram", "-foobar=backup.txt", "arg"};
        parser = new PosixParser();
        flattenedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"MyProgram", "-foobar=backup.txt", "arg"}, flattenedArgs);
    }

    /**
     * Test eating the rest.
     */
    @Test
    public void testEatingTheRest() {
        String[] args = new String[]{"MyProgram", "-f", "-o", "out.txt", "arg1", "arg2"};
        Options options = new Options();
        options.addRequiredOption("f", "file", true, "The input file");
        options.addRequiredOption("o", "output", true, "The output file");

        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f", "file", "--", "o", "out.txt"}, flattenedArgs);

        parser = new PosixParser();
        flattenedArgs = parser.flatten(options, args, true);

        Assert.assertArrayEquals(new String[]{"MyProgram", "--", "f", "file", "--", "o", "out.txt", "arg1", "arg2"}, flattenedArgs);
    }
}