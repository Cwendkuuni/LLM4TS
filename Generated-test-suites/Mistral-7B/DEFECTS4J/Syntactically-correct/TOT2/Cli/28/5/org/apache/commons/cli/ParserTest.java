import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class ParserTest
{
    private Parser parser;
    private Options options;

    @Before
    public void setUp()
    {
        parser = new DefaultParser();
        options = new Options();
        options.addOption("a", "alpha", true, "Alpha option");
        options.addOption("b", "beta", false, "Beta option");
        options.addOptionGroup()
                .addOption(new Option("g", "group", true, "Group option"))
                .required();
    }

    @Test
    public void testParseWithNormalArguments() throws ParseException
    {
        String[] arguments = {"-a", "1", "-b", "true"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("", cmd.getArgs().get(0));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("true", cmd.getOptionValue("b"));
        Assert.assertFalse(cmd.hasOption("g"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        String[] arguments = {"-a", "1"};
        parser.parse(options, arguments);
    }

    @Test(expected = ParseException.class)
    public void testParseWithUnrecognizedOption() throws ParseException
    {
        String[] arguments = {"--x", "-a", "1"};
        parser.parse(options, arguments);
    }

    @Test
    public void testParseWithGroupOption() throws ParseException
    {
        String[] arguments = {"-g", "groupValue"};
        CommandLine cmd = parser.parse(options, arguments);

        Assert.assertEquals(1, cmd.getArgs().size());
        Assert.assertEquals("groupValue", cmd.getArgs().get(0));
        Assert.assertTrue(cmd.hasOption("g"));
        Assert.assertEquals("groupValue", cmd.getOptionValue("g"));
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredGroupOption() throws ParseException
    {
        String[] arguments = {"-a", "1"};
        parser.parse(options, arguments, null, true);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedGroupOption() throws ParseException
    {
        String[] arguments = {"-x", "1"};
        parser.parse(options, arguments, null, true);
    }
}