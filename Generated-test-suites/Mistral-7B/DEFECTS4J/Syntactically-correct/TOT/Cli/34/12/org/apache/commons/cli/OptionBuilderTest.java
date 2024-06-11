package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest
{
    @Test
    public void testSimpleOption()
    {
        Option option = OptionBuilder.hasArg()
                                     .withDescription("Test Option 1")
                                     .isRequired()
                                     .create('t');

        CommandLineArgs args = new CommandLineArgs(new String[]{"-t", "arg1"});
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(option, args.toArray());
            assertEquals("Test Option 1", option.getLongOpt());
            assertEquals("arg1", option.getValue());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testOptionalArgument()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                     .withDescription("Test Option 2")
                                     .create('o');

        CommandLineArgs args1 = new CommandLineArgs(new String[]{"-o"});
        CommandLineParser parser1 = new DefaultParser();
        try
        {
            CommandLine line1 = parser1.parse(option, args1.toArray());
            assertNull(option.getValue());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        CommandLineArgs args2 = new CommandLineArgs(new String[]{"-o", "arg1", "arg2"});
        CommandLineParser parser2 = new DefaultParser();
        try
        {
            CommandLine line2 = parser2.parse(option, args2.toArray());
            assertEquals("arg1", option.getValue());
            assertEquals(args2.toArray()[2], option.getArgs()[0]);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testMultipleArguments()
    {
        Option option = OptionBuilder.hasArgs(2)
                                     .withDescription("Test Option 3")
                                     .isRequired()
                                     .create('m');

        CommandLineArgs args1 = new CommandLineArgs(new String[]{"-m", "arg1"});
        CommandLineParser parser1 = new DefaultParser();
        try
        {
            CommandLine line1 = parser1.parse(option, args1.toArray());
            fail("Should have thrown ParseException");
        }
        catch (ParseException e)
        {
            // expected
        }

        CommandLineArgs args2 = new CommandLineArgs(new String[]{"-m", "arg1", "arg2"});
        CommandLineParser parser2 = new DefaultParser();
        try
        {
            CommandLine line2 = parser2.parse(option, args2.toArray());
            assertEquals("Test Option 3", option.getLongOpt());
            assertArrayEquals(new String[]{"arg1", "arg2"}, option.getArgs());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testValueSeparator()
    {
        Option option = OptionBuilder.withValueSeparator(':')
                                     .withDescription("Test Option 4")
                                     .isRequired()
                                     .create('v');

        CommandLineArgs args = new CommandLineArgs(new String[]{"-v", "arg1:arg2:arg3"});
        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine line = parser.parse(option, args.toArray());
            assertEquals("Test Option 4", option.getLongOpt());
            assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, option.getArgs());
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}