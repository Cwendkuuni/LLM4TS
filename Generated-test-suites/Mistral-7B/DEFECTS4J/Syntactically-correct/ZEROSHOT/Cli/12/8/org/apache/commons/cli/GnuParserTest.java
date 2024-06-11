package org.apache.commons.cli.test;

import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("d", "debug", false, "Debug mode");
        options.addOption("i", "input", true, "Input file");
        options.addOption("o", "output", true, "Output file");

        String[] arguments = new String[]{"-d", "--", "-i", "input.txt", "--", "-o", "output.txt", "--", "arg1", "--", "arg2"};
        String[] expected = new String[]{"-d", "-d", "-i", "input.txt", "-o", "output.txt", "--", "arg1", "--", "arg2"};

        GnuParser parser = new GnuParser();
        String[] result = parser.flatten(options, arguments, false);

        Assertions.assertArrayEquals(expected, result);

        // Test with stopAtNonOption
        arguments = new String[]{"-d", "--", "-i", "input.txt", "-f", "output.txt", "--", "arg1", "--", "arg2"};
        String[] resultWithStopAtNonOption = parser.flatten(options, arguments, true);

        String[] expectedWithoutArgF = new String[]{"-d", "-d", "-i", "input.txt", "--", "arg1", "--", "arg2"};
        Assertions.assertArrayEquals(expectedWithoutArgF, resultWithStopAtNonOption);

        // Test with --
        arguments = new String[]{"-d", "--", "-i", "input.txt", "--", "arg1", "--", "--", "--", "arg3"};
        String[] resultWithDoubleDash = parser.flatten(options, arguments, false);

        String[] expectedWithDoubleDash = new String[]{"-d", "-d", "-i", "input.txt", "--", "--", "--", "arg3"};
        Assertions.assertArrayEquals(expectedWithDoubleDash, resultWithDoubleDash);
    }
}