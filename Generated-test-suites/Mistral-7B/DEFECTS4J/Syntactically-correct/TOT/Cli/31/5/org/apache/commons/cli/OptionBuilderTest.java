package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

/**
 * Test for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test for hasArg() method.
     */
    @Test
    public void testHasArg()
    {
        Option option = OptionBuilder.hasArg()
                                     .withLongOpt("long-opt")
                                     .withDescription("a long option that requires an argument")
                                     .create('l');

        // test option properties
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("a long option that requires an argument", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(1, option.getArgCount());
        assertNull(option.getValue());

        // test parse command line with argument
        String[] args = {"-l", "arg"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(args, OptionHandler.createHandler());
        assertEquals("arg", option.getValue(cmd));
    }

    /**
     * Test for hasOptionalArg() method.
     */
    @Test
    public void testHasOptionalArg()
    {
        Option option = OptionBuilder.hasOptionalArg()
                                     .withLongOpt("long-opt")
                                     .withDescription("a long option that can have an optional argument")
                                     .create('l');

        // test option properties
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("a long option that can have an optional argument", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(1, option.getArgCount());
        assertNull(option.getValue());

        // test parse command line with argument
        String[] args = {"-l", "arg"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(args, OptionHandler.createHandler());
        assertEquals("arg", option.getValue(cmd));

        // test parse command line without argument
        String[] args2 = {"-l"};
        CommandLine cmd2 = parser.parse(args2, OptionHandler.createHandler());
        assertNull(option.getValue(cmd2));
    }

    /**
     * Test for hasArgs() method.
     */
    @Test
    public void testHasArgs()
    {
        Option option = OptionBuilder.hasArgs()
                                     .withLongOpt("long-opt")
                                     .withDescription("a long option that can have multiple arguments")
                                     .create('l');

        // test option properties
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("a long option that can have multiple arguments", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNLIMITED_VALUES, option.getArgCount());
        assertNull(option.getValue());

        // test parse command line with multiple arguments
        String[] args = {"-l", "arg1", "arg2"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(args, OptionHandler.createHandler());
        String[] values = option.getValues(cmd);
        assertEquals(2, values.length);
        assertEquals("arg1", values[0]);
        assertEquals("arg2", values[1]);
    }

    /**
     * Test for isRequired() method.
     */
    @Test
    public void testIsRequired()
    {
        Option option = OptionBuilder.isRequired()
                                     .withLongOpt("long-opt")
                                     .withDescription("a long option that is required")
                                     .create('l');

        // test option properties
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("a long option that is required", option.getDescription());
        assertEquals(true, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgCount());
        assertNull(option.getValue());

        // test parse command line without argument
        String[] args = {};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(args, OptionHandler.createHandler());
        assertNull(option.getValue(cmd));
        assertEquals(true, cmd.hasOption("long-opt"));

        // test parse command line with argument
        String[] args2 = {"-l", "arg"};
        CommandLine cmd2 = parser.parse(args2, OptionHandler.createHandler());
        assertEquals("arg", option.getValue(cmd2));
        assertEquals(true, cmd2.hasOption("long-opt"));
    }

    /**
     * Test for withLongOpt() method.
     */
    @Test
    public void testWithLongOpt()
    {
        Option option = OptionBuilder.withLongOpt("long-opt")
                                     .withDescription("a long option with long opt name")
                                     .create('l');

        // test option properties
        assertEquals("long-opt", option.getLongOpt());
        assertEquals("a long option with long opt name", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgCount());
        assertNull(option.getValue());

        // test parse command line with short opt
        String[] args = {"-l", "arg"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(args, OptionHandler.createHandler());
        assertEquals("arg", option.getValue(cmd));
        assertEquals(true, cmd.hasOption("long-opt"));

        // test parse command line with long opt
        String[] args2 = {"--long-opt", "arg"};
        CommandLine cmd2 = parser.parse(args2, OptionHandler.createHandler());
        assertEquals("arg", option.getValue(cmd2));
        assertEquals(true, cmd2.hasOption("long-opt"));
    }

    /**
     * Test for withDescription() method.
     */
    @Test
    public void testWithDescription()
    {
        Option option = OptionBuilder.withDescription("a description")
                                     .create('l');

        // test option properties
        assertNull(option.getLongOpt());
        assertEquals("a description", option.getDescription());
        assertEquals(false, option.isRequired());
        assertEquals(Option.UNINITIALIZED, option.getArgCount());
        assertNull(option.getValue());
    }
}