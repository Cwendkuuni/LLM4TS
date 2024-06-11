package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * WriteableCommandLineImplTest class.
 */
public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;

    @Before
    public void setUp() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An example option")
                .create(false);
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#hasOption(Option)}.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testHasOption() throws ParseException {
        assertTrue(commandLine.hasOption(commandLine.getOption("option")));
        assertFalse(commandLine.hasOption(new Option("invalidOption", false, null, "")));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getOption(String)}.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testGetOption() throws ParseException {
        assertSame(commandLine.getOption("option"), commandLine.getOption("option"));
        assertNull(commandLine.getOption("invalidOption"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getValues(Option, List)}.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testGetValues() throws ParseException {
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");

        List values = commandLine.getValues(commandLine.getOption("option"), null);
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
        assertFalse(values.contains("value3"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getUndefaultedValues(Option)}.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testGetUndefaultedValues() throws ParseException {
        commandLine.addValue(commandLine.getOption("option"), "value1");
        List values = commandLine.getUndefaultedValues(commandLine.getOption("option"));
        assertTrue(values.contains("value1"));
        assertFalse(values.contains("value2"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getSwitch(Option, Boolean)}.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testGetSwitch() throws ParseException {
        commandLine.addSwitch(commandLine.getOption("option"), true);

        assertTrue(commandLine.getSwitch(commandLine.getOption("option"), null));
        assertFalse(commandLine.getSwitch(commandLine.getOption("option"), false));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperty(Option, String, String)}.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testGetProperty() throws ParseException {
        commandLine.addProperty(commandLine.getOption("option"), "property", "value");

        assertSame(commandLine.getProperty(commandLine.getOption("option"), "property"), "value");
        assertNull(commandLine.getProperty(commandLine.getOption("option"), "invalidProperty"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#getProperties(Option)}.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testGetProperties() throws ParseException {
        commandLine.addProperty(commandLine.getOption("option"), "property1", "value1");
        commandLine.addProperty(commandLine.getOption("option"), "property2", "value2");

        Set properties = commandLine.getProperties(commandLine.getOption("option"));
        assertTrue(properties.contains("property1"));
        assertTrue(properties.contains("property2"));
        assertFalse(properties.contains("property3"));
    }

    /**
     * Test method for {@link WriteableCommandLineImpl#toString()}.
     *
     * @throws ParseException if an error occurs during parsing
     */
    @Test
    public void testToString() throws ParseException {
        commandLine.addValue(commandLine.getOption("option"), "value1");
        commandLine.addValue(commandLine.getOption("option"), "value2");
        commandLine.addSwitch(commandLine.getOption("option"), true);

        List options = commandLine.getOptions();
        List values = commandLine.getValues(commandLine.getOption("option"), null);
        Boolean switchValue = commandLine.getSwitch(commandLine.getOption("option"), null);

        String expectedOutput = "--option value1 value2 --option";
        String actualOutput = commandLine.toString();

        assertTrue(actualOutput.contains(expectedOutput));
    }
}