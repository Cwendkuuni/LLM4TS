import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        String[] args = {"-a", "arg1", "-b", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals("arg1", cmd.getOptionValue("a"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("arg2", cmd.getOptionValue("b"));
        assertFalse(cmd.hasOption("c"));
        assertNull(cmd.getOptionValue("c"));
    }

    @Test
    public void testParseNoOptions() throws ParseException
    {
        String[] args = {};
        Options options = new Options();

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertFalse(cmd.hasOptions());
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOptionA() throws ParseException
    {
        String[] args = {"arg1"};
        Options options = new Options();
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        fail("Expected ParseException");
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOptionWithHelp() throws ParseException
    {
        String[] args = {"--help"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options, args);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            assertEquals("No option '--help'", e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArg() throws ParseException
    {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true, "arg");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options, args);
            fail("Expected ParseException");
        }
        catch (ParseException e)
        {
            assertEquals("Missing value for option: -a", e.getMessage());
        }
    }

    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() throws ParseException
    {
        String[] args = {"-b", "arg1"};
        Options options = new Options();
        options.addOption("a", true, "Option A", true);
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options, args);
            fail("Expected MissingOptionException");
        }
        catch (MissingOptionException e)
        {
            assertEquals("Missing required option: a", e.getMessage());
        }
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseUnrecognizedOption() throws ParseException
    {
        String[] args = {"--unknown"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        try
        {
            CommandLine cmd = parser.parse(options, args);
            fail("Expected UnrecognizedOptionException");
        }
        catch (UnrecognizedOptionException e)
        {
            assertEquals("Unrecognized option: --unknown", e.getMessage());
        }
    }

    @Test
    public void testParseLongOption() throws ParseException
    {
        String[] args = {"--long-option", "long-arg"};
        Options options = new Options();
        options.addOption("lo", true, "Long Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals("long-arg", cmd.getOptionValue("lo"));
        assertTrue(cmd.hasOption("lo"));
    }

    @Test
    public void testParseShortOptionWithArg() throws ParseException
    {
        String[] args = {"-a", "arg1", "-b", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B", true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("arg2", cmd.getOptionValue("b"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseShortOptionWithoutArg() throws ParseException
    {
        String[] args = {"-a", "-b"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseShortOptionWithArgAndLongOption() throws ParseException
    {
        String[] args = {"-a", "arg1", "--long-option", "long-arg"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("lo", true, "Long Option");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("long-arg", cmd.getOptionValue("lo"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("lo"));
    }

    @Test
    public void testParseShortOptionWithArgAndLongOptionWithArg() throws ParseException
    {
        String[] args = {"-a", "arg1", "--long-option", "long-arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("lo", true, "Long Option");
        options.addOption("lo2", true, "Long Option 2", true);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals("arg1", cmd.getOptionValue("a"));
        assertEquals("long-arg1", cmd.getOptionValue("lo"));
        assertEquals("arg2", cmd.getOptionValue("lo2"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("lo"));
        assertTrue(cmd.hasOption("lo2"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = {"--property1=prop1", "--property2=prop2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "prop1");
        properties.setProperty("property2", "prop2");
        Options options = new Options();
        options.addOption("p1", true, "Property 1");
        options.addOption("p2", true, "Property 2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals("prop1", cmd.getOptionValue("p1"));
        assertEquals("prop2", cmd.getOptionValue("p2"));
        assertTrue(cmd.hasOption("p1"));
        assertTrue(cmd.hasOption("p2"));
    }

    @Test
    public void testParseWithPropertiesAndMissingOption() throws ParseException
    {
        String[] args = {"--property1=prop1"};
        Properties properties = new Properties();
        properties.setProperty("property1", "prop1");
        Options options = new Options();
        options.addOption("p1", true, "Property 1");
        options.addOption("p2", true, "Property 2");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals("prop1", cmd.getOptionValue("p1"));
        assertTrue(cmd.hasOption("p1"));
        assertFalse(cmd.hasOption("p2"));
    }

    @Test
    public void testParseWithPropertiesAndMissingOptionGroup() throws ParseException
    {
        String[] args = {"--property1=prop1"};
        Properties properties = new Properties();
        properties.setProperty("property1", "prop1");
        Options options = new Options();
        OptionGroup group = new OptionGroup("group1");
        group.setOptionMutuallyExclusiveGroup(true);
        group.addOption(new Option("g1", "group1", false, "Group 1"));
        group.addOption(new Option("g2", "group2", false, "Group 2"));
        options.addOptionGroup(group);
        options.addOption("p1", true, "Property 1");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals("prop1", cmd.getOptionValue("p1"));
        assertTrue(cmd.hasOption("p1"));
        assertFalse(cmd.hasOption("g1"));
        assertFalse(cmd.hasOption("g2"));
    }
}