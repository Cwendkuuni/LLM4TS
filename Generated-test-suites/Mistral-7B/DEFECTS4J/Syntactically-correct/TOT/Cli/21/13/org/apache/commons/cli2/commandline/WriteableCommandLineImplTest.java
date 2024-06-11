package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setUp() {
        option = new Option("o", "option", false, "This is an option");
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        assertSame(option, retrievedOption);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        List expectedValues = Collections.singletonList("value1");
        assertSame(expectedValues, values);
    }

    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty("property");
        assertSame("value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        Set expectedProperties = Collections.singleton("property1,property2");
        assertSame(expectedProperties, properties);
    }

    @Test
    public void testGetValuesForUnspecifiedOption() {
        commandLine.addOption(option);
        List values = commandLine.getValues(option, null);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetValuesForSpecifiedOptionWithEmptyValueList() {
        commandLine.addOption(option);
        commandLine.addValue(option, new String[]{"value1", "value2"});
        List values = commandLine.getValues(option, null);
        assertSame(Collections.emptyList(), values);
    }

    @Test
    public void testGetValuesForSpecifiedOption() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        List expectedValues = Collections.singletonList("value1");
        assertSame(expectedValues, values);
    }

    @Test
    public void testGetValuesForSpecifiedOptionWithMultipleValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, new String[]{"value1", "value2"});
        List values = commandLine.getValues(option, null);
        List expectedValues = Arrays.asList("value1", "value2");
        assertSame(expectedValues, values);
    }

    @Test
    public void testGetValuesForSpecifiedOptionWithDefaultValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, defaultValues);
        List expectedValues = Arrays.asList("value1", "defaultValue1", "defaultValue2");
        assertSame(expectedValues, values);
    }

    @Test
    public void testGetSwitchForSpecifiedSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetSwitchForUnspecifiedSwitch() {
        commandLine.addOption(option);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertFalse(switchValue);
    }

    @Test
    public void testGetSwitchForSpecifiedSwitchWithDefaultValue() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.setDefaultSwitch(option, Boolean.FALSE);
        Boolean switchValue = commandLine.getSwitch(option, Boolean.FALSE);
        assertFalse(switchValue);
    }

    @Test
    public void testToString() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "option value1";
        assertSame(expectedCommandLineString, commandLineString);
    }
}