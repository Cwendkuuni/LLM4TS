package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Test cases for the PosixParser class.
 */
public class PosixParserTest {

    /**
     * Test flattening of basic command line arguments.
     */
    @Test
    public void testBasicFlattening() {
        String[] args = {"-a", "-b", "--optionC=valueC", "--optionD", "--optionE"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("-b");
        expected.add("--optionC");
        expected.add("valueC");
        expected.add("--optionD");
        expected.add("--optionE");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flattening with stopAtNonOption=true.
     */
    @Test
    public void testStopAtNonOption() {
        String[] args = {"-a", "--nonOption", "value", "-b", "--optionC=valueC"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, true);
        List<String> expected = new ArrayList<>();
        expected.add("-a");
        expected.add("--nonOption");
        expected.add("value");
        expected.add("--optionC");
        expected.add("valueC");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flattening with a long option.
     */
    @Test
    public void testLongOption() {
        String[] args = {"--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("longOption", true), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("--longOption");
        expected.add("value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flattening with an option that accepts an argument value.
     */
    @Test
    public void testOptionWithValue() {
        String[] args = {"-c", "file.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("c", true), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("-c");
        expected.add("file.txt");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flattening with an option that accepts multiple values.
     */
    @Test
    public void testOptionWithMultipleValues() {
        String[] args = {"-d", "file1.txt", "file2.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("d", true, 1), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("-d");
        expected.add("file1.txt");
        expected.add("file2.txt");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test flattening with an option that accepts multiple values and stopAtNonOption=true.
     */
    @Test
    public void testOptionWithMultipleValuesAndStopAtNonOption() {
        String[] args = {"-d", "file1.txt", "--nonOption", "value", "file3.txt"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("d", true, 1), args, true);
        List<String> expected = new ArrayList<>();
        expected.add("-d");
        expected.add("file1.txt");
        expected.add("--nonOption");
        expected.add("value");
        expected.add("file3.txt");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test handling of "--" as a special token.
     */
    @Test
    public void testDoubleDash() {
        String[] args = {"--", "--optionC=valueC"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("--optionC");
        expected.add("valueC");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test handling of "--" followed by an option.
     */
    @Test
    public void testDoubleDashWithOption() {
        String[] args = {"--", "--optionC=valueC"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("optionC", true), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("--optionC");
        expected.add("valueC");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test handling of "--" followed by an option and a value.
     */
    @Test
    public void testDoubleDashWithOptionAndValue() {
        String[] args = {"--", "--optionC=valueC"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options().addOption("optionC", true, "value"), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("--optionC");
        expected.add("valueC");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    /**
     * Test handling of "--" followed by non-option tokens.
     */
    @Test
    public void testDoubleDashWithArguments() {
        String[] args = {"--", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(new Options(), args, false);
        List<String> expected = new ArrayList<>();
        expected.add("--");
        expected.add("arg1");
        expected.add("arg2");
        Assert.assertArrayEquals(expected.toArray(), result);
    }
}