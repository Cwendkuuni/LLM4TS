import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        options.addOption(option);

        Assert.assertEquals(1, options.getOptions().size());
        Assert.assertEquals(option, options.getOption("o"));
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        group.setRequired(false);
        Option opt1 = new Option("opt1", "option1");
        Option opt2 = new Option("opt2", "option2");
        group.addOption(opt1);
        group.addOption(opt2);
        options.addOptionGroup(group);

        Assert.assertEquals(3, options.getOptions().size());
        Assert.assertEquals(opt1, options.getOption("group.opt1"));
        Assert.assertEquals(opt2, options.getOption("group.opt2"));
    }

    @Test
    public void testHasShortOption() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        options.addOption(option);

        Assert.assertTrue(options.hasShortOption("o"));
        Assert.assertFalse(options.hasShortOption("x"));
    }

    @Test
    public void testHasLongOption() {
        Options options = new Options();
        Option option = new Option("output", "op", true, "Output file path");
        options.addOption(option);

        Assert.assertTrue(options.hasLongOption("op"));
        Assert.assertFalse(options.hasLongOption("unknown"));
    }

    @Test
    public void testHasOption() {
        Options options = new Options();
        Option option = new Option("o", "output", true, "Output file path");
        options.addOption(option);

        Assert.assertTrue(options.hasOption("o"));
        Assert.assertFalse(options.hasOption("nonexistent"));
    }

    @Test
    public void testGetMatchingOptions() {
        Options options = new Options();
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        Option option3 = new Option("opt3", "option3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        List<String> matchingOptions = options.getMatchingOptions("opt");
        Assert.assertEquals(2, matchingOptions.size());
        Assert.assertTrue(matchingOptions.contains("opt1"));
        Assert.assertTrue(matchingOptions.contains("opt2"));
    }

    @Test
    public void testGetRequiredOptions() {
        Options options = new Options();
        Option option1 = new Option("opt1", "option1");
        Option option2 = new Option("opt2", "option2");
        Option option3 = new Option("opt3", "option3");
        Option option4 = new Option("opt4", "option4", true, "Required option");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        options.addOption(option4);

        List<Option> requiredOptions = options.getRequiredOptions();
        Assert.assertEquals(1, requiredOptions.size());
        Assert.assertEquals(option4, requiredOptions.get(0));
    }

    @Test
    public void testParseWithValidOptions() throws ParseException {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "Input file path");
        Option outputOption = new Option("o", "output", true, "Output file path");
        options.addOption(inputOption);
        options.addOption(outputOption);
        String[] args = {"-i", "input.txt", "-o", "output.txt"};

        CommandLine parsed = new DefaultParser().parse(options, args);

        Assert.assertEquals("input.txt", parsed.getOptionValue("input"));
        Assert.assertEquals("output.txt", parsed.getOptionValue("output"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidArgument() throws ParseException {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "Input file path");
        Option outputOption = new Option("o", "output", true, "Output file path");
        options.addOption(inputOption);
        options.addOption(outputOption);
        String[] args = {"-i", "input.txt", "-o", "output"};

        new DefaultParser().parse(options, args);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingArgument() throws ParseException {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "Input file path");
        Option outputOption = new Option("o", "output", true, "Output file path");
        options.addOption(inputOption);
        options.addOption(outputOption);
        String[] args = {"-i", "input.txt"};

        new DefaultParser().parse(options, args);
    }

    @Test
    public void testParseWithEmptyArguments() throws ParseException {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "Input file path");
        Option outputOption = new Option("o", "output", true, "Output file path");
        options.addOption(inputOption);
        options.addOption(outputOption);
        String[] args = {};

        CommandLine parsed = new DefaultParser().parse(options, args);

        Assert.assertNull(parsed.getOptionValue("input"));
        Assert.assertNull(parsed.getOptionValue("output"));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        Option inputOption = new Option("i", "input", true, "Input file path");
        Option outputOption = new Option("o", "output", true, "Output file path");
        options.addOption(inputOption);
        options.addOption(outputOption);
        String[] args = {"-x", "input.txt", "-o", "output.txt"};

        try {
            new DefaultParser().parse(options, args);
            Assert.fail();
        } catch (ParseException e) {
            Map<String, String> messages = e.getMessages();
            Assert.assertEquals(1, messages.size());
            Assert.assertEquals("Unrecognized option: -x", messages.get(""));
        }
    }
}