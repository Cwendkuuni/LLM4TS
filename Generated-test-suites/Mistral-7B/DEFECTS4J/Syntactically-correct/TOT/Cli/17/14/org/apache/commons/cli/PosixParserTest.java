package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    /**
     * Test flattening when all arguments are valid options.
     */
    @Test
    public void testFlatteningValidOptions() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with argument");
        options.addOption("c", 3, "Option C with argument");
        options.addOption("d", 4, "Option D with argument");
        String[] arguments = {"-a", "--b", "valueB", "-c", "valueC", "-d", "valueD"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals(arguments.length, result.length);
        for (int i = 0; i < arguments.length; i++) {
            assertEquals(arguments[i], result[i]);
        }
    }

    /**
     * Test flattening when encountering a non-option.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"arg1", "-a", "arg3", "--", "arg4"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, true);
    }

    /**
     * Test flattening when encountering a double hyphen.
     */
    @Test
    public void testFlatteningDoubleHyphen() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"--", "arg1", "-a", "arg2", "--"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"--", "-a", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

    /**
     * Test flattening when encountering a single hyphen.
     */
    @Test
    public void testFlatteningSingleHyphen() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        String[] arguments = {"-", "-a"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        String[] expected = {"-", "-a"};
        assertArrayEquals(expected, result);
    }

    /**
     * Test flattening when encountering a long option.
     */
    @Test
    public void testFlatteningLongOption() {
        Options options = new Options();
        options.addOption("longOption", true, "Long option with argument");
        String[] arguments = {"--longOption", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--longOption", result[0]);
        assertEquals("value", result[1]);
    }

    /**
     * Test flattening when encountering a long option with an argument.
     */
    @Test
    public void testFlatteningLongOptionWithArgument() {
        Options options = new Options();
        options.addOption("longOption", 1, "Long option with argument");
        String[] arguments = {"--longOption", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--longOption", result[0]);
        assertEquals("value", result[1]);
    }

    /**
     * Test flattening when encountering a long option with multiple arguments.
     */
    @Test
    public void testFlatteningLongOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("longOption", 3, "Long option with multiple arguments");
        String[] arguments = {"--longOption", "value1", "value2", "value3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("--longOption", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
        assertEquals("value3", result[3]);
    }

    /**
     * Test flattening when encountering an option with no argument.
     */
    @Test
    public void testFlatteningOptionWithoutArgument() {
        Options options = new Options();
        options.addOption("option", false, "Option without argument");
        String[] arguments = {"-option", "--", "arg1", "--longOption", "value"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-option", result[0]);
        assertEquals("-", result[1]);
        assertEquals("arg1", result[2]);
        assertEquals("--longOption", result[3]);
        assertEquals("value", result[4]);
    }

    /**
     * Test flattening when encountering an option with multiple arguments.
     */
    @Test
    public void testFlatteningOptionWithMultipleArguments() {
        Options options = new Options();
        options.addOption("option", true, "Option with multiple arguments");
        String[] arguments = {"-option", "--", "arg1", "arg2", "arg3"};

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);

        assertEquals("-option", result[0]);
        assertEquals("arg1", result[1]);
        assertEquals("arg2", result[2]);
        assertEquals("arg3", result[3]);
    }

    /**
     * Test flattening when encountering an unknown option.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningUnknownOption() {
        Options options = new Options();
        options.addOption("optionA", true, "Option A");
        options.addOption("optionB", true, "Option B");
        String[] arguments = {"-optionX", "--"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test flattening when encountering an option with an invalid argument.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningInvalidArgument() {
        Options options = new Options();
        options.addOption("option", true, "Option with invalid argument");
        String[] arguments = {"-option", "--", "invalidArg"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test flattening when encountering a long option with an invalid argument.
     */
    @Test(expected = ParseException.class)
    public void testFlatteningLongOptionWithInvalidArgument() {
        Options options = new Options();
        options.addOption("longOption", 1, "Long option with invalid argument");
        String[] arguments = {"--longOption", "invalidArg"};

        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    /**
     * Test handling of an empty arguments array.
     */
    @Test
    public void testEmptyArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, new String[0], false);

        String[] expected = new String[0];

        assertArrayEquals(expected, result);
    }

    /**
     * Test handling of a null arguments array.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testNullArguments() {
        Options options = new Options();
        PosixParser parser = new PosixParser();
        parser.flatten(options, null, false);
    }
}