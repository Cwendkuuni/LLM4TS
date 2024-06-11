package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSimpleOptions() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "Short option a");
        options.addOption("b", true, "Short option b");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testLongOptions() throws ParseException
    {
        String[] args = {"--long-option-a", "1", "--long-option-b=2"};
        Options options = new Options();
        options.addOption("longOptionA", true, "Long option A");
        options.addOption("longOptionB", true, "Long option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("longOptionA"));
        assertTrue(cmd.hasOption("longOptionB"));
        assertEquals("1", cmd.getOptionValue("longOptionA"));
        assertEquals("2", cmd.getOptionValue("longOptionB"));
    }

    @Test
    public void testRequiredOptions() throws ParseException
    {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Short option A");
        options.addRequiredOption("b", true, "Short option B");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            fail("Expected ParseException for missing required option");
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            assertEquals(1, missingOptions.size());
            assertEquals("b", missingOptions.get(0));
        }
    }

    @Test
    public void testOptionalArgs() throws ParseException
    {
        String[] args = {"-a", "1", "--optional=3"};
        Options options = new Options();
        Option option = new Option("a", true, "Option A");
        option.setOptionalArg(true);
        options.addOption(option);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("1", cmd.getArgs().get(0));
        assertEquals("3", cmd.getArgs().get(1));
    }

    @Test
    public void testGroups() throws ParseException
    {
        String[] args = {"--group1", "-g1", "-g2", "value"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.setRequired(false);
        options.addOptionGroup(group1);
        Option g1Option1 = new Option("g1", false, "Option g1.1");
        Option g1Option2 = new Option("g2", false, "Option g1.2");
        group1.addOption(g1Option1);
        group1.addOption(g1Option2);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("g1"));
        assertTrue(cmd.hasOption("g2"));
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = {"--foo", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException
    {
        String[] args = {"-a"};
        Options options = new Options();
        Option option = new Option("a", true, "Option A");
        option.setArgName("arg");
        options.addOption(option);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testParseException() throws ParseException
    {
        String[] args = {"foo"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.parse(options, args, false);
    }
}