import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option optionOne = new Option("o", "output", true, "Output directory")
                .setRequired(false);
        Option optionTwo = new Option("i", "input", true, "Input file")
                .setRequired(true);
        Option optionThree = new Option("h", "help", false, "Print help message");

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(optionOne,
                Arrays.asList(optionTwo, optionThree, new Argument("arg1", "value1")));

        // When
        commandLine.addOption(optionOne);
        commandLine.addValue(optionOne, "outputDir");
        commandLine.addOption(optionTwo);
        commandLine.addValue(optionTwo, "inputFile.txt");
        commandLine.addSwitch(optionThree, true);

        CommandLine parsedCommandLine;
        try {
            parsedCommandLine = commandLine.parse();
        } catch (ParseException e) {
            // In case of any parsing error
            Assert.fail("ParseException: " + e.getMessage());
            return;
        }

        // Then
        Assert.assertEquals("outputDir", parsedCommandLine.getOptionValue("output"));
        Assert.assertEquals("inputFile.txt", parsedCommandLine.getOptionValue("input"));
        Assert.assertTrue(parsedCommandLine.hasOption("help"));
        Assert.assertFalse(parsedCommandLine.hasOption("nonexistent"));

        // When -e option is provided
        commandLine = new WriteableCommandLineImpl(optionOne, Arrays.asList(new Option("e", "error", false)));
        commandLine.parse();
        Assert.assertTrue(parsedCommandLine.hasOption("error"));

        // When -o and -i options are missing
        commandLine = new WriteableCommandLineImpl(optionOne, Collections.emptyList());
        try {
            commandLine.parse();
            Assert.fail("ParseException expected");
        } catch (ParseException e) {
            Assert.assertEquals(1, e.getParsed().size());
            Assert.assertEquals("No option '-o' or '-i' provided", e.getParsed().get(0).getMessage());
        }

        // When -o option is missing but -i is present
        commandLine = new WriteableCommandLineImpl(optionOne, Arrays.asList(optionTwo));
        try {
            commandLine.parse();
            Assert.fail("ParseException expected");
        } catch (ParseException e) {
            Assert.assertEquals(2, e.getParsed().size());
            Assert.assertEquals("No option '-o' provided", e.getParsed().get(0).getMessage());
            Assert.assertEquals("'input' option requires an argument", e.getParsed().get(1).getMessage());
        }
    }
}