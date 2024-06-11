package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test cases for Parser class.
 */
public class ParserTest
{
    @Test
    public void testBasicParsing() throws ParseException
    {
        String[] args = {"-a", "1", "--optionB=2", "arg1", "--optionC=3"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getArgs().get(0));
    }

    @Test
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = {"--optionA"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "requiredB");

        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, args, null);
            Assert.fail("MissingRequiredOptionException expect to be thrown");
        }
        catch (MissingOptionException e)
        {
            List<String> requiredOptions = e.getMissingOptions();
            Assert.assertEquals(1, requiredOptions.size());
            Assert.assertEquals("requiredB", requiredOptions.get(0));
        }
    }

    @Test
    public void testMissingArgument() throws ParseException
    {
        String[] args = {"-a", "1"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "argB");

        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, args, null);
            Assert.fail("MissingArgumentException expect to be thrown");
        }
        catch (MissingArgumentException e)
        {
            String option = e.getOption();
            Assert.assertEquals("b", option);
            Assert.assertEquals(1, e.getIndex());
        }
    }

    @Test
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = {"--unknownOption"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();

        try
        {
            parser.parse(options, args, null);
            Assert.fail("UnrecognizedOptionException expect to be thrown");
        }
        catch (UnrecognizedOptionException e)
        {
            String option = e.getOption();
            Assert.assertEquals("unknownOption", option);
        }
    }
}