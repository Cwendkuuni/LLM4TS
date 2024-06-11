import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option optionWithValue;
    private Option optionWithSwitch;
    private Option optionWithProperty;

    @Before
    public void setUp() {
        commandLine = new WriteableCommandLineImpl(new Option("test-option", "Test option", true, "value"), new ArrayList());
        optionWithValue = new Option("value-option", "Value option", false, "no-value");
        optionWithSwitch = new Option("switch-option", "Switch option", false, null);
        optionWithProperty = new Option("property-option", "Property option", false, null).setArgName("property-value");

        commandLine.addOption(optionWithValue);
        commandLine.addOption(optionWithSwitch);
        commandLine.addOption(optionWithProperty);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(optionWithValue, "expected-value");
        CommandLine parsedCommandLine = commandLine.parse(new String[]{"--test-option", "--value-option", "expected-value"});
        List values = parsedCommandLine.getValues(optionWithValue, Collections.emptyList());
        assertEquals("expected-value", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueAlreadySet() {
        commandLine.addValue(optionWithValue, "value1");
        commandLine.addValue(optionWithValue, "value2");
        commandLine.addValue(optionWithValue, "value3");
        commandLine.parse(new String[]{"--test-option", "--value-option", "value4"});
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(optionWithSwitch, true);
        CommandLine parsedCommandLine = commandLine.parse(new String[]{"--test-option", "--switch-option"});
        Boolean switchValue = parsedCommandLine.getSwitch(optionWithSwitch, false);
        assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(optionWithProperty, "property-key", "property-value");
        CommandLine parsedCommandLine = commandLine.parse(new String[]{"--test-option", "--property-option=property-value"});
        String propertyValue = parsedCommandLine.getProperty("property-key");
        assertEquals("property-value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.parse(new String[]{"--test-option"});
        commandLine.addProperty(optionWithProperty, "property-key", "property-value");
        CommandLine parsedCommandLine = commandLine.parse(new String[]{"--test-option", "--property-option=property-value"});
        Set properties = parsedCommandLine.getProperties(optionWithProperty);
        assertTrue(properties.contains("property-key"));
    }

    @Test
    public void testParseWithNoOptions() {
        CommandLine parsedCommandLine = commandLine.parse(new String[0]);
        assertFalse(parsedCommandLine.hasOption(optionWithValue));
        assertFalse(parsedCommandLine.hasOption(optionWithSwitch));
        assertFalse(parsedCommandLine.hasOption(optionWithProperty));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidOption() {
        commandLine.parse(new String[]{"--invalid-option"});
    }
}