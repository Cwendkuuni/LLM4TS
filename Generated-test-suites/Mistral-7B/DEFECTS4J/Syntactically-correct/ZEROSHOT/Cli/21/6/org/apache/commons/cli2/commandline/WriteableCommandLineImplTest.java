import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("o"));
        assertFalse(commandLine.hasOption(new Option("i", "input")));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "be verbose");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testHasOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertTrue(commandLine.hasOption(option));
        assertFalse(commandLine.hasOption(new Option("i", "input")));
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Option retrievedOption = commandLine.getOption("o");
        assertEquals(option, retrievedOption);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        commandLine.addValue(option, "output.log");
        List values = commandLine.getValues(option, null);
        assertEquals(2, values.size());
        assertEquals("test.txt", values.get(0));
        assertEquals("output.log", values.get(1));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getUndefaultedValues(option);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "be verbose");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
        assertFalse(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value");
        assertEquals("value", commandLine.getProperty(option, "defaultValue"));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property", "value1");
        commandLine.addProperty(option, "property", "value2");
        assertEquals(2, commandLine.getProperties(option).size());
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        assertEquals(2, properties.size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("o", "output"), new ArrayList<>());
        assertTrue(commandLine.looksLikeOption("o"));
        assertFalse(commandLine.looksLikeOption("i"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        String commandLineString = commandLine.toString();
        String expected = "output test.txt";
        assertEquals(expected, commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("i", "input", true, "input file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        assertEquals(2, options.size());
        Option option = (Option) options.get(0);
        assertEquals(option1, option);
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("o", "output", true, "output file");
        Option option2 = new Option("i", "input", true, "input file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, new ArrayList<>());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(2, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
        assertTrue(optionTriggers.contains("i"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("default1.txt");
        defaultValues.add("default2.txt");
        commandLine.setDefaultValues(option, defaultValues);
        List values = commandLine.getValues(option, null);
        assertEquals(0, values.size());
        assertEquals(2, commandLine.getValues(option, defaultValues).size());
        assertEquals("default1.txt", commandLine.getValues(option, defaultValues).get(0));
        assertEquals("default2.txt", commandLine.getValues(option, defaultValues).get(1));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "be verbose");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertFalse(commandLine.getSwitch(option, false));
        assertTrue(commandLine.getSwitch(option, true));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List normalised = commandLine.getNormalised();
        assertEquals(1, normalised.size());
        assertEquals("output test.txt", normalised.get(0));
    }
}