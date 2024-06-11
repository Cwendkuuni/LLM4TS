package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParse() throws ParseException {
        String[] args = new String[]{"-a", "--optionB=valueB", "-p", "valueC"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("p", true, "Option P");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);

        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("p"));
        assertEquals("valueB", cmd.getOptionValue("b"));
        assertEquals("valueC", cmd.getOptionValue("p"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] args = new String[]{"-a"};

        Options options = new Options();
        options.addOption("a", true, "Option A", true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNull(cmd);
    }

    @Test(expected = ParseException.class)
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unknownOption"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNull(cmd);
    }

    @Test
    public void testParseOptionalArgument() throws ParseException {
        String[] args = new String[]{"-a", "valueA", "--optionB=valueB"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);

        assertTrue(cmd.hasOption("a"));
        assertSame("valueA", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseValueArgument() throws ParseException {
        String[] args = new String[]{"--optionB=valueB"};

        Options options = new Options();
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);

        assertTrue(cmd.hasOption("b"));
        assertSame("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseMultipleValueArguments() throws ParseException {
        String[] args = new String[]{"-a", "valueA1", "valueA2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);

        assertTrue(cmd.hasOption("a"));
        assertSame("valueA1,valueA2", cmd.getOptionValues("a").toString());
    }

    @Test
    public void testParseLongOption() throws ParseException {
        String[] args = new String[]{"--longOption=value"};

        Options options = new Options();
        options.addOption("lo", true, "Long Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNotNull(cmd);

        assertTrue(cmd.hasOption("lo"));
        assertSame("value", cmd.getOptionValue("lo"));
    }

    @Test
    public void testParseHelp() throws ParseException {
        String[] args = new String[]{"--help"};

        Options options = new Options();

        HelpFormatter formatter = new HelpFormatter();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true, formatter);

        assertNull(cmd);
        assertTrue(formatter.isHelpDisplayed());
    }
}