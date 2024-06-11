import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option1"), Collections.emptyList());
        Option option = new Option("option2", "o2", false, "Option 2");
        commandLine.addOption(option);

        CommandLine parsedCommandLine = commandLine.parse("");
        Assert.assertTrue(parsedCommandLine.hasOption(option));

        
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", true, "v");
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        CommandLine parsedCommandLine = commandLine.parse("");
        List<String> values = parsedCommandLine.getValues(option, null);
        Assert.assertEquals(values, Arrays.asList("value1", "value2"));

        
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", false, null, "Option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);

        CommandLine parsedCommandLine = commandLine.parse("");
        Boolean switchValue = parsedCommandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);

        
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("option"), Collections.emptyList());
        Option option = new Option("option", false, null, "Option");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);

        CommandLine parsedCommandLine = commandLine.parse("");
        // will throw an exception

        
    }
}