import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

/**
 * Test class for {@link WriteableCommandLine} interface
 */
public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new CliCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("option").longOpt("longOption").valueSeparator('=').build();
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getOptionValues("option");
        assertArrayEquals(new String[]{"value1"}, values.toArray());
    }

    @Test
    public void testGetOptionValues() {
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.addOption(option);
        List<String> values = commandLine.getOptionValues("option");
        assertNull(values);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetOptionValuesEmptyList() {
        List<String> values = commandLine.getOptionValues("nonexistentOption");
    }

    @Test
    public void testSetDefaultValues() {
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getOptionValues("option");
        assertArrayEquals(new String[]{"default1", "default2"}, values.toArray());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.hasOption("option", true));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = Option.builder("option").longOpt("longOption").build();
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.hasOption("option", true));
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getProperty("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }
}