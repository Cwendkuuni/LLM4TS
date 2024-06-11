import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
        assertEquals(option, writeableCommandLine.getOption("o"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        Option option = new Option("v", "verbose", false, "Verbose mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertEquals(option, writeableCommandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        writeableCommandLine.addValue(option, "output.log");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("test.txt", values.get(0));
        assertEquals("output.log", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose mode");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "Property value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "property.name", "property.value");
        assertEquals("property.value", writeableCommandLine.getProperty(option, "property.name", null));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "Property value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property.name", "property.value");
        writeableCommandLine.addProperty(option, properties);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(1, propertiesSet.size());
        assertTrue(propertiesSet.contains("property.name"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property value");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Properties properties = new Properties();
        properties.setProperty("property.name1", "property.value1");
        properties.setProperty("property.name2", "property.value2");
        writeableCommandLine.addProperty(option, properties);
        Set propertiesSet = writeableCommandLine.getProperties(option);
        assertEquals(2, propertiesSet.size());
        assertTrue(propertiesSet.containsAll(properties.stringPropertyNames()));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(new Option("o", "output", true, "Output file path"), new ArrayList<>());
        assertTrue(writeableCommandLine.looksLikeOption("o"));
        assertTrue(writeableCommandLine.looksLikeOption("output"));
        assertFalse(writeableCommandLine.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "Output file path");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        writeableCommandLine.addValue(option, "output.log");
        String expected = "output test.txt output.log";
        assertEquals(expected, writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        List options = writeableCommandLine.getOptions();
        assertEquals(2, options.size());
        assertTrue(options.contains(option1));
        assertTrue(options.contains(option2));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", false, "Option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.containsAll(Arrays.asList("o1", "o2")));
    }
}