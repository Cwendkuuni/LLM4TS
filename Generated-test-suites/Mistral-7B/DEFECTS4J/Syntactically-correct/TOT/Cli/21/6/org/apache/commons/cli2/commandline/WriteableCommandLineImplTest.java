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
        option = new Option("t", "test", false, "Test option");
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertSame(option, commandLine.getOption("t"));
    }

    @Test
    public void testGetOptionWithTrigger() {
        commandLine.addOption(option);
        assertSame(option, commandLine.getOption("test"));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testNoOption() {
        assertFalse(commandLine.hasOption(new Option("x", "extra", false, "Extra option")));
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
    public void testGetSwitchDefaultValue() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, false);

        Boolean switchValue = commandLine.getSwitch(option, true);
        assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addOption(new Option("p", "property", true, "Property option"));
        commandLine.addProperty(option, "property", "value");

        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testGetPropertyWithDefaultValue() {
        commandLine.addOption(new Option("p", "property", true, "Property option"));
        commandLine.addProperty(option, "property", null);

        String propertyValue = commandLine.getProperty("property", "defaultValue");
        assertEquals("defaultValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        commandLine.addOption(new Option("p", "property", true, "Property option"));
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Set<String> properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testGetPropertiesEmpty() {
        Set<String> properties = commandLine.getProperties(option);
        assertEquals(0, properties.size());
    }

    @Test
    public void testToString() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        String commandLineString = commandLine.toString();
        String expected = "test value1";
        assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);

        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        commandLine.addOption(new Option("x", "extra", false, "Extra option"));

        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("t"));
        assertTrue(optionTriggers.contains("test"));
        assertTrue(optionTriggers.contains("x"));
        assertTrue(optionTriggers.contains("extra"));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);

        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = NullPointerException.class)
    public void testSetDefaultValuesNullOption() {
        commandLine.setDefaultValues(null, Collections.emptyList());
    }

    @Test(expected = NullPointerException.class)
    public void testSetDefaultSwitchNullOption() {
        commandLine.setDefaultSwitch(null, true);
    }

    @Test(expected = NullPointerException.class)
    public void testSetDefaultValuesNullValues() {
        commandLine.addOption(option);
        commandLine.setDefaultValues(option, null);
    }

    @Test(expected = NullPointerException.class)
    public void testSetDefaultSwitchNullValue() {
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, null);
    }

    @Test
    public void testSetDefaultValuesAndProperties() {
        commandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);
        commandLine.addProperty(option, "property", "value");

        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("defaultValue1", values.get(0));

        String propertyValue = commandLine.getProperty("property");
        assertEquals("value", propertyValue);
    }
}