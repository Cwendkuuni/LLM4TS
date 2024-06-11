import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithNoOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertNull(cmd.getOptionValues("help"));
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Path to output file");
        options.addOption(option);

        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "output.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"output.txt"}, cmd.getOptionValues("output"));
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        Option option1 = new Option("o", "output", true, "Path to output file");
        Option option2 = new Option("i", "input", true, "Path to input file");

        options.addOption(option1);
        options.addOption(option2);

        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "output.txt", "-i", "input.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"output.txt"}, cmd.getOptionValues("output"));
        Assert.assertEquals(new String[]{"input.txt"}, cmd.getOptionValues("input"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Path to output file");
        options.addOption(option);

        Parser parser = new DefaultParser();

        String[] arguments = {"-i", "input.txt"};
        parser.parse(options, arguments);

        // This line should throw a ParseException
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("x", "output", true, "Path to output file");
        options.addOption(option);

        Parser parser = new DefaultParser();

        String[] arguments = {"-x", "output.txt"};
        parser.parse(options, arguments);

        // This line should throw a ParseException
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        Option option = new Option("long-output", "long-output", true, "Path to long output file");
        options.addOption(option);

        Parser parser = new DefaultParser();

        String[] arguments = {"--long-output", "long-output.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"long-output.txt"}, cmd.getOptionValues("long-output"));
    }

    @Test
    public void testParseWithMixedShortAndLongOptions() throws ParseException {
        Options options = new Options();
        Option optionShort = new Option("o", "output", true, "Path to output file");
        Option optionLong = new Option("long-output", "long-output", true, "Path to long output file");
        options.addOption(optionShort);
        options.addOption(optionLong);

        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "output.txt", "--long-output", "long-output.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"output.txt"}, cmd.getOptionValues("output"));
        Assert.assertEquals(new String[]{"long-output.txt"}, cmd.getOptionValues("long-output"));
    }

    @Test
    public void testParseWithMixedShortAndLongOptionsGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        group.setRequired(true);

        Option optionShort = new Option("s", "short", true, "Short option value");
        group.addOption(optionShort);

        Option optionLong = new Option("long", "long", true, "Long option value");
        group.addOption(optionLong);

        options.addOptionGroup(group);

        Parser parser = new DefaultParser();

        String[] arguments = {"-s", "short_value", "--long", "long_value"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"short_value"}, cmd.getOptionValues("short"));
        Assert.assertEquals(new String[]{"long_value"}, cmd.getOptionValues("long"));
        Assert.assertTrue(group.isSelected());
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Path to output file");
        option.setArgs(1);
        options.addOption(option);

        Parser parser = new DefaultParser();

        String[] arguments = {"-o", "output1.txt", "-o", "output2.txt"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertEquals(new String[]{"output1.txt", "output2.txt"}, cmd.getOptionValues("output"));
    }

    @Test
    public void testParseWithMinimumNumberOfArguments() throws ParseException {
        Options options = new Options();
        Option argOption = new Option("a", "arg", true, "Argument to be passed");
        argOption.setMinimum(1);
        options.addOption(argOption);

        Parser parser = new DefaultParser();

        String[] arguments = {"--arg", "arg1", "arg2"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("arg2", cmd.getArgs().get(0));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option argOption = new Option("a", "arg", true, "Argument to be passed");
        argOption.setMinimum(1);
        options.addOption(argOption);

        Parser parser = new DefaultParser();

        String[] arguments = {"--arg"};
        parser.parse(options, arguments);

        // This line should throw a ParseException
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException {
        Options options = new Options();
        Option argOption = new Option("a", "arg", true, "Argument to be passed");
        options.addOption(argOption);

        Parser parser = new DefaultParser();

        String[] arguments = {"--arg", "arg1", "arg2", "arg3"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("arg2", cmd.getArgs().get(0));
        Assert.assertEquals("arg3", cmd.getArgs().get(1));
    }
}