import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {
    private CommandLine commandLine;
    private Option option;

    @Before
    public void setUp() {
        commandLine = new CommandLine();
        option = new Option("optionName", "optionDescription");
    }

    @Test
    public void testAddOption() {
        Option option = new Option("optionName", "optionDescription");
        commandLine.addOption(option);
        assertEquals(1, commandLine.getOptions().size());
        assertEquals(option, commandLine.getOptions().get(0));

        // Edge cases and exception handling:
        // Check if the option is null
        try {
            commandLine.addOption(null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Check if the option description is null or empty
        try {
            commandLine.addOption(new Option(null, ""));
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Check if the option is already present in the command line
        commandLine.addOption(option);
        try {
            commandLine.addOption(option);
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }
    }

    @Test
    public void testAddValue() {
        commandLine.addOption(option);
        commandLine.addValue(option, "testValue");
        assertNotNull(commandLine.getValuesForOption(option));
        assertEquals(1, commandLine.getValuesForOption(option).size());
        assertEquals("testValue", commandLine.getValuesForOption(option).get(0));

        // Edge cases and exception handling:
        // Check if the option is null
        try {
            commandLine.addValue(null, "testValue");
            fail();
        } catch (NullPointerException e) {
            // Expected
        }

        // Check if the option already has a value
        commandLine.addValue(option, "testValue");
        try {
            commandLine.addValue(option, "newTestValue");
            fail();
        } catch (IllegalStateException e) {
            // Expected
        }

        // Check if the value is null
        commandLine.addValue(option, null);
        try {
            commandLine.getValuesForOption(option).get(0);
            fail();
        } catch (NullPointerException e) {
            // Expected, since the value is null
        }
    }
}