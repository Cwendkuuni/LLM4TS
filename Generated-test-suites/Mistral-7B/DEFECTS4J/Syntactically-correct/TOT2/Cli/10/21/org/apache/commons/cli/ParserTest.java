import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class ParserTest {

    @Test
    public void testParseBasic() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-a"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
    }

    @Test
    public void testParseWithArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("b", 1, "An option with an integer value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-a", "true", "-b", "3"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("true", cmd.getOptionValue("a"));
        Assert.assertEquals(3, cmd.getOptionValue("b", 0).intValue());
    }

    @Test
    public void testParseRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value", true);
        options.addOption("b", true, "Another required option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-a"};
        try {
            parser.parse(options, arguments);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: b", e.getMessage());
        }

        arguments = new String[]{"-a", "-b"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
    }

    @Test
    public void testParseLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("long-option", true, "A long option");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"--long-option"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertEquals(true, cmd.hasOption("long-option"));
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException {
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"--invalid-option"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");

        Properties properties = new Properties();
        properties.setProperty("a", "true");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[0];
        CommandLine cmd = parser.parse(options, arguments, properties);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("true", cmd.getOptionValue("a"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("b", true, "Another option with a boolean value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"-a", "-b", "nonOption"};
        CommandLine cmd = parser.parse(options, arguments, null, true);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("nonOption", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithGroup() throws ParseException {
        Options options = new Options();

        OptionGroup group = new OptionGroup("group", "A group of options");
        group.addOption("a", true, "Option A");
        group.addOption("b", true, "Option B");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        String[] arguments = new String[]{"--group", "-a"};
        CommandLine cmd = parser.parse(options, arguments);
        Assert.assertTrue(cmd.isSetGroup("group"));
        Assert.assertEquals(true, cmd.hasOption("a"));
    }
}