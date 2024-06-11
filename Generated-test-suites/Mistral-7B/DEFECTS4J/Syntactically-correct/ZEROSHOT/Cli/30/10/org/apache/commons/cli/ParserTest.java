import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseCommonOptions() throws ParseException {
        String[] inputArgs = {"-a", "arg1", "--optionB", "arg2", "--optionC=arg3"};
        Options options = new Options();
        options.addOption("a", true, "Option A", "an option with an argument");
        options.addOption("b", false, "Option B", "Option B");
        options.addOption("c", true, "Option C", "Option C with an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, inputArgs, (Properties) null, false);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertEquals("arg1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
        Assert.assertEquals("arg3", cmd.getOptionValue("c"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingOptionArgument() throws ParseException {
        String[] inputArgs = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A", "an option with an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, inputArgs, (Properties) null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] inputArgs = {};
        Options options = new Options();
        options.addOption("a", true, "Option A", "an option with an argument");
        options.addRequiredOption("b", false, "Option B", "an option without an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, inputArgs, (Properties) null, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] inputArgs = {"--unknownOption", "arg1"};
        Options options = new Options();
        options.addOption("a", true, "Option A", "an option with an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, inputArgs, (Properties) null, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testParseWithInvalidInput() throws ParseException {
        String[] inputArgs = {"--"};
        Options options = new Options();
        options.addOption("a", true, "Option A", "an option with an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, inputArgs, (Properties) null, false);
    }

    @Test
    public void testParseWithEmptyInput() throws ParseException {
        String[] inputArgs = {};
        Options options = new Options();
        options.addOption("a", true, "Option A", "an option with an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, inputArgs, (Properties) null, false);

        Assert.assertTrue(cmd.getArgs().isEmpty());
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] inputArgs = {"-p", "prop1=value1", "--optionB=arg2"};
        Options options = new Options();
        options.addOption("p", true, "Property", "Property option");
        options.addOption("b", false, "Option B", "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, inputArgs, new Properties(), false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("value1", cmd.getOptionValue("p", null));
        Assert.assertEquals("arg2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithMixedInput() throws ParseException {
        String[] inputArgs = {"-x", "--optionB=arg2", "-y", "--optionC=arg3", "arg1"};
        Options options = new Options();
        options.addOption("x", false, "Option X", "Option X");
        options.addOption("b", false, "Option B", "Option B");
        options.addOption("c", true, "Option C", "Option C with an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, inputArgs, (Properties) null, false);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertFalse(cmd.hasOption("x"));
        Assert.assertEquals("arg2", cmd.getOptionValue("b"));
        Assert.assertEquals("arg3", cmd.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnprocessedArguments() throws ParseException {
        String[] inputArgs = {"--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A", "an option with an argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, inputArgs, (Properties) null, true);
    }
}