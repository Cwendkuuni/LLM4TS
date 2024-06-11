package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    @Test
    public void testFlatten() {
        // Test case 1: Empty arguments
        String[] arguments = {};
        Options options = new Options();
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[0], result);

        // Test case 2: Single argument
        arguments = new String[]{"arg1"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"arg1"}, result);

        // Test case 3: Option with no value
        options.addOption("o", "option", false, "Description for option o");
        arguments = new String[]{"--option"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option"}, result);

        // Test case 4: Option with value
        options.addOption("v", "value", true, "Description for option v");
        arguments = new String[]{"--value", "value1"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--value", "value1"}, result);

        // Test case 5: Multiple options with no values
        arguments = new String[]{"--option1", "--option2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option1", "--option2"}, result);

        // Test case 6: Option with value and other arguments
        arguments = new String[]{"--value", "value1", "arg1", "arg2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--value", "value1", "arg1", "arg2"}, result);

        // Test case 7: Long option with value
        arguments = new String[]{"--long-option", "longValue"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--long-option", "longValue"}, result);

        // Test case 8: Option with no value and other arguments
        arguments = new String[]{"--option", "arg1", "arg2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--option", "arg1", "arg2"}, result);

        // Test case 9: Multiple options with values
        options.addOption("i", "inputFile", true, "Input file for the command");
        options.addOption("o", "outputFile", true, "Output file for the command");
        arguments = new String[]{"--inputFile", "input.txt", "--outputFile", "output.txt", "arg1", "arg2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"--inputFile", "input.txt", "--outputFile", "output.txt", "arg1", "arg2"}, result);

        // Test case 10: Stop at non-option
        arguments = new String[]{"--option", "arg1", "-a", "arg2"};
        result = parser.flatten(options, arguments, true);
        Assert.assertArrayEquals(new String[]{"--option", "arg1"}, result);

        // Test case 11: Bursting token
        options.addOption("b", "burst", true, "Description for burst option");
        arguments = new String[]{"-b", "burstOption", "arg1", "arg2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-b", "--burst", "arg1", "arg2"}, result);

        // Test case 12: Bursting long token
        arguments = new String[]{"-b", "--burst-long", "longArgument", "arg1", "arg2"};
        result = parser.flatten(options, arguments, false);
        Assert.assertArrayEquals(new String[]{"-b", "--burst-long", "--burst-long", "longArgument", "arg1", "arg2"}, result);
    }
}