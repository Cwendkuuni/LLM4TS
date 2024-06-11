package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest
{
    @Test
    public void testOptionBuilder()
    {
        // Test case 1: Basic usage
        OptionBuilder ob = OptionBuilder.withLongOpt("optionA")
                .hasArg()
                .withArgName("argA")
                .isRequired()
                .withDescription("This is an optional description for optionA")
                .withType(String.class);

        Option optionA = ob.create('A');

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine = null;

        try
        {
            commandLine = parser.parse(new String[]{"-A", "argValueA"});
            String argValueA = optionA.getValue(commandLine);
            assertEquals("argValueA", argValueA);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test case 2: Optional argument
        OptionBuilder ob2 = OptionBuilder.withLongOpt("optionB")
                .hasOptionalArg()
                .withArgName("argB")
                .withDescription("This is an optional description for optionB")
                .withType(String.class);

        Option optionB = ob2.create('B');

        CommandLine commandLineB = null;

        try
        {
            commandLineB = parser.parse(new String[]{"-B", "argValueB"});
            String argValueB = optionB.getValue(commandLineB);
            assertEquals("argValueB", argValueB);

            commandLineB = parser.parse(new String[]{"-B"});
            assertNull(optionB.getValue(commandLineB));
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        // Test case 3: Multiple arguments
        OptionBuilder ob3 = OptionBuilder.withLongOpt("optionC")
                .hasArgs(2)
                .withArgName("argC1 argC2")
                .withDescription("This is an optional description for optionC")
                .withType(String[].class);

        Option optionC = ob3.create('C');

        CommandLine commandLineC = null;

        try
        {
            commandLineC = parser.parse(new String[]{"-C", "argValueC1", "argValueC2"});
            String[] argsC = optionC.getValues(commandLineC);
            assertEquals("argValueC1", argsC[0]);
            assertEquals("argValueC2", argsC[1]);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }
    }
}