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
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = new String[]{"-a", "1", "--foo", "bar"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("foo", true, "Foo value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(true, cmd.hasOption("a"));
        assertEquals(true, cmd.hasOption("foo"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertFalse(cmd.hasOption("unknown"));
        assertNull(cmd.getOptionValue("unknown"));
    }

    @Test
    public void testParseMissingOption() throws ParseException {
        String[] args = new String[]{"--missing"};
        Options options = new Options();
        options.addOption("a", true, "A value");

        CommandLineParser parser = new DefaultParser();

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, args, null, false);
        });
    }

    @Test
    public void testParseMissingArgument() throws ParseException {
        String[] args = new String[]{"-a", "--arg"};
        Options options = new Options();
        options.addOption("a", true, "A value").hasArg();

        CommandLineParser parser = new DefaultParser();

        assertThrows(MissingArgumentException.class, () -> {
            parser.parse(options, args, null, false);
        });
    }

    @Test
    public void testParseUnrecognizedOption() throws ParseException {
        String[] args = new String[]{"--unknown"};
        Options options = new Options();
        options.addOption("a", true, "A value");

        CommandLineParser parser = new DefaultParser();

        assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(options, args, null, false);
        });
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = new String[]{"--foo=bar"};
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");

        Options options = new Options();
        options.addOption("foo", true, "Foo value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals("bar", cmd.getOptionValue("foo"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] args = new String[]{"-a", "--foo", "bar", "-b", "-c"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("foo", true, "Foo value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);

        assertEquals(3, cmd.getArgs().size());
        assertEquals("-b", cmd.getArgs().get(0));
        assertEquals("-c", cmd.getArgs().get(1));
    }

    @Test
    public void testParseWithHelp() throws ParseException {
        String[] args = new String[]{"--help"};
        Options options = new Options();
        options.addOption("a", true, "A value");

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new HelpFormatter();

        try {
            parser.parse(options, args, null, false);
        } catch (ParseException e) {
            formatter.printHelp("parser", options);
            System.out.println();
            fail("Help should have been printed");
        }
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] args = new String[]{"-g", "foo"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(false);
        group.addOption("g", true, "Group value");
        group.addOption("foo", true, "Foo value");
        options.addOptionGroup(group);
        options.addOption("a", true, "A value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, false);

        assertTrue(group.isSelected());
        assertEquals("foo", cmd.getOptionValue("foo"));
        assertEquals(false, cmd.hasOption("g"));
    }

    @Test
    public void testParseWithNullArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "A value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, null, false);
        assertNull(cmd);
    }
}