package org.apache.commons.cli.test;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest
{
    @Test
    public void testSimpleParse() throws ParseException
    {
        String[] args = {"-a", "1", "--optionB", "2"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with a flag");
        options.addOption("b", true, "An option with a value");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(0, cmd.args.size());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException
    {
        String[] args = {"--invalidOption"};

        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);
        parser.parse(args);

        fail("Expected ParseException");
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = {"-a"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "A required option");

        parser.setOptions(options);

        try
        {
            parser.parse(args);
        }
        catch (MissingArgumentException e)
        {
            throw new AssertionError("Unexpected MissingArgumentException", e);
        }

        fail("Expected MissingOptionException");
    }

    @Test
    public void testArgumentParse() throws ParseException
    {
        String[] args = {"-a", "test"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with an argument");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args);

        assertTrue(cmd.hasOption("a"));
        assertEquals("test", cmd.getOptionValue("a"));
        assertEquals(1, cmd.args.size());
        assertEquals("test", cmd.args.get(0));
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgument() throws ParseException
    {
        String[] args = {"-a", "test", "--invalidOption"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "An option with an argument");

        parser.setOptions(options);

        try
        {
            parser.parse(args);
        }
        catch (MissingArgumentException e)
        {
            throw new AssertionError("Unexpected MissingArgumentException", e);
        }

        fail("Expected ParseException");
    }

    @Test
    public void testPropertiesParsing() throws ParseException
    {
        String[] args = {"--foo=abc", "--bar=def"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("foo", true, "An option with a value from properties");
        options.addOption("bar", true, "Another option with a value from properties");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, new Properties());

        assertTrue(cmd.hasOption("foo"));
        assertEquals("abc", cmd.getOptionValue("foo"));
        assertTrue(cmd.hasOption("bar"));
        assertEquals("def", cmd.getOptionValue("bar"));
        assertEquals(0, cmd.args.size());
    }

    @Test(expected = ParseException.class)
    public void testMissingPropertyOption() throws ParseException
    {
        String[] args = {"--invalidOption=abc"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("invalidOption", true, "An option with an invalid name");

        parser.setOptions(options);

        try
        {
            parser.parse(args, new Properties());
        }
        catch (MissingOptionException e)
        {
            throw new AssertionError("Unexpected MissingOptionException", e);
        }

        fail("Expected ParseException");
    }

    @Test
    public void testMultipleProperties() throws ParseException
    {
        String[] args = {"--foo=abc", "--bar=def", "--baz=ghi"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("foo", true, "An option with a value from properties");
        options.addOption("bar", true, "Another option with a value from properties");
        options.addOption("baz", true, "Another option with a value from properties");

        parser.setOptions(options);
        CommandLine cmd = parser.parse(args, new Properties());

        assertTrue(cmd.hasOption("foo"));
        assertEquals("abc", cmd.getOptionValue("foo"));
        assertTrue(cmd.hasOption("bar"));
        assertEquals("def", cmd.getOptionValue("bar"));
        assertTrue(cmd.hasOption("baz"));
        assertEquals("ghi", cmd.getOptionValue("baz"));
        assertEquals(0, cmd.args.size());
    }

    @Test(expected = ParseException.class)
    public void testUnrecognizedOption() throws ParseException
    {
        String[] args = {"--invalidOption"};

        Parser parser = new DefaultParser();
        Options options = new Options();

        parser.setOptions(options);

        try
        {
            parser.parse(args, new Properties());
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: invalidOption", e.getMessage());
            fail("Expected ParseException");
        }
    }

    @Test
    public void testStopAtNonOption() throws ParseException
    {
        String[] args = {"test", "--optionA", "optionB"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("optionA", true, "An option");

        parser.setOptions(options);

        CommandLine cmd = parser.parse(args, null, true);

        assertEquals(3, cmd.args.size());
        assertEquals("test", cmd.args.get(0));
        assertEquals("optionB", cmd.args.get(2));
    }

    @Test(expected = ParseException.class)
    public void testStopAtNonOptionWithMissingOption() throws ParseException
    {
        String[] args = {"test", "--optionA"};

        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("optionB", true, "An option");

        parser.setOptions(options);

        try
        {
            parser.parse(args, null, true);
        }
        catch (MissingOptionException e)
        {
            fail("Expected ParseException");
        }

        fail("Expected ParseException");
    }
}