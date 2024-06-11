package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParser() throws Exception
    {
        // Test case for parsing a simple command line with no options or arguments
        Options options = new Options();
        String[] arguments = new String[0];
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);
        Assert.assertEquals(Arrays.asList(new String[0]), cmd.getArgs());
        Assert.assertNull(cmd.getOptionValues(null));

        // Test case for parsing a command line with required options
        options = new Options();
        options.addRequiredOption("a", "optionA", true, "an option value");
        options.addRequiredOption("b", "optionB", true, "another option value");
        arguments = new String[]{"--optionA", "--optionB"};
        cmd = parser.parse(options, arguments, false);
        List<String> args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList(new String[0]), args);
        Assert.assertEquals(Arrays.asList("an option value", "another option value"), cmd.getOptionValues("a"));

        // Test case for parsing a command line with optional options
        options = new Options();
        options.addOption("a", "optionA", false, "an option value");
        options.addOption("b", "optionB", false, "another option value");
        options.addOption("c", "optionC", false, "another option value");
        arguments = new String[]{"--optionA", "--optionB", "--optionC"};
        cmd = parser.parse(options, arguments, false);
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList(new String[0]), args);
        Assert.assertEquals(Arrays.asList("an option value", null, null), cmd.getOptionValues(new String[]{"a", "b", "c"}));

        // Test case for parsing a command line with positional arguments
        options = new Options();
        options.addRequiredOption("a", "optionA", false, "an option value");
        arguments = new String[]{"optionA", "arg1", "arg2"};
        cmd = parser.parse(options, arguments, false);
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), args);
        Assert.assertEquals(Arrays.asList("optionA"), cmd.getOptionValues(null));

        // Test case for parsing a command line with mixed options and arguments
        options = new Options();
        options.addRequiredOption("a", "optionA", false, "an option value");
        options.addOption("b", "optionB", false, "another option value");
        arguments = new String[]{"--optionA", "arg1", "--optionB", "arg2"};
        cmd = parser.parse(options, arguments, false);
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), args);
        Assert.assertEquals(Arrays.asList("optionA"), cmd.getOptionValues(new String[]{"a"}));
        Assert.assertEquals(Arrays.asList("another option value"), cmd.getOptionValues("b"));

        // Test case for parsing a command line with multiple occurrences of the same option
        options = new Options();
        options.addOption("a", "optionA", false, "an option value");
        arguments = new String[]{"--optionA", "--optionA", "arg1", "arg2"};
        cmd = parser.parse(options, arguments, false);
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList("arg1", "arg2"), args);
        Assert.assertEquals(Arrays.asList("optionA", "optionA"), cmd.getOptionValues(new String[]{"a"}));

        // Test case for parsing a command line with an unrecognized option
        options = new Options();
        arguments = new String[]{"--unknown"};
        try
        {
            cmd = parser.parse(options, arguments, false);
            fail("Expected ParseException");
        } catch (ParseException e)
        {
            String message = e.getMessage();
            assertTrue(message.contains("Unrecognized option: unknown"));
        }

        // Test case for parsing a command line with an option that requires an argument, but none is provided
        options = new Options();
        options.addOption("a", "optionA", true, "an option value");
        arguments = new String[]{"--optionA"};
        try
        {
            cmd = parser.parse(options, arguments, false);
            fail("Expected MissingArgumentException");
        } catch (ParseException e)
        {
            if (e.getCause() instanceof MissingArgumentException)
            {
                String message = e.getCause().getMessage();
                assertTrue(message.contains("option A requires an argument."));
            } else
            {
                fail("Unexpected exception: " + e.getMessage());
            }
        }

        // Test case for parsing a command line with an option that has a default value
        options = new Options();
        options.addOption("a", "optionA", false, "an option value (default: none)");
        arguments = new String[0];
        cmd = parser.parse(options, arguments, false);
        Assert.assertEquals(Arrays.asList(null), cmd.getOptionValues("a"));
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList(new String[0]), args);

        arguments = new String[]{"--optionA"};
        cmd = parser.parse(options, arguments, false);
        Assert.assertEquals(Arrays.asList("none"), cmd.getOptionValues("a"));
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList(new String[0]), args);

        arguments = new String[]{"--optionA", "value"};
        cmd = parser.parse(options, arguments, false);
        Assert.assertEquals(Arrays.asList("value"), cmd.getOptionValues("a"));
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList(new String[0]), args);

        // Test case for parsing a command line with an option that has an argument list
        options = new Options();
        options.addOption("a", "optionA", true, "an option value");
        options.addOption("b", "optionB", true, "an option value");
        options.addOption("c", "optionC", true, "an option value");
        arguments = new String[]{"--optionA=value1", "--optionB=value2", "--optionC=value3"};
        cmd = parser.parse(options, arguments, false);
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList(new String[0]), args);
        Assert.assertEquals(Arrays.asList(new String[]{"value1", "value2", "value3"}), cmd.getOptionValues(new String[]{"a", "b", "c"}));

        // Test case for parsing a command line with an option that has an optional argument
        options = new Options();
        options.addOption("a", "optionA", false, "an option value");
        options.addOption("b", "optionB", false, "an option value [default: none]");
        arguments = new String[]{"--optionA", "--optionB=value"};
        cmd = parser.parse(options, arguments, false);
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList(new String[0]), args);
        Assert.assertEquals(Arrays.asList("value"), cmd.getOptionValues("b"));

        arguments = new String[]{"--optionA", "--optionB"};
        cmd = parser.parse(options, arguments, false);
        args = cmd.getArgs();
        Assert.assertEquals(Arrays.asList(new String[0]), args);
        Assert.assertEquals(Arrays.asList(null), cmd.getOptionValues("b"));
    }
}