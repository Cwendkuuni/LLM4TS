import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.ParseException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

public class WriteableCommandLineImplTest {

    @org.junit.Test
    public void testCreateCommandLine() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .withArgName("ARG")
                .isRequired(true)
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("ARG1", "ARG2"));

        assertEquals("ARG1", commandLine.getArgument(0));
        assertEquals("ARG2", commandLine.getArgument(1));
        assertEquals(2, commandLine.getSize());
        assertEquals("option", commandLine.getOption(0).getLongOpt());
        assertEquals("ARG", commandLine.getOption(0).getArgName());
        assertEquals("An option", commandLine.getOption(0).getDescription());
        assertEquals(true, commandLine.hasOption(commandLine.getOption(0)));
    }

    @org.junit.Test
    public void testAddOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .withArgName("ARG")
                .isRequired(true)
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(null, Collections.emptyList());
        commandLine.addOption(option);

        assertEquals(1, commandLine.getSize());
        assertEquals("option", commandLine.getOption(0).getLongOpt());
        assertEquals("ARG", commandLine.getOption(0).getArgName());
        assertEquals("An option", commandLine.getOption(0).getDescription());
        assertEquals(true, commandLine.hasOption(commandLine.getOption(0)));
    }

    @org.junit.Test
    public void testAddValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addValue(option, "VALUE1");
        commandLine.addValue(option, "VALUE2");

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("VALUE1", values.get(0));
        assertEquals("VALUE2", values.get(1));
    }

    @org.junit.Test
    public void testAddSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @org.junit.Test
    public void testGetOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);

        Option retrievedOption = commandLine.getOption("option");
        assertEquals(option, retrievedOption);
    }

    @org.junit.Test
    public void testGetValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("VALUE1", "VALUE2"));

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("VALUE1", values.get(0));
        assertEquals("VALUE2", values.get(1));

        values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("VALUE1", values.get(0));
        assertEquals("VALUE2", values.get(1));
    }

    @org.junit.Test
    public void testGetSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @org.junit.Test
    public void testGetProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addProperty(option, "property", "VALUE");

        assertEquals("VALUE", commandLine.getProperty(option, ""));
    }

    @org.junit.Test
    public void testAddProperty() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        Properties properties = new Properties();
        properties.setProperty("property1", "VALUE1");
        commandLine.addProperty(option, "property", properties);

        Map propertiesMap = commandLine.getProperties(option);
        assertEquals(1, propertiesMap.size());
        assertEquals("property1", propertiesMap.keySet().iterator().next());
        assertEquals("VALUE1", propertiesMap.get("property1"));
    }

    @org.junit.Test
    public void testSetDefaultValues() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        List defaultValues = Arrays.asList("VALUE1", "VALUE2");
        commandLine.setDefaultValues(option, defaultValues);

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("VALUE1", values.get(0));
        assertEquals("VALUE2", values.get(1));
    }

    @org.junit.Test
    public void testSetDefaultSwitch() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.getSwitch(option, false));
    }

    @org.junit.Test(expected = IllegalStateException.class)
    public void testSetSwitchTwice() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @org.junit.Test
    public void testParseInvalidOption() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option"));

        try {
            commandLine.parse();
            fail();
        } catch (ParseException e) {
            // expected
        }

        assertFalse(commandLine.hasOption(option));
    }

    @org.junit.Test
    public void testParseInvalidValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .isRequired(true)
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option", "VALUE1", "--option VALUE2"));

        try {
            commandLine.parse();
            fail();
        } catch (ParseException e) {
            // expected
        }

        assertFalse(commandLine.hasOption(option));
    }

    @org.junit.Test
    public void testParseMissingValue() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .isRequired(true)
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option"));

        try {
            commandLine.parse();
            fail();
        } catch (ParseException e) {
            // expected
        }

        assertFalse(commandLine.hasOption(option));
    }

    @org.junit.Test
    public void testParseValidOptions() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option", "VALUE1", "--option VALUE2"));

        commandLine.parse();

        assertTrue(commandLine.hasOption(option));

        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("VALUE1", values.get(0));
        assertEquals("VALUE2", values.get(1));
    }

    @org.junit.Test
    public void testParseValidProperties() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("--option", "--property=VALUE"));

        commandLine.parse();

        Map properties = commandLine.getProperties(option);
        assertEquals(1, properties.size());
        assertEquals("VALUE", properties.get("property"));
    }

    @org.junit.Test
    public void testToString() {
        Option option = OptionBuilder.withLongOpt("option")
                .withDescription("An option")
                .create();

        WriteableCommandLine commandLine = new WriteableCommandLineImpl(option, Arrays.asList("VALUE1", "VALUE2", "--option"));

        String commandString = commandLine.toString();
        assertEquals("VALUE1 VALUE2 --option", commandString);
    }
}