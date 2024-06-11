import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("a", "append", true, "Append to a file.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        Option option = new Option("a", "append", true, "Append to a file.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("d", "debug", false, "Enable debug mode.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("a", "append", true, "Append to a file.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        boolean hasOption = commandLine.hasOption(option);
        Assert.assertTrue(hasOption);
    }

    @Test
    public void testGetOption() {
        Option option = new Option("a", "append", true, "Append to a file.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Option fromCommandLine = commandLine.getOption("a");
        Assert.assertEquals(option, fromCommandLine);
    }

    @Test
    public void testGetValues() {
        Option option = new Option("a", "append", true, "Append to a file.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetUndefaultedValues() {
        Option option = new Option("a", "append", true, "Append to a file.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getUndefaultedValues(option);
        Assert.assertEquals(0, values.size());
    }

    @Test
    public void testGetSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("p", "property", true, "Set a property.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "propertyName", "propertyValue");
        String propertyValue = commandLine.getProperty(option, "propertyName", "defaultValue");
        Assert.assertEquals("propertyValue", propertyValue);
    }

    @Test
    public void testGetProperties() {
        Option option = new Option("p", "property", true, "Set a property.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        commandLine.addProperty(option, "property2", "value2");
        Set properties = commandLine.getProperties(option);
        Assert.assertEquals(2, properties.size());
    }

    @Test
    public void testToString() {
        Option option = new Option("a", "append", true, "Append to a file.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Arrays.asList("arg1", "arg2"));
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        String commandLineString = commandLine.toString();
        Assert.assertEquals("[arg1, arg2] -a append test.txt", commandLineString);
    }

    @Test
    public void testGetOptions() {
        Option option1 = new Option("a", "append", true, "Append to a file.");
        Option option2 = new Option("b", "backup", false, "Create a backup.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        List options = commandLine.getOptions();
        Assert.assertEquals(2, options.size());
    }

    @Test
    public void testGetOptionTriggers() {
        Option option1 = new Option("a", "append", true, "Append to a file.");
        Option option2 = new Option("b", "backup", false, "Create a backup.");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Collections.emptyList());
        commandLine.addOption(option1);
        commandLine.addOption(option2);
        Set optionTriggers = commandLine.getOptionTriggers();
        Assert.assertEquals(2, optionTriggers.size());
    }
}