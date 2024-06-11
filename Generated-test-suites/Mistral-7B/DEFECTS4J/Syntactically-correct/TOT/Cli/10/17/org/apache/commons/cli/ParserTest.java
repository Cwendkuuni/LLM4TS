import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = {"-a", "1", "--b", "2"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Option A");
        Option optionB = new Option("b", "b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(args.length, cmd.args().size());
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] args = {"--c"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Option A");
        Option optionB = new Option("b", "b", true, "Option B");
        Option optionC = new Option("c", "c", true, "Option C");
        options.addOption(optionA);
        options.addOption(optionB);
        options.addRequiredOption(optionC);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: 'c'", e.getMessage());
        }
    }

    @Test
    public void testParseWithMissingOptionalOption() throws ParseException {
        String[] args = {"-a", "1"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Option A");
        Option optionB = new Option("b", "b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(1, cmd.args().size());
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = {"-x", "1"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Option A");
        Option optionB = new Option("b", "b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, args);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: -x", e.getMessage());
        }
    }

    @Test
    public void testParseWithMixedLongAndShortOptions() throws ParseException {
        String[] args = {"-a", "1", "--b", "2"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Option A");
        Option optionB = new Option("b", "b", true, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(args.length, cmd.args().size());
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithBooleanOptions() throws ParseException {
        String[] args = {"--verbose"};
        Options options = new Options();
        Option optionVerbose = new Option("verbose", "verbose", false, "output detailed info");
        options.addOption(optionVerbose);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertTrue(cmd.hasOption("verbose"));
    }

    @Test
    public void testParseWithValueOptions() throws ParseException {
        String[] args = {"--num", "5"};
        Options options = new Options();
        Option optionNum = new Option("num", "n", true, "Number of iterations");
        options.addOption(optionNum);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals("5", cmd.getOptionValue("num"));
    }

    @Test
    public void testParseWithStringArrays() throws ParseException {
        String[] args = {"--list", "item1", "--list", "item2"};
        Options options = new Options();
        Option optionList = new Option("list", "l", true, "list of items");
        options.addOption(optionList);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        String[] list = cmd.getOptionValues("list");
        Assert.assertArrayEquals(new String[]{"item1", "item2"}, list);
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException {
        String[] args = {"--group", "group1", "--group.option1", "value1", "--group.option2", "value2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "g", "Group options");
        Option optionGroup = new Option("group", "g", false, "Group options");
        Option optionGroup1 = new Option("group1", "group1", false, "Group option 1");
        Option optionGroupOption1 = new Option("option1", "o1", true, "Option 1");
        Option optionGroupOption2 = new Option("option2", "o2", true, "Option 2");
        group.addOption(optionGroup);
        group.addOption(optionGroup1);
        group.addOption(optionGroupOption1);
        group.addOption(optionGroupOption2);
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertTrue(cmd.hasOption("group"));
        Assert.assertTrue(cmd.hasOption("group1"));
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
    }

    @Test
    public void testParseWithMixedOptionsAndArgs() throws ParseException {
        String[] args = {"--option1", "value1", "--option2=value2", "arg1", "--option3"};
        Options options = new Options();
        Option option1 = new Option("option1", "o1", true, "Option 1");
        Option option2 = new Option("option2", "o2", true, "Option 2");
        Option option3 = new Option("option3", "o3", true, "Option 3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(args.length - 2, cmd.args().size());
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2"));
        Assert.assertEquals("arg1", cmd.args().get(0));
        Assert.assertEquals("", cmd.args().get(1));
        Assert.assertEquals("", cmd.args().get(2));
        Assert.assertEquals("", cmd.args().get(3));
        Assert.assertEquals("value3", cmd.getOptionValue("option3"));
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] args = {};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertEquals(args.length, cmd.args().size());
    }

    @Test
    public void testParseWithCustomHelpFormatter() throws ParseException {
        String[] args = {"--help"};
        Options options = new Options();
        Option helpOption = new Option("help", "h", false, "Print this message.");
        options.addOption(helpOption);
        OptionGroup helpGroup = new OptionGroup("help", "h");
        helpGroup.setRequired(false);
        helpGroup.addOption(helpOption);
        options.addOptionGroup(helpGroup);

        CommandLineParser parser = new DefaultParser();
        HelpFormatter formatter = new DefaultHelpFormatter();
        CommandLine cmd = parser.parse(options, args);
        System.out.println(formatter.printHelp("Test", options));
    }
}