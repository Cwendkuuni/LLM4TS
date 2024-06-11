import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Options class.
 */
public class OptionsTest {

    @Test
    public void testAddOption() {
        Options options = new Options();

        // Test adding an option with a short name and no argument
        Option optionShortNoArg = options.addOption("a", "alias", false, "Description for option 'a'");
        assertEquals("a", optionShortNoArg.getKey());
        assertEquals("alias", optionShortNoArg.getLongOpt());
        assertFalse(optionShortNoArg.hasArg());
        assertEquals("Description for option 'a'", optionShortNoArg.getDescription());
        assertFalse(optionShortNoArg.isRequired());

        // Test adding an option with a short name and an argument
        Option optionShortArg = options.addOption("b", "boolean", true, "Description for option 'b'");
        assertEquals("b", optionShortArg.getKey());
        assertEquals(null, optionShortArg.getLongOpt());
        assertTrue(optionShortArg.hasArg());
        assertEquals("Description for option 'b'", optionShortArg.getDescription());
        assertFalse(optionShortArg.isRequired());

        // Test adding an option with a long name and no argument
        Option optionLongNoArg = options.addOption("c", false, "Description for option 'c'");
        assertEquals(null, optionLongNoArg.getKey());
        assertEquals("c", optionLongNoArg.getLongOpt());
        assertFalse(optionLongNoArg.hasArg());
        assertEquals("Description for option 'c'", optionLongNoArg.getDescription());
        assertFalse(optionLongNoArg.isRequired());

        // Test adding an option with a long name and an argument
        Option optionLongArg = options.addOption("d", "data", true, "Description for option 'd'");
        assertEquals(null, optionLongArg.getKey());
        assertEquals("d", optionLongArg.getLongOpt());
        assertTrue(optionLongArg.hasArg());
        assertEquals("Description for option 'd'", optionLongArg.getDescription());
        assertFalse(optionLongArg.isRequired());

        // Test adding an option with a short name and a long name
        Option optionBoth = options.addOption("e", "extra", false, "Description for option 'e'");
        assertEquals("e", optionBoth.getKey());
        assertEquals("extra", optionBoth.getLongOpt());
        assertFalse(optionBoth.hasArg());
        assertEquals("Description for option 'e'", optionBoth.getDescription());
        assertFalse(optionBoth.isRequired());

        // Test adding an option with a short name and a long name and an argument
        Option optionBothArg = options.addOption("f", "flag", true, "Description for option 'f'");
        assertEquals("f", optionBothArg.getKey());
        assertEquals("flag", optionBothArg.getLongOpt());
        assertTrue(optionBothArg.hasArg());
        assertEquals("Description for option 'f'", optionBothArg.getDescription());
        assertFalse(optionBothArg.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionDuplicateShort() {
        Options options = new Options();
        Option optionShort = options.addOption("a", false, "Description for option 'a'");
        options.addOption("a", false, "Duplicate option 'a'");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionDuplicateLong() {
        Options options = new Options();
        Option optionLong = options.addOption(null, "longOption", false, "Description for option 'longOption'");
        options.addOption(null, "longOption", false, "Duplicate option 'longOption'");
    }

    @Test
    public void testAddOptionGroup() {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("group", "Group description");
        optionGroup.setRequired(true);

        Option option1 = new Option("opt1", "option 1", false, "Option 1");
        option1.setRequired(true);
        optionGroup.addOption(option1);

        Option option2 = new Option("opt2", "option 2", false, "Option 2");
        option2.setRequired(true);
        optionGroup.addOption(option2);

        options.addOptionGroup(optionGroup);

        assertEquals(2, options.getOptions().size());
        assertTrue(options.hasOption("opt1"));
        assertTrue(options.hasOption("opt2"));
        assertTrue(options.isRequired("opt1"));
        assertTrue(options.isRequired("opt2"));
        assertFalse(options.isRequired("nonexistent"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionGroupDuplicate() {
        Options options = new Options();
        OptionGroup optionGroup = new OptionGroup("group", "Group description");
        options.addOptionGroup(optionGroup);
        options.addOptionGroup(optionGroup);
    }

    @Test
    public void testParseOptions() throws ParseException {
        Options options = new Options();

        Option optionShortNoArg = options.addOption("a", "alias", false, "Description for option 'a'");
        Option optionShortArg = options.addOption("b", "boolean", true, "Description for option 'b'");
        Option optionLongNoArg = options.addOption(null, "c", false, "Description for option 'c'");
        Option optionLongArg = options.addOption(null, "d", true, "Description for option 'd'");

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"--a", "--c", "value", "--d", "anotherValue"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertFalse(cmd.hasOption("b"));
        assertFalse(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("d"));
        assertEquals("anotherValue", cmd.getOptionValue("d"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test(expected = ParseException.class)
    public void testParseOptionsMissingRequiredOption() throws ParseException {
        Options options = new Options();

        Option optionShortNoArg = options.addOption("a", "alias", false, "Description for option 'a'");
        Option optionShortArg = options.addOption("b", "boolean", true, "Description for option 'b'");
        Option optionLongNoArg = options.addOption(null, "c", false, "Description for option 'c'");
        Option optionLongArg = options.addOption(null, "d", true, "Description for option 'd'");
        OptionGroup optionGroup = new OptionGroup("group", "Group description");
        optionGroup.setRequired(true);
        Option optionGroupOption = new Option("g", "groupOption", false, "Option in group");
        optionGroup.addOption(optionGroupOption);
        options.addOptionGroup(optionGroup);

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"--c"};
        CommandLine cmd = parser.parse(options, args);

        assertTrue(options.isRequired("a"));
        assertTrue(options.isRequired("b"));
        assertTrue(options.isRequired("c"));
        assertTrue(options.isRequired("d"));
        assertTrue(options.isRequired("g"));

        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            assertFalse(e.getMessage().contains("Option 'g' is required"));
            fail("Unexpected error message");
        }
    }

    @Test(expected = ParseException.class)
    public void testParseOptionsInvalidOption() throws ParseException {
        Options options = new Options();

        Option optionShortNoArg = options.addOption("a", "alias", false, "Description for option 'a'");
        Option optionShortArg = options.addOption("b", "boolean", true, "Description for option 'b'");
        Option optionLongNoArg = options.addOption(null, "c", false, "Description for option 'c'");
        Option optionLongArg = options.addOption(null, "d", true, "Description for option 'd'");

        CommandLineParser parser = new DefaultParser();
        String[] args = new String[]{"--invalid"};
        CommandLine cmd = parser.parse(options, args);

        fail("Unexpectedly parsed command line");
    }

    @Test
    public void testToString() {
        Options options = new Options();

        Option optionShortNoArg = options.addOption("a", "alias", false, "Description for option 'a'");
        Option optionShortArg = options.addOption("b", "boolean", true, "Description for option 'b'");
        Option optionLongNoArg = options.addOption(null, "c", false, "Description for option 'c'");
        Option optionLongArg = options.addOption(null, "d", true, "Description for option 'd'");

        String expected =
                "[ Options: [ short {a=(no-args)} alias=Description for option 'a'}] [ long {c=no-args} c=Description for option 'c'} ]";

        assertEquals(expected, options.toString());
    }
}