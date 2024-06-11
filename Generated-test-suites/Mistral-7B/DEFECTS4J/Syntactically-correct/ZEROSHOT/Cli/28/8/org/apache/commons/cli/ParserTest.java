import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.CommandLine;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

public class ParserTest
{
    @Test
    public void testParseWithSimpleOptions() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
    }

    @Test
    public void testParseWithOptionalArgs() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2", "--foo", "bar"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("foo", false, "Option Foo");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(3, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertTrue(cmd.hasOption("foo"));
        assertNull(cmd.getOptionValue("foo"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException
    {
        String[] args = {"-a", "1"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("b", false, "Option B is required");

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, false);
            fail("MissingRequiredOptionException was expected");
        }
        catch (MissingOptionException e)
        {
            assertEquals("[b]", e.getMissingOptions().toString());
        }

        options.setRequiredOption("b", true);
        args = new String[]{"-a", "1", "-b", "2"};

        cmd = parser.parse(options, args, false);
        assertEquals(1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertFalse(cmd.hasArg("b"));
        assertNotNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        String[] args = {"--long-option-a", "1", "--long-option-b", "2"};
        Options options = new Options();
        options.addOption("loa", true, "Long Option A");
        options.addOption("lob", true, "Long Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("loa"));
        assertEquals("2", cmd.getOptionValue("lob"));
        assertTrue(cmd.hasOption("loa"));
        assertTrue(cmd.hasOption("lob"));
    }

    @Test
    public void testParseWithMixedOptions() throws ParseException
    {
        String[] args = {"-a", "1", "--long-option-b", "2", "-b", "3"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("lb", true, "Long Option B");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("lb"));
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("lb"));
        assertFalse(cmd.hasOption("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = {"-Dproperty1=value1", "-Dproperty2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        options.addRequiredOption("property1", false, "Property 1");
        options.addRequiredOption("property2", false, "Property 2");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("property1"));
        assertTrue(cmd.hasOption("property2"));
        assertEquals("value1", cmd.getOptionValue("property1"));
        assertEquals("value2", cmd.getOptionValue("property2"));
    }

    @Test
    public void testParseWithOptionGroups() throws ParseException
    {
        String[] args = {"-g", "group1", "-a", "1", "-g", "group2"};
        Options options = new Options();

        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("a", true, "Option A"));
        options.addOptionGroup(group1);

        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, false);

        assertEquals(0, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertSame(group1, cmd.getOptionGroup("group1"));
        assertSame(group2, cmd.getOptionGroup("group2"));
    }
}