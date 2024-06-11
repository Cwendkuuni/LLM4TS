package org.apache.commons.cli.test;

import static org.apache.commons.cli.Option.*;
import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.junit.Test;

/**
 * Test cases for OptionBuilder class.
 */
public class OptionBuilderTest
{
    /**
     * Test creating an option with no arguments.
     */
    @Test
    public void testCreateNoArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withLongOpt("testOption")
                                      .isRequired()
                                      .create('T');

        CommandLine parser = new DefaultParser();
        CommandLine cmdLine = parser.parse(new String[] {"--testOption"});

        assertNotNull(cmdLine.getOptionValues("testOption"));
        assertEquals(0, cmdLine.getOptionValues("testOption").length);
    }

    /**
     * Test creating an option with one required argument.
     */
    @Test
    public void testCreateOneArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withLongOpt("testOption")
                                      .hasArg()
                                      .isRequired()
                                      .create('T');

        CommandLine parser = new DefaultParser();
        CommandLine cmdLine = parser.parse(new String[] {"--testOption", "testValue"});

        assertNotNull(cmdLine.getOptionValues("testOption"));
        assertEquals(1, cmdLine.getOptionValues("testOption").length);
        assertEquals("testValue", cmdLine.getOptionValues("testOption")[0]);
    }

    /**
     * Test creating an option with one optional argument.
     */
    @Test
    public void testCreateOneOptionalArg()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withLongOpt("testOption")
                                      .hasArg(true)
                                      .isRequired(false)
                                      .create('T');

        CommandLine parser = new DefaultParser();
        CommandLine cmdLine1 = parser.parse(new String[] {"--testOption"});
        CommandLine cmdLine2 = parser.parse(new String[] {"--testOption", "testValue"});

        assertNotNull(cmdLine1.getOptionValues("testOption"));
        assertEquals(0, cmdLine1.getOptionValues("testOption").length);
        assertNull(cmdLine1.getOptionValues("testOption")[0]);

        assertNotNull(cmdLine2.getOptionValues("testOption"));
        assertEquals(1, cmdLine2.getOptionValues("testOption").length);
        assertEquals("testValue", cmdLine2.getOptionValues("testOption")[0]);
    }

    /**
     * Test creating an option with multiple optional arguments.
     */
    @Test
    public void testCreateMultipleOptionalArgs()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withLongOpt("testOption")
                                      .hasArgs(3)
                                      .hasOptionalArgs()
                                      .create('T');

        CommandLine parser = new DefaultParser();
        CommandLine cmdLine1 = parser.parse(new String[] {"--testOption"});
        CommandLine cmdLine2 = parser.parse(new String[] {"--testOption", "arg1", "arg2"});
        CommandLine cmdLine3 = parser.parse(new String[] {"--testOption", "arg1", "arg2", "arg3"});

        assertNotNull(cmdLine1.getOptionValues("testOption"));
        assertEquals(0, cmdLine1.getOptionValues("testOption").length);

        assertNotNull(cmdLine2.getOptionValues("testOption"));
        assertEquals(2, cmdLine2.getOptionValues("testOption").length);
        assertEquals("arg1", cmdLine2.getOptionValues("testOption")[0]);
        assertEquals("arg2", cmdLine2.getOptionValues("testOption")[1]);

        assertNotNull(cmdLine3.getOptionValues("testOption"));
        assertEquals(3, cmdLine3.getOptionValues("testOption").length);
        assertEquals("arg1", cmdLine3.getOptionValues("testOption")[0]);
        assertEquals("arg2", cmdLine3.getOptionValues("testOption")[1]);
        assertEquals("arg3", cmdLine3.getOptionValues("testOption")[2]);
    }

    /**
     * Test creating an option with a value separator.
     */
    @Test
    public void testCreateValueSeparator()
    {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.withLongOpt("testOption")
                                      .hasArgs()
                                      .withValueSeparator(':')
                                      .create('T');

        CommandLine parser = new DefaultParser();
        CommandLine cmdLine = parser.parse(new String[] {"--testOption", "arg1:arg2"});

        assertNotNull(cmdLine.getOptionValues("testOption"));
        assertEquals(2, cmdLine.getOptionValues("testOption").length);
        assertEquals("arg1", cmdLine.getOptionValues("testOption")[0]);
        assertEquals("arg2", cmdLine.getOptionValues("testOption")[1]);
    }
}