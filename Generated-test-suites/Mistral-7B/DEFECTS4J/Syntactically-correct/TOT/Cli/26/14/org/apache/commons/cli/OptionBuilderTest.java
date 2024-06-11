package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

/**
 * Test cases for OptionBuilder class in Apache Commons CLI library.
 */
public class OptionBuilderTest {

    /**
     * Test for creating a simple option using OptionBuilder.
     */
    @Test
    public void testSimpleOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.withLongOpt("inputFile")
                .withDescription("Input file name")
                .hasArg()
                .create('i');

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-i", "test.txt"});
        Assert.assertEquals("test.txt", cmd.getOptionValue("inputFile"));
    }

    /**
     * Test for creating an option with an optional argument.
     */
    @Test
    public void testOptionalArgOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.withLongOpt("outputDir")
                .withDescription("Output directory")
                .isRequired(false)
                .hasArg()
                .create('o');

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd1 = parser.parse(new String[]{"-o"});
        Assert.assertNull(cmd1.getOptionValue("outputDir"));

        CommandLine cmd2 = parser.parse(new String[]{"-o", "output"});
        Assert.assertEquals("output", cmd2.getOptionValue("outputDir"));
    }

    /**
     * Test for creating an option with multiple optional arguments.
     */
    @Test
    public void testMultipleOptionalArgsOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.withLongOpt("ports")
                .withDescription("List of ports to bind to")
                .isRequired(false)
                .hasArgs()
                .create('p');

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd1 = parser.parse(new String[]{"-p"});
        Assert.assertNull(cmd1.getArgs());

        CommandLine cmd2 = parser.parse(new String[]{"-p", "8080", "8001"});
        Assert.assertArrayEquals(new String[]{"8080", "8001"}, cmd2.getArgs());
    }

    /**
     * Test for creating an option with a specified value separator.
     */
    @Test
    public void testValueSeparatorOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.withLongOpt("commaSeparatedList")
                .withDescription("Comma-separated list of values")
                .isRequired(false)
                .hasArgs()
                .withValueSeparator(',')
                .create('l');

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd1 = parser.parse(new String[]{"-l"});
        Assert.assertNull(cmd1.getArgs());

        CommandLine cmd2 = parser.parse(new String[]{"-l", "value1,value2,value3"});
        Assert.assertArrayEquals(new String[]{"value1", "value2", "value3"}, cmd2.getArgs());
    }

    /**
     * Test for creating an option with a custom type.
     */
    @Test
    public void testCustomTypeOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.withLongOpt("threshold")
                .withDescription("Threshold value")
                .isRequired(false)
                .hasArg()
                .withType(Double.class)
                .create('t');

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd1 = parser.parse(new String[]{"-t"});
        Assert.assertNull(cmd1.getOptionValue("threshold"));

        CommandLine cmd2 = parser.parse(new String[]{"-t", "123.45"});
        Assert.assertEquals(123.45, Double.parseDouble(cmd2.getOptionValue("threshold")));
    }

    /**
     * Test for creating an option with an empty description.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyDescriptionOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.withLongOpt("")
                .create('e');
    }

    /**
     * Test for creating an option with an empty long option name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testEmptyLongOptOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.create((char) 0);
    }

    /**
     * Test for creating an option without setting the long option name.
     */
    @Test(expected = IllegalArgumentException.class)
    public void testMissingLongOptOption() {
        OptionBuilder ob = new OptionBuilder();
        Option option = ob.create((char) 0);
    }
}