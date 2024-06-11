import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = {"-o", "output.txt"};

        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        String[] args = {"--input", "input.txt", "--output", "output.txt"};

        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file path");
        options.addRequiredOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = {"-o"};

        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail(); // should throw MissingArgumentException
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingOption() throws ParseException {
        String[] args = {"--input", "input.txt"};

        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail(); // should throw MissingOptionException
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"--unknown"};

        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail(); // should throw UnrecognizedOptionException
    }

    @Test(expected = NoSuchOptionException.class)
    public void testNoSuchOption() throws ParseException {
        String[] args = {"--invalid-option"};

        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail(); // should throw NoSuchOptionException
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--input", "input.txt", "--output", "output.txt"};
        Properties props = new Properties();
        props.setProperty("input", "input.txt");
        props.setProperty("output", "output.txt");

        Options options = new Options();
        options.addRequiredOption("i", "input", true, "input file path");
        options.addRequiredOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props, false);

        Assert.assertEquals("input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("output.txt", cmd.getOptionValue("output"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = {"--option1", "--option2"};

        Options options = new Options();
        options.addRequiredOption("o1", "option1");
        options.addRequiredOption("o2", "option2");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args, null, false);
            Assert.fail(); // should throw MissingOptionException
        } catch (MissingOptionException e) {
            // expected
        }
    }

    @Test(expected = ParseException.class)
    public void testInvalidParse() throws ParseException {
        String[] args = {"--", "--input"};

        Options options = new Options();
        options.addRequiredOption("o", "output", true, "output file path");

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args, null, false);
            Assert.fail(); // should throw ParseException
        } catch (ParseException e) {
            // expected
        }
    }
}