import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--option value");

        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);

        List<Option> options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertSame(option, options.get(0));

        commandLine.parse("--option value2");
        assertEquals("value2", commandLine.getValueForOption("option"));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--option");
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.parse("--option value2");

        List<String> values = commandLine.getValuesForOption("option");
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));
    }

    @Test
    public void testGetValuesForOption() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--option value1 --option value2");

        List<String> values = commandLine.getValuesForOption("option");
        assertEquals(2, values.size());
        assertTrue(values.contains("value1"));
        assertTrue(values.contains("value2"));

        values = commandLine.getValuesForOption("nonexistent");
        assertNull(values);
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--option");
        Option option = Option.builder("o").longOpt("option").hasArg(true).build();

        commandLine.setDefaultValues(option, Arrays.asList("default1", "default2"));

        commandLine.parse("--option value");
        List<String> values = commandLine.getValuesForOption("option");
        assertEquals(2, values.size());
        assertTrue(values.contains("default1"));
        assertTrue(values.contains("default2"));
        assertFalse(values.contains("value"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadyAdded() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--switch");
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--switch");
        Option option = Option.builder("s").longOpt("switch").build();

        commandLine.addSwitch(option, true);

        assertTrue(commandLine.hasOption("switch"));
        assertTrue(commandLine.getOption("switch").getValue().booleanValue());

        commandLine.addSwitch(option, false);

        assertTrue(commandLine.hasOption("switch"));
        assertFalse(commandLine.getOption("switch").getValue().booleanValue());
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--switch");
        Option option = Option.builder("s").longOpt("switch").build();

        commandLine.setDefaultSwitch(option, true);

        assertTrue(commandLine.hasOption("switch"));
        assertTrue(commandLine.getOption("switch").getValue().booleanValue());

        commandLine.setDefaultSwitch(option, false);

        assertTrue(commandLine.hasOption("switch"));
        assertFalse(commandLine.getOption("switch").getValue().booleanValue());
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CommandStarter().parse("--property property=value");

        commandLine.addProperty("property", "newValue");

        assertEquals("newValue", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("--value"));
        assertFalse(commandLine.looksLikeOption("--option value"));
    }
}