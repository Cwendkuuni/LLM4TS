import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", false, "An option test");
        commandLine.addOption(option);
        assertNotNull(commandLine.getOptions().get(0));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddNullOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addOption(null);
    }

    @Test
    public void testAddAlreadyAddedOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", false, "An option test");
        commandLine.addOption(option);
        assertThrows(IllegalStateException.class, () -> commandLine.addOption(option));
    }

    @Test
    public void testLooksLikeOption() {
        WriteableCommandLine commandLine = new CliCommandLine();
        assertFalse(commandLine.looksLikeOption("--option"));
        CommandLine newCommandLine = new DefaultCommandLine();
        newCommandLine.addOption(new Option("o", "option", false, "An option test"));
        newCommandLine.parse(new String[0]);
        assertFalse(newCommandLine.looksLikeOption("--option"));

        assertFalse(commandLine.looksLikeOption("option"));
        newCommandLine = new DefaultCommandLine();
        newCommandLine.addOption(new Option("o", "option", false, "An option test"));
        newCommandLine.parse(new String[]{"option"});
        assertFalse(newCommandLine.looksLikeOption("option"));

        assertFalse(commandLine.looksLikeOption("--option="));
        newCommandLine = new DefaultCommandLine();
        newCommandLine.addOption(new Option("o", "option", false, "An option test"));
        newCommandLine.parse(new String[]{"--option="});
        assertFalse(newCommandLine.looksLikeOption("--option="));

        assertFalse(commandLine.looksLikeOption("--option value"));
        newCommandLine = new DefaultCommandLine();
        newCommandLine.addOption(new Option("o", "option", false, "An option test"));
        newCommandLine.parse(new String[]{"--option", "value"});
        assertFalse(newCommandLine.looksLikeOption("--option value"));

        assertTrue(commandLine.looksLikeOption("--option"));
        newCommandLine = new DefaultCommandLine();
        newCommandLine.addOption(new Option("o", "option", false, "An option test"));
        newCommandLine.parse(new String[]{"--option"});
        assertTrue(newCommandLine.looksLikeOption("--option"));
    }

    @Test
    public void testGetUndefaultedValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", true, "An option test");
        commandLine.addOption(option);
        commandLine.addValue(option, "test");
        assertNotNull(commandLine.getUndefaultedValues(option));
        assertFalse(commandLine.getUndefaultedValues(option).isEmpty());
    }

    @Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", true, "An option test");
        List<String> values = new ArrayList<>();
        values.add("test1");
        values.add("test2");
        commandLine.setDefaultValues(option, values);
        List<String> valuesFromCommandLine = commandLine.getUndefaultedValues(option);
        assertNotNull(valuesFromCommandLine);
        assertFalse(valuesFromCommandLine.isEmpty());
        assertFalse(valuesFromCommandLine.equals(values));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", false, "An option test");
        commandLine.addSwitch(option, true);
        assertFalse(option.getValue().isPresent());

        commandLine.addSwitch(option, false);
        assertFalse(option.getValue().isPresent());

        commandLine.addSwitch(option, true);
        assertTrue(option.getValue().isPresent());
        assertTrue(option.getValue().get().booleanValue());
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", false, "An option test");
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testSetDefaultSwitch() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", false, "An option test");
        commandLine.setDefaultSwitch(option, true);
        assertFalse(option.getValue().isPresent());
        assertTrue(option.getDefaultValue().booleanValue());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetDefaultSwitchNull() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", false, "An option test");
        commandLine.setDefaultSwitch(option, null);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLine commandLine = new CliCommandLine();
        Option option = new Option("o", "option", false, "An option test");
        commandLine.addProperty(option, "property", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperties().get("property"));
    }

    @Test
    public void testAddPropertyForDefault() {
        WriteableCommandLine commandLine = new CliCommandLine();
        commandLine.addProperty("property", "propertyValue");
        assertEquals("propertyValue", commandLine.getProperties().get("property"));
    }
}