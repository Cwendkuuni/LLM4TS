import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class ParserTest
{
    @Test
    public void testParseSimple() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2", "--"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertNotNull(cmd);
        assertEquals(2, cmd.getArgList().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertFalse(cmd.hasOption("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseMissingOption() throws ParseException
    {
        String[] args = {"--test"};
        Options options = new Options();
        options.addOption("a", true, "A value");

        Parser parser = new DefaultParser();
        parser.parse(options, args, null);
    }

    @Test(expected = ParseException.class)
    public void testParseMissingArgument() throws ParseException
    {
        String[] args = {"-a"};
        Options options = new Options();
        options.addOption("a", true, "A value");

        Parser parser = new DefaultParser();
        parser.parse(options, args, null);
    }

    @Test
    public void testParseLongOption() throws ParseException
    {
        String[] args = {"--long-option", "123"};
        Options options = new Options();
        options.addOption("longOption", true, "Long option value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertNotNull(cmd);
        assertEquals("123", cmd.getOptionValue("longOption"));
    }

    @Test
    public void testParseShortOption() throws ParseException
    {
        String[] args = {"-a", "1", "-b", "2", "--"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertNotNull(cmd);
        assertEquals(2, cmd.getArgList().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertFalse(cmd.hasOption("c"));

        assertEquals(0, cmd.getOptionValues("a").length);
        assertEquals(0, cmd.getOptionValues("b").length);

        List<String> aValues = cmd.getOptionValues("a");
        List<String> bValues = cmd.getOptionValues("b");

        aValues.add(0, "1");
        bValues.add(0, "2");

        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
        assertEquals(new String[]{"1"}, cmd.getOptionValues("a"));
        assertEquals(new String[]{"2"}, cmd.getOptionValues("b"));
    }

    @Test
    public void testParseShortOptionWithArg() throws ParseException
    {
        String[] args = {"-a", "1", "--optionB=2", "-b", "3"};
        Options options = new Options();
        options.addOption("a", true, "A value");
        options.addOption("b", true, "B value");
        options.addOption("optionB", true, "Option B value");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertNotNull(cmd);
        assertEquals(3, cmd.getArgList().size());
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("optionB"));
        assertEquals("3", cmd.getOptionValue("b"));
        assertFalse(cmd.hasOption("c"));

        assertEquals(new String[]{"1"}, cmd.getOptionValues("a"));
        assertEquals(new String[]{"2"}, cmd.getOptionValues("optionB"));
        assertEquals(new String[]{"3"}, cmd.getOptionValues("b"));
    }

    @Test
    public void testParseRequiredOption() throws ParseException
    {
        String[] args = {"--test"};
        Options options = new Options();
        options.addOption("a", true, "A value").required(true);

        Parser parser = new DefaultParser();
        try
        {
            parser.parse(options, args, null);
            fail("An exception should have been thrown");
        } catch (MissingOptionException e)
        {
            assertEquals(Arrays.asList("a"), e.getMissingOptions());
        }
    }

    @Test
    public void testParseGroupOption() throws ParseException
    {
        String[] args = {"--test", "-g", "group1"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("test", true, "Test option"));
        group1.addOption(new Option("test2", true, "Test option 2"));
        options.addOptionGroup(group1);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertNotNull(cmd);
        assertTrue(cmd.hasOptionGroup("group1"));
        assertEquals("group1", cmd.getOptionGroup("group1").getName());
        assertTrue(cmd.hasOption("test"));
        assertFalse(cmd.hasOption("test2"));

        assertEquals(0, cmd.getArgList().size());
    }

    @Test
    public void testParseGroupOptionWithValues() throws ParseException
    {
        String[] args = {"--test", "-g", "group1", "--test2=1", "--test2=2"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("test", true, "Test option"));
        group1.addOption(new Option("test2", true, "Test option 2").setValueSeparator('='));
        options.addOptionGroup(group1);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertNotNull(cmd);
        assertTrue(cmd.hasOptionGroup("group1"));
        assertEquals("group1", cmd.getOptionGroup("group1").getName());
        assertTrue(cmd.hasOption("test"));
        assertEquals(new String[]{"1", "2"}, cmd.getOptionValues("test2"));

        assertEquals(0, cmd.getArgList().size());
    }

    @Test(expected = ParseException.class)
    public void testParseGroupOptionMissingValue() throws ParseException
    {
        String[] args = {"--test", "-g", "group1", "--test2"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("test", true, "Test option"));
        group1.addOption(new Option("test2", true, "Test option 2").setValueSeparator('='));
        options.addOptionGroup(group1);

        Parser parser = new DefaultParser();
        parser.parse(options, args, null);
    }

    @Test
    public void testParseMultipleGroupOptions() throws ParseException
    {
        String[] args = {"--test", "-g1", "group1", "--test2", "-g2", "group2"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        group1.addOption(new Option("test1", true, "Test option 1"));
        group1.addOption(new Option("test2", true, "Test option 2"));
        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        group2.addOption(new Option("test3", true, "Test option 3"));
        options.addOptionGroup(group1);
        options.addOptionGroup(group2);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertNotNull(cmd);
        assertTrue(cmd.hasOptionGroup("group1"));
        assertTrue(cmd.hasOptionGroup("group2"));
        assertTrue(cmd.hasOption("test1"));
        assertTrue(cmd.hasOption("test2"));
        assertTrue(cmd.hasOption("test3"));

        assertEquals(0, cmd.getArgList().size());
    }

    @Test
    public void testParseProperties() throws ParseException
    {
        String[] args = {"--test", "-DmyProperty=value"};
        Options options = new Options();
        options.addOption("test", true, "Test option");

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, new Properties());

        assertEquals(1, cmd.getProperties().propertyCount());
        assertEquals("value", cmd.getProperties().getProperty("myProperty"));
    }

    @Test
    public void testParseWithHelp() throws ParseException
    {
        String[] args = {"--help"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null);

        assertNotNull(cmd.getHelp());
        assertNotNull(cmd.getHelp().getOptionHandler().getHelpFormatter());
    }
}