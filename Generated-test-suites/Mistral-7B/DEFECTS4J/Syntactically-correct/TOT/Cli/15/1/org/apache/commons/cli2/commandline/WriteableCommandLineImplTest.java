package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.parsers.DefaultParser;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = OptionBuilder.withLongOpt("test")
                .withDescription("Test")
                .create(false);
        CommandLine cmdLine = new DefaultParser().parse(option.toBuilder().build(), Arrays.asList("--test"));
        commandLine = (WriteableCommandLineImpl) cmdLine;
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption(commandLine.getOption("test")));
    }

    @Test
    public void testGetOption() {
        Option option = commandLine.getOption("test");
        assertEquals("Test", option.getLongOpt());
        assertEquals(false, option.getValue());
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(commandLine.getOption("test"), "value1");
        List values = commandLine.getValues(commandLine.getOption("test"), null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(commandLine.getOption("test"), "value2");
        values = commandLine.getValues(commandLine.getOption("test"), null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(commandLine.getOption("test"), true);
        assertTrue(commandLine.getSwitch(commandLine.getOption("test"), false));
    }

    @Test
    public void testGetSwitch() {
        assertFalse(commandLine.getSwitch(commandLine.getOption("test"), false));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        commandLine.addSwitch(commandLine.getOption("test"), true);
        commandLine.addSwitch(commandLine.getOption("test"), true);
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(commandLine.getOption("test"), "value1");
        commandLine.addValue(commandLine.getOption("test"), "value2");

        List values = commandLine.getValues(commandLine.getOption("test"), null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));

        values = commandLine.getValues(commandLine.getOption("test"), Arrays.asList("value3"));
        assertEquals(3, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
        assertEquals("value3", values.get(2));
    }

    @Test
    public void testGetUndefaultedValues() {
        List values = commandLine.getUndefaultedValues(commandLine.getOption("test"));
        assertNull(values);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(commandLine.getOption("test"), "property", "value");
        assertEquals("value", commandLine.getProperty(commandLine.getOption("test"), "property", null));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(commandLine.getOption("test"), "property1", "value1");
        commandLine.addProperty(commandLine.getOption("test"), "property2", "value2");

        Set properties = commandLine.getProperties(commandLine.getOption("test"));
        assertEquals(2, properties.size());
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));

        assertEquals("value1", commandLine.getProperty(commandLine.getOption("test"), "property1", null));
        assertEquals("value2", commandLine.getProperty(commandLine.getOption("test"), "property2", null));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--test"));
        assertFalse(commandLine.looksLikeOption("--other"));
    }

    @Test
    public void testToString() {
        assertEquals("--test", commandLine.toString());
    }
}