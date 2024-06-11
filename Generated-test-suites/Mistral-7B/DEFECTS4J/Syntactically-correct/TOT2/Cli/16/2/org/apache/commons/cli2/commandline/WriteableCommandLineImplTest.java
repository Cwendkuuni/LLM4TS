package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("p", "property", true, "propertyValue"));

        Option option = new Option("p", "property");
        writeableCommandLine.addOption(option);

        List<Option> options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertEquals(option, options.get(1));

        Option optionWithTriggers = new Option("i", "input");
        writeableCommandLine.addOption(optionWithTriggers);

        Assert.assertEquals(3, options.size());
        Assert.assertEquals(optionWithTriggers, options.get(2));

        Map<String, Option> nameToOption = writeableCommandLine.getNameToOption();
        Option optionByName = nameToOption.get("p");
        Assert.assertEquals(option, optionByName);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("v", "value", 1, "testValue"));

        writeableCommandLine.addValue(writeableCommandLine.getOption("v"), "newValue");

        List<Object> values = writeableCommandLine.getValues(writeableCommandLine.getOption("v"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("newValue", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("s", "switch"));

        writeableCommandLine.addSwitch(writeableCommandLine.getOption("s"), false);

        Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("s"), null);
        Assert.assertFalse(switchValue);

        writeableCommandLine.addSwitch(writeableCommandLine.getOption("s"), true);

        switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("s"), null);
        Assert.assertTrue(switchValue);

        writeableCommandLine.addSwitch(writeableCommandLine.getOption("s"), null);

        switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("s"), null);
        Assert.assertNull(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        Option option = new Option("s", "switch");
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, false);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("v", "value", 1, "testValue"));

        boolean hasOption = writeableCommandLine.hasOption(writeableCommandLine.getOption("v"));
        Assert.assertTrue(hasOption);

        hasOption = writeableCommandLine.hasOption(new Option("x", "nonexistentOption"));
        Assert.assertFalse(hasOption);
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("v", "value", 1, "testValue"));

        Option option = writeableCommandLine.getOption("v");
        Assert.assertEquals(option, writeableCommandLine.getOption("value"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("v", "value", 1, "testValue"));

        writeableCommandLine.addValue(writeableCommandLine.getOption("v"), "value1");
        writeableCommandLine.addValue(writeableCommandLine.getOption("v"), "value2");

        List<Object> values = writeableCommandLine.getValues(writeableCommandLine.getOption("v"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("v", "value", 1, "testValue"));

        List<Object> values = writeableCommandLine.getUndefaultedValues(writeableCommandLine.getOption("v"));
        Assert.assertEquals(0, values.size());

        writeableCommandLine.addValue(writeableCommandLine.getOption("v"), "value1");

        values = writeableCommandLine.getUndefaultedValues(writeableCommandLine.getOption("v"));
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("s", "switch"));

        Boolean switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("s"), null);

        // Default value should be false
        Assert.assertFalse(switchValue);

        writeableCommandLine.addSwitch(writeableCommandLine.getOption("s"), true);

        switchValue = writeableCommandLine.getSwitch(writeableCommandLine.getOption("s"), null);

        // Switch value should be true now
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testgetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("p", "property", true, "propertyValue"));

        writeableCommandLine.addProperty(writeableCommandLine.getOption("p"), "name", "testName");

        String propertyValue = writeableCommandLine.getProperty(writeableCommandLine.getOption("p"), "name");
        Assert.assertEquals("testName", propertyValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("p", "property", true, "propertyValue"));

        writeableCommandLine.addProperty(writeableCommandLine.getOption("p"), "name1", "testName1");
        writeableCommandLine.addProperty(writeableCommandLine.getOption("p"), "name2", "testName2");

        Map<String, String> properties = writeableCommandLine.getProperties(writeableCommandLine.getOption("p"));
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("testName1", properties.get("name1"));
        Assert.assertEquals("testName2", properties.get("name2"));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("p", "property", true, "propertyValue"));

        Map<String, String> properties = writeableCommandLine.getProperties();
        Assert.assertEquals(0, properties.size());

        writeableCommandLine.addProperty(writeableCommandLine.getOption("p"), "name1", "testName1");
        writeableCommandLine.addProperty(writeableCommandLine.getOption("p"), "name2", "testName2");

        properties = writeableCommandLine.getProperties();
        Assert.assertEquals(2, properties.size());
        Assert.assertEquals("testName1", properties.get("p.name1"));
        Assert.assertEquals("testName2", properties.get("p.name2"));
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("v", "value", 1, "testValue"));
        writeableCommandLine.addValue(writeableCommandLine.getOption("v"), "value1");

        String toString = writeableCommandLine.toString();
        List<String> args = Arrays.asList("--option", "value1");

        Assert.assertEquals(args.toString(), toString);
    }

    @Test
    public void testGetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("v", "value", 1, "testValue"));

        List<Option> options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "option"), Arrays.asList("--option", "-o"));
        writeableCommandLine.addOption(new Option("v", "value", 1, "testValue"));

        Set<String> optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(3, optionTriggers.size());
        Assert.assertTrue(optionTriggers.contains("--option"));
        Assert.assertTrue(optionTriggers.contains("-o"));
        Assert.assertTrue(optionTriggers.contains("v"));
    }
}