package org.apache.commons.cli2.commandline;

import static org.junit.Assert.*;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.apache.commons.cli2.option.PropertyOption;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * Test class for WriteableCommandLineImpl.
 */
public class WriteableCommandLineImplTest {

    @org.junit.Before
    public void setUp() {
        // Set up
    }

    @org.junit.After
    public void tearDown() {
        // Clean up
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        Option rootOption = new Option("opt", "option", true, "Option description.");
        List arguments = Arrays.asList("arg1", "arg2", "--opt", "value1", "--opt", "value2");

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertSame(rootOption, commandLine.getRootOption());
        assertSame(arguments, commandLine.getArguments());
    }

    /**
     * Test addOption method.
     */
    @org.junit.Test
    public void testAddOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        assertTrue(commandLine.getOptions().contains(option));
        assertEquals(option, commandLine.getOption(option.getPreferredName()));
    }

    /**
     * Test addValue method.
     */
    @org.junit.Test
    public void testAddValue() {
        Option option = new Option("opt", "option", true, "Option description.");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals( Arrays.asList("value1", "value2"), commandLine.getValues(option, null) );
    }

    /**
     * Test addSwitch method.
     */
    @org.junit.Test
    public void testAddSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    /**
     * Test hasOption method.
     */
    @org.junit.Test
    public void testHasOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
    }

    /**
     * Test getOption method.
     */
    @org.junit.Test
    public void testGetOption() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption(option.getPreferredName());

        assertEquals(option, retrievedOption);
    }

    /**
     * Test getValues method.
     */
    @org.junit.Test
    public void testGetValues() {
        Option option = new Option("opt", "option", true, "Option description.");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        List values = commandLine.getValues(option, null);

        assertEquals(Arrays.asList("value1", "value2"), values);
    }

    /**
     * Test getUndefaultedValues method.
     */
    @org.junit.Test
    public void testGetUndefaultedValues() {
        Option option = new Option("opt", "option", true, "Option description.");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        List values = commandLine.getUndefaultedValues(option);

        assertEquals(Collections.singletonList("value1"), values);
    }

    /**
     * Test getSwitch method.
     */
    @org.junit.Test
    public void testGetSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertFalse(switchValue);

        commandLine.addSwitch(option, true);

        switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    /**
     * Test getProperty method.
     */
    @org.junit.Test
    public void testGetProperty() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        commandLine.addProperty(option, "property", "value");

        assertEquals("value", commandLine.getProperty(option, "defaultValue"));
    }

    /**
     * Test addProperty method.
     */
    @org.junit.Test
    public void testAddProperty() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addProperty(option, "property", properties);

        Map optionProperties = commandLine.getProperties(option);

        assertEquals(properties, optionProperties);
    }

    /**
     * Test getProperties method.
     */
    @org.junit.Test
    public void testGetProperties() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        commandLine.addProperty(option, "property", properties);

        Map allProperties = commandLine.getProperties();

        assertEquals(properties, allProperties.get(option));
    }

    /**
     * Test looksLikeOption method.
     */
    @org.junit.Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        assertTrue(commandLine.looksLikeOption("opt"));
        assertTrue(commandLine.looksLikeOption("opt--"));
        assertTrue(commandLine.looksLikeOption("--opt"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    /**
     * Test toString method.
     */
    @org.junit.Test
    public void testToString() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList("arg1", "--opt", "arg2"));

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        String commandLineString = commandLine.toString();

        assertEquals("arg1 --opt value1 arg2", commandLineString);
    }

    /**
     * Test getOptions method.
     */
    @org.junit.Test
    public void testGetOptions() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        List options = commandLine.getOptions();

        assertEquals(Arrays.asList(option1, option2), options);
    }

    /**
     * Test getOptionTriggers method.
     */
    @org.junit.Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option1);
        commandLine.addOption(option2);

        Set optionTriggers = commandLine.getOptionTriggers();

        assertEquals(Arrays.asList(option1.getPreferredName(), option2.getPreferredName()), optionTriggers);
    }

    /**
     * Test setDefaultValues method.
     */
    @org.junit.Test
    public void testSetDefaultValues() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        List defaultValues = Arrays.asList("value1", "value2");

        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);

        assertEquals(defaultValues, values);
    }

    /**
     * Test setDefaultSwitch method.
     */
    @org.junit.Test
    public void testSetDefaultSwitch() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList());

        commandLine.addOption(option);

        Boolean defaultSwitch = true;

        commandLine.setDefaultSwitch(option, defaultSwitch);

        Boolean switchValue = commandLine.getSwitch(option, false);

        assertTrue(switchValue);
    }

    /**
     * Test getNormalised method.
     */
    @org.junit.Test
    public void testGetNormalised() {
        Option option = new Option("opt", "option");
        WriteableCommandLine commandLine = new WriteableCommandLineImpl(new Option("root"), Arrays.asList("--opt value1", "--opt value2"));

        commandLine.addOption(option);

        List normalised = commandLine.getNormalised();

        assertEquals(Arrays.asList("--opt value1", "--opt value2"), normalised);
    }
}