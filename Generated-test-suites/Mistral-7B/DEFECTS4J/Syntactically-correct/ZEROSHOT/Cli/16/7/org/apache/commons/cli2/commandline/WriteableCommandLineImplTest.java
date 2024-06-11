import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());

        commandLine.addOption(option);

        // Test adding value
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        assertEquals("test.txt", values.get(0));

        // Test adding switch
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        assertTrue(switchValue);

        // Test hasOption
        assertTrue(commandLine.hasOption(option));

        // Test getOption
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);

        // Test getValues
        List defaultValues = new ArrayList();
        defaultValues.add("default.txt");
        List retrievedValues = commandLine.getValues(option, defaultValues);
        assertEquals(1, retrievedValues.size());
        assertEquals("test.txt", retrievedValues.get(0));
        assertEquals("default.txt", defaultValues.get(0));

        // Test getUndefaultedValues
        List unfaultedValues = commandLine.getUndefaultedValues(option);
        assertTrue(unfaultedValues.isEmpty());

        // Test getSwitch
        Boolean defaultSwitch = false;
        Boolean retrievedSwitch = commandLine.getSwitch(option, defaultSwitch);
        assertFalse(retrievedSwitch);

        // Test getProperty
        Option propertyOption = new Option("p", "property");
        commandLine.addProperty(propertyOption, "myProperty", "myValue");
        String retrievedProperty = commandLine.getProperty(propertyOption, "defaultValue");
        assertEquals("myValue", retrievedProperty);

        // Test getProperties
        Set retrievedProperties = commandLine.getProperties(propertyOption);
        assertTrue(retrievedProperties.contains("myProperty"));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");

        String output = commandLine.toString();
        String expected = "output test.txt";
        assertEquals(expected, output);
    }
}