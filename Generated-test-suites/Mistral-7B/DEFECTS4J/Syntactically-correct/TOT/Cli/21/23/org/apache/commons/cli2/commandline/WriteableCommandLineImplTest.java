import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        Option option = new Option("o", "output", true, "Output file path")
                .setRequired(false);
        Option inputOption = new Option("i", "input", true, "Input file path")
                .setRequired(true);
        Option helpOption = new Option("h", "help", false, "Print usage")
                .setAllowedValues(Collections.singletonList("help"));

        WriteableCommandLineImpl cmd = new WriteableCommandLineImpl(helpOption, asList("h"));
        cmd.addOption(option);
        cmd.addOption(inputOption);

        // Add values and switches
        cmd.addValue(option, "output.txt");
        cmd.addValue(inputOption, "input.txt");
        cmd.addSwitch(option, true);

        // Test getters
        assertEquals(helpOption, cmd.getOption(0));
        assertEquals(option, cmd.getOption("o"));
        assertEquals(inputOption, cmd.getOption("i"));
        assertEquals("Output file path", option.getLongOpt());
        assertEquals("output.txt", cmd.getValue(option, null));
        assertEquals("Input file path", inputOption.getLongOpt());
        assertEquals("input.txt", cmd.getValue(inputOption, null));
        assertTrue(cmd.hasOption(option));
        assertTrue(cmd.hasOption(inputOption));
        assertFalse(cmd.hasOption(new Option("nonexistent")));
        assertFalse(cmd.getSwitch(helpOption, false));
        assertTrue(cmd.getSwitch(option, null));

        // Test adding an existing option
        try {
            cmd.addOption(option);
            fail();
        } catch (IllegalStateException e) {
            // expected
        }

        // Test getting properties
        Properties optionsProperties = cmd.getProperties(option);
        assertEquals(1, optionsProperties.size());
        assertEquals("Output file path", optionsProperties.getProperty("name"));

        // Test adding properties
        cmd.addProperty(option, "name", "newName");
        optionsProperties = cmd.getProperties(option);
        assertEquals("newName", optionsProperties.getProperty("name"));

        // Test toString
        assertEquals("h i=\"input.txt\" o=\"output.txt\"", cmd.toString());
    }
}