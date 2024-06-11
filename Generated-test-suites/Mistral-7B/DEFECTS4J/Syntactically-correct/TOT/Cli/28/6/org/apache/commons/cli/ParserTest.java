package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

/**
 * Test parser functionality.
 */
public class ParserTest
{
    /**
     * Test parsing simple command line with options and required arguments.
     */
    @Test
    public void testSimpleParse()
    {
        String[] arguments = new String[]{"-a", "--optionB=valueB", "-c", "argC"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B")
                .required(true)
                .longOpt("optionB")
                .valueSeparator('=')
                .hasArg();
        options.addOption("c", true, "Option C");

        try
        {
            CommandLine cmd = parser.parse(options, arguments);
            assertEquals("Option A should be set", true, cmd.hasOption("a"));
            assertEquals("Option B should be set", "valueB", cmd.getOptionValue("optionB"));
            assertEquals("Option C should be set", "argC", cmd.getOptionValue("c"));
            assertEquals("The required option should be present", 3, cmd.getArgs().size());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test parsing command line with multiple occurrences of the same option.
     */
    @Test
    public void testMultipleOptions()
    {
        String[] arguments = new String[]{"-a", "-a", "-a"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");

        try
        {
            CommandLine cmd = parser.parse(options, arguments);
            assertEquals("Option A should have been set 3 times", 3, cmd.getOptionValues("a").length);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test parsing command line with a long option.
     */
    @Test
    public void testLongOption()
    {
        String[] arguments = new String[]{"--optionB=valueB"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("b", false, "Option B")
                .longOpt("optionB");

        try
        {
            CommandLine cmd = parser.parse(options, arguments);
            assertEquals("Option B should be set", true, cmd.hasOption("optionB"));
            assertEquals("The value of the long option should be set to 'valueB'", "valueB", cmd.getOptionValue("optionB"));
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test parsing command line with a group of options.
     */
    @Test
    public void testOptionGroup()
    {
        String[] arguments = new String[]{"-g", "--groupA", "-f", "argF"};
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setDescription("Group of options");
        group.addOption(new Option("a", "groupA", false, "Option A in group"));
        group.addOption(new Option("f", "groupF", true, "Option F in group")
                .longOpt("groupF")
                .valueSeparator('='));
        options.addOptionGroup(group);

        try
        {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue("Option A in group should be set", cmd.hasOption("groupA"));
            assertEquals("The value of option F in group should be 'argF'", "argF", cmd.getOptionValue("groupF"));
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test parsing command line with an empty property file.
     */
    @Test
    public void testEmptyProperties()
    {
        String[] arguments = new String[]{"--propertyFile", ""};
        Parser parser = new DefaultParser();
        Options options = new Options();

        try
        {
            CommandLine cmd = parser.parse(options, arguments);
            assertTrue("No options should be present", cmd.getOptions().isEmpty());
            assertTrue("No args should be present", cmd.getArgs().isEmpty());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test parsing command line with a property file containing options.
     */
    @Test
    public void testPropertiesWithOptions()
    {
        String[] arguments = new String[]{"--propertyFile", "options.properties"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B")
                .longOpt("optionB")
                .valueSeparator('=')
                .hasArg();

        Properties props = new Properties();
        props.setProperty("optionB", "valueB");

        try
        {
            CommandLine cmd = parser.parse(options, arguments, props);
            assertEquals("Option A should not be set from properties", 0, cmd.getOptionValues("a").length);
            assertEquals("Option B should be set from properties", "valueB", cmd.getOptionValue("optionB"));
            assertTrue("The required option should be present", cmd.hasOption("optionB"));
            assertEquals("The value of the option from properties should be present in the args", "valueB", cmd.getArgs()[0]);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    /**
     * Test parsing command line with an unknown option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testUnknownOption()
    {
        String[] arguments = new String[]{"--unknownOption", "value"};
        Parser parser = new DefaultParser();
        Options options = new Options();

        try
        {
            parser.parse(options, arguments, null, true);
        }
        catch (ParseException e)
        {
            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
        fail("An UnrecognizedOptionException should have been thrown");
    }

    /**
     * Test parsing command line with a required option that is missing.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption()
    {
        String[] arguments = new String[]{"--optionB"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("b", true, "Option B")
                .required(true)
                .longOpt("optionB")
                .valueSeparator('=')
                .hasArg();

        try
        {
            parser.parse(options, arguments, null, true);
        }
        catch (ParseException e)
        {
            assertEquals("Missing required option: optionB", e.getMessage());
            throw e;
        }
        fail("A MissingOptionException should have been thrown");
    }

    /**
     * Test parsing command line with a non-existent option group.
     */
    @Test(expected = MissingCommandLineException.class)
    public void testMissingOptionGroup()
    {
        String[] arguments = new String[]{"-g"};
        Parser parser = new DefaultParser();
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setDescription("Group of options");
        group.addOption(new Option("a", "groupA", false, "Option A in group"));
        options.addOptionGroup(group);

        try
        {
            parser.parse(options, arguments, null, true);
        }
        catch (ParseException e)
        {
            if (e.getMessage().startsWith("No such option"))
            {
                // Ignore this error, as it's expected when the option group doesn't exist
                return;
            }

            throw new AssertionError("Unexpected exception: " + e.getMessage());
        }
        fail("A MissingCommandLineException should have been thrown");
    }

    /**
     * Test parsing command line with an inconsistent number of values for an option that requires an even number.
     */
    @Test(expected = ParseException.class)
    public void testOddNumberOfValuesForOption()
    {
        String[] arguments = new String[]{"--optionX", "value1", "value2", "value3"};
        Parser parser = new DefaultParser();
        Options options = new Options();
        options.addOption("x", true, "Option X")
                .required(true)
                .longOpt("optionX")
                .valueSeparator(',')
                .hasArg()
                .numberOfValues(2);

        try
        {
            parser.parse(options, arguments, null, true);
        }
        catch (ParseException e)
        {
            assertEquals("optionX requires an even number of values", e.getMessage());
            throw e;
        }
        fail("A ParseException should have been thrown");
    }
}