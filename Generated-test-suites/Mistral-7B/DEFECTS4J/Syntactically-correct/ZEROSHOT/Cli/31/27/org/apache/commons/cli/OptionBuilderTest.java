package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import java.util.Arrays;

/**
 * Test class for OptionBuilder
 */
public class OptionBuilderTest
{
    @org.junit.Test
    public void testOptionBuilder()
    {
        // test withLongOpt
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
        Option expectedOption = optionBuilder.create('l');
        Option actualOption = optionBuilder.create('l');
        assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());

        // test hasArg
        optionBuilder.reset();
        optionBuilder = OptionBuilder.hasArg();
        expectedOption = optionBuilder.create('a');
        actualOption = optionBuilder.create('a');
        assertEquals(expectedOption.getValueSeparator(), Option.DEFAULT_VALUE_SEPARATOR);
        assertEquals(expectedOption.isRequired(), false);
        assertEquals(expectedOption.getArgName(), "arg");
        assertEquals(expectedOption.getArgs(), 1);
        assertEquals(expectedOption.getValue(null), null);

        // test hasOptionalArg
        optionBuilder.reset();
        optionBuilder = OptionBuilder.hasOptionalArg();
        expectedOption = optionBuilder.create('o');
        actualOption = optionBuilder.create('o');
        assertEquals(expectedOption.getValueSeparator(), Option.DEFAULT_VALUE_SEPARATOR);
        assertEquals(expectedOption.isRequired(), false);
        assertEquals(expectedOption.getArgName(), "arg");
        assertEquals(expectedOption.getArgs(), 1);
        assertEquals(expectedOption.getValue(null), null);
        assertEquals(expectedOption.getValues(), new String[0]);

        // test hasArgs
        optionBuilder.reset();
        optionBuilder = OptionBuilder.hasArgs();
        expectedOption = optionBuilder.create('m');
        actualOption = optionBuilder.create('m');
        assertEquals(expectedOption.getValueSeparator(), Option.DEFAULT_VALUE_SEPARATOR);
        assertEquals(expectedOption.isRequired(), false);
        assertEquals(expectedOption.getArgName(), "arg");
        assertEquals(expectedOption.getArgs(), Option.UNLIMITED_VALUES);

        // test hasArgs with value
        optionBuilder.reset();
        optionBuilder = OptionBuilder.hasArgs(2);
        expectedOption = optionBuilder.create('n');
        actualOption = optionBuilder.create('n');
        assertEquals(expectedOption.getValueSeparator(), Option.DEFAULT_VALUE_SEPARATOR);
        assertEquals(expectedOption.isRequired(), false);
        assertEquals(expectedOption.getArgName(), "arg");
        assertEquals(expectedOption.getArgs(), 2);

        // test withValueSeparator
        optionBuilder.reset();
        optionBuilder = OptionBuilder.withValueSeparator(':');
        expectedOption = optionBuilder.create('v');
        actualOption = optionBuilder.create('v');
        assertEquals(expectedOption.getValueSeparator(), ':');

        // test withDescription
        optionBuilder.reset();
        optionBuilder = OptionBuilder.withDescription("description");
        expectedOption = optionBuilder.create('d');
        actualOption = optionBuilder.create('d');
        assertEquals(expectedOption.getDescription(), "description");

        // test isRequired
        optionBuilder.reset();
        optionBuilder = OptionBuilder.isRequired();
        expectedOption = optionBuilder.create('r');
        actualOption = optionBuilder.create('r');
        assertEquals(expectedOption.isRequired(), true);

        // test hasOptionalArgs
        optionBuilder.reset();
        optionBuilder = OptionBuilder.hasOptionalArgs(3);
        expectedOption = optionBuilder.create('u');
        actualOption = optionBuilder.create('u');
        assertEquals(expectedOption.getValueSeparator(), Option.DEFAULT_VALUE_SEPARATOR);
        assertEquals(expectedOption.isRequired(), false);
        assertEquals(expectedOption.getArgName(), "arg");
        assertEquals(expectedOption.getArgs(), 3);

        String[] args = {"-l", "--longOpt", "value1", "value2"};
        OptionParser parser = new OptionParser();
        CommandLine line = null;

        try
        {
            line = parser.parse(args);
        }
        catch (ParseException e)
        {
            fail("Unexpected exception: " + e.getMessage());
        }

        assertEquals(1, line.getArgs().length);

        Option option = line.getOption("longOpt");
        assertEquals(option.getValue(), Arrays.asList("value1", "value2").toArray(new String[0]));
    }
}