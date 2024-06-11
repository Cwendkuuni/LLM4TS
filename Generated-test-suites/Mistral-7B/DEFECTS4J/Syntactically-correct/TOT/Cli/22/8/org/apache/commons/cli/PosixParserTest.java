package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the PosixParser class
 */
public class PosixParserTest
{
    @Test
    public void testSimpleOption()
    {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Test option a");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try
        {
            parsedArgs = parser.parse(options, args, false);
            assertEquals("Test option a is supposed to be set", true, parsedArgs[0]);
            assertEquals("There should be no remaining arguments", 0, parsedArgs.length - 1);
        }
        catch (ParseException e)
        {
            fail("Expected no ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testOptionWithValue()
    {
        String[] args = {"-a", "value"};
        Options options = new Options();
        options.addOption("a", true, "Test option a");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try
        {
            parsedArgs = parser.parse(options, args, false);
            assertEquals("Test option a value should be 'value'", "value", parsedArgs[0]);
            assertEquals("There should be no remaining arguments", 0, parsedArgs.length - 1);
        }
        catch (ParseException e)
        {
            fail("Expected no ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testLongOption()
    {
        String[] args = {"--test-long-option"};
        Options options = new Options();
        options.addOption("test-long-option", true, "Test long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try
        {
            parsedArgs = parser.parse(options, args, false);
            assertEquals("Test long option is supposed to be set", true, parsedArgs[0]);
            assertEquals("There should be no remaining arguments", 0, parsedArgs.length - 1);
        }
        catch (ParseException e)
        {
            fail("Expected no ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testLongOptionWithValue()
    {
        String[] args = {"--test-long-option", "value"};
        Options options = new Options();
        options.addOption("test-long-option", true, "Test long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try
        {
            parsedArgs = parser.parse(options, args, false);
            assertEquals("Test long option value should be 'value'", "value", parsedArgs[0]);
            assertEquals("There should be no remaining arguments", 0, parsedArgs.length - 1);
        }
        catch (ParseException e)
        {
            fail("Expected no ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testNoOptions()
    {
        String[] args = {"foo", "bar"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try
        {
            parsedArgs = parser.parse(options, args, false);
            fail("Expected ParseException: unrecognized option: foo");
        }
        catch (ParseException e)
        {
            // Expected
        }

        assertEquals("There should be no parsed arguments", 0, parsedArgs.length);
    }

    @Test
    public void testTrailingOptions()
    {
        String[] args = {"--test-long-option", "value", "--another-option"};
        Options options = new Options();
        options.addOption("test-long-option", true, "Test long option");
        options.addOption("another-option", false, "Test another option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try
        {
            parsedArgs = parser.parse(options, args, false);
            assertEquals("Test long option value should be 'value'", "value", parsedArgs[0]);
            assertEquals("Test another option is supposed to be set", true, parsedArgs[1]);
            assertEquals("There should be no remaining arguments", 0, parsedArgs.length - 2);
        }
        catch (ParseException e)
        {
            fail("Expected no ParseException: " + e.getMessage());
        }
    }

    @Test
    public void testStopAtNonOption()
    {
        String[] args = {"--test-long-option", "value", "foo"};
        Options options = new Options();
        options.addOption("test-long-option", true, "Test long option");

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try
        {
            parsedArgs = parser.parse(options, args, true);
            fail("Expected ParseException: unrecognized option: foo");
        }
        catch (ParseException e)
        {
            // Expected
        }

        assertEquals("Test long option value should be 'value'", "value", parsedArgs[0]);
        assertEquals("There should be no remaining parsed arguments", 0, parsedArgs.length - 1);

        // Check remaining arguments
        assertEquals("There should be 1 remaining argument", 1, args.length - parsedArgs.length);
        assertEquals("Last argument should be 'foo'", "foo", args[args.length - 1]);
    }

    @Test
    public void testEmptyOption()
    {
        String[] args = {"--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        String[] parsedArgs;

        try
        {
            parsedArgs = parser.parse(options, args, false);
            fail("Expected ParseException: unrecognized option: " + (args.length > 0 ? args[0] : ""));
        }
        catch (ParseException e)
        {
            // Expected
        }

        // No parsed arguments should have been returned
        assertEquals("There should be no parsed arguments", 0, parsedArgs.length);
    }
}