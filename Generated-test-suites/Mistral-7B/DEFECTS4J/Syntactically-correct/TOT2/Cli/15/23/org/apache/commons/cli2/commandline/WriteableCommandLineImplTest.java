import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        Option option = new Option("option1", true, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("option1"));
    }

    @Test
    public void testAddValue() {
        Option option = new Option("option1", true, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addValue(option, "value1");
        List values = commandLine.getValues(option, Collections.emptyList());
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value1", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        Option option = new Option("switch1", true, "s");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testHasOption() {
        Option option = new Option("option1", true, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchException() {
        Option option = new Option("option1", true, "o");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testGetProperty() {
        Option option = new Option("option1", false, "p");
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, Collections.emptyList());
        commandLine.addOption(option);
        commandLine.addProperty(option, "property1", "value1");
        String propertyValue = commandLine.getProperty(option, "property1", "defaultValue");
        Assert.assertEquals("value1", propertyValue);
    }

    @Test
    public void testToString() {
        Option option1 = new Option("option1", true, "o1");
        Option option2 = new Option("option2", false, "o2");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option1, Arrays.asList(option2));
        commandLine.addOption(option1);
        commandLine.addValue(option1, "value1");
        commandLine.addSwitch(option2, true);
        String commandLineString = commandLine.toString();
        Assert.assertEquals("-o1 value1 -o2", commandLineString);
    }
}