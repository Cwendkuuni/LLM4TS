package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for the Options class.
 */
public class OptionsTest
{
    @Test
    public void testBasicUsage() throws ParseException
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withLongOpt("test")
                .withDescription("Test some functionality."));

        Assert.assertEquals(1, options.getOptions().size());
    }

    @Test
    public void testShortOption() throws ParseException
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withShortOpt('t')
                .withLongOpt("test")
                .withDescription("Test some functionality.")
                .isRequired());

        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals("Test some functionality.", options.getOptions().get(0).getDescription());
        Assert.assertTrue(options.getOptions().get(0).isRequired());
    }

    @Test
    public void testLongOption() throws ParseException
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withLongOpt("test")
                .withDescription("Test some functionality.")
                .isRequired());

        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals("Test some functionality.", options.getOptions().get(0).getDescription());
        Assert.assertTrue(options.getOptions().get(0).isRequired());
    }

    @Test
    public void testShortAndLongOptions() throws ParseException
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withShortOpt('t')
                .withLongOpt("test")
                .withDescription("Test some functionality.")
                .isRequired());

        options.addOption(OptionBuilder.withLongOpt("test-long")
                .withDescription("Another test functionality.")
                .hasArg(true)
                .isRequired());

        Assert.assertEquals(2, options.getOptions().size());
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withLongOpt("test")
                .isRequired());

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"--"});
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredShortOption() throws ParseException
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withShortOpt('t')
                .isRequired());

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"-"});
    }

    @Test(expected = ParseException.class)
    public void testInvalidLongOption() throws ParseException
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withLongOpt("test")
                .withDescription("Test some functionality."));

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"--invalid"});
    }

    @Test(expected = ParseException.class)
    public void testInvalidShortOption() throws ParseException
    {
        Options options = new Options();

        options.addOption(OptionBuilder.withShortOpt('t')
                .withDescription("Test some functionality."));

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"-z"});
    }

    @Test
    public void testOptionGroup() throws ParseException
    {
        Options options = new Options();

        OptionGroup group = new OptionGroup("test-group", "Test group options");
        group.addOption(OptionBuilder.withLongOpt("test1")
                .withDescription("Test option 1.")
                .isRequired());
        group.addOption(OptionBuilder.withLongOpt("test2")
                .withDescription("Test option 2.")
                .hasArg(true));

        options.addOptionGroup(group);

        Assert.assertEquals(2, group.getOptions().size());

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"--test-group", "--test1", "arg1", "--test2", "arg2"});

        Assert.assertEquals(3, options.getOptions().size());
        Assert.assertEquals(2, group.getOptions().size());
        Assert.assertTrue(group.getOptions().get(0).isRequired());
    }
}