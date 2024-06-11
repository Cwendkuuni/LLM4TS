import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        String[] args = new String[] { "-a", "1" };
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testMultipleSimpleOptions() throws ParseException
    {
        String[] args = new String[] { "-a", "1", "-b", "2" };
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testOptionalArgument() throws ParseException
    {
        String[] args = new String[] { "-a", "1", "arg" };
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("a", 1, "Argument A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("arg", cmd.getOptionValue("a", new String[0]));
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = new String[] { "-b" };
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B").required(true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // this test should fail with a ParseException
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        String[] args = new String[] { "-x" };
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // this test should fail with an UnrecognizedOptionException
    }

    @Test(expected = ParseException.class)
    public void testMissingArgument() throws ParseException
    {
        String[] args = new String[] { "-a" };
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B").requiredArg();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        // this test should fail with a MissingArgumentException
    }

    @Test
    public void testPropertiesOption() throws ParseException
    {
        String[] args = new String[] { "--propfile", "test.properties" };
        Properties props = new Properties();
        props.setProperty("test.prop", "test");

        Options options = new Options();
        options.addOption("propfile", false, "Property file");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props, false);

        Assert.assertEquals("test", cmd.getOptionValue("prop"));
    }

    @Test
    public void testMixedOptionsAndArguments() throws ParseException
    {
        String[] args = new String[] { "-a", "1", "-b", "2", "arg" };
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("arg", cmd.getArgument(0));
    }
}