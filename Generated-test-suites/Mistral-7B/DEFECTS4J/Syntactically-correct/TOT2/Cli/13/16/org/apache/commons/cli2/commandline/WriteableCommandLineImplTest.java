import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.WriteableCommandLineImpl;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("o", "output", true, "Output file path");
        commandLine.addOption(option);
        Assert.assertEquals(1, commandLine.getOptions().size());
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("o", "output", true, "Output file path");
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Enable verbose output");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchAlreadySet() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Enable verbose output");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        commandLine.addSwitch(option, true);
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("o", "output", true, "Output file path");
        commandLine.addOption(option);
        Assert.assertTrue(commandLine.hasOption(option));
    }

    @Test
    public void testGetOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("o", "output", true, "Output file path");
        commandLine.addOption(option);
        Assert.assertEquals(option, commandLine.getOption("o"));
    }

    @Test
    public void testGetValues() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("o", "output", true, "Output file path");
        commandLine.addOption(option);
        commandLine.addValue(option, "test.txt");
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        Option option = new Option("v", "verbose", false, "Enable verbose output");
        commandLine.addOption(option);
        commandLine.addSwitch(option, true);
        Boolean switchValue = commandLine.getSwitch(option, false);
        Assert.assertTrue(switchValue);
    }

    @Test
    public void testAddProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("test.property", "test value");
        String propertyValue = commandLine.getProperty("test.property", null);
        Assert.assertEquals("test value", propertyValue);
    }

    @Test
    public void testgetProperty() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("test.property", "test value");
        String propertyValue = commandLine.getProperty("test.property", null);
        Assert.assertEquals("test value", propertyValue);
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addProperty("test.property1", "test value 1");
        commandLine.addProperty("test.property2", "test value 2");
        Set properties = commandLine.getProperties();
        Assert.assertEquals(2, properties.size());
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(null, new ArrayList<>());
        commandLine.addOption(new Option("o", "output", true, "Output file path"));
        commandLine.addValue(commandLine.getOption("o"), "test.txt");
        String commandLineString = commandLine.toString();
        Assert.assertEquals("-o test.txt", commandLineString);
    }
}