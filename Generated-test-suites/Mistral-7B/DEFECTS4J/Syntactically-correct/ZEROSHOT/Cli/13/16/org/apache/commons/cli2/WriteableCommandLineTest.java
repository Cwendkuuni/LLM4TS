package org.apache.commons.cli2.test;

import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Test suite for WriteableCommandLine class.
 */
public class WriteableCommandLineTest {

    /**
     * Test method for {@link WriteableCommandLine#addOption(Option)}.
     */
    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("o").longOpt("option").build();
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#addValue(Option, Object)}.
     */
    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("v").longOpt("value").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test");
        List<String> values = commandLine.getValues("v");
        assertEquals(1, values.size());
        assertEquals("test", values.get(0));
    }

    /**
     * Test method for {@link WriteableCommandLine#getValues(Option)}.
     */
    @Test
    public void testGetValues() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("a").longOpt("arg").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "test1");
        commandLine.addValue(option, "test2");
        List<String> values = commandLine.getValues("a");
        assertEquals(2, values.size());
        assertEquals("test1", values.get(0));
        assertEquals("test2", values.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultValues(Option, List)}.
     */
    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("d").longOpt("defaultValues").build();
        List<String> defaults = new ArrayList<>();
        defaults.add("default1");
        defaults.add("default2");
        commandLine.setDefaultValues(option, defaults);
        List<String> obtainedDefaultValues = commandLine.getValues("d");
        assertEquals(2, obtainedDefaultValues.size());
        assertEquals("default1", obtainedDefaultValues.get(0));
        assertEquals("default2", obtainedDefaultValues.get(1));
    }

    /**
     * Test method for {@link WriteableCommandLine#addSwitch(Option, boolean)}.
     */
    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getValueForOption(option).isPresent());
        assertTrue(commandLine.getValueForOption(option).get());
    }

    /**
     * Test method for {@link WriteableCommandLine#setDefaultSwitch(Option, Boolean)}.
     */
    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("sw").longOpt("switch").build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertFalse(commandLine.getValueForOption(option).isPresent());
        assertFalse(commandLine.getValueForOption(option).get());
    }

    /**
     * Test method for {@link WriteableCommandLine#addProperty(String, String)}.
     */
    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = createCommandLine();
        commandLine.addProperty("property", "test");
        assertEquals("test", commandLine.getProperty("property"));
    }

    /**
     * Test method for {@link WriteableCommandLine#looksLikeOption(String)}.
     */
    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = createCommandLine();
        Option option = Option.builder("option").build();
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption("option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    private WriteableCommandLine createCommandLine() {
        CommandLineParser parser = new DefaultParser();
        return new DefaultWriteableCommandLine();
    }
}