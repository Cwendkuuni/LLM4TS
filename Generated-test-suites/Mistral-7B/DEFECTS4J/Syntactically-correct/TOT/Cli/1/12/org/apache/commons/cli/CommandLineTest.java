import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class CommandLineTest {

    @Test
    public void testSimpleOption() throws ParseException {
        String[] args = {"-o", "output.txt"};
        Options options = new Options();
        options.addOptional("o", "outputFile", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("o"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("o"));
    }

    @Test
    public void testMissingOption() {
        String[] args = {"--invalid"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = null;

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            // expected
        }

        Assert.assertNull(cmd);
    }

    @Test
    public void testMultipleOptions() throws ParseException {
        String[] args = {"-o", "output1.txt", "-o", "output2.txt"};
        Options options = new Options();
        options.addOptional("o", "outputFile", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("o"));
        Assert.assertEquals("output2.txt", cmd.getOptionValue("o"));

        // check that the first option was also processed
        Assert.assertTrue(cmd.hasOption("o"));
        Assert.assertEquals("output1.txt", cmd.getOptionValue("o"));
    }

    @Test
    public void testLongOption() throws ParseException {
        String[] args = {"--output", "output.txt"};
        Options options = new Options();
        options.addOptional("output", "outputFile", true, "output file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("output"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testNoOptions() {
        String[] args = {};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(0, cmd.getArgList().size());
    }

    @Test
    public void testArguments() throws ParseException {
        String[] args = {"--foo", "arg1", "arg2"};
        Options options = new Options();
        options.addOptional("foo", "arg", true, "an argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        List<String> argsList = cmd.getArgList();
        Assert.assertEquals(2, argsList.size());
        Assert.assertEquals("arg1", argsList.get(0));
        Assert.assertEquals("arg2", argsList.get(1));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = {};
        Options options = new Options();
        Option option = new Option("o", "outputFile", true, "output file");
        option.setRequired(true);
        options.addOption(option);

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testHelp() throws ParseException {
        String[] args = {"--help"};
        Options options = new Options();
        options.addHelp(true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("CommandLineTest", options);
    }

    @Test
    public void testInvalidOption() throws ParseException {
        String[] args = {"--invalid"};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertNull(cmd);
    }

    @Test
    public void testMixedOptions() throws ParseException {
        String[] args = {"-o", "output.txt", "--foo", "arg1"};
        Options options = new Options();
        options.addOptional("o", "outputFile", true, "output file");
        options.addOptional("foo", "arg", true, "an argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("o"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("o"));
        Assert.assertTrue(cmd.hasOption("foo"));
        Assert.assertEquals("arg1", cmd.getOptionValue("foo"));
    }
}