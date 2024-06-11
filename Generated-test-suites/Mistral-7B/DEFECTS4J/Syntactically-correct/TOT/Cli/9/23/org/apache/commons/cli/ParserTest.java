import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseSimpleCommandLine() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");

        CommandLineParser parser = new DefaultParser();

        String[] arguments = new String[]{"-i", "/path/to/input.txt"};
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("inputFile"));
        Assert.assertNull(cmd.getOptionValue("unknownOption"));
    }

    @Test
    public void testParseCommandLineWithMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");
        options.addOption("o", "outputFile", true, "output file path");

        String[] arguments = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
        CommandLine cmd = new DefaultParser().parse(options, arguments, false);

        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("outputFile"));
        Assert.assertNull(cmd.getOptionValue("unknownOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseCommandLineWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");
        options.addRequiredOption("o", "outputFile", true, "output file path");

        String[] arguments = new String[]{"-i", "/path/to/input.txt"};
        new DefaultParser().parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseCommandLineWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file path");

        String[] arguments = new String[]{"-x", "/path/to/input.txt"};
        new DefaultParser().parse(options, arguments, false);
    }

    @Test
    public void testParseCommandLineWithMultipleValuesForAnOption() throws ParseException {
        Options options = new Options();
        options.addOption("i", "inputFiles", true, "input files");

        String[] arguments = new String[]{"-i", "/path/to/input1.txt", "-i", "/path/to/input2.txt"};
        CommandLine cmd = new DefaultParser().parse(options, arguments, false);

        String[] inputFiles = cmd.getOptionValues("inputFiles");
        Assert.assertTrue(inputFiles.length > 0);
        Assert.assertEquals("/path/to/input1.txt", inputFiles[0]);
        Assert.assertEquals("/path/to/input2.txt", inputFiles[1]);
    }
}