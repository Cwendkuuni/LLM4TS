package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest
{
    @org.junit.Test
    public void testOptionBuilder()
    {
        OptionBuilder ob = new OptionBuilder();

        // Test withLongOpt
        Option option1 = ob.withLongOpt("inputFile")
                .hasArg()
                .create('i');

        assertEquals("inputFile", option1.getLongOpt());
        assertEquals('i', option1.getValueSeparator());
        assertEquals("Input file", option1.getDescription());
        assertTrue(option1.isRequired());
        assertEquals(1, option1.getArgPosition());
        assertNull(option1.getValue());

        // Test withLongOpt and description
        Option option2 = ob.withLongOpt("outputFile")
                .hasArg()
                .withDescription("Output file")
                .create('o');

        assertEquals("outputFile", option2.getLongOpt());
        assertEquals('o', option2.getValueSeparator());
        assertEquals("Output file", option2.getDescription());
        assertTrue(option2.isRequired());
        assertEquals(1, option2.getArgPosition());
        assertNull(option2.getValue());

        // Test hasArg and noArg
        Option option3 = ob.hasArg()
                .create('a');

        assertFalse(option3.isRequired());
        assertEquals(1, option3.getArgPosition());
        assertNull(option3.getValue());

        Option option4 = ob.hasArg(false)
                .create('b');

        assertFalse(option4.isRequired());
        assertFalse(option4.isOptional());
        assertEquals(0, option4.getArgPosition());
        assertNull(option4.getValue());

        // Test isRequired
        Option option5 = ob.isRequired()
                .create('c');

        assertTrue(option5.isRequired());
        assertEquals(0, option5.getArgPosition());
        assertNull(option5.getValue());

        // Test hasOptionalArg
        Option option6 = ob.hasOptionalArg()
                .create('d');

        assertTrue(option6.isOptional());
        assertEquals(0, option6.getArgPosition());
        assertNull(option6.getValue());

        // Test hasArgs
        Option option7 = ob.hasArgs()
                .create('e');

        assertTrue(option7.isOptional());
        assertEquals(Option.UNLIMITED_VALUES, option7.getArgPosition());
        assertNull(option7.getValue());

        // Test hasArgs with limit
        Option option8 = ob.hasArgs(2)
                .create('f');

        assertTrue(option8.isOptional());
        assertEquals(2, option8.getArgPosition());
        assertNull(option8.getValue());

        // Test withType
        Option option9 = ob.withType(Integer.TYPE)
                .hasArg()
                .create('g');

        assertEquals(Integer.TYPE, option9.getType());
        assertTrue(option9.isRequired());
        assertEquals(1, option9.getArgPosition());
        assertNull(option9.getValue());

        // Test parse command line
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-i", "input.txt", "-o", "output.txt", "-a", "arg1", "-a", "arg2"};
        CommandLine commandLine = null;

        try
        {
            commandLine = parser.parse(option1, args);
            assertEquals("input.txt", option1.getValue(commandLine));
        }
        catch (ParseException e)
        {
            fail("Exception raised: " + e.getMessage());
        }

        try
        {
            commandLine = parser.parse(option2, args);
            assertEquals("output.txt", option2.getValue(commandLine));
        }
        catch (ParseException e)
        {
            fail("Exception raised: " + e.getMessage());
        }

        try
        {
            commandLine = parser.parse(option3, args);
            assertNull(option3.getValue(commandLine));
        }
        catch (ParseException e)
        {
            fail("Exception raised: " + e.getMessage());
        }

        try
        {
            commandLine = parser.parse(option4, args);
            assertNull(option4.getValue(commandLine));
        }
        catch (ParseException e)
        {
            fail("Exception raised: " + e.getMessage());
        }

        try
        {
            commandLine = parser.parse(option5, args);
            fail("Option is required but not provided.");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            commandLine = parser.parse(option6, args);
            assertEquals("arg1", option6.getValue(commandLine));
            assertEquals("arg2", option6.getValue(commandLine, 1));
        }
        catch (ParseException e)
        {
            fail("Exception raised: " + e.getMessage());
        }

        try
        {
            commandLine = parser.parse(option7, args);
            fail("Too many arguments.");
        }
        catch (ParseException e)
        {
            // expected
        }

        try
        {
            commandLine = parser.parse(option8, args);
            assertEquals("arg1", option8.getValue(commandLine, 0));
            assertEquals("arg2", option8.getValue(commandLine, 1));
        }
        catch (ParseException e)
        {
            fail("Exception raised: " + e.getMessage());
        }

        try
        {
            commandLine = parser.parse(option9, args);
            assertEquals(new Integer(42), option9.getValue(commandLine));
        }
        catch (ParseException e)
        {
            fail("Exception raised: " + e.getMessage());
        }

        // Test parse command line with unknown option
        try
        {
            commandLine = parser.parse(option1, args);
            fail("Unknown option: a");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with ambiguous option
        try
        {
            Option opt = new Option("g", "group");
            ob.withLongOpt("group")
                    .hasArgs()
                    .create(opt.getOpt());
            commandLine = parser.parse(option1, args);
            fail("Ambiguous option: g");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with multiple options of the same type
        try
        {
            Option opt = new Option("g", "group");
            Option opt2 = new Option("g", "group");
            ob.withLongOpt("group")
                    .hasArgs()
                    .create(opt.getOpt());
            ob.withLongOpt("group")
                    .hasArgs()
                    .create(opt2.getOpt());
            commandLine = parser.parse(option1, args);
            fail("Multiple options of the same type: g");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with missing required option
        try
        {
            commandLine = parser.parse(option5, args);
            fail("Option is required but not provided.");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with too few arguments
        Option option10 = ob.hasArg()
                .create('h');

        try
        {
            commandLine = parser.parse(option10, args);
            fail("Option requires an argument but none was provided.");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with too many arguments
        Option option11 = ob.hasArgs()
                .create('j');

        try
        {
            commandLine = parser.parse(option11, args);
            fail("Too many arguments for option.");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with invalid argument
        Option option12 = new Option("k", "invalid", true, "invalid");
        OptionBuilder ob2 = new OptionBuilder();
        ob2.withLongOpt("invalid")
                .withValueSeparator(' ')
                .hasOptionalArg()
                .create(option12.getOpt());
        commandLine = parser.parse(option12, args);

        try
        {
            assertEquals("arg1 arg2", option12.getValue(commandLine));
            fail("Invalid argument format.");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with invalid value type
        Option option13 = new Option("l", "value", true, Integer.TYPE);
        OptionBuilder ob3 = new OptionBuilder();
        ob3.withLongOpt("value")
                .withValueSeparator(' ')
                .hasOptionalArg()
                .create(option13.getOpt());
        commandLine = parser.parse(option13, args);

        try
        {
            assertEquals(new Integer(42), option13.getValue(commandLine));
            fail("Invalid argument value type.");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with invalid option character
        Option option14 = new Option("m", "invalid", true, "invalid");
        OptionBuilder ob4 = new OptionBuilder();
        ob4.withLongOpt("invalid")
                .withValueSeparator(' ')
                .hasOptionalArg()
                .create(option14.getOpt());
        commandLine = parser.parse(option14, args);

        try
        {
            char optChar = option14.getOpt();
            fail("Invalid option character: " + optChar);
        }
        catch (IllegalArgumentException e)
        {
            // expected
        }

        // Test parse command line with invalid value separator
        Option option15 = new Option("n", "value", true, Integer.TYPE);
        OptionBuilder ob5 = new OptionBuilder();
        ob5.withLongOpt("value")
                .withValueSeparator(',')
                .hasOptionalArg()
                .create(option15.getOpt());
        commandLine = parser.parse(option15, args);

        try
        {
            assertEquals("arg1, arg2", option15.getValue(commandLine));
            fail("Invalid value separator.");
        }
        catch (ParseException e)
        {
            // expected
        }

        // Test parse command line with multiple options
        Option option16 = ob.hasArg()
                .create('p');
        Option option17 = ob.hasArg()
                .create('p');

        try
        {
            commandLine = parser.parse(option16, args);
            assertEquals("input.txt", option16.getValue(commandLine));
            assertEquals("output.txt", option17.getValue(commandLine));
        }
        catch (ParseException e)
        {
            fail("Exception raised: " + e.getMessage());
        }

        // Test help format
        CommandLine cmdLine = null;
        try
        {
            cmdLine = parser.parse(option1, args);
        }
        catch (ParseException e)
        {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("java", "Options test", true);
            fail("Exception raised: " + e.getMessage());
        }

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("java", "Options test", false, "inputFile, -i <inputFile>",
                "Sets the input file.");
        formatter.printHelp("java", "Options test", false, "outputFile, -o <outputFile>",
                "Sets the output file.");
        formatter.printHelp("java", "Options test", false, "a {}",
                "Optional argument with no arguments.");
        formatter.printHelp("java", "Options test", false, "a <arg>",
                "Optional argument with one argument.");
        formatter.printHelp("java", "Options test", false, "",
                "Required option with no arguments.");
        formatter.printHelp("java", "Options test", false, "<arg1> <arg2>",
                "Optional argument with two arguments.");
        formatter.printHelp("java", "Options test", false, "g <num>",
                "Optional argument of type Integer.");
        formatter.printHelp("java", "Options test", false, "",
                "Print this help message.");
    }
}