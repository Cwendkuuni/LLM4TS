import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.parse.ParseResult;
import org.junit.Assert;
import org.junit.Test;

public class WriteableCommandLineImplTest {

    @Test
    public void testWriteableCommandLineImpl() {
        // Given
        Option option = new Option("o", "output", true, "Output file path");
        Option option2 = new Option("i", "input", true, "Input file path");
        Option option3 = new Option("v", "verbose", false, "Enable verbose logging");
        Option rootOption = new Option("t", "transform", 1, "Transform file(s) using specified options", option, option2);

        WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(rootOption, Collections.singletonList("input.txt"));

        // When
        commandLine.addOption(option);
        commandLine.addOption(option2);
        commandLine.addOption(option3);
        commandLine.addValue(option, "output.txt");
        commandLine.addValue(option2, "input.txt");
        commandLine.addSwitch(option3, true);

        // Then
        Assert.assertEquals(2, commandLine.getOptions().size());
        Assert.assertEquals("output", commandLine.getOption(0).getLongOpt());
        Assert.assertEquals("output", commandLine.getOption(0).getShortName());
        Assert.assertEquals("Output file path", commandLine.getOption(0).getDescription());
        Assert.assertEquals(true, commandLine.getOption(1).isValuePresent());
        Assert.assertEquals("output.txt", commandLine.getOption(1).getValue());
        Assert.assertEquals("input", commandLine.getOption(2).getLongOpt());
        Assert.assertEquals("input", commandLine.getOption(2).getShortName());
        Assert.assertEquals("Input file path", commandLine.getOption(2).getDescription());
        Assert.assertEquals(false, commandLine.getOption(3).isValuePresent());
        Assert.assertEquals(true, commandLine.getSwitch(option3, false));

        // When parsing the command line
        ParseResult result = rootOption.parse(commandLine.getNormalised().toArray(new String[0]));
        CommandLine parsedCommandLine = result.getParsedCommandLine();

        // Then the parsed command line should be equivalent to the original one
        Assert.assertEquals(commandLine, parsedCommandLine);

        // When reading properties from the parsed command line
        String outputFile = parsedCommandLine.getOptionValue("output");
        String inputFile = parsedCommandLine.getOptionValue("input");
        boolean verbose = parsedCommandLine.hasOption("verbose");

        // Then the parsed properties should be equivalent to the original values
        Assert.assertEquals("output.txt", outputFile);
        Assert.assertEquals("input.txt", inputFile);
        Assert.assertEquals(true, verbose);
    }
}