package org.apache.commons.cli2.test;

import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Test class for WriteableCommandLine interface
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultParser().parse("test", new String[]{"-a", "valueA", "--optionB", "valueB", "-p", "property=value"});
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("test").longOpt("test-long").build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("option").hasArg().build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value");
        List<String> values = commandLine.getOptionValue("option");
        assertEquals(1, values.size());
        assertEquals("value", values.get(0));
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("option").list().build();
        commandLine.addOption(option);
        commandLine.addValue(option, "valueA");
        commandLine.addValue(option, "valueB");
        List<String> values = commandLine.getOptionValues("option");
        assertEquals(2, values.size());
        assertEquals("valueA", values.get(0));
        assertEquals("valueB", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("option").list().build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValueA");
        defaultValues.add("defaultValueB");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("option");
        assertEquals(2, values.size());
        assertEquals("defaultValueA", values.get(0));
        assertEquals("defaultValueB", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("switch").build();
        commandLine.addOption(option);
        assertFalse(option.getValue().isPresent());
        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = Option.builder("switch").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("switch").build();
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get());
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "valueC");
        assertEquals("valueC", commandLine.getOptionValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("-a"));
        assertFalse(commandLine.looksLikeOption("valueA"));
    }
}