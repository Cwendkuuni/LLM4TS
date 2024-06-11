import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file path");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(option, writeableCommandLine.getOption("output"));
        Assert.assertEquals("test.txt", writeableCommandLine.getValues(option, null).get(0));
    }

    @Test
    public void testWriteableCommandLineImplWithSwitch() {
        Option option = new Option("d", "debug", false, "Enable debug mode");
        List arguments = new ArrayList();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        writeableCommandLine.addOption(option);
        writeableCommandLine.addSwitch(option, true);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(option, writeableCommandLine.getOption("debug"));
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplWithProperties() {
        Option option = new Option("p", "property", true, "Property value");
        List arguments = new ArrayList();
        arguments.add("--property=value");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "value");
        writeableCommandLine.addProperty("property", "value");

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(option, writeableCommandLine.getOption("property"));
        Assert.assertEquals("value", writeableCommandLine.getProperty("property", "defaultValue"));
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultValues() {
        Option option = new Option("i", "input", true, "Input file path", new String[]{"input.txt"});
        List arguments = new ArrayList();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultValues(option, new ArrayList(Collections.singletonList("input.txt")));

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(option, writeableCommandLine.getOption("input"));
        Assert.assertEquals(1, writeableCommandLine.getValues(option, null).size());
        Assert.assertEquals("input.txt", writeableCommandLine.getValues(option, null).get(0));
    }

    @Test
    public void testWriteableCommandLineImplWithDefaultSwitch() {
        Option option = new Option("v", "verbose", false, "Display verbose output", true);
        List arguments = new ArrayList();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        writeableCommandLine.addOption(option);
        writeableCommandLine.setDefaultSwitch(option, true);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(option, writeableCommandLine.getOption("verbose"));
        Assert.assertTrue(writeableCommandLine.getSwitch(option, false));
    }

    @Test
    public void testWriteableCommandLineImplToString() {
        Option option = new Option("o", "output", true, "Output file path");
        List arguments = new ArrayList();
        arguments.add("test.txt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        writeableCommandLine.addOption(option);
        writeableCommandLine.addValue(option, "test.txt");

        Assert.assertEquals("output test.txt", writeableCommandLine.toString());
    }
}