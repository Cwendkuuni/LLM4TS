import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseWithNoOptions() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = new String[0];

        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals(0, cmd.getArgs().size());
    }

    @Test
    public void testParseWithSingleOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a"};

        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
    }

    @Test
    public void testParseWithOptionalArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a", "arg"};

        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("arg", cmd.getOptionValues("a")[0]);
    }

    @Test
    public void testParseWithRequiredArg() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        String[] args = new String[0];

        try {
            parser.parse(options, args, false);
            Assert.fail("Missing required argument for option -a");
        } catch (MissingArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testParseWithMultipleOptions() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a", "-b"};

        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        Properties properties = new Properties();
        properties.setProperty("a", "value");

        CommandLine cmd = parser.parse(options, null, properties, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("value", cmd.getOptionValues("a")[0]);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        Options options = new Options();
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-x"};

        parser.parse(options, args, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", false, "Option B");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a"};

        parser.parse(options, args, false);
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"-a", "arg", "-"};

        CommandLine cmd = parser.parse(options, args, true);
        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("-", cmd.getArgs().get(0));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        Options options = new Options();
        options.addOption("long-option-a", true, "Long option A");
        Parser parser = new DefaultParser();
        String[] args = new String[]{"--long-option-a"};

        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("long-option-a"));
    }

    @Test
    public void testParseWithGroupedOptions() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        options.addOptionGroup(group);
        Parser parser = new DefaultParser();
        String[] args = new String[]{"--group", "-a"};

        CommandLine cmd = parser.parse(options, args, false);
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
    }
}