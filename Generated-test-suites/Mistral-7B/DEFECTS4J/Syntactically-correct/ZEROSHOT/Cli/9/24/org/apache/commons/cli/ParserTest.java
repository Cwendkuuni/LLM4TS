package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParse() throws Exception {
        String[] args = new String[]{"--test", "-opt", "arg", "arg2", "--another-opt", "--another-opt-arg"};
        Options options = new Options();
        options.addRequiredOption("test", true, "t", "test option");
        options.addOption("opt", false, "o", "option");
        options.addOption("another-opt", true, "a", "another option", true, "value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, true, false);

        assertEquals("test option", cmd.getOptionValue("test"));
        assertTrue(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));
        assertEquals("arg", cmd.getArg(0));
        assertEquals("arg2", cmd.getArg(1));
        assertEquals("value", cmd.getOptionValue("another-opt"));

        assertArrayEquals(new String[]{"arg2"}, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws Exception {
        String[] args = new String[]{"--test", "-o", "arg"};
        Options options = new Options();
        options.addRequiredOption("test", true, "t", "test option");
        options.addOption("opt", false, "o", "option", true, "value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true, false);

        fail("Expected ParseException but it didn't occur.");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws Exception {
        String[] args = new String[]{"--unknown-option"};
        Options options = new Options();
        options.addOption("test", true, "t", "test option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true, false);

        fail("Expected UnrecognizedOptionException but it didn't occur.");
    }

    @Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[]{"--test", "-o", "arg", "--prop=value"};
        Properties properties = new Properties();
        properties.setProperty("prop", "property value");

        Options options = new Options();
        options.addRequiredOption("test", true, "t", "test option");
        options.addOption("opt", false, "o", "option");
        options.addOption("prop", false, "p", "property", true, "value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, null, properties, false);

        assertEquals("test option", cmd.getOptionValue("test"));
        assertTrue(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));
        assertEquals("arg", cmd.getArg(0));
        assertEquals("property value", cmd.getOptionValue("prop"));

        assertArrayEquals(new String[]{"arg"}, cmd.getArgs());
    }

    @Test
    public void testParseWithStopAtNonOption() throws Exception {
        String[] args = new String[]{"--test", "-o", "arg", "--non-option", "arg2"};
        Options options = new Options();
        options.addRequiredOption("test", true, "t", "test option");
        options.addOption("opt", false, "o", "option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, null, null, true);

        assertEquals("test option", cmd.getOptionValue("test"));
        assertTrue(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));
        assertEquals("arg", cmd.getArg(0));
        assertEquals("arg2", cmd.getArg(1));
        assertArrayEquals(new String[]{"--non-option", "arg2"}, cmd.getArgs());
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws Exception {
        String[] args = new String[]{"--test", "-o", "arg"};
        Options options = new Options();
        options.addRequiredOption("test", true, "t", "test option");
        options.addOption("opt", false, "o", "option");
        options.addOption("arg", false, "a", "argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(args, true, false);

        fail("Expected MissingArgumentException but it didn't occur.");
    }

    @Test
    public void testParseWithMultipleOptionalArguments() throws Exception {
        String[] args = new String[]{"--test", "-o", "arg1", "arg2", "arg3"};
        Options options = new Options();
        options.addRequiredOption("test", true, "t", "test option");
        options.addOption("opt", false, "o", "option");
        options.addOption("arg", false, "a", "argument", 3);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, true, false);

        assertEquals("test option", cmd.getOptionValue("test"));
        assertTrue(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));
        assertEquals("arg1", cmd.getArg(0));
        assertEquals("arg2", cmd.getArg(1));
        assertEquals("arg3", cmd.getArg(2));

        assertArrayEquals(new String[]{"arg3"}, cmd.getArgs());
    }

    @Test
    public void testParseWithMultipleOptionalArgumentsAndValues() throws Exception {
        String[] args = new String[]{"--test", "-o", "arg1", "arg2=val1,val2"};
        Options options = new Options();
        options.addRequiredOption("test", true, "t", "test option");
        options.addOption("opt", false, "o", "option");
        options.addOption("arg", false, "a", "argument", 2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, true, false);

        assertEquals("test option", cmd.getOptionValue("test"));
        assertTrue(cmd.hasOption("opt"));
        assertNull(cmd.getOptionValue("opt"));
        assertEquals("arg1", cmd.getArg(0));
        String[] argValues = cmd.getOptionValues("arg");
        assertArrayEquals(new String[]{"val1", "val2"}, argValues);

        assertArrayEquals(new String[]{}, cmd.getArgs());
    }
}