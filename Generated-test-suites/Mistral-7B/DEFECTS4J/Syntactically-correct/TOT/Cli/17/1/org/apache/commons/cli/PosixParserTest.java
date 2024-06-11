package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test flattening a simple command line with no options.
     */
    @Test
    public void testSimpleCommandLine() {
        String[] arguments = new String[]{"java", "MyClass"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "MyClass"}, parsedArguments);
    }

    /**
     * Test flattening a command line with a single option.
     */
    @Test
    public void testSingleOption() {
        String[] arguments = new String[]{"java", "-a", "value"};
        Options options = new Options().addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "--Option-A", "value"}, parsedArguments);
    }

    /**
     * Test flattening a command line with multiple options.
     */
    @Test
    public void testMultipleOptions() {
        String[] arguments = new String[]{"java", "-a", "value1", "-b", "value2"};
        Options options = new Options()
                .addOption("a", true, "Option A")
                .addOption("b", true, "Option B");
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "--Option-A", "value1", "--Option-B", "value2"}, parsedArguments);
    }

    /**
     * Test flattening a command line with an option with an argument.
     */
    @Test
    public void testOptionWithArgument() {
        String[] arguments = new String[]{"java", "-d", "/path/to/dir"};
        Options options = new Options().addOption("d", true, "Directory");
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "--Directory", "/path/to/dir"}, parsedArguments);
    }

    /**
     * Test flattening a command line with multiple options with arguments.
     */
    @Test
    public void testMultipleOptionsWithArguments() {
        String[] arguments = new String[]{"java", "-a", "value1", "-d", "/path/to/dir"};
        Options options = new Options()
                .addOption("a", true, "Option A")
                .addOption("d", true, "Directory");
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "--Option-A", "value1", "--Directory", "/path/to/dir"}, parsedArguments);
    }

    /**
     * Test flattening a command line with an option that accepts multiple values.
     */
    @Test
    public void testOptionWithMultipleValues() {
        String[] arguments = new String[]{"java", "-e", "value1", "value2"};
        Options options = new Options().addOption("e", true, "Extra", 1);
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "--Extra", "value1", "--Extra", "value2"}, parsedArguments);
    }

    /**
     * Test flattening a command line with a long option.
     */
    @Test
    public void testLongOption() {
        String[] arguments = new String[]{"java", "--long-option-value", "value"};
        Options options = new Options().addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "--Long-option--option-value", "value"}, parsedArguments);
    }

    /**
     * Test flattening a command line with a short and a long option.
     */
    @Test
    public void testShortAndLongOptions() {
        String[] arguments = new String[]{"java", "-a", "value1", "--long-option-value", "value2"};
        Options options = new Options()
                .addOption("a", true, "Option A")
                .addOption("longOption", true, "Long option");
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "--Option-A", "value1", "--Long-option--option-value", "value2"}, parsedArguments);
    }

    /**
     * Test flattening a command line with an option that does not exist.
     */
    @Test(expected = ParseException.class)
    public void testUnknownOption() {
        String[] arguments = new String[]{"java", "-x", "value"};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test stopping at a non-option.
     */
    @Test
    public void testStopAtNonOption() {
        String[] arguments = new String[]{"java", "-a", "value1", "input.txt", "-o", "output.txt"};
        Options options = new Options().addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"java", "-a", "value1"}, parsedArguments);
    }

    /**
     * Test handling of "--" to indicate the end of options.
     */
    @Test
    public void testEndOfOptions() {
        String[] arguments = new String[]{"java", "-a", "value1", "--", "input.txt", "-o", "output.txt"};
        Options options = new Options().addOption("a", true, "Option A");
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"java", "--Option-A", "value1", "input.txt", "-o", "output.txt"}, parsedArguments);
    }
}