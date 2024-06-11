package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test for Parser class
 */
public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        // given
        String[] args = {"test", "--option1", "value1", "--option2"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1 description");
        options.addOption("option2", true, "Option 2 description");

        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, false);

        // then
        Assert.assertEquals("test", cmd.getArgs().get(0));
        Assert.assertTrue(cmd.hasOption("option1"));
        Assert.assertTrue(cmd.getOptionValue("option1"));
        Assert.assertTrue(cmd.hasOption("option2"));
        Assert.assertTrue(cmd.getOptionValue("option2"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        // given
        String[] args = {"test", "--unknown"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1 description");

        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, false);

        // then
        // an exception should be thrown
    }

    @Test
    public void testMissingValue() throws ParseException
    {
        // given
        String[] args = {"test", "--option1"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1 description");

        CommandLineParser parser = new DefaultParser();

        // when
        try
        {
            CommandLine cmd = parser.parse(options, args, false);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("The following options do not have a value:", e.getMessage());
            return;
        }

        // then
        Assert.fail("An exception should have been thrown");
    }

    @Test
    public void testInvalidOption() throws ParseException
    {
        // given
        String[] args = {"test", "--invalid"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1 description");

        CommandLineParser parser = new DefaultParser();

        // when
        try
        {
            CommandLine cmd = parser.parse(options, args, false);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Unrecognized option: invalid", e.getMessage());
            return;
        }

        // then
        Assert.fail("An exception should have been thrown");
    }

    @Test
    public void testHelp()
    {
        // given
        String[] args = {"test", "--help"};
        Options options = new Options();
        options.addOption("option1", true, "Option 1 description");

        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = null;
        try
        {
            cmd = parser.parse(options, args, true);
        }
        catch (ParseException e)
        {
            // ignored
        }

        // then
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("test", options);
        System.out.println(help);
    }

    @Test
    public void testProperties() throws ParseException
    {
        // given
        String[] args = {"test", "--property1=value1", "--property2=value2"};
        Options options = new Options();
        options.addOption("property1", true, "Property 1 description");
        options.addOption("property2", true, "Property 2 description");

        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, args, null, false);

        // then
        Assert.assertEquals(2, cmd.getProperties().size());
        Assert.assertEquals("value1", cmd.getProperty("property1"));
        Assert.assertEquals("value2", cmd.getProperty("property2"));
    }
}