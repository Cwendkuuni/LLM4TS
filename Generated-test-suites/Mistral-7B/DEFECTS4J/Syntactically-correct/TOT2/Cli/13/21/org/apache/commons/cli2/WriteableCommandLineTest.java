package org.apache.commons.cli2;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WriteableCommandLine interface
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CliCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));

        // Test adding the same option twice
        commandLine.addOption(option);

        // Test conflicting short opts
        Option optionWithConflictingShortOpt = Option.builder("o").shortOpt("o").longOpt("option2").build();
        assertThrows(IllegalStateException.class, () -> commandLine.addOption(optionWithConflictingShortOpt));

        // Test invalid Option
        Option invalidOption = new Option("invalidOption");
        assertThrows(IllegalArgumentException.class, () -> commandLine.addOption(invalidOption));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getOptionValues("option");
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));

        // Test adding multiple values
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        // Test invalid value
        String invalidValue = new String(" Invalid Value ");
        assertThrows(IllegalArgumentException.class, () -> commandLine.addValue(option, invalidValue));

        // Test adding value to non-existent option
        assertThrows(IllegalArgumentException.class, () -> commandLine.addValue(new Option("invalidOption"), "value"));
    }

    @Test
    public void testLooksLikeOption() {
        // Test basic functionality
        assertTrue(commandLine.looksLikeOption("option"));
        assertFalse(commandLine.looksLikeOption("value"));

        // Test option already exists
        Option option = Option.builder("o").build();
        commandLine.addOption(option);
        assertFalse(commandLine.looksLikeOption("option"));

        // Test option value
        assertFalse(commandLine.looksLikeOption("option="));
    }
}