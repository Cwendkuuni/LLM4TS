import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testSimpleOption() throws ParseException {
        String[] args = {"-a", "arg1"};
        Options options = new Options();
        options.addOption("a", "an.option", true, "an option with an argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("arg1", cmd.getOptionValue("a"));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() throws ParseException {
        String[] args = {"-b"};
        Options options = new Options();
        options.addOption("a", "an.option", true, "an option with an argument");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // This test should fail with a ParseException
    }

    @Test
    public void testNoOptions() throws ParseException {
        String[] args = {};
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        // This test should pass with an empty CommandLine
    }

    @Test
    public void testLongOptions() throws ParseException {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("lo", "long.option", false, "a long option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        Assert.assertTrue(cmd.hasOption("lo"));
    }

    @Test(expected = ParseException.class)
    public void testMissingLongOption() throws ParseException {
        String[] args = {"--missing-long-option"};
        Options options = new Options();
        options.addOption("lo", "long.option", false, "a long option");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);

        // This test should fail with a ParseException
    }

    @Test
    public void testRequiredOptions() throws ParseException {
        String[] args = {"-a", "-b"};
        Options options = new Options();
        options.addOption("a", "optionA", true, "option A");
        options.addOption("b", "optionB", true, "option B");
        options.addRequiredOption("a");
        options.addRequiredOption("b");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals("arg1", cmd.getOptionValue("a"));
        Assert.assertEquals("arg2", cmd.getOptionValue("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOptions() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", "optionA", true, "option A");
        options.addRequiredOption("a");
        options.addRequiredOption("b");

        CommandLineParser parser = new DefaultParser();
        parser.parse(options, args);

        // This test should fail with a MissingOptionException
    }

    @Test
    public void testGroupedOptions() throws ParseException {
        String[] args = {"-g", "-foo", "-bar"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "group options");
        group.setRequired(false);

        options.addOptionGroup(group);
        group.addOption(new Option("foo", "foo", false, "foo option"));
        group.addOption(new Option("bar", "bar", false, "bar option"));
        options.addOption("a", "optionA", true, "option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        Assert.assertTrue(cmd.hasOption("foo"));
        Assert.assertTrue(cmd.hasOption("bar"));
        Assert.assertEquals("", cmd.getOptionValue("a"));
    }

    @Test
    public void testProperties() throws ParseException {
        String[] args = {"--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addOptions(PropertySettingCommandLineParser.parsePropertySettingDefinition("--", properties));

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);

        Assert.assertEquals("value1", cmd.getOptionValue("property1"));
        Assert.assertEquals("value2", cmd.getOptionValue("property2"));
    }
}