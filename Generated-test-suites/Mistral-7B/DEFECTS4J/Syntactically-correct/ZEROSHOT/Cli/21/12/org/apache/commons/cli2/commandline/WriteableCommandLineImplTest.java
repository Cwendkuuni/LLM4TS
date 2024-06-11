import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option rootOption = new Option("root", "rootOption", false, "Root option");
        List arguments = new ArrayList();
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, arguments);

        // Test adding options
        Option option1 = new Option("opt1", "option1", true, "Option 1");
        Option option2 = new Option("opt2", "option2", false, "Option 2");
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        assertTrue(commandLine.getOptions().contains(option1));
        assertTrue(commandLine.getOptions().contains(option2));

        // Test looking up options by trigger
        Option lookedUpOption1 = commandLine.getOption("opt1");
        Option lookedUpOption2 = commandLine.getOption("opt2");
        assertEquals(option1, lookedUpOption1);
        assertEquals(option2, lookedUpOption2);

        // Test adding values to an option
        commandLine.addValue(option1, "value1");
        List values1 = commandLine.getValues(option1, null);
        assertEquals(Collections.singletonList("value1"), values1);

        commandLine.addValue(option1, "value2");
        List values2 = commandLine.getValues(option1, null);
        assertEquals(Collections.unmodifiableList(Arrays.asList("value1", "value2")), values2);

        // Test adding a switch to an option
        commandLine.addSwitch(option2, true);
        Boolean switchStatus = commandLine.getSwitch(option2, false);
        assertTrue(switchStatus);

        // Test getting properties
        Option propertyOption = new Option("prop", "propertyOption", false, "Property option");
        commandLine.addProperty(propertyOption, "property", "propertyValue");
        Map<String, String> properties = commandLine.getProperties(propertyOption);
        assertEquals(Collections.singletonMap("property", "propertyValue"), properties);
        String propertyValue = commandLine.getProperty(propertyOption, "property", "defaultValue");
        assertEquals("propertyValue", propertyValue);

        // Test toString method
        String commandLineString = commandLine.toString();
        assertTrue(commandLineString.contains("rootOption"));
        assertTrue(commandLineString.contains("opt1"));
        assertTrue(commandLineString.contains("opt2"));
    }
}