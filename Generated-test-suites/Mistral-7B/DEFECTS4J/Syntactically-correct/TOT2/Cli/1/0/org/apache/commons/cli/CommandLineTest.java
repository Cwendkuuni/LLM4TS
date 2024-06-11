package org.junit.test;

import org.apache.commons.cli.CommandLine;
import org.junit.*;
import static org.junit.Assert.*;

public class CommandLineTest {

    CommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
    }

    @Test
    public void testHasOptionFalse() {
        assertFalse("Should not have option when command line is empty", commandLine.hasOption("option"));
    }

    @Test
    public void testHasOptionTrue() {
        commandLine.addOption(new Option("option", false, null, "an option"));
        assertTrue("Should have option when option is set", commandLine.hasOption("option"));
    }

    @Test
    public void testGetOptionValue() {
        commandLine.addOption(new Option("option", true, null, "an option"));
        commandLine.addArg("value");

        assertEquals("Should return option value", "value", commandLine.getOptionValue("option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionValueMissingOption() {
        commandLine.addArg("value");

        commandLine.getOptionValue("nonexistentOption");
    }

    @Test
    public void testGetOptionValuesEmpty() {
        assertNull("Should return null when no option is set", commandLine.getOptionValues("option"));
    }

    @Test
    public void testGetOptionValuesSingleValue() {
        commandLine.addOption(new Option("option", true, null, "an option"));
        commandLine.addArg("value");

        String[] expected = {"value"};
        assertArrayEquals("Should return array with expected value", expected, commandLine.getOptionValues("option"));
    }

    @Test
    public void testGetOptionValuesMultipleValues() {
        commandLine.addOption(new Option("option", true, null, "an option"));
        commandLine.addArg("value1");
        commandLine.addArg("value2");

        String[] expected = {"value1", "value2"};
        assertArrayEquals("Should return array with expected values", expected, commandLine.getOptionValues("option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionValuesMissingOption() {
        commandLine.addArg("value");

        commandLine.getOptionValues("nonexistentOption");
    }

    @Test
    public void testGetOptionValueWithNullArgument() {
        commandLine.addOption(new Option("option", true, null, "an option"));
        commandLine.addArg(null);

        assertNull("Should return null when option value is null", commandLine.getOptionValue("option"));
    }

    @Test
    public void testGetOptionValueWithArrayArgument() {
        commandLine.addOption(new Option("option", true, null, "an option"));
        commandLine.addArg(new String[]{"value1", "value2"});

        String[] expected = {"value1", "value2"};
        assertArrayEquals("Should return array with expected values when option value is an array", expected, commandLine.getOptionValues("option"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetOptionValuesWithInvalidArgument() {
        commandLine.addOption(new Option("option", true, null, "an option"));
        commandLine.addArg("invalid");

        commandLine.getOptionValues("option");
    }

    @Test
    public void testGetOptionValuesWithInvalidShortOptionName() {
        Option option = new Option('x', true, null, "an option");
        commandLine.addOption(option);
        commandLine.addArg("value");

        assertArrayEquals("Should return array with expected value when option value is valid, even if short option name is invalid", new String[]{"value"}, commandLine.getOptionValues("x"));
    }

    @Test
    public void testGetOptionValuesWithInvalidLongOptionName() {
        Option option = new Option("invalidOption", true, null, "an option");
        commandLine.addOption(option);
        commandLine.addArg("value");

        assertArrayEquals("Should return array with expected value when option value is valid, even if long option name is invalid", new String[]{"value"}, commandLine.getOptionValues("invalidOption"));
    }
}