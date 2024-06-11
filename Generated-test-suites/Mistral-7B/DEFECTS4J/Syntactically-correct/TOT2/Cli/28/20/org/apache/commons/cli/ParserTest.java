import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        // Create test options
        Options options = new Options();
        options.addOption("o", "output", true, "output file name");

        // Create test data
        String[] args = new String[]{"--output", "test.txt"};

        // Create test parser
        Parser parser = new DefaultParser();

        // Run test
        CommandLine cmd = parser.parse(options, args, false);

        // Test results
        Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getArgs().length);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        // Create test options
        Options options = new Options();

        // Create test data
        String[] args = new String[]{"-- nonsense"};

        // Create test parser
        Parser parser = new DefaultParser();

        // Run test
        parser.parse(options, args, false);

        // Test results not reached, as a ParseException is expected
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException
    {
        // Create test options
        Options options = new Options();
        options.addOption("o", "output", true, "output file name");

        // Create test data
        String[] args = new String[]{"--output"};

        // Create test parser
        Parser parser = new DefaultParser();

        // Run test
        parser.parse(options, args, false);

        // Test results not reached, as a ParseException is expected
    }

    @Test
    public void testParseMultipleOptions() throws ParseException
    {
        // Create test options
        Options options = new Options();
        options.addOption("o", "output", true, "output file name");
        options.addOption("h", "help", false, "print this message");

        // Create test data
        String[] args = new String[]{"--output", "test.txt", "--help"};

        // Create test parser
        Parser parser = new DefaultParser();

        // Run test
        CommandLine cmd = parser.parse(options, args, false);

        // Test results
        Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
        Assert.assertTrue(cmd.hasOption("help"));
        Assert.assertNull(cmd.getArgs().length);
    }

    @Test
    public void testParseMultipleArguments() throws ParseException
    {
        // Create test options
        Options options = new Options();

        // Create test data
        String[] args = new String[]{"arg1", "arg2", "arg3"};

        // Create test parser
        Parser parser = new DefaultParser();

        // Run test
        CommandLine cmd = parser.parse(options, args, false);

        // Test results
        Assert.assertNull(cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getArgs());

        // Test that all arguments are present in the cmd.args list
        Assert.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, cmd.getArgs());
    }

    @Test
    public void testParseMultipleOptionsAndArguments() throws ParseException
    {
        // Create test options
        Options options = new Options();
        options.addOption("o", "output", true, "output file name");
        options.addOption("h", "help", false, "print this message");

        // Create test data
        String[] args = new String[]{"--output", "test.txt", "--help", "arg4"};

        // Create test parser
        Parser parser = new DefaultParser();

        // Run test
        CommandLine cmd = parser.parse(options, args, false);

        // Test results
        Assert.assertEquals("test.txt", cmd.getOptionValue("output"));
        Assert.assertTrue(cmd.hasOption("help"));
        Assert.assertArrayEquals(new String[]{"arg4"}, cmd.getArgs());
    }
}