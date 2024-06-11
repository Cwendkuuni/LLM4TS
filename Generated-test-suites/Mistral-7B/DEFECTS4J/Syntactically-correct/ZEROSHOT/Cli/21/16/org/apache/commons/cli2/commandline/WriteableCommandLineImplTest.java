import org.apache.commons.cli2.CommandLine;
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
        assertTrue(commandLine.getOptions().contains(option));
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
        assertEquals("test.txt", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("v", "verbose", false, "print verbose output");
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
    }

    @Test
    public void testGetOption() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        assertEquals(1, commandLine.getValues(option, null).size());
        assertEquals("test.txt", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        assertEquals(1, commandLine.getUndefaultedValues(option).size());
        assertEquals("test.txt", commandLine.getUndefaultedValues(option).get(0));
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("v", "verbose", false, "print verbose output");
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
        commandLine.addProperty(option, "propertyKey", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperty(option, "propertyKey", null));
        assertNull(commandLine.getProperty(option, "propertyKey2", null));
    }

    @Test
    public void testAddProperty() {
        Option option = new Option("p", "property", true, "property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("propertyKey", "propertyValue");
        commandLine.addProperty(option, properties);
        assertEquals(1, commandLine.getProperties(option).size());
        assertEquals("propertyKey", commandLine.getProperties(option).iterator().next());
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "property value");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Properties properties = new Properties();
        properties.put("propertyKey1", "propertyValue1");
        properties.put("propertyKey2", "propertyValue2");
        commandLine.addProperty(option, properties);
        assertEquals(2, commandLine.getProperties(option).size());
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option(), new ArrayList<>());
        assertTrue(commandLine.looksLikeOption("o"));
        assertTrue(commandLine.looksLikeOption("--output"));
        assertFalse(commandLine.looksLikeOption("arg"));
    }

    @Test
    public void testToString() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        WriteableCommandLine writeableCommandLine = new WriteableCommandLine(commandLine);
        assertEquals("--output test.txt", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        List options = commandLine.getOptions();
        assertEquals(1, options.size());
        assertTrue(options.contains(option));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        Set optionTriggers = commandLine.getOptionTriggers();
        assertEquals(1, optionTriggers.size());
        assertTrue(optionTriggers.contains("o"));
    }

    @Test
    public void testSetDefaultValues() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        List defaultValues = new ArrayList();
        defaultValues.add("default1.txt");
        commandLine.setDefaultValues(option, defaultValues);
        assertEquals("default1.txt", commandLine.getValues(option, null).get(0));
    }

    @Test
    public void testSetDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "print verbose output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.setDefaultSwitch(option, true);
        assertTrue(commandLine.getSwitch(option, false));
    }

    @Test
    public void testGetNormalised() {
        Option option = new Option("o", "output", true, "output file");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList<>());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List normalised = commandLine.getNormalised();
        assertEquals(2, normalised.size());
        assertEquals("--output", ((String) normalised.get(0)).substring(0, 7));
        assertEquals("test.txt", ((String) normalised.get(1)).substring(1));
    }
}