package org.apache.commons.cli.test;

import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        String[] args = {"test", "-a", "1", "-b", "2", "--"};

        Options options = new Options();
        Option aOpt = new Option("a", true, "a").setValueSeparator(';');
        Option bOpt = new Option("b", true, "b");
        options.addOption(aOpt);
        options.addOption(bOpt);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(args[1], cmd.getOptionValue("a"));
        assertEquals(args[3], cmd.getOptionValue("b"));
        assertArrayEquals(new String[]{"1"}, cmd.getOptions().getOptionValues("a"));
        assertArrayEquals(new String[]{"2"}, cmd.getOptions().getOptionValues("b"));
        assertNull(cmd.getOptionValue("c"));

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("Test", options);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException
    {
        String[] args = {"test", "-a", "-b"};

        Options options = new Options();
        Option aOpt = new Option("a", true, "a").setValueSeparator(';');
        Option bOpt = new Option("b", true, "b");
        options.addOption(aOpt);
        options.addOption(bOpt);

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        assertFalse(true);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        String[] args = {"test", "-x", "1", "-b", "2", "--"};

        Options options = new Options();
        Option aOpt = new Option("a", true, "a").setValueSeparator(';');
        Option bOpt = new Option("b", true, "b");
        options.addOption(aOpt);
        options.addOption(bOpt);

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        assertFalse(true);
    }
}