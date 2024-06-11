import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("option", "an option", true, "defaultValue");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals(2, commandLine.getValues(option, null).size());
        Assert.assertEquals("value1", commandLine.getValues(option, null).get(0));
        Assert.assertEquals("value2", commandLine.getValues(option, null).get(1));
        Assert.assertEquals("defaultValue", commandLine.getProperty(option, "property"));

        commandLine.addProperty(option, "property", "value");
        Assert.assertEquals("value", commandLine.getProperty(option, "property"));

        Option otherOption = new Option("otherOption", "another option");
        commandLine.addOption(otherOption);
        Assert.assertFalse(commandLine.hasOption(otherOption));

        commandLine.addSwitch(option, true);
        Assert.assertTrue(commandLine.getSwitch(option, false));
    }

    @Test(expected = IllegalStateException.class)
    public void testSwitchAlreadySet() {
        Option option = new Option("option", "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testToString() {
        Option option = new Option("option", "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addValue(option, "value1");
        commandLine.addValue(option, "value2");

        Assert.assertEquals("-option value1 value2", commandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("option", "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);

        Assert.assertEquals(1, commandLine.getOptions().size());
        Assert.assertEquals(option, commandLine.getOptions().get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("option", "an option");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());

        commandLine.addOption(option);
        commandLine.addOption(new Option("otherOption", "another option"));

        Assert.assertEquals(2, commandLine.getOptionTriggers().size());
        Assert.assertEquals("option", commandLine.getOptionTriggers().toArray()[0].toString());
        Assert.assertEquals("otherOption", commandLine.getOptionTriggers().toArray()[1].toString());
    }
}