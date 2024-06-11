package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.annotations.MockitoAnnotations;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

@RunWith(PowerMockRunner.class)
@PrepareForTest(WriteableCommandLineImpl.class)
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        option = Mockito.mock(Option.class);
        commandLine = new WriteableCommandLineImpl(option, null);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine.addArgument("arg", true, null, null);
        commandLine.addValue(option, "value");
        List values = commandLine.getValues(option, null);
        assertEquals("value", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertSame(option, commandLine.getOption("trigger"));
    }

    @Test
    public void testGetValues() {
        commandLine.addArgument("arg1", false, null, null);
        commandLine.addArgument("arg2", false, null, null);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        List expectedValues = new ArrayList();
        expectedValues.add("value1");
        expectedValues.add("value2");
        assertEquals(expectedValues, values);
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
        assertEquals(null, commandLine.getProperty("nonExistentProperty"));
    }

    @Test
    public void testGetProperties() {
        commandLine.addProperty("property1", "value1");
        commandLine.addProperty("property2", "value2");
        Set properties = commandLine.getProperties();
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
    }

    @Test
    public void testLooksLikeOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.looksLikeOption("trigger"));
        assertFalse(commandLine.looksLikeOption("nonExistentOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseExceptionWhenInvokedWithNullArguments() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, null);
        commandLine.parse();
    }

    @Test
    public void testToString() {
        commandLine.addArgument("arg1", true, null, null);
        commandLine.addArgument("arg2", true, null, null);
        String commandLineString = commandLine.toString();
        String expectedCommandLineString = "arg1 arg2";
        assertEquals(expectedCommandLineString, commandLineString);
    }

    @Test
    public void testGetOptions() {
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        commandLine.addOption(option);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertTrue(optionTriggers.containsAll(Collections.singleton("trigger")));
    }
}