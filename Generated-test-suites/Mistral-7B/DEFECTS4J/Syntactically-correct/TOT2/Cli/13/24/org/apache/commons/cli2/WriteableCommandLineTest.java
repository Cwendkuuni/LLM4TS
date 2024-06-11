package org.junit.jupiter.api.func.writing.commandline;

import org.apache.commons.cli2.*;
import org.junit.*;
import static org.junit.Assert.*;
import java.util.*;
import static java.util.Arrays.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine createMockCommandLine() {
        return new CommandLineParser() {};
    }

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithExistingLongName() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        try {
            commandLine.addOption(option);
        } catch (Exception e) {
            fail("Should not throw an exception for adding an existing long name");
        }
        // Test for the actual exception
        Option option2 = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option2);
        assertThrows(IllegalArgumentException.class, () -> commandLine.addOption(option2));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        assertEquals("testValue", commandLine.getValuesForOption(option).get(0));
    }

    @Test
    public void testGetValuesForOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");
        List<Object> values = commandLine.getValuesForOption(option);
        assertEquals(asList("testValue1", "testValue2"), values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        List<Object> defaultValues = asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals(defaultValues, commandLine.getValuesForOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingSwitch() {
        WriteableCommandLine commandLine = createMockCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addSwitch(option, true);
        try {
            commandLine.addSwitch(option, true);
        } catch (Exception e) {
            fail("Should not throw an exception for adding an existing switch");
        }
        // Test for the actual exception
        try {
            commandLine.addSwitch(option, true);
        } catch (Exception e) {
            assertThrows(IllegalStateException.class, () -> commandLine.addSwitch(option, true));
        }
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createMockCommandLine();
        commandLine.addProperty("property", "testValue");
        assertEquals("testValue", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOptionWithOption() {
        WriteableCommandLine commandLine = createMockCommandLine();
        String argument = "testOption";
        assertTrue(commandLine.looksLikeOption(argument));
    }

    @Test
    public void testLooksLikeOptionWithValue() {
        WriteableCommandLine commandLine = createMockCommandLine();
        String argument = "testValue";
        assertFalse(commandLine.looksLikeOption(argument));
    }
}