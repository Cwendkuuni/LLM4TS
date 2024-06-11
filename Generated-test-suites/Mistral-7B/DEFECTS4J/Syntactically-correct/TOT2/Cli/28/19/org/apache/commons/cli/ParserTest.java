import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParsingException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for Parser.
 */
public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
        options = new Options();
        options.addRequiredOption("i", "input", true, "input file");
        options.addRequiredOption("o", "output", true, "output file");
        options.addOption("h", "help", false, "display this help message");
        options.addOption("v", "verbose", false, "display verbose messages");
    }

    /**
     * Test parsing of valid arguments.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseValidArguments() throws ParseException
    {
        String[] arguments = {"-i", "input.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("help"));
        Assert.assertNull(cmd.getOptionValue("verbose"));
    }

    /**
     * Test parsing of arguments with required options missing.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOptions() throws ParseException
    {
        String[] arguments = {"-o", "output.txt"};
        parser.parse(options, arguments);
    }

    /**
     * Test parsing of arguments with invalid option.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() throws ParseException
    {
        String[] arguments = {"--unknown"};
        parser.parse(options, arguments);
    }

    /**
     * Test parsing of arguments with optional option without value.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseOptionalOptionWithoutValue() throws ParseException
    {
        String[] arguments = {"-h"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNull(cmd.getOptionValue("input"));
        Assert.assertNull(cmd.getOptionValue("output"));
        Assert.assertTrue(cmd.hasOption("help"));
        Assert.assertNull(cmd.getOptionValue("verbose"));
    }

    /**
     * Test parsing of arguments with optional option with value.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseOptionalOptionWithValue() throws ParseException
    {
        String[] arguments = {"-v"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNull(cmd.getOptionValue("input"));
        Assert.assertNull(cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("help"));
        Assert.assertTrue(cmd.hasOption("verbose"));
    }

    /**
     * Test parsing of arguments with multiple optional options.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseMultipleOptionalOptions() throws ParseException
    {
        String[] arguments = {"-h", "-v"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertNull(cmd.getOptionValue("input"));
        Assert.assertNull(cmd.getOptionValue("output"));
        Assert.assertTrue(cmd.hasOption("help"));
        Assert.assertTrue(cmd.hasOption("verbose"));
    }

    /**
     * Test parsing of arguments with both required and optional options.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseBothRequiredAndOptionalOptions() throws ParseException
    {
        String[] arguments = {"-i", "input.txt", "-o", "output.txt", "-v"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertTrue(cmd.hasOption("verbose"));
        Assert.assertNull(cmd.getOptionValue("help"));
    }

    /**
     * Test parsing of arguments with both required and invalid options.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseBothRequiredAndInvalidOptions() throws ParseException
    {
        String[] arguments = {"--input", "input.txt", "-o", "output.txt"};
        parser.parse(options, arguments);
    }

    /**
     * Test parsing of arguments with multiple required options.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMultipleRequiredOptionsMissing() throws ParseException
    {
        String[] arguments = {"-o", "output.txt"};
        parser.parse(options, arguments);
    }

    /**
     * Test parsing of arguments with multiple required options with values.
     * @throws ParseException if an error occurs when parsing the arguments.
     */
    @Test
    public void testParseMultipleRequiredOptionsWithValues() throws ParseException
    {
        String[] arguments = {"-i", "input1.txt", "-i", "input2.txt", "-o", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments);
        List<String> inputFiles = Arrays.asList(cmd.getOptionValues("input"));
        Assert.assertEquals(2, inputFiles.size());
        Assert.assertEquals("input1.txt", inputFiles.get(0));
        Assert.assertEquals("input2.txt", inputFiles.get(1));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getOptionValue("help"));
        Assert.assertNull(cmd.getOptionValue("verbose"));
    }
}