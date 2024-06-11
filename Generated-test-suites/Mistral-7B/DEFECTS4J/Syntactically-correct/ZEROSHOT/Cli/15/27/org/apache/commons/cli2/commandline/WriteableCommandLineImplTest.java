package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;

/**
 * Test class for {@link WriteableCommandLineImpl}.
 */
public class WriteableCommandLineImplTest {

    @SuppressWarnings("unchecked")
    private static Option createTestOption() {
        return OptionBuilder.withLongOpt("test")
                .withValueSeparator('=')
                .hasArg()
                .build();
    }

    @SuppressWarnings("unchecked")
    private static Argument createTestArgument() {
        return OptionBuilder.withLongOpt("testArg")
                .build();
    }

    /**
     * Test constructor.
     */
    @org.junit.Test
    public void testConstructor() {
        final Option rootOption = createTestOption();
        final List arguments = new ArrayList();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        assertSame(rootOption, commandLine.getRootOption());
        assertEquals(arguments, commandLine.getArguments());
    }

    /**
     * Test adding an option.
     */
    @org.junit.Test
    public void testAddOption() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        final Option option = createTestOption();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertEquals(1, commandLine.getOptions().size());
        assertSame(option, commandLine.getOption(option.getLongOpt()));
    }

    /**
     * Test getting an option.
     */
    @org.junit.Test
    public void testGetOption() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        assertSame(option, commandLine.getOption(option.getLongOpt()));
        assertSame(option, commandLine.getOption(option.getPreferredName()));
        assertNull(commandLine.getOption("test2"));
    }

    /**
     * Test adding a value.
     */
    @org.junit.Test
    public void testAddValue() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        assertEquals("value1", commandLine.getValues(option, Collections.emptyList()).get(0));

        commandLine.addValue(option, "value2");
        assertEquals("value2", commandLine.getValues(option, Collections.emptyList()).get(1));

        commandLine.addValue(option, null);
        assertEquals(2, commandLine.getValues(option, Collections.emptyList()).size());

        commandLine.addValue(option, Collections.singletonList("value3"));
        assertEquals(3, commandLine.getValues(option, Collections.emptyList()).size());
    }

    /**
     * Test adding a switch.
     */
    @org.junit.Test
    public void testAddSwitch() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    /**
     * Test hasOption.
     */
    @org.junit.Test
    public void testHasOption() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option()));
    }

    /**
     * Test getValues.
     */
    @org.junit.Test
    public void testGetValues() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        assertEquals("value1", commandLine.getValues(option, Collections.emptyList()).get(0));
        assertEquals(1, commandLine.getValues(option, Collections.emptyList()).size());

        commandLine.addValue(option, "value2");
        assertEquals("value2", commandLine.getValues(option, Collections.emptyList()).get(1));
        assertEquals(2, commandLine.getValues(option, Collections.emptyList()).size());

        commandLine.addValue(option, null);
        assertEquals(3, commandLine.getValues(option, Collections.emptyList()).size());

        commandLine.addValue(option, Collections.singletonList("value3"));
        assertEquals(4, commandLine.getValues(option, Collections.emptyList()).size());

        final List defaults = new ArrayList();
        defaults.add("default1");
        commandLine.setDefaultValues(option, defaults);

        assertEquals(5, commandLine.getValues(option, defaults).size());
    }

    /**
     * Test getUndefaultedValues.
     */
    @org.junit.Test
    public void testGetUndefaultedValues() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();

        commandLine.addOption(option);
        commandLine.addValue(option, "value1");

        assertEquals(Collections.singletonList("value1"), commandLine.getUndefaultedValues(option));

        commandLine.addValue(option, null);
        commandLine.setDefaultValues(option, new ArrayList());
        assertEquals(Collections.emptyList(), commandLine.getUndefaultedValues(option));
    }

    /**
     * Test getSwitch.
     */
    @org.junit.Test
    public void testGetSwitch() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        assertFalse(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));

        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));

        commandLine.addSwitch(option, false);
        assertFalse(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    /**
     * Test addProperty.
     */
    @org.junit.Test
    public void testAddProperty() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");

        final Map properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("key1"));
        assertEquals("value2", properties.get("key2"));

        final Properties defaultProperties = new Properties();
        defaultProperties.setProperty("key3", "value3");
        commandLine.setDefaultValues(option, defaultProperties);

        final Map properties2 = commandLine.getProperties(option);
        assertEquals(3, properties2.size());
        assertEquals("value1", properties2.get("key1"));
        assertEquals("value2", properties2.get("key2"));
        assertEquals("value3", properties2.get("key3"));
    }

    /**
     * Test getProperty.
     */
    @org.junit.Test
    public void testGetProperty() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");

        assertEquals("value1", commandLine.getProperty(option, "key1"));
        assertEquals("value2", commandLine.getProperty(option, "key2"));
        assertEquals("defaultValue", commandLine.getProperty(option, "key3"));

        final Properties defaultProperties = new Properties();
        defaultProperties.setProperty("key3", "value3");
        commandLine.setDefaultValues(option, defaultProperties);

        assertEquals("value1", commandLine.getProperty(option, "key1"));
        assertEquals("value2", commandLine.getProperty(option, "key2"));
        assertEquals("value3", commandLine.getProperty(option, "key3"));
    }

    /**
     * Test getProperties.
     */
    @org.junit.Test
    public void testGetProperties() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");

        final Map expectedProperties = new HashMap();
        expectedProperties.put("key1", "value1");
        expectedProperties.put("key2", "value2");

        assertEquals(expectedProperties, commandLine.getProperties(option));

        final Properties defaultProperties = new Properties();
        defaultProperties.setProperty("key3", "value3");
        commandLine.setDefaultValues(option, defaultProperties);

        final Map expectedProperties2 = new HashMap();
        expectedProperties2.put("key1", "value1");
        expectedProperties2.put("key2", "value2");
        expectedProperties2.put("key3", "value3");

        assertEquals(expectedProperties2, commandLine.getProperties(option));

        assertEquals(Collections.emptyMap(), commandLine.getProperties());
    }

    /**
     * Test looksLikeOption.
     */
    @org.junit.Test
    public void testLooksLikeOption() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());

        assertTrue(commandLine.looksLikeOption("--test"));
        assertTrue(commandLine.looksLikeOption("--test="));
        assertTrue(commandLine.looksLikeOption("--testArg"));
        assertFalse(commandLine.looksLikeOption("--testArg="));
        assertFalse(commandLine.looksLikeOption("--testArgValue"));
    }

    /**
     * Test toString.
     */
    @org.junit.Test
    public void testToString() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option = createTestOption();
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        final String expected = "--test=value1 value2";
        assertEquals(expected, commandLine.toString());

        commandLine.addProperty(option, "key1", "value1");
        commandLine.addProperty(option, "key2", "value2");

        final Map properties = commandLine.getProperties(option);
        final StringBuilder sb = new StringBuilder();
        for (final Object key : properties.keySet()) {
            sb.append("--test=").append((String) key).append("=").append(properties.get(key));
        }
        final String expected2 = sb.toString();
        assertEquals(expected2, commandLine.toString());
    }

    /**
     * Test getOptions.
     */
    @org.junit.Test
    public void testGetOptions() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option1 = createTestOption();
        final Option option2 = createTestOption();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        assertEquals(Collections.singletonList(option1), commandLine.getOptions());
        assertEquals(2, commandLine.getOptions().size());
    }

    /**
     * Test getOptionTriggers.
     */
    @org.junit.Test
    public void testGetOptionTriggers() {
        final Option rootOption = createTestOption();
        final WriteableCommandLine commandLine = new WriteableCommandLineImpl(rootOption, new ArrayList());
        final Option option1 = createTestOption();
        final Option option2 = createTestOption();
        commandLine.addOption(option1);
        commandLine.addOption(option2);

        final Set expected = Collections.singleton(option1.getLongOpt());
        assertEquals(expected, commandLine.getOptionTriggers());
        assertEquals(1, commandLine.getOptionTriggers().size());
    }
}