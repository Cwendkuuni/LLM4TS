package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-a", "--b"}, options);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertEquals("", cmd.getOptionValue("a"));
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("", cmd.getArgs()[0]);
    }

    @Test
    public void testParseWithArg() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-a", "value1", "--b=value2"}, options);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("value1", cmd.getOptionValue("a"));
        Assert.assertEquals("value2", cmd.getOptionValue("b"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("c", false, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"-a", "value1"}, options);

        Assert.fail("Missing required option C");
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--x"}, options);

        Assert.fail("Unrecognized option: -x");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Properties props = new Properties();
        props.setProperty("optionA", "value1");
        props.setProperty("optionB.optionC", "value2");

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("b.c", true, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(new String[]{"--"}, options, props);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("b.c"));
        Assert.assertEquals("value1", cmd.getOptionValue("a"));
        Assert.assertEquals("value2", cmd.getOptionValue("b.c"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }
}