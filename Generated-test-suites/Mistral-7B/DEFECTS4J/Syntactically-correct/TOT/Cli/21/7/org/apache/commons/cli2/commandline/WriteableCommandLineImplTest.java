package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl cmdLine;

    @Before
    public void setUp() {
        Option option1 = new Option("o", "option1")
                .longOpt("long-option1")
                .desc("Option 1 description")
                .valueSeparator('=')
                .requiredArg()
                .numberOfValues(2);

        Option option2 = new Option("s", "switch")
                .longOpt("long-switch")
                .desc("Switch description")
                .requiredArg(false);

        Option optionGroup = new Option("g", "group")
                .longOpt("long-group")
                .desc("Group description")
                .requiredArg(false)
                .group();

        cmdLine = new WriteableCommandLineImpl(optionGroup, Collections.emptyList());
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);
    }

    @Test
    public void testAddOption() {
        Option option = new Option("t", "test")
                .longOpt("long-test")
                .desc("Test option")
                .requiredArg()
                .numberOfValues(1);

        cmdLine.addOption(option);
        assertTrue(cmdLine.hasOption(option));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "option")
                .longOpt("long-option")
                .desc("Option")
                .valueSeparator('=')
                .requiredArg()
                .numberOfValues(1);

        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        List values = cmdLine.getValues(option, null);
        assertEquals(values.size(), 2);
        assertEquals(values.get(0), "value1");
        assertEquals(values.get(1), "value2");
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("s", "switch")
                .longOpt("long-switch")
                .desc("Switch")
                .requiredArg(false);

        cmdLine.addOption(option);

        cmdLine.addSwitch(option, true);
        assertTrue(cmdLine.getSwitch(option, false));
        assertFalse(cmdLine.getSwitch(option, true));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "option")
                .longOpt("long-option")
                .desc("Option");

        cmdLine.addOption(option);

        assertTrue(cmdLine.hasOption(option));
        assertFalse(cmdLine.hasOption(new Option("x", "x")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "option")
                .longOpt("long-option")
                .desc("Option");

        cmdLine.addOption(option);

        Option optionFromCmdLine = cmdLine.getOption("o");
        assertEquals(optionFromCmdLine, option);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "option")
                .longOpt("long-option")
                .desc("Option")
                .valueSeparator('=')
                .requiredArg()
                .numberOfValues(1);

        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");
        cmdLine.addValue(option, "value2");

        List values = cmdLine.getValues(option, null);
        assertEquals(values.size(), 2);
        assertEquals(values.get(0), "value1");
        assertEquals(values.get(1), "value2");
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "option")
                .longOpt("long-option")
                .desc("Option")
                .valueSeparator('=')
                .requiredArg()
                .numberOfValues(1);

        cmdLine.addOption(option);

        cmdLine.addValue(option, "value1");

        List values = cmdLine.getUndefaultedValues(option);
        assertEquals(values.size(), 1);
        assertEquals(values.get(0), "value1");
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("s", "switch")
                .longOpt("long-switch")
                .desc("Switch")
                .requiredArg(false);

        cmdLine.addOption(option);

        cmdLine.addSwitch(option, true);

        assertTrue(cmdLine.getSwitch(option, false));
        assertFalse(cmdLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option option = new PropertyOption("p", "property")
                .longOpt("long-property")
                .desc("Property option")
                .requiredArg();

        cmdLine.addOption(option);

        cmdLine.addProperty(option, "key1", "value1");
        cmdLine.addProperty(option, "key2", "value2");

        assertEquals(cmdLine.getProperty(option, "key1"), "value1");
        assertEquals(cmdLine.getProperty(option, "key2"), "value2");
        assertEquals(cmdLine.getProperty(option, "unknown"), null);
    }

    @Test
    public void testAddProperty() {
        Option option = new PropertyOption("p", "property")
                .longOpt("long-property")
                .desc("Property option")
                .requiredArg();

        cmdLine.addOption(option);

        Properties properties = new Properties();
        properties.put("key1", "value1");
        properties.put("key2", "value2");

        cmdLine.addProperty(option, properties);

        assertEquals(cmdLine.getProperties(option).size(), 2);
        assertEquals(cmdLine.getProperty(option, "key1"), "value1");
        assertEquals(cmdLine.getProperty(option, "key2"), "value2");
    }

    @Test
    public void testGetProperties() {
        Option option = new PropertyOption("p", "property")
                .longOpt("long-property")
                .desc("Property option")
                .requiredArg();

        Properties properties = new Properties();
        properties.put("key1", "value1");
        properties.put("key2", "value2");

        cmdLine.addOption(option);
        cmdLine.addProperty(option, properties);

        Map propertiesMap = cmdLine.getProperties(option);
        assertEquals(propertiesMap.size(), 2);
        assertEquals(propertiesMap.get("key1"), "value1");
        assertEquals(propertiesMap.get("key2"), "value2");
    }

    @Test
    public void testToString() {
        Option option1 = new Option("o", "option1")
                .longOpt("long-option1")
                .desc("Option 1 description")
                .valueSeparator('=')
                .requiredArg()
                .numberOfValues(2);

        Option option2 = new Option("s", "switch")
                .longOpt("long-switch")
                .desc("Switch description")
                .requiredArg(false);

        cmdLine = new WriteableCommandLineImpl(new Option("g", "group")
                .longOpt("long-group")
                .desc("Group description")
                .requiredArg(false)
                .group(), Collections.emptyList());
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        cmdLine.addValue(option1, "value1");
        cmdLine.addValue(option1, "value2");
        cmdLine.addSwitch(option2, true);

        cmdLine.addProperty(option1, new Properties() {{
            put("prop1", "value1");
            put("prop2", "value2");
        }});

        cmdLine.addProperty(option2, "value3");

        String expected = "group -g long-group Option1=value1 value2 " +
                "Option2 switch=true property.prop1=value1 property.prop2=value2 " +
                "property.value3=value3";

        assertEquals(cmdLine.toString(), expected);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o", "option1")
                .longOpt("long-option1")
                .desc("Option 1 description")
                .valueSeparator('=')
                .requiredArg()
                .numberOfValues(2);

        Option option2 = new Option("s", "switch")
                .longOpt("long-switch")
                .desc("Switch description")
                .requiredArg(false);

        cmdLine = new WriteableCommandLineImpl(new Option("g", "group")
                .longOpt("long-group")
                .desc("Group description")
                .requiredArg(false)
                .group(), Collections.emptyList());
        cmdLine.addOption(option1);
        cmdLine.addOption(option2);

        cmdLine.addValue(option1, "value1");
        cmdLine.addValue(option1, "value2");
        cmdLine.addSwitch(option2, true);

        cmdLine.addProperty(option1, new Properties() {{
            put("prop1", "value1");
            put("prop2", "value2");
        }});

        cmdLine.addProperty(option2, "value3");

        List options = cmdLine.getOptions();
        assertEquals(options.size(), 3);
        assertEquals(options.get(0), option1);
        assertEquals(options.get(1), option2);
        assertEquals(options.get(2), new Option("", ""));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("o", "option")
                .longOpt("long-option")
                .desc("Option")
                .valueSeparator('=')
                .requiredArg()
                .numberOfValues(1);

        cmdLine.addOption(option);

        Set optionTriggers = cmdLine.getOptionTriggers();
        assertEquals(optionTriggers.size(), 1);
        assertEquals(optionTriggers.iterator().next(), "o");
    }
}