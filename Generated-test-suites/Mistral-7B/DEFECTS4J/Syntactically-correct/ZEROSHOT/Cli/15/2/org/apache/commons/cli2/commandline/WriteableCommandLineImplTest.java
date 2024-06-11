package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testCreateInstance() {
        Option rootOption = new Option("opt", "option", true, "Option description");
        List arguments = new ArrayList();

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        Assert.assertNotNull(commandLine);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());

        commandLine.addOption(option);

        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));

        Option optionWithTriggers = new Option("t", "trigger", false, "Trigger description");
        commandLine.addOption(optionWithTriggers);

        options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option, options.get(0));
        Assert.assertEquals(optionWithTriggers, options.get(1));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "Switch description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        boolean hasOption = commandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("o");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));

        commandLine.setDefaultValues(option, Collections.singletonList("defaultValue"));
        values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "Switch description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        commandLine.addSwitch(option, false);
        switchValue = commandLine.getSwitch(option, true);
        Assert.assertFalse(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "Property option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName", "propertyValue");
        String propertyValue = commandLine.getProperty("propertyName");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "Property option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName1", "propertyValue1");
        Map<String, String> properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("propertyValue1", properties.get("propertyName1"));

        commandLine.addProperty(option, "propertyName2", "propertyValue2");
        properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("propertyValue1", properties.get("propertyName1"));
        Assert.assertEquals("propertyValue2", properties.get("propertyName2"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "propertyName1", "propertyValue1");
        Map<String, String> properties = commandLine.getProperties(option);
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("propertyValue1", properties.get("propertyName1"));

        commandLine.addProperty(option, "propertyName2", "propertyValue2");
        properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("propertyValue1", properties.get("propertyName1"));
        Assert.assertEquals("propertyValue2", properties.get("propertyName2"));

        properties = commandLine.getProperties();
        Assert.assertEquals(0, properties.size());
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine.setNormalised(arguments);

        String commandLineString = commandLine.toString();
        Assert.assertEquals("arg1 arg2", commandLineString);
    }

    @Test
    public void testGetOptionsList() {
        Option option1 = new Option("o1", "option1", false, "Option 1 description");
        Option option2 = new Option("o2", "option2", false, "Option 2 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List<Option> options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option1, options.get(0));
        Assert.assertEquals(option2, options.get(1));
    }

    @Test
    public void testGetOptionTriggersSet() {
        Option option1 = new Option("o1", "option1", false, "Option 1 description");
        Option option2 = new Option("o2", "option2", false, "Option 2 description");
        Option option3 = new Option("o3", null, false, "Option 3 description");
        Option option4 = new Option("o4", "o4", false, "Option 4 description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        commandLine.addOption(option4);

        Set<String> optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(4, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("o1", "o2", "o3", "o4")));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("defaultValue1", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "Switch description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);

        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetNormalisedList() {
        Option option = new Option("o", "option", false, "Option description");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("root"), Collections.emptyList());
        commandLine.addOption(option);
        List arguments = new ArrayList();
        arguments.add("arg1");
        arguments.add("arg2");
        commandLine.setNormalised(arguments);

        List normalised = commandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("arg1", normalised.get(0));
        Assert.assertEquals("arg2", normalised.get(1));
    }
}