import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    private WriteableCommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = Mockito.mock(WriteableCommandLine.class);
        option = Mockito.mock(Option.class);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getValuesForOption(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testSetDefaultValues() {
        commandLine.addOption(option);
        List<String> defaultValues = new ArrayList<>(Arrays.asList("default1", "default2"));
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValuesForOption(option);
        assertEquals(2, values.size());
        assertEquals("default1", values.get(0));
        assertEquals("default2", values.get(1));
    }

    @Test
    public void testAddSwitch() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.isSet());
        assertTrue(option.getValue().equals("true"));
    }

    @Test
    public void testAddProperty() {
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getPropertyValue(option, "property"));
        assertNull(commandLine.getPropertyValue(option, "anotherProperty"));
    }

    @Test
    public void testAddDefaultProperty() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getPropertyValue(null, "property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
        assertFalse(commandLine.looksLikeOption("value"));
    }

    @Test
    public void testGetCurrentOption() {
        Option currentOption = Mockito.mock(Option.class);
        commandLine.setCurrentOption(currentOption);
        assertEquals(currentOption, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = Mockito.mock(Option.class);
        commandLine.setCurrentOption(currentOption);
        commandLine.setCurrentOption(null);
        assertNull(commandLine.getCurrentOption());
    }
}