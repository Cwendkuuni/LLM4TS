import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

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
        assertTrue(commandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        List<String> values = commandLine.getValues(option);
        assertEquals(1, values.size());
        assertEquals("testValue", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue1");
        commandLine.addValue(option, "testValue2");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertEquals(2, values.size());
        assertEquals("testValue1", values.get(0));
        assertEquals("testValue2", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        List<String> defaultValues = new ArrayList<>();
        defaultValues.add("defaultValue1");
        defaultValues.add("defaultValue2");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getValues(option);
        assertEquals(2, values.size());
        assertEquals("defaultValue1", values.get(0));
        assertEquals("defaultValue2", values.get(1));
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
        commandLine.setDefaultSwitch(option, true);
        assertTrue(option.isSet());
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "propertyName", "propertyValue");
        assertEquals("propertyValue", commandLine.getPropertyValue(option, "propertyName"));
    }

    @Test
    public void testAddPropertyToDefaultSet() {
        commandLine.addProperty("propertyName", "propertyValue");
        assertEquals("propertyValue", commandLine.getPropertyValue("propertyName"));
    }

    @Test
    public void testLooksLikeOption() {
        assertTrue(commandLine.looksLikeOption("--testOption"));
        assertFalse(commandLine.looksLikeOption("--testOptionValue"));
        assertFalse(commandLine.looksLikeOption("testOptionValue"));
    }

    @Test
    public void testGetCurrentOption() {
        Option currentOption = Mockito.mock(Option.class);
        Mockito.when(commandLine.getCurrentOption()).thenReturn(currentOption);
        assertEquals(currentOption, commandLine.getCurrentOption());
    }

    @Test
    public void testSetCurrentOption() {
        Option currentOption = Mockito.mock(Option.class);
        commandLine.setCurrentOption(currentOption);
        assertEquals(currentOption, commandLine.getCurrentOption());
    }

}