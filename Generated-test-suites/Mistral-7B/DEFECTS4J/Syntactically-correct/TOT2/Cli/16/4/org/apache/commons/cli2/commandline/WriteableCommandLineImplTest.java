import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("b"));

        Option optionB = commandLine.getOption("b");
        Assert.assertNotNull(optionB);

        List options = commandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(optionB, options.get(0));
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), new String[]{"value"});

        Option optionA = commandLine.getOption("a");
        List values = commandLine.getValues(optionA, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("value", values.get(0));
    }

    @Test
    public void testHasOption() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a"));

        Assert.assertTrue(commandLine.hasOption(new Option("a")));
        Assert.assertFalse(commandLine.hasOption(new Option("b")));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addSwitch(new Option("a"), true);
        commandLine.addSwitch(new Option("a"), true);
    }

    @Test
    public void testGetSwitch() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addSwitch(new Option("a"), true);

        Assert.assertTrue(commandLine.getSwitch(new Option("a"), null));
    }

    @Test
    public void testGetProperties() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addProperty(new Option("a"), "property", "value");

        Set properties = commandLine.getProperties(new Option("a"));
        Assert.assertEquals(1, properties.size());
        Assert.assertEquals("property", properties.iterator().next());

        String propertyValue = commandLine.getProperty(new Option("a"), "property");
        Assert.assertEquals("value", propertyValue);
    }

    @Test
    public void testToString() {
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(new Option("a"), Collections.emptyList());
        commandLine.addOption(new Option("a"));
        commandLine.addValue(new Option("a"), new String[]{"value"});

        Assert.assertEquals("value", commandLine.toString());
    }
}