package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser class.
 */
public class ParserTest {

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)}.
     */
    @Test
    public void testParse() throws ParseException {
        // given
        String[] args = {"-a", "--optionB", "valueB", "--optionC=valueC"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C")
                .setValueSeparator('=')
                .required(false);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        CommandLine cmd = parser.parse(options, args, null, false);

        // then
        Assert.assertArrayEquals(new String[]{"--optionB", "valueB", "--optionC=valueC"}, cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)}.
     */
    @Test
    public void testParseWithProperties() throws ParseException {
        // given
        String[] args = {"--optionA=valueA", "--optionB=valueB"};
        Properties properties = new Properties();
        properties.setProperty("optionC", "valueC");
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        CommandLine cmd = parser.parse(options, args, properties, false);

        // then
        Assert.assertArrayEquals(new String[]{"--optionA=valueA", "--optionB=valueB"}, cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} when stopAtNonOption is true.
     */
    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        // given
        String[] args = {"--optionA", "nonOption", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B")
                .hasArg(true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        CommandLine cmd = parser.parse(options, args, null, true);

        // then
        Assert.assertEquals(3, cmd.getArgs().length);
        Assert.assertEquals("nonOption", cmd.getArgs()[1]);
        Assert.assertArraySubarrayEquals(new String[]{"--optionA"}, cmd.getOptionNames());
        Assert.assertFalse(cmd.hasOption("b"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], Properties, boolean)} when stopAtNonOption is true.
     */
    @Test
    public void testParseWithPropertiesAndStopAtNonOption() throws ParseException {
        // given
        String[] args = {"--optionA", "nonOption", "--optionB=valueB"};
        Properties properties = new Properties();
        properties.setProperty("optionC", "valueC");
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B")
                .hasArg(true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        CommandLine cmd = parser.parse(options, args, properties, true);

        // then
        Assert.assertEquals(4, cmd.getArgs().length);
        Assert.assertEquals("nonOption", cmd.getArgs()[1]);
        Assert.assertEquals("valueC", cmd.getOptionValue("optionC"));
        Assert.assertArraySubarrayEquals(new String[]{"--optionA"}, cmd.getOptionNames());
        Assert.assertFalse(cmd.hasOption("b"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} when an unrecognized option is passed.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        // given
        String[] args = {"--unrecognizedOption"};
        Options options = new Options();
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        parser.parse(options, args, null, false);

        // then
        // UnrecognizedOptionException is thrown
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} when a missing required option is passed.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        // given
        String[] args = {};
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .required(true);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        parser.parse(options, args, null, false);

        // then
        // MissingOptionException is thrown
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} when an option with a required argument is passed without an argument.
     */
    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        // given
        String[] args = {"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .hasArg(true)
                .required(false);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        parser.parse(options, args, null, false);

        // then
        // MissingArgumentException is thrown
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} when an option with an optional argument is passed without an argument.
     */
    @Test
    public void testOptionalArgument() throws ParseException {
        // given
        String[] args = {"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .hasArg(true)
                .required(false);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        CommandLine cmd = parser.parse(options, args, null, false);

        // then
        Assert.assertArrayEquals(new String[]{"--optionA"}, cmd.getArgs());
        Assert.assertFalse(cmd.hasOption("a"));
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} when an option with an optional argument is passed with an argument.
     */
    @Test
    public void testOptionalArgumentWithValue() throws ParseException {
        // given
        String[] args = {"--optionA=value"};
        Options options = new Options();
        options.addOption("a", true, "Option A")
                .hasArg(true)
                .required(false);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        CommandLine cmd = parser.parse(options, args, null, false);

        // then
        Assert.assertEquals("value", cmd.getOptionValue("a"));
        Assert.assertEquals(1, cmd.getOptions().length);
    }

    /**
     * Test method for {@link Parser#parse(Options, String[], boolean)} when an option group is passed.
     */
    @Test
    public void testOptionGroup() throws ParseException {
        // given
        String[] args = {"--group1", "--optionB"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.setRequired(false);
        group1.addOption("b", true, "Option B")
                .hasArg(true);
        options.addOptionGroup(group1);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        // when
        CommandLine cmd = parser.parse(options, args, null, false);

        // then
        Assert.assertEquals(2, cmd.getArgs().length);
        Assert.assertEquals("--group1", cmd.getArgs()[0]);
        Assert.assertEquals("--optionB", cmd.getArgs()[1]);
        Assert.assertEquals(1, cmd.getOptions().length);
        Assert.assertEquals("Option B", cmd.getOptions()[0].getLongOpt());
        Assert.assertEquals("Option B", cmd.getOptions()[0].getOpt());
        Assert.assertTrue(cmd.getOptions()[0].isSelected());
    }
}