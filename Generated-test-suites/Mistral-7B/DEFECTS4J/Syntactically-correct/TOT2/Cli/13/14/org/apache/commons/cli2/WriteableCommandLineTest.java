import static org.junit.Assert.*;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;

public class WriteableCommandLineTest {

    @org.junit.Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));

        try {
            commandLine.addOption(option);
            fail("Option has already been added.");
        } catch (IllegalStateException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testAddValue() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value");
        Option option = Option.builder("t").longOpt("test").hasArg(true).build();
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        assertEquals("testValue", commandLine.getOptionValue("test"));

        try {
            commandLine.addValue(option, "testValue2");
            fail("Value has already been added.");
        } catch (IllegalStateException e) {
            // expected
        }

        try {
            commandLine.addValue(Option.builder("x").longOpt("x").build(), "testValue");
            fail("Invalid option name.");
        } catch (IllegalArgumentException e) {
            // expected
        }

        try {
            commandLine.addValue(Option.builder("t").longOpt("test").hasArg(false).build(), "testValue");
            fail("Invalid option type.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testGetValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test value1 value2");
        Option option = Option.builder("t").longOpt("test").hasArgs(true).build();
        commandLine.addOption(option);
        List<String> values = commandLine.getValuesForOption("test");
        assertEquals(Arrays.asList("value1", "value2"), values);

        try {
            commandLine.getValuesForOption("invalidOption");
            fail("Invalid option name.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @org.junit.Test
    public void testSetDefaultValues() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.setDefaultValues(option, Arrays.asList("defaultValue1", "defaultValue2"));
        assertEquals(Arrays.asList("defaultValue1", "defaultValue2"), commandLine.getValuesForOption("test"));

        try {
            commandLine.setDefaultValues(Option.builder("x").longOpt("x").build(), Arrays.asList("defaultValue1", "defaultValue2"));
            fail("Invalid option name.");
        } catch (IllegalArgumentException e) {
            // expected
        }
    }
}