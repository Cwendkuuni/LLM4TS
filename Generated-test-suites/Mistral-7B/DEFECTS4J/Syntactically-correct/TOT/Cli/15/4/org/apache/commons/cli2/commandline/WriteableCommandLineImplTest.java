import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given a WriteableCommandLineImpl instance
        Option option = new Option("o", "output", true, "Output file path");
        List arguments = Arrays.asList("MyApp.jar", "--output", "output.txt");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // When adding an option
        commandLine.addOption(option);

        // Then the option should be present in the list of options
        Assert.assertEquals(1, commandLine.getOptions().size());

        // And it should have the correct name and value
        Option optionFromCommandLine = commandLine.getOption("output");
        Assert.assertEquals(option, optionFromCommandLine);

        // When adding a value to the option
        commandLine.addValue(option, "test.txt");

        // Then the value should be present in the list of values for the option
        List values = commandLine.getValues(option, null);
        Assert.assertEquals(1, values.size());
        Assert.assertEquals("test.txt", values.get(0));

        // And the command line should have the value set
        Assert.assertEquals("test.txt", commandLine.getProperty("output"));

        // When setting a switch
        Option switchOption = new Option("s", "silent", false, "Run in silent mode");
        commandLine = new WriteableCommandLineImpl(switchOption, arguments);
        commandLine.addOption(switchOption);
        commandLine.addSwitch(switchOption, true);

        // Then the switch should be set to true
        Assert.assertTrue(commandLine.getSwitch(switchOption, false));

        // And it should not have a value
        Assert.assertFalse(commandLine.hasOption(option));
        Assert.assertNull(commandLine.getProperty("output"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidArgument() {
        // Given an invalid command line argument
        Option option = new Option("o", "output", true, "Output file path");
        List arguments = Arrays.asList("MyApp.jar", "--output", "{not_a_valid_file_path}");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // When parsing the command line
        commandLine.parse();

        // Then an exception should be thrown
    }

    @Test
    public void testEmptyOption() {
        // Given an empty option argument
        Option option = new Option("o", "output", true, "Output file path");
        List arguments = Arrays.asList("MyApp.jar", "--output");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // When parsing the command line
        commandLine.parse();

        // Then no value should be set for the option
        Assert.assertFalse(commandLine.hasOption(option));
        Assert.assertNull(commandLine.getProperty("output"));
    }

    @Test
    public void testOptionalOption() {
        // Given an optional option argument
        Option option = new Option("o", "output", false, "Output file path");
        List arguments = Arrays.asList("MyApp.jar");
        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, arguments);

        // When parsing the command line
        commandLine.parse();

        // Then the option should not be present
        Assert.assertFalse(commandLine.hasOption(option));
        Assert.assertNull(commandLine.getProperty("output"));

        // And when the option is provided in a subsequent command line
        commandLine = new WriteableCommandLineImpl(option, Arrays.asList("MyApp.jar", "--output", "output.txt"));

        // Then the option should be present and have the correct value
        Assert.assertTrue(commandLine.hasOption(option));
        Assert.assertEquals("output.txt", commandLine.getProperty("output"));
    }
}