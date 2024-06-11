import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "output file path");
        List arguments = new ArrayList();
        arguments.add("--output=test.txt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertTrue(writeableCommandLine.hasOption(option));
        Assert.assertEquals(option, writeableCommandLine.getOption("output"));
        Assert.assertEquals("test.txt", writeableCommandLine.getValues(option).get(0));
        Assert.assertFalse(writeableCommandLine.getSwitch(option, false));

        Option propertyOption = new Option("p", "property", true, "property name");
        writeableCommandLine.addProperty(propertyOption, "prop.name", "prop.value");
        Assert.assertEquals("prop.value", writeableCommandLine.getProperty(propertyOption, "prop.name"));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        Option option = new Option("s", "switch", false);
        List arguments = new ArrayList();

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }

    @Test
    public void testToString() {
        Option option = new Option("t", "test", false);
        List arguments = new ArrayList();
        arguments.add("--test");
        arguments.add("arg1");
        arguments.add("arg2");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        Assert.assertEquals("--test arg1 arg2", writeableCommandLine.toString());
    }

    @Test
    public void testGetOptions() {
        Option option = new Option("i", "input", true, "input file path");
        List arguments = new ArrayList();
        arguments.add("--input=input.txt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        List options = writeableCommandLine.getOptions();
        Assert.assertEquals(1, options.size());
        Assert.assertEquals(option, options.get(0));
    }

    @Test
    public void testGetOptionTriggers() {
        Option option = new Option("o", "output", true, "output file path");
        List arguments = new ArrayList();
        arguments.add("--output=test.txt");

        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(option, arguments);

        Set optionTriggers = writeableCommandLine.getOptionTriggers();
        Assert.assertEquals(1, optionTriggers.size());
        Assert.assertEquals("output", optionTriggers.iterator().next().getLongOpt());
    }
}