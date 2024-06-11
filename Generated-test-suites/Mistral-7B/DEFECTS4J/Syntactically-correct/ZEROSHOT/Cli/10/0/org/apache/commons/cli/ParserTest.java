import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.UnrecognizedOptionException;

public class ParserTest {

    @Test
    public void testParseWithOptions() throws Exception {
        String[] args = new String[]{"-a", "--optionB", "valueB", "-c", "valueC"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", true, "Option C");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
        Assert.assertEquals(true, cmd.hasOption("c"));
        Assert.assertEquals("valueC", cmd.getOptionValue("c"));
        Assert.assertEquals(3, cmd.getArgs().length);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws Exception {
        String[] args = new String[]{"--missingOption"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws Exception {
        String[] args = new String[]{"-a"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addRequiredOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, null, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredGroupOption() throws Exception {
        String[] args = new String[]{"--groupOption"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        group.setRequired(true);
        group.addOption("groupOption", true, "Group Option");

        options.addOption("a", true, "Option A");
        options.addOptionGroup(group);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, null, false);
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() throws Exception {
        String[] args = new String[]{"-a", "--optionB"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "valueB");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, null, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws Exception {
        String[] args = new String[]{"--unrecognizedOption"};

        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, null, false);
    }

    @Test
    public void testParseWithProperties() throws Exception {
        String[] args = new String[]{"--foo=valueFoo", "--bar=valueBar"};

        Options options = new Options();
        options.addOption("foo", true, "Option Foo");
        options.addOption("bar", true, "Option Bar");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, new Properties(), false);

        Assert.assertEquals("valueFoo", cmd.getOptionValue("foo"));
        Assert.assertEquals("valueBar", cmd.getOptionValue("bar"));

        Assert.assertEquals(0, cmd.getArgs().length);
    }
}