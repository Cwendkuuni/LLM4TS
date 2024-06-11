package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
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
    public void testCreateNewWriteableCommandLineImpl() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);

        List arguments = Arrays.asList("--option", "value1 value2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        assertEquals(option, commandLine.getOption("o"));
        assertEquals(option, commandLine.getOption("option"));
        assertEquals(option, commandLine.getOption("long-option"));
        assertEquals(2, commandLine.getOptions().size());
        assertEquals(Arrays.asList(option), commandLine.getOptions());
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
        assertFalse(commandLine.hasOption(new Option("n", "no-option")));
    }

    @Test
    public void testAddOption() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("h", "help"), Arrays.asList());

        commandLine.addOption(option);

        assertEquals(3, commandLine.getOptions().size());
        assertEquals(Arrays.asList("help", "option"), commandLine.getOptionTriggers());
        assertEquals(option, commandLine.getOption("o"));
        assertEquals(option, commandLine.getOption("option"));
        assertEquals(option, commandLine.getOption("long-option"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch", false, "a switch")
                .required(false)
                .longOpt("long-switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        Option option = new Option("s", "switch", false, "a switch")
                .required(false)
                .longOpt("long-switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("n", "no-option")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        commandLine.addOption(option);

        assertEquals(option, commandLine.getOption("o"));
        assertEquals(option, commandLine.getOption("option"));
        assertEquals(option, commandLine.getOption("long-option"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
        assertEquals(Collections.emptyList(), commandLine.getValues(option, Arrays.asList("value3")));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch", false, "a switch")
                .required(false)
                .longOpt("long-switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("h", "help"), Arrays.asList());

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property", null));
    }

    @Test
    public void testGetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("h", "help"), Arrays.asList());

        commandLine.addProperty("property", "value");

        assertEquals("value", commandLine.getProperty("property", null));
        assertEquals(null, commandLine.getProperty("unknown-property", null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("h", "help"), Arrays.asList());

        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");

        Set properties = commandLine.getProperties();

        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option", "value1 value2"));

        assertEquals("--option value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option", "value1 value2"));

        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option", "value1 value2"));

        Set optionTriggers = commandLine.getOptionTriggers();

        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
        assertTrue(optionTriggers.contains("option"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");

        commandLine.setDefaultValues(option, defaultValues);

        assertEquals(defaultValues, commandLine.getValues(option, null));
        assertEquals(Collections.emptyList(), commandLine.getValues(option, Arrays.asList("value1", "value2")));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("s", "switch", false, "a switch")
                .required(false)
                .longOpt("long-switch");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList());

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "option", true, "an option")
                .required(false)
                .longOpt("long-option")
                .valueSeparator(' ')
                .numberOfValues(2);
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option", "value1 value2"));

        assertEquals(Arrays.asList("--option", "value1", "value2"), commandLine.getNormalised());
    }
}