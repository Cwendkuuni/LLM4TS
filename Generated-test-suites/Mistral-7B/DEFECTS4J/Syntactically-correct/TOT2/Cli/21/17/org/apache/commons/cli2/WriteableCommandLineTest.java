import org.apache.commons.cli2.*;
import org.apache.commons.cli2.parser.CliFactory;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliFactory().createWriteableCommandLine();
        Option option = new Option("input", true, "i")
                .longOpt("input")
                .valueSeparator('=')
                .required(false);
        commandLine.addOption(option);
        assertNotNull(commandLine.getOptions().get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliFactory().createWriteableCommandLine();
        Option option = new Option("input", true, "i")
                .longOpt("input")
                .valueSeparator('=');
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertNotNull(values);
        assertEquals(1, values.size());
        assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliFactory().createWriteableCommandLine();
        Option option = new Option("input", true, "i")
                .longOpt("input")
                .valueSeparator('=');
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        commandLine.addValue(option, "test2.txt");
        List<String> values = commandLine.getUndefaultedValues(option);
        assertNotNull(values);
        assertEquals(2, values.size());
        assertEquals("test.txt", values.get(0));
        assertEquals("test2.txt", values.get(1));
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliFactory().createWriteableCommandLine();
        Option option = new Option("input", true, "i")
                .longOpt("input")
                .valueSeparator('=');
        List<String> defaultValues = Arrays.asList("test1.txt", "test2.txt");
        commandLine.setDefaultValues(option, defaultValues);
        List<String> values = commandLine.getUndefaultedValues(option);
        assertNotNull(values);
        assertTrue(values.isEmpty());
        assertEquals(2, commandLine.getDefaultValues(option).size());
        assertEquals("test1.txt", commandLine.getDefaultValues(option).get(0));
        assertEquals("test2.txt", commandLine.getDefaultValues(option).get(1));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchWithExistingValue() {
        WriteableCommandLine commandLine = new CliFactory().createWriteableCommandLine();
        Option option = new Option("input", true, "i")
                .longOpt("input")
                .valueSeparator('=');
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        try {
            commandLine.addSwitch(option, true);
        } catch (IllegalStateException e) {
            assertEquals("Option 'input' already has a switch value", e.getMessage());
            throw e;
        }
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliFactory().createWriteableCommandLine();
        Option option = new Option("input", true, "i")
                .longOpt("input")
                .valueSeparator('=');
        commandLine.addOption(option);
        commandLine.addProperty(option, "name", "value");
        assertEquals("value", commandLine.getOption("name").getValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyWithInvalidName() {
        WriteableCommandLine commandLine = new CliFactory().createWriteableCommandLine();
        Option option = new Option("input", true, "i")
                .longOpt("input")
                .valueSeparator('=');
        commandLine.addOption(option);
        try {
            commandLine.addProperty(null, "name", "value");
        } catch (IllegalArgumentException e) {
            assertEquals("Option 'null' not found", e.getMessage());
            throw e;
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddPropertyWithInvalidValue() {
        WriteableCommandLine commandLine = new CliFactory().createWriteableCommandLine();
        Option option = new Option("input", true, "i")
                .longOpt("input")
                .valueSeparator('=');
        commandLine.addOption(option);
        try {
            commandLine.addProperty(option, "name", null);
        } catch (IllegalArgumentException e) {
            assertEquals("Invalid value for property 'name': null", e.getMessage());
            throw e;
        }
    }
}