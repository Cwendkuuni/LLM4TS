import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test case for the Parser class.
 */
public class ParserTest {

    /**
     * Test the parse method with valid options and arguments.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseValidOptionsAndArguments()
        throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "input file");
        options.addOption("o", "outputFile", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]),
                                      new String[]{"-i", "test.txt", "-o", "output.txt"});

        Assert.assertNotNull(cmd.getOptionValue("inputFile"));
        Assert.assertNotNull(cmd.getOptionValue("outputFile"));
        Assert.assertEquals("test.txt", cmd.getOptionValue("inputFile"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("outputFile"));
        Assert.assertNull(cmd.getArgs());
    }

    /**
     * Test the parse method with missing required option.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption()
        throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]),
                                      new String[0]);
    }

    /**
     * Test the parse method with missing required argument.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseMissingRequiredArgument()
        throws ParseException
    {
        Options options = new Options();
        options.addOption("o", "outputFile", true, "output file");
        options.addRequiredOption("i", "inputFile", true, "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]),
                                      new String[]{"-o", "output.txt"});
    }

    /**
     * Test the parse method with unrecognized option.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption()
        throws ParseException
    {
        Options options = new Options();
        options.addOption("i", "inputFile", true, "input file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]),
                                      new String[]{"-x", "test.txt"});
    }

    /**
     * Test the parse method with multiple values for an option.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseMultipleValuesForOption()
        throws ParseException
    {
        Options options = new Options();
        options.addOption("l", "logs", true, "log files");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]),
                                      new String[]{"-l", "log1.txt", "-l", "log2.txt"});

        Assert.assertArrayEquals(new String[]{"log1.txt", "log2.txt"},
                               cmd.getOptionValues("logs"));
    }

    /**
     * Test the parse method with an option group.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseOptionGroup()
        throws ParseException
    {
        // Define the options
        Options options = new Options();
        OptionGroup group = new OptionGroup("profiles", "Profiles:");
        group.addOption(new Option("p", "profile", true, "profile key"));
        group.addOption(new Option("f", "file", true, "file name"));

        options.addOptionGroup(group);

        // Define the test case
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]),
                                      new String[]{"-p", "test1", "-f", "test.txt"});

        Assert.assertEquals("test1", cmd.getOptionValue("profile"));
        Assert.assertEquals("test.txt", cmd.getOptionValue("file"));
        Assert.assertNull(cmd.getOptionValue("profiles.test2.file"));

        // Test the second profile
        cmd = parser.parse(options.toArray(new String[0]),
                          new String[]{"-p", "test2", "--", "-f", "test2.txt"});

        Assert.assertEquals("test2", cmd.getOptionValue("profile"));
        Assert.assertEquals("test2.txt", cmd.getOptionValue("file"));
        Assert.assertEquals("test2.txt", cmd.getOptionValue("profiles.test2.file"));
    }

    /**
     * Test the parse method with properties.
     *
     * @throws ParseException if an error occurs when parsing the
     * arguments.
     */
    @Test
    public void testParseProperties()
        throws ParseException
    {
        Options options = new Options();
        options.addRequiredOption("i", "inputFile", true, "input file");

        Properties properties = new Properties();
        properties.setProperty("inputFile", "input.txt");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new String[0]),
                                      new String[0], properties);

        Assert.assertEquals("input.txt", cmd.getOptionValue("inputFile"));
    }
}