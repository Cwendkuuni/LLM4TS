package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setup() {
        option = new Option("t", "test", false, "Test flag");
        commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Option retOption = commandLine.getOption("t");
        assertEquals(option, retOption);
    }

    @Test
    public void testGetValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        Map properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("value", properties.get("property"));
    }

    @Test
    public void testGetProperty() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "property", null));
    }

    @Test
    public void testGetProperties() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        String result = commandLine.toString();
        String expected = "value1 value2";
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        Option anotherOption = new Option("a", "another", false, "Another option");
        commandLine.addOption(anotherOption);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option));
        assertTrue(options.contains(anotherOption));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        commandLine.addOption(new Option("t", "test2", false, "Another test flag"));
        Set triggers = commandLine.getOptionTriggers();
        assertEquals(2, triggers.size());
        assertTrue(triggers.contains("t"));
        assertTrue(triggers.contains("test2"));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.addOption(option);
        List defaults = new ArrayList();
        defaults.add("defaultValue1");
        defaults.add("defaultValue2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testSetDefaultSwitchTwice() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.setDefaultSwitch(option, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultSwitchNullOption() {
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(null, true);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNullOption() {
        commandLine.addOption(option);
        commandLine.setDefaultValues(null, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultValuesNullValues() {
        commandLine.addOption(option);
        List values = new ArrayList();
        commandLine.setDefaultValues(option, values);
    }
}