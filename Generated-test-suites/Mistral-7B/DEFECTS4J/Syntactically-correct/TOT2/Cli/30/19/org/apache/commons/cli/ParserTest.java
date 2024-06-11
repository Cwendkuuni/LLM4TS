package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest
{
    @Test
    public void testParser_NoOptions_NoArguments() throws ParseException
    {
        // given
        Parser parser = new DefaultParser();
        Options options = new Options();

        // when
        CommandLine cmd = parser.parse(options, new String[0]);

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(parser.getOptions());
        Assert.assertNull(parser.getRequiredOptions());
    }

    @Test
    public void testParser_SimpleOption_NoArgument() throws ParseException
    {
        // given
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Print a message");

        // when
        CommandLine cmd = parser.parse(options, new String[]{"-a"});

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParser_SimpleOption_WithArgument() throws ParseException
    {
        // given
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Print a message with value: %s");

        // when
        CommandLine cmd = parser.parse(options, new String[]{"-a", "value"});

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(1, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testParser_MultipleOptions() throws ParseException
    {
        // given
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Print a message with value: %s");
        options.addOption("b", false, "Print another message");

        // when
        CommandLine cmd = parser.parse(options, new String[]{"-a", "value1", "-b"});

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("value1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test(expected = ParseException.class)
    public void testParser_MissingOption() throws ParseException
    {
        // given
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Print a message with value: %s");

        // when
        parser.parse(options, new String[]{"-b"});

        // then an exception should be thrown
    }

    @Test(expected = ParseException.class)
    public void testParser_MissingRequiredOption() throws ParseException
    {
        // given
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Print a message with value: %s");
        options.addOption("b", true, "Print another message with value: %s");
        List<String> requiredOptions = Arrays.asList("a");

        // when
        parser.setOptions(options);
        parser.setRequiredOptions(requiredOptions);
        parser.parse(options, new String[0]);

        // then an exception should be thrown
    }

    @Test
    public void testParser_Properties() throws ParseException
    {
        // given
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Print a message with value: %s");
        options.addOption("b", true, "Print another message with value: %s");
        Properties properties = new Properties();
        properties.setProperty("a", "value1");

        // when
        CommandLine cmd = parser.parse(options, new String[0], properties);

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(2, cmd.getOptions().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("value1", cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testParser_InvalidProperties() throws ParseException
    {
        // given
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Print a message with value: %s");
        Properties properties = new Properties();
        properties.setProperty("a", "value1");
        properties.setProperty("c", "value2");

        // when
        parser.parse(options, new String[0], properties);

        // then an exception should be thrown
    }
}