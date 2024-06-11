import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest {

    @Test
    public void testParseWithOptions() throws ParseException {
        String[] arguments = new String[]{"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("nonExistentOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException {
        String[] arguments = new String[]{"--optionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Required Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, arguments);

        // This test should throw a ParseException
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] arguments = new String[]{"--optionA=valueA", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, properties);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"--invalidOption"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        parser.parse(options, arguments);

        // This test should throw an UnrecognizedOptionException
    }

    @Test
    public void testParseWithNoOptions() throws ParseException {
        String[] arguments = new String[]{"arg1", "arg2"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertFalse(cmd.hasOptions());
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"--optionA", "arg1", "--optionB", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments, true);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertFalse(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] arguments = new String[]{"--group", "--optionA", "arg1"};
        Options options = new Options();

        OptionGroup group = new OptionGroup("group");
        group.setRequired(false);
        options.addOptionGroup(group);
        group.addOption("optionA", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("optionA"));
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertTrue(group.isSelected());
    }
}