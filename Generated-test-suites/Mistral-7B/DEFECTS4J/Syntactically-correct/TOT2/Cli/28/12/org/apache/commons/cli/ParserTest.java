import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParserWithSimpleOptions() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2"};

        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test(expected = ParseException.class)
    public void testParserWithMissingRequiredOption() throws ParseException
    {
        String[] args = {"-a", "1"};

        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B", true, "requiredB");

        parser.setOptions(options);

        try
        {
            parser.parse(options, args, false);
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("Missing required option 'b'", e.getMessage());
            throw new ParseException("Test error", e);
        }
    }

    @Test(expected = ParseException.class)
    public void testParserWithInvalidOption() throws ParseException
    {
        String[] args = {"--invalid"};

        Parser parser = new DefaultParser();

        Options options = new Options();

        try
        {
            parser.parse(options, args, false);
        }
        catch (UnrecognizedOptionException e)
        {
            Assert.assertEquals("Unrecognized option: invalid", e.getMessage());
            throw new ParseException("Test error", e);
        }
    }

    @Test
    public void testParserWithLongOptions() throws ParseException
    {
        String[] args = {"--long-option-a", "valueA", "--long-option-b", "valueB"};

        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("longOptionA", true, "long option A");
        options.addOption("longOptionB", true, "long option B");

        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals("valueA", cmd.getOptionValue("longOptionA"));
        Assert.assertEquals("valueB", cmd.getOptionValue("longOptionB"));
        Assert.assertNull(cmd.getOptionValue("shortOption"));
    }

    @Test
    public void testParserWithProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("optionA", "valueA");
        properties.setProperty("optionB", "valueB");

        String[] args = {};

        Parser parser = new DefaultParser();

        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParserWithGroups() throws ParseException
    {
        String[] args = {"--group1", "-a", "-b", "--group2", "-c"};

        Parser parser = new DefaultParser();

        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1");
        group1.addOption(new Option("a", true, "option A in group1"));
        group1.addOption(new Option("b", true, "option B in group1"));

        OptionGroup group2 = new OptionGroup("group2");
        group2.addOption(new Option("c", true, "option C in group2"));

        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        parser.setOptions(options);

        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(group1, cmd.getOptionGroup());
        Assert.assertEquals("a", cmd.getOptionValue("a"));
        Assert.assertEquals("b", cmd.getOptionValue("b"));
        Assert.assertEquals("c", cmd.getOptionValue("c"));
    }
}