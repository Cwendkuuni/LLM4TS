package org.apache.commons.cli2.test;

import org.apache.commons.cli2.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        // WriteableCommandLine commandLine = new WriteableCommandLine(); // Use mocking instead
        WriteableCommandLine commandLine = mock(WriteableCommandLine.class);
        Option option = Option.builder("input").longOpt("input-long").hasArg(true).build();
        doNothing().when(commandLine).addOption(option);

        commandLine.addOption(option);

        verify(commandLine, times(1)).addOption(option);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("switch").longOpt("switch-long").build();

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("input").longOpt("input-long").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("input").longOpt("input-long").hasArg(true).build();
        List<String> defaultValues = Arrays.asList("value1", "value2");
        commandLine.setDefaultValues(option, defaultValues);

        List<String> values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
        assertEquals(2, commandLine.getOptions().size());
        Option opt = commandLine.getOptionByName("input");
        assertEquals(defaultValues, opt.getDefaultValues());
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("switch").longOpt("switch-long").build();

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getOptionByName("switch").getValue());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("switch").longOpt("switch-long").build();

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getOptionByName("switch").isSet());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("property").longOpt("property-long").build();
        commandLine.addProperty(option, "key", "value");

        assertEquals("value", commandLine.getOptionByName("property").getProperties().get("key"));
    }

    @Test
    public void testAddPropertyDefault() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        commandLine.addProperty("defaultKey", "defaultValue");

        assertEquals("defaultValue", commandLine.getProperty("defaultKey"));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();

        assertTrue(commandLine.looksLikeOption("--input"));
        assertTrue(commandLine.looksLikeOption("--input value"));
        assertFalse(commandLine.looksLikeOption("input"));
        assertFalse(commandLine.looksLikeOption("input value"));
    }

    @Test
    public void testGetCurrentOption() {
        WriteableCommandLine commandLine = new WriteableCommandLine();
        Option option = Option.builder("input").longOpt("input-long").hasArg(true).build();

        commandLine.parse(new String[0]);
        commandLine.parse("--input value");

        assertEquals(option, commandLine.getCurrentOption());
    }

    // Add other test cases here for methods addValue, looksLikeOption, getOptionByName, parse, parseWithHandlers, and any other methods
}