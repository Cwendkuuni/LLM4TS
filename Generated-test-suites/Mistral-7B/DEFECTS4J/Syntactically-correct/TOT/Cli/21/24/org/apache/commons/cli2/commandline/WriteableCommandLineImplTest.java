package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option optionWithValue;
    private Option optionWithSwitch;
    private Option optionWithProperty;

    @Before
    public void setUp() {
        Option rootOption = new Option("t", "test", true, "test option");
        commandLine = new WriteableCommandLineImpl(rootOption, Collections.emptyList());

        optionWithValue = new Option("v", "value", true, "specify a value");
        optionWithValue.setLongOpt("value-long");

        optionWithSwitch = new Option("s", "switch", false, "toggle a switch");
        optionWithSwitch.setLongOpt("switch-long");

        optionWithProperty = new Option("p", "property", true, "set a property");
        optionWithProperty.setValueSeparator('=');

        commandLine.addOption(optionWithValue);
        commandLine.addOption(optionWithSwitch);
        commandLine.addOption(optionWithProperty);
    }

    @Test
    public void testAddValueOption() {
        commandLine.addValue(optionWithValue, "testValue");
        List values = commandLine.getValues(optionWithValue, null);
        Assert.assertEquals("testValue", values.get(0));
    }

    @Test
    public void testAddSwitchOption() {
        commandLine.addSwitch(optionWithSwitch, true);
        boolean switchValue = commandLine.getSwitch(optionWithSwitch, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddPropertyOption() {
        commandLine.addProperty(optionWithProperty, "propertyName", "propertyValue");
        String propertyValue = commandLine.getProperty(optionWithProperty, "propertyName", "defaultPropertyValue");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddPropertyOptionDuplicate() {
        commandLine.addProperty(optionWithProperty, "propertyName", "propertyValue");
        commandLine.addProperty(optionWithProperty, "propertyName", "newPropertyValue");
    }

    @Test
    public void testHasOption() {
        Assert.assertTrue(commandLine.hasOption(optionWithValue));
        Assert.assertTrue(commandLine.hasOption(optionWithSwitch));
        Assert.assertTrue(commandLine.hasOption(optionWithProperty));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("v");
        Assert.assertEquals(optionWithValue, option);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(optionWithValue, "testValue1");
        commandLine.addValue(optionWithValue, "testValue2");
        List values = commandLine.getValues(optionWithValue, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("testValue1", values.get(0));
        Assert.assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = commandLine.getUndefaultedValues(optionWithValue);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(optionWithSwitch, false);
        boolean switchValue = commandLine.getSwitch(optionWithSwitch, true);
        Assert.assertFalse(switchValue);

        commandLine.addSwitch(optionWithSwitch, true);
        switchValue = commandLine.getSwitch(optionWithSwitch, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(optionWithProperty, "propertyName", "propertyValue");
        String propertyValue = commandLine.getProperty(optionWithProperty, "propertyName");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty(optionWithProperty, "propertyName1", "propertyValue1");
        commandLine.addProperty(optionWithProperty, "propertyName2", "propertyValue2");
        Set properties = commandLine.getProperties(optionWithProperty);
        Assert.assertEquals(2, properties.size());
    }

    @Test
    public void testToString() {
        commandLine.addValue(optionWithValue, "testValue1");
        commandLine.addValue(optionWithValue, "testValue2");
        commandLine.addSwitch(optionWithSwitch, true);
        commandLine.addProperty(optionWithProperty, "propertyName1", "propertyValue1");
        commandLine.addProperty(optionWithProperty, "propertyName2", "propertyValue2");

        String commandLineString = commandLine.toString();
        String expected = "test --value=\"testValue1\" --value=\"testValue2\" --switch";
        Assert.assertEquals(expected, commandLineString);
    }
}