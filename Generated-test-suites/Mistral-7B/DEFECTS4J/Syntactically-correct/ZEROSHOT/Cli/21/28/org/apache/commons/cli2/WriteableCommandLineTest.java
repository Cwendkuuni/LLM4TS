import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    private CommandLine commandLine;
    private Option option;

    @Before
    public void setup() {
        commandLine = Mockito.mock(CommandLine.class);
        option = Mockito.mock(Option.class);
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOptionParser().getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        List<Object> values = new ArrayList<>();
        values.add("value1");
        commandLine.addValue(option, "value1");
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueForExistingOption() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
    }

    @Test
    public void testGetUndefaultedValues() {
        List<Object> values = new ArrayList<>();
        values.add("value1");
        Mockito.when(option.getValue()).thenReturn(values);
        assertEquals(values, commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testGetUndefaultedValuesEmpty() {
        List<Object> values = new ArrayList<>();
        Mockito.when(option.getValue()).thenReturn(values);
        assertNull(commandLine.getUndefaultedValues(option));
    }

    @Test
    public void testSetDefaultValues() {
        List<Object> values = new ArrayList<>();
        values.add("value1");
        values.add("value2");
        commandLine.setDefaultValues(option, values);
        assertEquals(values, option.getDefaultValues());
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        Mockito.verify(option).setValue(ArgumentMatchers.eq(true));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.isSetDefault());
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", option.getPropertyValue("property"));
    }

    @Test
    public void testAddPropertyToDefault() {
        commandLine.addProperty("property", "value");
        assertEquals("value", commandLine.getDefaultPropertyValue("property"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--option"));
    }

    @Test
    public void testLooksLikeOptionFalse() {
        assertFalse(commandLine.looksLikeOption("argument"));
    }

    @Test
    public void testCurrentOption() {
        Option currentOption = Mockito.mock(Option.class);
        commandLine.setCurrentOption(currentOption);
        assertEquals(currentOption, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = Mockito.mock(Option.class);
        commandLine.setCurrentOption(currentOption);
        Mockito.verify(currentOption).setValue(commandLine);
    }
}