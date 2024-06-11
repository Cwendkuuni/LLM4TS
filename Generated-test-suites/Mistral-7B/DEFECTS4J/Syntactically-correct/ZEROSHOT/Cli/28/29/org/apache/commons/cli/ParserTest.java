package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "--foo", "3"};
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("foo", 3, "option foo with an int value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("foo"));
        assertEquals(3, cmd.getOptionIntValue("foo"));
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = new String[]{"--foo=bar", "--baz=qux"};
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");
        properties.setProperty("baz", "qux");

        Options options = new Options();
        options.addOption("foo", true, "option foo");
        options.addOption("baz", true, "option baz");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, null, properties, false);

        assertTrue(cmd.hasOption("foo"));
        assertTrue(cmd.getOptionValue("foo"));
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertTrue(cmd.hasOption("baz"));
        assertTrue(cmd.getOptionValue("baz"));
        assertEquals("qux", cmd.getOptionValue("baz"));
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertEquals(0, cmd.getArgs().length);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"--test"};
        Options options = new Options();
        options.addOption("test", true, "test option");
        options.addRequiredOption("required", true, "required option");

        Parser parser = new DefaultParser();
        parser.parse(options, args, null);
        fail("MissingOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingRequiredArgument() throws ParseException
    {
        String[] args = new String[]{"-a"};
        Options options = new Options();
        options.addOption("a", true, "option a");
        options.addOption("b", true, "option b with required argument");

        Parser parser = new DefaultParser();
        parser.parse(options, args, null);
        fail("MissingArgumentException expected");
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] args = new String[]{"--foo", "bar", "-x", "qux"};
        Options options = new Options();
        options.addOption("foo", true, "option foo");
        options.addOption("x", true, "option x");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);

        assertTrue(cmd.hasOption("foo"));
        assertEquals("bar", cmd.getOptionValue("foo"));
        assertTrue(cmd.hasOption("x"));
        assertEquals("qux", cmd.getOptionValue("x"));
        assertFalse(cmd.hasOption("nonExistentOption"));
        assertEquals(new String[]{"--foo=bar", "-x=qux"}, cmd.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException
    {
        String[] args = new String[]{"--invalid"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.parse(options, args, null);
        fail("UnrecognizedOptionException expected");
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOptionValue() throws ParseException
    {
        String[] args = new String[]{"-a", "invalid"};
        Options options = new Options();
        options.addOption("a", true, "option a");

        Parser parser = new DefaultParser();
        parser.parse(options, args, null);
        fail("IllegalArgumentException expected");
    }
}

class DefaultParser extends Parser
{
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException
    {
        return opts.parse(arguments);
    }
}