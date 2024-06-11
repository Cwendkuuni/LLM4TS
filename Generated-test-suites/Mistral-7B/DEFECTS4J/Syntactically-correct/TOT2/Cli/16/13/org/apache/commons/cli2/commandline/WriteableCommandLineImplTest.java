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
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOption(option.getPreferredName()));
        Assert.assertEquals(1, commandLine.getOptionTriggers().size());
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("output.txt", values.get(0));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("v", "verbose", false, "Print verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Assert.assertEquals(true, commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_addSwitch_alreadySet() {
        Option option = new Option("v", "verbose", false, "Print verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("p", "prefix", true, "Prefix for options");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "prefix.key", "prefix.value");
        String propertyValue = commandLine.getProperty(option, "prefix.key");
        Assert.assertEquals("prefix.value", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("p", "prefix", true, "Prefix for options");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Map<String, String> properties = new HashMap<>();
        properties.put("prefix.key1", "prefix.value1");
        commandLine.addProperty(option, "prefix.key2", "prefix.value2");
        commandLine.addProperty(option, "prefix.key3", "prefix.value3");
        commandLine.setDefaultValues(option, new ArrayList<>(properties.values()));
        Map propertiesMap = commandLine.getProperties(option);
        List propertiesList = new ArrayList(propertiesMap.entrySet());
        Assert.assertEquals(3, propertiesList.size());
        for (int i = 0; i < propertiesList.size(); i++) {
            Map.Entry<String, String> entry = (Map.Entry<String, String>) propertiesList.get(i);
            Assert.assertEquals(entry.getKey(), entry.getValue().getKey());
            Assert.assertEquals(entry.getValue().getValue(), entry.getValue().getValue());
        }
    }

    @Test
    public void testWriteableCommandLineImpl_looksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "Output file"), Collections.emptyList());
        Assert.assertTrue(commandLine.looksLikeOption("output"));
        Assert.assertFalse(commandLine.looksLikeOption("input"));
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "output.txt";
        if (commandLineString.contains("\"")) {
            expectedCommandLineString = commandLineString.substring(commandLineString.indexOf("\"") + 1, commandLineString.lastIndexOf("\""));
        }
        Assert.assertEquals(expectedCommandLineString, commandLineString);
    }
}