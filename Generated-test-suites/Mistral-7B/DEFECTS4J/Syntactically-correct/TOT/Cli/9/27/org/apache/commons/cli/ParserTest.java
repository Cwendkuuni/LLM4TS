import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithValidOptions() throws ParseException {
        String[] args = {"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(2, cmd.getNumArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithMissingRequiredOption() throws ParseException {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        DefaultParser parser = new DefaultParser();

        try {
            parser.parse(options, args, true);
            Assert.fail();
        } catch (MissingOptionException e) {
            Assert.assertEquals("Missing required option: b", e.getMessage());
        }
    }

    @Test
    public void testParseWithInvalidOption() throws ParseException {
        String[] args = {"-x", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        DefaultParser parser = new DefaultParser();

        try {
            parser.parse(options, args, true);
            Assert.fail();
        } catch (UnrecognizedOptionException e) {
            Assert.assertEquals("Unrecognized option: -x", e.getMessage());
        }
    }

    @Test
    public void testParseWithMissingValueForOption() throws ParseException {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        DefaultParser parser = new DefaultParser();

        try {
            parser.parse(options, args, true);
            Assert.fail();
        } catch (MissingArgumentException e) {
            Assert.assertEquals("Missing argument for option: a", e.getMessage());
        }
    }

    @Test
    public void testParseWithMultipleValuesForOption() throws ParseException {
        String[] args = {"-a", "1", "2", "--optionB", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(2, cmd.getNumArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals(2, cmd.getValues("a").length);
        Assert.assertEquals("1", cmd.getOptionValue("a")[0]);
        Assert.assertEquals("2", cmd.getOptionValue("a")[1]);
        Assert.assertEquals("3", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException {
        String[] args = {"-g", "group1", "--optionA", "1", "--optionB", "2"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        options.addOptionGroup(group);

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(0, cmd.getNumArgs());
        Assert.assertTrue(cmd.hasOption("g"));
        Assert.assertEquals("group1", cmd.getOptionValue("g"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }
}