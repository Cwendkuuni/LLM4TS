package org.apache.commons.cli2.test;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", false, "Test option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", true, "Test option");
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.contains("testValue"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_AlreadyAdded() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", false, "Test option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", true, "Test option");
        List defaultValues = new ArrayList();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
        assertTrue(commandLine.getDefaultValues(option).equals(defaultValues));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitch_NotAnOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        commandLine.addSwitch("test", true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", false, "Test option");
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        boolean defaultSwitch = commandLine.getDefaultSwitch(option);
        assertTrue(defaultSwitch);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProperty_NullProperty() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", false, "Test option");
        commandLine.addOption(option);
        commandLine.addProperty(option, null, "value");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddProperty_NullValue() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", false, "Test option");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", null);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", false, "Test option");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        String property = commandLine.getProperty(option, "property");
        assertEquals("value", property);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddProperty_AlreadyExists() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        Option option = new Option("test", "testOption", false, "Test option");
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value1");
        commandLine.addProperty(option, "property", "value2");
    }

    @Test
    public void testAddProperty_DefaultProperty() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        commandLine.addProperty("property1", "value1");
        String property = commandLine.getProperty("property1");
        assertEquals("value1", property);
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test");
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("--test="));
        assertFalse(commandLine.looksLikeOption("test"));
        assertFalse(commandLine.looksLikeOption("testValue"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test --option");
        commandLine.setCurrentOption(new Option("test", "test", false, "Test"));
        Option currentOption = commandLine.getCurrentOption();
        Option expectedOption = new Option("test", "test", false, "Test");
        assertEquals(expectedOption, currentOption);
    }

    @Test
    public void testSetCurrentOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test --option1 --option2");
        Option option = new Option("option1", "option1", false, "Option 1");
        commandLine.setCurrentOption(option);
        Option currentOption = commandLine.getCurrentOption();
        assertEquals(option, currentOption);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetCurrentOption_NullOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test --option1 --option2");
        commandLine.setCurrentOption(null);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetCurrentOption_NotAnOption() {
        WriteableCommandLine commandLine = new Cli2DefaultParser().parse("--test --option1 --option2");
        commandLine.setCurrentOption("test");
    }
}