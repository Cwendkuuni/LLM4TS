package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl_addOption() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("value"));
        writeableCommandLine.addOption(option);

        Assert.assertEquals(1, writeableCommandLine.getOptions().size());
        Option optionFromCommandLine = writeableCommandLine.getOption("o");
        Assert.assertEquals(option, optionFromCommandLine);
    }

    @Test
    public void testWriteableCommandLineImpl_addValue() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addValue(option, "value1");
        writeableCommandLine.addValue(option, "value2");

        List values = writeableCommandLine.getValues(option, null);
        Assert.assertEquals(2, values.size());
        Assert.assertEquals("value1", values.get(0));
        Assert.assertEquals("value2", values.get(1));
    }

    @Test
    public void testWriteableCommandLineImpl_addSwitch() {
        Option option = new Option("d", "debug", false, "enable debug mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        Assert.assertEquals(true, switchValue);

        writeableCommandLine.addSwitch(option, false);
        switchValue = writeableCommandLine.getSwitch(option, true);
        Assert.assertEquals(false, switchValue);
    }

    @Test
    public void testWriteableCommandLineImpl_hasOption() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        boolean hasOption = writeableCommandLine.hasOption(option);
        Assert.assertEquals(true, hasOption);

        Option wrongOption = new Option("w", "wrong option", true, "a wrong option");
        hasOption = writeableCommandLine.hasOption(wrongOption);
        Assert.assertEquals(false, hasOption);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperty() {
        Option option = new Option("p", "property", true, "a property");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addProperty(option, "propertyName", "propertyValue");
        String propertyValue = writeableCommandLine.getProperty(option, "propertyName", "defaultValue");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testWriteableCommandLineImpl_getProperties() {
        Option option = new Option("p", "property", true, "a property");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        Map<String, String> properties = new HashMap<>();
        properties.put("propertyName1", "propertyValue1");
        properties.put("propertyName2", "propertyValue2");
        writeableCommandLine.addProperty(option, properties);

        Map<String, String> optionsProperties = writeableCommandLine.getProperties(option);
        Assert.assertEquals(2, optionsProperties.size());
        Assert.assertEquals("propertyValue1", optionsProperties.get("propertyName1"));
        Assert.assertEquals("propertyValue2", optionsProperties.get("propertyName2"));
    }

    @Test(expected = IllegalStateException.class)
    public void testWriteableCommandLineImpl_switchAlreadySet() {
        Option option = new Option("d", "debug", false, "enable debug mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList());
        writeableCommandLine.addOption(option);

        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testWriteableCommandLineImpl_toString() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("value1", "value2"));
        writeableCommandLine.addOption(option);

        String toString = writeableCommandLine.toString();
        Assert.assertEquals("value1 value2", toString);
    }

    @Test
    public void testWriteableCommandLineImpl_getNormalised() {
        Option option = new Option("o", "option", true, "an option");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, Arrays.asList("value1", "value2"));
        writeableCommandLine.addOption(option);

        List normalised = writeableCommandLine.getNormalised();
        Assert.assertEquals(2, normalised.size());
        Assert.assertEquals("value1", normalised.get(0));
        Assert.assertEquals("value2", normalised.get(1));
    }
}