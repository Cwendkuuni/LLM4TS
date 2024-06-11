package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link WriteableCommandLineImpl}.
 */
public class WriteableCommandLineImplTest {

    @Test
    public void testCreation() {
        Option rootOption = new Option("test", "testOption", false, "Test Option");
        List arguments = new ArrayList();
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertEquals("Test Option", commandLine.getOption(rootOption.getPreferredName()).getLongOpt());
        assertEquals("test", commandLine.getOption(rootOption.getPreferredName()).getOptName());
        assertEquals(false, commandLine.hasOption(rootOption));
        assertEquals(Collections.emptyList(), commandLine.getValues(rootOption, null));
        assertNull(commandLine.getSwitch(rootOption, null));
        assertEquals(Collections.emptyMap(), commandLine.getProperties());
        assertEquals(Collections.emptySet(), commandLine.getOptionTriggers());
        assertEquals(Collections.emptyList(), commandLine.getOptions());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultSwitches());
        assertEquals(Collections.emptyList(), commandLine.getNormalised());
    }

    @Test
    public void testAddOption() {
        Option rootOption = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        commandLine.addOption(rootOption);

        assertEquals("Test Option", commandLine.getOption(rootOption.getPreferredName()).getLongOpt());
        assertEquals("test", commandLine.getOption(rootOption.getPreferredName()).getOptName());
        assertEquals(true, commandLine.hasOption(rootOption));
        assertEquals(Collections.emptyList(), commandLine.getValues(rootOption, null));
        assertNull(commandLine.getSwitch(rootOption, null));
        assertEquals(Collections.emptyMap(), commandLine.getProperties());
        assertEquals(Collections.singleton(rootOption), commandLine.getOptionTriggers());
        assertEquals(Collections.singletonList(rootOption), commandLine.getOptions());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultValues());
        assertEquals(Collections.emptyMap(), commandLine.getDefaultSwitches());
        assertEquals(Collections.emptyList(), commandLine.getNormalised());

        Option subOption = new Option("subtest", "subTestOption", false, "Sub Test Option");
        commandLine.addOption(subOption);

        assertEquals(Collections.singletonList(subOption), commandLine.getOptions());
        assertEquals(Collections.singleton(subOption), commandLine.getOptionTriggers());

        Option parentOption = new Option("parent", "parentOption", false, "Parent Option");
        commandLine.addOption(parentOption);
        commandLine.addOption(rootOption);
        commandLine.addOption(subOption);
        commandLine.addOption(parentOption);

        assertEquals(Collections.singletonList(parentOption), commandLine.getOptions());
        assertEquals(Collections.singleton(parentOption), commandLine.getOptions().get(0).getParentOptions());
        assertEquals(Collections.singletonList(rootOption), commandLine.getOptions().get(1).getParentOptions());
        assertEquals(Collections.singletonList(subOption), commandLine.getOptions().get(2).getParentOptions());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(Collections.singleton(option), commandLine.getOptionTriggers());
        assertEquals(Arrays.asList("value1", "value2"), commandLine.getValues(option, null));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);

        assertEquals(Collections.singletonList(option), commandLine.getOptions());
        assertEquals(Collections.singleton(option), commandLine.getOptionTriggers());
        assertEquals(Boolean.TRUE, commandLine.getSwitch(option, null));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("unknown", "unknownOption", false, "Unknown Option")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        Option result = commandLine.getOption(option.getPreferredName());
        assertEquals("Test Option", result.getLongOpt());
        assertEquals("test", result.getOptName());
        assertEquals(false, result.isRequired());
        assertEquals(Collections.emptyList(), result.getValues());
        assertEquals(Collections.emptyMap(), result.getOptionParser().getOptionDefinitions());
        assertEquals(List.of(), result.getOptionParser().getPosixCompatible());
    }

    @Test
    public void testGetValues() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        commandLine.addValue(option, "value3");

        values = commandLine.getValues(option, null);
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, null));
    }

    @Test
    public void testGetSwitchWithDefaultValue() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        assertFalse(commandLine.getSwitch(option, Boolean.FALSE));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");

        Map<String, String> properties = commandLine.getProperties(option);

        assertEquals(2, properties.size());
        assertEquals("property1", properties.keySet().iterator().next());
        assertEquals("value1", properties.get("property1"));
        assertEquals("property2", properties.keySet().iterator().next());
        assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testToString() {
        Option option = new Option("test", "testOption", false, "Test Option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, new ArrayList());
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        String result = commandLine.toString();

        String[] args = result.split(" ");

        assertEquals(6, args.length);
        assertEquals("testOption", args[0]);
        assertEquals("test", args[1]);
        assertEquals("value1", args[2]);
        assertEquals("value2", args[3]);
        assertEquals("--testOption", args[4]);
        assertEquals("--", args[5]);
    }
}