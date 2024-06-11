package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setup() {
        option = new Option("t", "test", false, "Test option");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void shouldAddOption() {
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void shouldAddValueToOption() {
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void shouldAddSwitchToOption() {
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        assertFalse(switchValue);
    }

    @Test
    public void shouldReturnNullIfOptionNotExists() {
        assertNull(commandLine.getOption("unknownOption"));
    }

    @Test
    public void shouldReturnPropertyValue() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property", "defaultValue"));
    }

    @Test
    public void shouldHaveProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Set properties = commandLine.getProperties();
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void shouldLookLikeOption() {
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("unknown"));
    }

    @Test
    public void shouldToStringReturnCorrectString() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals("--test value1 value2", commandLine.toString());
    }

    @Test
    public void shouldReturnListOfOptions() {
        commandLine.addOption(option);
        commandLine.addOption(new Option("o", "option", true, "Option with value"));

        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertSame(option, options.get(0));
        assertSame(new Option("o", "option", true, "Option with value"), options.get(1));
    }
}