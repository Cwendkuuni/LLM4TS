import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    @Test
    public void testCommandLineParsing() throws ParseException {
        String[] args = new String[]{"-a", "1", "--optionB", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("c"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("c"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        List<String> unrecognizedArgs = cmd.getArgList();
        Assert.assertEquals(0, unrecognizedArgs.size());
    }

    @Test(expected = ParseException.class)
    public void testCommandLineParsingWithInvalidOption() throws ParseException {
        String[] args = new String[]{"-x", "1", "--optionB", "2"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.fail("Should throw ParseException with message containing 'Unrecognized option: -x'");
    }

    @Test
    public void testCommandLineParsingWithNoOptions() {
        String[] args = new String[]{"arg1", "arg2"};

        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        List<String> unrecognizedArgs = cmd.getArgList();
        Assert.assertEquals(2, unrecognizedArgs.size());
        Assert.assertEquals("arg1", unrecognizedArgs.get(0));
        Assert.assertEquals("arg2", unrecognizedArgs.get(1));
    }
}