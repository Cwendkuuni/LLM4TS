import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest
{
    private Options options;
    private CommandLineParser parser;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addRequiredOption("o", "output", true, "Output file path");
        options.addOption("h", "help", false, "Print this message");
    }

    @Test
    public void testParseWithValidArguments() throws ParseException
    {
        String[] arguments = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
        Assert.assertFalse(cmd.hasOption("help"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingInputArgument() throws ParseException
    {
        String[] arguments = new String[]{"-o", "/path/to/output.txt"};
        parser.parse(options, arguments);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingOutputArgument() throws ParseException
    {
        String[] arguments = new String[]{"-i", "/path/to/input.txt"};
        parser.parse(options, arguments);
    }

    @Test(expected = NoSuchOptionException.class)
    public void testParseUnknownOption() throws ParseException
    {
        String[] arguments = new String[]{"--unknown"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOptionValue() throws ParseException
    {
        String[] arguments = new String[]{"-i", "/path/to/input.txt", "--output", "invalidpath"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseHelpOption() throws ParseException
    {
        String[] arguments = new String[]{"-h"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.hasOption("help"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Properties props = new Properties();
        props.setProperty("input", "/path/to/input.txt");
        props.setProperty("output", "/path/to/output.txt");

        CommandLine cmd = parser.parse(options, null, props, false);
        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
        Assert.assertFalse(cmd.hasOption("help"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException
    {
        String[] arguments = new String[]{"-o"};
        parser.parse(options, arguments, null, false);
    }

    @Before
    public void setUpParser()
    {
        parser = new DefaultParser();
    }
}