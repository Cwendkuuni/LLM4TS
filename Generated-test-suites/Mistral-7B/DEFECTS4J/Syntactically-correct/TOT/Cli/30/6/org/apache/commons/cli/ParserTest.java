import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleOption() throws ParseException
    {
        String[] args = {"-a"};

        Options options = new Options();
        options.addOption("a", true, "An option with a flag");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.getOptionValue("a"));
    }

    @Test
    public void testOptionWithValue() throws ParseException
    {
        String[] args = {"-a", "value"};

        Options options = new Options();
        options.addOption("a", true, "An option with a value");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("value", cmd.getOptionValue("a"));
    }

    @Test
    public void testRequiredOption() throws ParseException
    {
        String[] args = {"--myOption"};

        Options options = new Options();
        options.addOption("myOption", true, "A required option");
        options.getOption("myOption").isRequired();

        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(options, args, false);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Required option 'myOption' is missing", e.getMessage());
            return;
        }

        Assert.fail("Expected ParseException");
    }

    @Test
    public void testMultipleOptions() throws ParseException
    {
        String[] args = {"-a", "-b"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertFalse(cmd.hasOption("c"));
    }

    @Test
    public void testOptionGroup() throws ParseException
    {
        String[] args = {"-g", "group1", "--option1", "--option2"};

        Options options = new Options();
        OptionGroup group = new OptionGroup("group1", "Group 1");
        group.setRequired(true);

        options.addOptionGroup(group);
        group.addOption("option1", true, "Option 1");
        group.addOption("option2", true, "Option 2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(cmd.hasOption("group1.option1"));
        Assert.assertTrue(cmd.hasOption("group1.option2"));
        Assert.assertFalse(cmd.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() throws ParseException
    {
        String[] args = {"--invalid"};

        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testInvalidOptionWithValue() throws ParseException
    {
        String[] args = {"--invalid", "value"};

        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.fail();
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() throws ParseException
    {
        String[] args = {"--option1", "--option2"};

        Options options = new Options();
        options.addOption("option1", true, "Option 1", true);
        options.addOption("option2", true, "Option 2", true);

        CommandLineParser parser = new DefaultParser();

        try
        {
            CommandLine cmd = parser.parse(options, args, false);
        }
        catch (ParseException e)
        {
            Assert.assertEquals("Missing required options:", e.getMessage());
            return;
        }

        Assert.fail("Expected ParseException");
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = {"--property1=value1", "--property2=value2", "--myOption"};

        Properties props = new Properties();
        props.setProperty("property1", "value1");
        props.setProperty("property2", "value2");

        Options options = new Options();
        options.addOption("myOption", true, "My option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, props, false);

        Assert.assertTrue(cmd.hasOption("myOption"));
        Assert.assertFalse(cmd.hasOption("property1"));
        Assert.assertFalse(cmd.hasOption("property2"));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] args = {"--myOption", "arg1", "arg2"};

        Options options = new Options();
        options.addOption("myOption", true, "My option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false, true);

        Assert.assertTrue(cmd.hasOption("myOption"));
        Assert.assertEquals(2, cmd.getArgList().size());
        Assert.assertEquals("arg1", cmd.getArgList().get(0));
        Assert.assertEquals("arg2", cmd.getArgList().get(1));
    }
}