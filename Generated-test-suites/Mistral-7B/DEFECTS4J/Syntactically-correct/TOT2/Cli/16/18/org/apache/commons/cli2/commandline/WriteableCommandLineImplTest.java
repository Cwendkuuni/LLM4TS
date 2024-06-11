package org.apache.commons.cli2.commandline;

import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;

/**
 * Tests for {@link WriteableCommandLineImpl}.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("output")
                              .required(false)
                              .hasArg(true)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        List values = commandLine.getValues(option, null);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("output")
                              .required(true)
                              .hasArg(true)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals("output.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("v").longOpt("verbose")
                              .required(false)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("v").longOpt("verbose")
                              .required(false)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = Option.builder("o").longOpt("output")
                              .required(false)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = Option.builder("o").longOpt("output")
                              .required(false)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addOption(option);
        Option returnedOption = commandLine.getOption("o");
        assertEquals(option, returnedOption);
    }

    @Test
    public void testGetValues() {
        Option option = Option.builder("o").longOpt("output")
                              .required(true)
                              .hasArg(true)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addValue(option, "output.txt");
        List values = commandLine.getValues(option, null);
        assertEquals("output.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = Option.builder("o").longOpt("output")
                              .required(true)
                              .hasArg(true)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "output.txt"));
        List values = commandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option option = Option.builder("v").longOpt("verbose")
                              .required(false)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new PropertyOption("p", "property", true, "default");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--property=value"));
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "default"));
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption("p", "property", true, "default");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--property=value1", "--property=value2"));
        Map properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.containsKey("property"));
        assertTrue(properties.get("property") instanceof List);
        List propertyValues = (List) properties.get("property");
        assertEquals(2, propertyValues.size());
        assertEquals("value1", propertyValues.get(0));
        assertEquals("value2", propertyValues.get(1));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, Arrays.asList(""));
        assertTrue(commandLine.looksLikeOption("o"));
        assertTrue(commandLine.looksLikeOption("--output"));
        assertFalse(commandLine.looksLikeOption("h"));
    }

    @Test
    public void testToString() {
        Option option = Option.builder("o").longOpt("output")
                              .required(false)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "output.txt"));
        commandLine.addOption(option);
        commandLine.addValue(option, "output.txt");
        String commandLineString = commandLine.toString();
        String expected = "test output.txt";
        assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = Option.builder("o1").longOpt("option1")
                               .required(false)
                               .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                               .required(false)
                               .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test1"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = Option.builder("o1").longOpt("option1")
                               .required(false)
                               .build();
        Option option2 = Option.builder("o2").longOpt("option2")
                               .required(false)
                               .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList("test1"));
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("o").longOpt("output")
                              .required(true)
                              .hasArg(true)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        List defaults = Arrays.asList("default1", "default2");
        commandLine.setDefaultValues(option, defaults);
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("v").longOpt("verbose")
                              .required(false)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test"));
        commandLine.setDefaultSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetNormalised() {
        Option option = Option.builder("o").longOpt("output")
                              .required(false)
                              .build();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("test", "output.txt"));
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("test", normalised.get(0));
        assertEquals("output.txt", normalised.get(1));
    }
}