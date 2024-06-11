import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parser.DefaultParser;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;

    @Before
    public void setUp() {
        commandLine = new DefaultCommandLine();
    }

    @Test
    public void testAddOption() {
        Option option = Option.builder("o").longOpt("option").valueRequired(true).build();
        commandLine.addOption(option);

        assertEquals("option", commandLine.getOption(0).getLongOpt());
        assertEquals("o", commandLine.getOption(0).getOpt());
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test
    public void testAddValue() {
        Option option = Option.builder("o").longOpt("option").valueRequired(true).build();
        commandLine.addOption(option);

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getUndefaultedValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchMultiple() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, false);
    }

    @Test
    public void testAddSwitch() {
        Option option = Option.builder("s").longOpt("switch").build();
        commandLine.addSwitch(option, true);

        assertTrue(commandLine.getOption(0).getValues().contains(Boolean.TRUE));
    }

    @Test
    public void testAddProperty() {
        Option option = Option.builder("p").longOpt("property").build();
        commandLine.addProperty(option, "key", "value");

        assertEquals("value", commandLine.getOption(0).getProperty("key"));
    }

    @Test
    public void testAddDefaultValue() {
        Option option = Option.builder("o").longOpt("option").build();
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("default1");
        defaultValues.add("default2");
        commandLine.setDefaultValues(option, defaultValues);

        assertNull(commandLine.getUndefaultedValues(option));
        assertEquals(defaultValues, commandLine.getValues(option));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test(expected = IllegalStateException.class)
    public void testLooksLikeOptionWithValue() {
        assertTrue(commandLine.looksLikeOption("--option value"));
    }

    @Test
    public void testParse() {
        Option option = Option.builder("o").longOpt("option").valueRequired(true).build();
        commandLine.addOption(option);

        DefaultParser parser = new DefaultParser();
        parser.parse(Arrays.asList("--option", "value1", "--option", "value2"), commandLine);

        assertEquals(Arrays.asList("value1", "value2"), commandLine.getUndefaultedValues(option));
    }
}