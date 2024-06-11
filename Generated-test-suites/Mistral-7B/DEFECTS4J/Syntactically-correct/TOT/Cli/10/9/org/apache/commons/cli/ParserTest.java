package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @org.junit.Test
    public void testParseWithNoOptionsAndNoArguments() throws ParseException {
        Options options = new Options();
        String[] args = new String[0];

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertNull(cmd.getArgs());
        assertEquals(0, cmd.getOptions().size());
    }

    @org.junit.Test
    public void testParseWithOptionsAndNoArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = new String[0];

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertNull(cmd.getArgs());
    }

    @org.junit.Test
    public void testParseWithOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--a", "1", "--b", "2"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("", cmd.getArgs()[0]);
        assertEquals("2", cmd.getArgs()[1]);
    }

    @org.junit.Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        String[] args = {"--a", "1"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals(null, cmd.getOptionValue("b"));

        String[] argsError = {"--a", "1"};

        assertThrows(MissingOptionException.class, () -> {
            parser.parse(options, argsError);
        });
    }

    @org.junit.Test
    public void testParseWithOptionalArgument() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");
        options.addOption("c", 1, "Option C");

        String[] args = {"--a", "1", "--b", "2", "--c", "3"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("c"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals("3", cmd.getOptionValue("c"));
        assertEquals(0, cmd.getArgs().length);
    }

    @org.junit.Test
    public void testParseWithOptionalArgumentNoValue() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--a", "--b"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals(null, cmd.getOptionValue("a"));
        assertEquals(null, cmd.getOptionValue("b"));
    }

    @org.junit.Test
    public void testParseWithNoSuchOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        String[] args = {"--c"};

        CommandLineParser parser = new DefaultParser();

        assertThrows(NoSuchOptionException.class, () -> {
            parser.parse(options, args);
        });
    }

    @org.junit.Test
    public void testParseWithUnrecognizedOption() {
        Options options = new Options();

        String[] args = {"--x"};

        CommandLineParser parser = new DefaultParser();

        assertThrows(UnrecognizedOptionException.class, () -> {
            parser.parse(options, args);
        });
    }

    @org.junit.Test
    public void testParseWithMixedOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--a", "1", "--b", "2", "arg1", "arg2"};

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(2, cmd.getArgs().length);
        assertEquals("arg1", cmd.getArgs()[0]);
        assertEquals("arg2", cmd.getArgs()[1]);
    }

    @org.junit.Test
    public void testParseWithMissingArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B");

        String[] args = {"--a", "1"};

        CommandLineParser parser = new DefaultParser();

        assertThrows(MissingArgumentException.class, () -> {
            parser.parse(options, args);
        });
    }
}