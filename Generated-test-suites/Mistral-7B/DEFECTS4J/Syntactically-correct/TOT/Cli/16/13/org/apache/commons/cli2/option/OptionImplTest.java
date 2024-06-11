import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.DefaultParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.OptionGroup;
import org.apache.commons.cli2.OptionSet;
import org.apache.commons.cli2.CommandLine;

import java.util.Arrays;
import java.util.List;

public class OptionImplTest {

    @Test
    public void testOptionImpl() {
        Option option = new OptionBuilder()
                .withLongOpt("input")
                .withDescription("Input file")
                .isRequired()
                .create();

        OptionGroup group = new OptionGroup()
                .withLongName("input-options")
                .withDescription("Input options")
                .addOption(option);

        CommandLineParser parser = new DefaultParser();
        OptionSet options = parser.parse(group, new String[]{"-i", "--input", "test.txt"});

        WriteableCommandLine commandLine = options.getCommandLine();

        // Test id
        assertEquals(option.getId(), commandLine.getOptionValues("input")[0].hashCode());

        // Test required
        assertTrue(option.isRequired());
        assertFalse(commandLine.hasOption("output")); // should fail because input is required

        // Test toString
        assertEquals("Input file", option.getLongOpt());
        assertEquals("-i, --input", option.getOpt());
        assertEquals("Input file\n", option.toString());

        // Test equals
        Option opt = new OptionBuilder()
                .withLongOpt("input")
                .withDescription("Input file")
                .isRequired()
                .create();
        assertTrue(option.equals(opt));

        // Test hashCode
        Option opt2 = new OptionBuilder()
                .withLongOpt("input2")
                .withDescription("Input file2")
                .isRequired()
                .create();
        assertNotEquals(option.hashCode(), opt2.hashCode());

        // Test findOption
        Option foundOption = option.findOption("input");
        assertSame(option, foundOption);

        // Test canProcess
        WriteableCommandLine emptyCommandLine = new WriteableCommandLine();
        WriteableCommandLine commandLineWithInput = new WriteableCommandLine();
        WriteableCommandLine commandLineWithOutput = new WriteableCommandLine();
        commandLineWithOutput.addOption(option);
        commandLineWithOutput.addOption(OptionBuilder.withLongOpt("output").create());

        assertFalse(option.canProcess(emptyCommandLine, null));
        assertFalse(option.canProcess(commandLineWithOutput, null));
        assertTrue(option.canProcess(commandLineWithInput, null));

        // Test toString with DisplaySetting.ALL
        String expectedUsage = "Input file\n" +
                "  is required.\n";
        assertEquals(expectedUsage, option.toString(DisplaySetting.ALL, null));

        // Test checkPrefixes
        Set prefixes = Arrays.asList("--input", "--i");
        option.checkPrefixes(prefixes);

        try {
            Set invalidPrefixes = Arrays.asList("--output");
            option.checkPrefixes(invalidPrefixes);
            fail("IllegalArgumentException expected");
        } catch (IllegalArgumentException e) {
            String expectedMessage = "option 'input' requires a prefix among [" + prefixes + "] but '--output' was provided";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}