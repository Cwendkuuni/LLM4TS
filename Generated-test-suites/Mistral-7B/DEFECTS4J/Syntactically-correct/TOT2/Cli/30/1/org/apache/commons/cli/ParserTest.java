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
    public void testParseBasic() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"-a", "1", "--optionB", "2"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        Options options = new Options();
        options.addRequiredOption("a", true, "This is a required option");
        String[] arguments = new String[]{"--optionB", "2"};

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertThrows(MissingOptionException.class, () -> parser.parse(options, new String[0], false));
    }

    @Test
    public void testParseWithOptionalGroup() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "This is a group");
        group.addOption(new Option("b", true, "Test option b"));
        group.addOption(new Option("c", true, "Test option c"));
        options.addOptionGroup(group);

        String[] arguments = new String[]{"--group1", "--b", "--c"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, false);

        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--invalidOption"};
        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOptionValue() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a", true, "Test option a"));
        String[] arguments = new String[]{"-a"};

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false);
    }

    @Test(expected = ParseException.class)
    public void testParseWithMultipleValues() throws ParseException {
        Options options = new Options();
        options.addOption(new Option("a", true, "Test option a"));
        String[] arguments = new String[]{"-a", "1", "-a", "2"};

        Parser parser = new DefaultParser();
        parser.parse(options, arguments, false);
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--myOption", "value"};
        Properties properties = new Properties();
        properties.setProperty("myOption", "otherValue");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, properties, false);

        Assert.assertEquals("otherValue", cmd.getOptionValue("myOption"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        String[] arguments = new String[]{"--optionA", "valueA", "-x", "--optionB", "valueB"};
        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertEquals("valueA", cmd.getOptionValue("optionA"));
        Assert.assertNull(cmd.getOptionValue("optionB"));
    }
}