import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class ParserTest
{
    private CommandLineParser parser;
    private Options options;

    @Before
    public void setup()
    {
        options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", false, "option B");
        options.addOption("c", 1, "option C with an integer value");
        options.addRequiredOption("d", false, "required option D");
        options.addOptionGroup(new OptionGroup("group1", "group 1") {{
            addOption("e", true, "option E in group 1");
            addOption("f", false, "option F in group 1, which is required if option E is present");
        }});
        parser = new DefaultParser();
    }

    @Test
    public void testParseBasic() throws ParseException
    {
        String[] args = {"-a", "--d", "--c", "42"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(Arrays.asList("--d", "--c=42"), cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals(42, cmd.getOptionValue("c").intValue());
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertFalse(cmd.hasOption("e"));
        Assert.assertFalse(cmd.hasOption("f"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        String[] args = {"-a", "--c", "42"};
        parser.parse(options, args, true);

        Assert.fail("Expected ParseException for missing required option 'd'");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOption() throws ParseException
    {
        String[] args = {"-x", "--c", "42"};
        parser.parse(options, args, true);

        Assert.fail("Expected ParseException for unrecognized option 'x'");
    }

    @Test(expected = ParseException.class)
    public void testParseInvalidOptionValue() throws ParseException
    {
        String[] args = {"-a", "--c", "foo"};
        parser.parse(options, args, true);

        Assert.fail("Expected ParseException for invalid integer value 'foo' for option 'c'");
    }

    @Test
    public void testParseWithOptionGroup() throws ParseException
    {
        String[] args = {"-a", "--d", "--group1", "--e"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(Arrays.asList("--d", "--group1", "--e"), cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertTrue(cmd.hasOption("e"));
        Assert.assertTrue(cmd.hasOption("f"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithoutOptionGroupRequiredOption() throws ParseException
    {
        String[] args = {"-a", "--group1", "--e"};
        parser.parse(options, args, true);

        Assert.fail("Expected ParseException for missing required option 'd' in option group 'group1'");
    }

    @Test
    public void testParseWithOptionGroupOptionalOption() throws ParseException
    {
        String[] args = {"-a", "--group1", "--e", "--f"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(Arrays.asList("--a", "--d", "--group1", "--e", "--f"), cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertTrue(cmd.hasOption("e"));
        Assert.assertTrue(cmd.hasOption("f"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.put("c", "42");
        properties.put("d", "default");

        CommandLine cmd = parser.parse(options, new String[0], properties, true);

        Assert.assertEquals(Arrays.asList("--d=default"), cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals(42, cmd.getOptionValue("c").intValue());
        Assert.assertTrue(cmd.hasOption("d"));
        Assert.assertEquals("default", cmd.getOptionValue("d"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] args = {"--x", "--a", "--b"};
        CommandLine cmd = parser.parse(options, args, true);

        Assert.assertEquals(Arrays.asList("--x", "--a", "--b"), cmd.getArgs());
        Assert.assertTrue(cmd.hasOption("x"));
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }
}