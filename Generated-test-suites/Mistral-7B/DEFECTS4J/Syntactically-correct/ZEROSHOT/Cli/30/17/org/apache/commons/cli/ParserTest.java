import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class ParserTest
{
    @Test
    public void testParseWithNoOptions() throws ParseException
    {
        String[] args = new String[0];
        Options options = new Options();

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
        assertEquals(0, cmd.getArgs().size());
        assertNull(cmd.getOptionValues("nonExistentOption"));
    }

    @Test
    public void testParseWithSimpleOptions() throws ParseException
    {
        String[] args = new String[]{"-a", "one", "--b=two"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Show verbose output.");
        Option optionB = new Option("b", "b", true, "Set the value of B.");

        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(2, cmd.getArgs().size());
        assertEquals("one", cmd.getOptionValues("a")[0]);
        assertEquals("two", cmd.getOptionValues("b")[0]);
        assertNull(cmd.getOptionValues("nonExistentOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingRequiredOption() throws ParseException
    {
        String[] args = new String[]{"--foo"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Show verbose output.");
        Option optionB = new Option("b", "b", true, "Set the value of B.");
        Option optionC = new Option("c", "c", true, "Show cool feature.");
        optionC.setRequired(true);

        options.addOption(optionA);
        options.addOption(optionB);
        options.addOption(optionC);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNull(cmd.getOptionValues("a"));
        assertNull(cmd.getOptionValues("b"));
        assertNull(cmd.getOptionValues("c"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithMissingOption() throws ParseException
    {
        String[] args = new String[]{"--nonExistentOption"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Show verbose output.");

        options.addOption(optionA);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertNull(cmd.getOptionValues("nonExistentOption"));
    }

    @Test(expected = ParseException.class)
    public void testParseWithTwoDashes() throws ParseException
    {
        String[] args = new String[]{"--"};
        Options options = new Options();

        Parser parser = new DefaultParser();
        parser.parse(options, args);
    }

    @Test
    public void testParseWithLongOptions() throws ParseException
    {
        String[] args = new String[]{"--longOption=longValue"};
        Options options = new Options();
        Option option = new Option("l", "longOption", true, "Set a long option.");

        options.addOption(option);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertEquals("longValue", cmd.getOptionValues("longOption")[0]);
    }

    @Test
    public void testParseWithGroupOptions() throws ParseException
    {
        String[] args = new String[]{"--group1", "-g", "group", "--optionInGroup=value"};
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group");
        Option option = new Option("g", "group", false, "Show group.");
        group.addOption(option);
        Option optionInGroup = new Option("o", "optionInGroup", true, "Set the value of optionInGroup.");
        group.addOption(optionInGroup);

        options.addOption(group);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertSame(option, cmd.findOption("group"));
        assertEquals("value", cmd.getOptionValues("optionInGroup")[0]);
    }

    @Test
    public void testParseWithMultipleGroups() throws ParseException
    {
        String[] args = new String[]{"--group1", "-g1", "group1", "--group2", "-g2", "group2"};
        Options options = new Options();
        OptionGroup group1 = new OptionGroup("group1", "Group 1");
        Option option1 = new Option("g1", "group1", false, "Show group 1.");
        group1.addOption(option1);
        Option option1Value = new Option("v1", "value1", true, "Set the value of option1.");
        group1.addOption(option1Value);
        OptionGroup group2 = new OptionGroup("group2", "Group 2");
        Option option2 = new Option("g2", "group2", false, "Show group 2.");
        group2.addOption(option2);
        Option option2Value = new Option("v2", "value2", true, "Set the value of option2.");
        group2.addOption(option2Value);

        options.addOption(group1);
        options.addOption(group2);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(0, cmd.getArgs().size());
        assertSame(option1, cmd.findOption("group1"));
        assertEquals("value1", cmd.getOptionValues("value1")[0]);
        assertSame(option2, cmd.findOption("group2"));
        assertEquals("value2", cmd.getOptionValues("value2")[0]);
    }

    @Test
    public void testParseWithProperties() throws ParseException
    {
        String[] args = new String[]{"--propertyFile", "test.properties"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        Option option = new Option("p", "propertyFile", true, "Set the property file.");
        option.setLongOpt("propertyFile");

        options.addOption(option);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertEquals(0, cmd.getArgs().size());
        assertEquals("test.properties", cmd.getOptionValues("propertyFile")[0]);
        assertEquals("value1", properties.getProperty("property1"));
        assertEquals("value2", properties.getProperty("property2"));
    }

    @Test
    public void testParseWithExtraArguments() throws ParseException
    {
        String[] args = new String[]{"--foo", "one", "--b=two", "three", "four", "five"};
        Options options = new Options();
        Option optionFoo = new Option("f", "foo", true, "Show verbose output.");
        Option optionB = new Option("b", "b", true, "Set the value of B.");

        options.addOption(optionFoo);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertEquals(3, cmd.getArgs().size());
        assertEquals("one", cmd.getOptionValues("foo")[0]);
        assertEquals("two", cmd.getOptionValues("b")[0]);
        assertEquals("three", cmd.getArg(0));
        assertEquals("four", cmd.getArg(1));
        assertEquals("five", cmd.getArg(2));
    }

    @Test
    public void testParseWithStopAtNonOption() throws ParseException
    {
        String[] args = new String[]{"--foo", "-n", "nonOption", "--b=two"};
        Options options = new Options();
        Option optionFoo = new Option("f", "foo", true, "Show verbose output.");
        Option optionB = new Option("b", "b", true, "Set the value of B.");

        options.addOption(optionFoo);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, true);

        assertEquals(3, cmd.getArgs().size());
        assertEquals("nonOption", cmd.getArg(1));
        assertEquals("two", cmd.getOptionValues("b")[0]);
    }
}