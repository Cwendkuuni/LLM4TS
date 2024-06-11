import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest
{
    @Test
    public void testParserWithSimpleOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "1", "-b", "2", "--"};

        Options options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", true, "option B");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
        Assert.assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testParserWithRequiredOptions() throws ParseException
    {
        String[] args = new String[]{"--foo"};

        Options options = new Options();
        options.addOption("foo", true, "Option foo is required");
        options.addRequiredOption("foo");

        DefaultParser parser = new DefaultParser();

        try
        {
            parser.parse(options, args, false);
            Assert.fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            Assert.assertEquals("Option foo is required", e.getMessage());
        }
    }

    @Test
    public void testParserWithOptionGroups() throws ParseException
    {
        String[] args = new String[]{"--group1", "-A", "-B"};

        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Option group 1");
        group1.setRequired(false);
        group1.addOption("A", true, "Option A in group 1");
        group1.addOption("B", true, "Option B in group 1");
        options.addOptionGroup(group1);
        options.addRequiredOption("group1");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        Assert.assertTrue(group1.isSelected());
        Assert.assertTrue(cmd.hasOption("A"));
        Assert.assertTrue(cmd.hasOption("B"));
    }

    @Test
    public void testParserWithProperties() throws ParseException
    {
        Properties properties = new Properties();
        properties.setProperty("foo", "bar");
        properties.setProperty("option1", "value1");
        properties.setProperty("option2.key2", "value2");

        String[] args = new String[]{"--propertyfile", "classpath:/path/to/properties.txt"};

        Options options = new Options();
        options.addOption("propertyfile", true, "path to properties file");
        options.addOption("option1", true, "Option 1");
        options.addOption("option2", true, "Option 2");

        DefaultParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals("classpath:/path/to/properties.txt", cmd.getOptionValue("propertyfile"));
        Assert.assertEquals("value1", cmd.getOptionValue("option1"));
        Assert.assertEquals("value2", cmd.getOptionValue("option2.key2"));
    }

    @Test(expected = ParseException.class)
    public void testParserWithUnrecognizedOption() throws ParseException
    {
        String[] args = new String[]{"--unknown"};

        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        Assert.fail("Expected UnrecognizedOptionException");
    }

    @Test(expected = ParseException.class)
    public void testParserWithNonOptionArgument() throws ParseException
    {
        String[] args = new String[]{"--", "file.txt"};

        Options options = new Options();
        options.addOption("file", true, "File to process");

        DefaultParser parser = new DefaultParser();
        parser.parse(options, args, false);

        Assert.fail("Expected ParseException");
    }
}