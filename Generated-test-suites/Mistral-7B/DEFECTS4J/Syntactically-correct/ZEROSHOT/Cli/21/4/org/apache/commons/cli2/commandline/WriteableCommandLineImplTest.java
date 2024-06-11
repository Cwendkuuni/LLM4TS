import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.getOptions().contains(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionWithDuplicateTrigger() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        Option result = writeableCommandLine.getOption("o");
        assertEquals(option, result);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        List values = writeableCommandLine.getUndefaultedValues(option);
        assertTrue(values.isEmpty());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testgetProperty() {
        Option option = new Option("p", "property", true, "Property");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "name", "value");
        assertEquals("value", writeableCommandLine.getProperty(option, "name"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "Property");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "name1", "value1");
        writeableCommandLine.addProperty(option, "name2", "value2");
        Map properties = writeableCommandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.containsKey("name1"));
        assertTrue(properties.containsKey("name2"));
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Property");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addProperty(option, "name1", "value1");
        writeableCommandLine.addProperty(option, "name2", "value2");
        Set properties = writeableCommandLine.getProperties(option);
        assertEquals(2, properties.size());
        assertTrue(properties.contains("name1"));
        assertTrue(properties.contains("name2"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        String result = writeableCommandLine.toString();
        String expected = String.join(" ", new String[]{"-o", "test.txt"});
        assertEquals(expected, result);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o1", "option1", true, "Option 1");
        Option option2 = new Option("o2", "option2", true, "Option 2");
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
        Option option2 = new Option("o2", "option2", true, "Option 2");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        writeableCommandLine.addOption(option1);
        writeableCommandLine.addOption(option2);
        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o1"));
        assertTrue(optionTriggers.contains("o2"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        List defaults = new ArrayList<>();
        defaults.add("default1.txt");
        writeableCommandLine.setDefaultValues(option, defaults);
        List values = writeableCommandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("default1.txt", values.get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "Verbose output");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);
        assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "output", true, "Output file");
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");
        List normalised = writeableCommandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals("-o test.txt", normalised.get(0).toString());
    }
}