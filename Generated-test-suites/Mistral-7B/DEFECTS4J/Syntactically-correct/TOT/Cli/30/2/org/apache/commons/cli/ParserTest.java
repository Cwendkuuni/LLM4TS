import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testSimpleParsing() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2", "--", "-c", "3"};

        Options options = new Options();
        options.addOption("a", true, "A option with no argument");
        options.addOption("b", true, "B option with no argument");
        options.addOption("c", true, "C option with no argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.assertEquals(3, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
    }

    @Test
    public void testParsingWithRequiredOptions() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2"};

        Options options = new Options();
        options.addOption("a", true, "A option with no argument", true, "requiredA");
        options.addOption("b", true, "B option with no argument", true, "requiredB");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        try
        {
            parser.parse(options, args, null, false);
            Assert.fail();
        }
        catch (MissingOptionException e)
        {
            List<String> missingOptions = e.getMissingOptions();
            Assert.assertEquals(2, missingOptions.size());
            Assert.assertEquals("requiredA", missingOptions.get(0));
            Assert.assertEquals("requiredB", missingOptions.get(1));
        }
    }

    @Test
    public void testParsingWithOptionalArgument() throws ParseException
    {
        String[] args = {"-a", "1", "-c", "3", "--", "-a"};

        Options options = new Options();
        options.addOption("a", true, "A option with an optional argument");
        options.addOption("c", true, "C option with no argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
        Assert.assertNull(cmd.getOptionValue("a", "defaultValue"));
    }

    @Test(expected = ParseException.class)
    public void testParsingWithInvalidOption() throws ParseException
    {
        String[] args = {"-x", "1"};

        Options options = new Options();
        options.addOption("a", true, "A option with no argument");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        parser.parse(options, args, null, false);

        Assert.fail();
    }

    @Test
    public void testParsingWithValueProperty() throws ParseException
    {
        String[] args = {"--foo", "1"};

        Options options = new Options();
        options.addOption("foo", true, "Foo option with value");

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.assertTrue(cmd.hasOption("foo"));
        Assert.assertEquals("1", cmd.getOptionValue("foo"));
    }

    @Test
    public void testParsingWithOptionsGroup() throws ParseException
    {
        String[] args = {"--a", "1", "--group", "group1", "-b", "2", "--group", "group2", "-c", "3"};

        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1");
        group1.setRequired(false);
        group1.addOption(new Option("a", true, "A option with no argument"));
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2");
        group2.setRequired(true);
        group2.addOption(new Option("c", true, "C option with no argument"));
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();
        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals(2, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("c"));
        Assert.assertEquals("3", cmd.getOptionValue("c"));
    }
}