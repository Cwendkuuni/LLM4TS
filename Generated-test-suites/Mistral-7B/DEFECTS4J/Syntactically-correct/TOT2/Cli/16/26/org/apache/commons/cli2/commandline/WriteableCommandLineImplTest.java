package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_AddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option2", "o2", false, "Description for option2");
        writeableCommandLine.addOption(option);
        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
    }

    @Test
    public void testWriteableCommandLineImpl_AddValue() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1", null, true), null);
        writeableCommandLine.addValue(new Option("option1"), "value1");
        List values = writeableCommandLine.getValues(new Option("option1"), null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_AddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option1", null, false, "Description for option1");
        writeableCommandLine.addSwitch(option, true);
        Boolean value = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(value);
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_AddSwitch_AlreadySet() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option1", null, false, "Description for option1");
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, false);
    }

    @Test
    public void testWriteableCommandLineImpl_GetOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option1", null, false, "Description for option1");
        writeableCommandLine.addOption(option);
        Option retrievedOption = writeableCommandLine.getOption("option1");
        Assert.assertEquals(option, retrievedOption);
    }

    @Test
    public void testWriteableCommandLineImpl_GetValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1", null, true), null);
        writeableCommandLine.addValue(new Option("option1"), "value1");
        writeableCommandLine.addValue(new Option("option1"), "value2");
        List values = writeableCommandLine.getValues(new Option("option1"), null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_GetUndefaultedValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1", null, true), null);
        writeableCommandLine.addValue(new Option("option1"), "value1");
        List undefaultedValues = writeableCommandLine.getUndefaultedValues(new Option("option1"));
        Assert.assertEquals(1, undefaultedValues.size());
        Assert.assertEquals("value1", undefaultedValues.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_GetSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option1", null, false, "Description for option1");
        writeableCommandLine.addSwitch(option, true);
        Boolean value = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(value);
        value = writeableCommandLine.getSwitch(option, null);
        Assert.assertTrue(value);
    }

    @Test
    public void testWriteableCommandLineImpl_GetProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option1", null, true);
        option.setLongOpt("longOption");
        writeableCommandLine.addProperty(option, "property1", "value1");
        String propertyValue = writeableCommandLine.getProperty(option, "property1");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_AddProperty() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option1", null, true);
        Map<String, String> properties = new HashMap<>();
        properties.put("property1", "value1");
        writeableCommandLine.addProperty(option, properties);
        Map<String, String> retrievedProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(1, retrievedProperties.size());
        Assert.assertEquals("value1", retrievedProperties.get("property1"));
    }

    @Test
    public void testWriteableCommandLineImpl_ToString() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        writeableCommandLine.addValue(new Option("option1"), "value1");
        writeableCommandLine.addValue(new Option("option1"), "value2");
        String toString = writeableCommandLine.toString();
        String expectedToString = "value1 value2";
        Assert.assertEquals(expectedToString, toString);
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptions() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option1 = new Option("option1", null, false, "Description for option1");
        Option option2 = new Option("option2", null, false, "Description for option2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(2, options.size());
        Assert.assertTrue(options.contains(option1));
        Assert.assertTrue(options.contains(option2));
    }

    @Test
    public void testWriteableCommandLineImpl_GetOptionTriggers() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option1 = new Option("option1", null, false, "Description for option1");
        Option option2 = new Option("option2", null, false, "Description for option2");
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
        Assert.assertTrue(optionTriggers.containsAll(Arrays.asList("option1", "option2")));
    }

    @Test
    public void testWriteableCommandLineImpl_SetDefaultValues() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option1", null, true, "Description for option1");
        List defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        writeableCommandLine.setDefaultValues(option, defaults);
        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertTrue(values.containsAll(defaults));
    }

    @Test
    public void testWriteableCommandLineImpl_SetDefaultSwitch() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        Option option = new Option("option1", null, false, "Description for option1");
        writeableCommandLine.setDefaultSwitch(option, true);
        Boolean value = writeableCommandLine.getSwitch(option, false);
        Assert.assertTrue(value);
    }

    @Test
    public void testWriteableCommandLineImpl_GetNormalised() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("option1"), null);
        writeableCommandLine.addValue(new Option("option1"), "value1");
        writeableCommandLine.addValue(new Option("option1"), "value2");
        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertTrue(normalised.containsAll(Arrays.asList("value1", "value2")));
    }
}