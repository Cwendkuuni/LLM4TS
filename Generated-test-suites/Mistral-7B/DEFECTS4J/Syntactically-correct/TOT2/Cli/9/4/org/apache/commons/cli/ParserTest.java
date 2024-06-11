import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    private CommandLineParser parser;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addRequiredOption("i", "input", true, "Input file path");
        options.addOption("o", "output", true, "Output file path");

        parser = new DefaultParser();
    }

    @Test
    public void testParseWithValidArguments() throws ParseException {
        String[] arguments = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"-o", "/path/to/output.txt"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"-x", "/path/to/output.txt"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithExtraArguments() throws ParseException {
        String[] arguments = new String[]{"-i", "/path/to/input.txt", "-o", "/path/to/output.txt", "extra"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getArgs());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Properties properties = new Properties();
        properties.put("input", "/path/to/input.txt");
        properties.put("output", "/path/to/output.txt");

        CommandLine cmd = parser.parse(options, null, properties);

        Assert.assertEquals("/path/to/input.txt", cmd.getOptionValue("input"));
        Assert.assertEquals("/path/to/output.txt", cmd.getOptionValue("output"));
        Assert.assertNull(cmd.getArgs());
    }
}