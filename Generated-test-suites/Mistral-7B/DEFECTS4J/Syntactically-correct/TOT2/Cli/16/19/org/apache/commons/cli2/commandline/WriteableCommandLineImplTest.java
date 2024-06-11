import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setup() {
        option = new Option("option", "an option");
        commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
    }

    @Test
    public void testAddOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));

        commandLine.addValue(option, "value2");
        values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddValueToNonArgumentOption() {
        commandLine.addValue(new Option("option", "an option"), "value");
    }

    @Test
    public void testAddSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("option"));
    }

    @Test
    public void testGetValues() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("value1", values.get(0));
        assertEquals("value2", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        commandLine.addValue(option, "value1");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("value1", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        commandLine.addProperty(option, "property", "value");
        String propertyValue = commandLine.getProperty(option, "defaultValue");
        assertEquals("value", propertyValue);
    }

    @Test
    public void testAddProperty() {
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Map properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertEquals("value1", properties.get("property1"));
        assertEquals("value2", properties.get("property2"));
    }

    @Test
    public void testToString() {
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");
        String commandLineString = commandLine.toString();
        assertTrue(commandLineString.contains("value1"));
        assertTrue(commandLineString.contains("value2"));
    }
}