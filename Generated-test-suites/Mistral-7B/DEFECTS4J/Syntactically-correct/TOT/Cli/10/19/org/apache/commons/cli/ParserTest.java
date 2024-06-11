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
import static org.junit.Assert.assertTrue;

/**
 * Test cases for Parser class
 */
public class ParserTest {

    @Test
    public void testParseWithSimpleOptions() throws ParseException {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(String.class, cmd.getOptionValues("a")[0].getClass());
        assertEquals(String.class, cmd.getOptionValues("b")[0].getClass());
        assertEquals("1", cmd.getOptionValues("a")[0]);
        assertEquals("2", cmd.getOptionValues("b")[0]);
        assertFalse(cmd.getArgs().hasNext());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = {"--optionA", "--optionB"};
        Options options = new Options();

        options.addRequiredOption("optionA", true, "Required option A");
        options.addRequiredOption("optionB", true, "Required option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("optionA"));
        assertTrue(cmd.hasOption("optionB"));
        assertFalse(cmd.getArgs().hasNext());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] args = {"--optionC"};
        Options options = new Options();
        options.addRequiredOption("optionA", true, "Required option A");
        options.addRequiredOption("optionB", true, "Required option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertNull(cmd);
    }

    @Test
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = {"--unknown-option"};
        Options options = new Options();
        options.addOption("optionA", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertNull(cmd);
    }

    @Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        String[] args = {"-a", "1", "-b", "2", "--no-option", "3", "4"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        assertNotNull(cmd);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(String.class, cmd.getOptionValues("a")[0].getClass());
        assertEquals(String.class, cmd.getOptionValues("b")[0].getClass());
        assertEquals("1", cmd.getOptionValues("a")[0]);
        assertEquals("2", cmd.getOptionValues("b")[0]);
        assertTrue(cmd.getArgs().hasNext());
        assertEquals("3", cmd.getArgs().next());
        assertEquals("4", cmd.getArgs().next());
        assertFalse(cmd.getArgs().hasNext());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"-p", "prop1=value1", "-p", "prop2=value2"};
        Properties properties = new Properties();
        properties.setProperty("prop1", "value1");
        properties.setProperty("prop2", "value2");

        Options options = new Options();
        options.addOptions(true, "p", "property", true, "Property", "Property value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, true);

        assertNotNull(cmd);
        assertEquals(2, cmd.getOptions().size());
        assertEquals("value1", cmd.getOptionValues("prop1")[0]);
        assertEquals("value2", cmd.getOptionValues("prop2")[0]);
        assertFalse(cmd.getArgs().hasNext());
    }

    @Test
    public void testHelp() {
        String[] args = {"--help"};
        Options options = new Options();
        options.addOption("h", "help", false, "Print this message.");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args, true);
        } catch (ParseException e) {
            // expected
        }

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("TestParser", options);

        assertTrue(cmd == null);

        // print the help message to the console to check its formatting
        System.out.println();
        formatter.printHelp("TestParser", options);
    }
}