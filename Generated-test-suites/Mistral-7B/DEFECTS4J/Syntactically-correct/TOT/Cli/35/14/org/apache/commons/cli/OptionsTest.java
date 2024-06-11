import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Test class for Options class.
 */
public class OptionsTest {

    /**
     * Test for addOption method with a single character short option.
     */
    @Test
    public void testAddOptionWithSingleCharacterShortOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is option A");

        assertTrue(options.hasOption("a"));
        assertEquals("This is option A", options.getOption("a").getDesc());
    }

    /**
     * Test for addOption method with a long option.
     */
    @Test
    public void testAddOptionWithLongOption() {
        Options options = new Options();
        options.addOption("a", "optionA", false, "This is option A");
        options.addOption("l", "optionL", false, "This is option L");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("l"));
        assertEquals("This is option A", options.getOption("a").getDesc());
        assertEquals("This is option L", options.getOption("l").getDesc());
    }

    /**
     * Test for addOptionGroup method with a required option group.
     */
    @Test
    public void testAddOptionGroupWithRequiredOptionGroup() {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("group", "This is a group");
        optionGroup.addOption(new Option("g", "groupOption", true, "This is a group option"));
        options.addOptionGroup(optionGroup);

        options.addOption("a", "optionA", false, "This is option A");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("g"));
        assertFalse(options.getOption("g").isRequired());
        assertTrue(options.getOption("groupOption").isRequired());
    }

    /**
     * Test for addOptionGroup method with a non-required option group.
     */
    @Test
    public void testAddOptionGroupWithNonRequiredOptionGroup() {
        Options options = new Options();

        OptionGroup optionGroup = new OptionGroup("group", "This is a group");
        optionGroup.addOption(new Option("g", "groupOption", false, "This is a group option"));
        options.addOptionGroup(optionGroup);

        options.addOption("a", "optionA", false, "This is option A");

        assertTrue(options.hasOption("a"));
        assertTrue(options.hasOption("g"));
        assertFalse(options.getOption("g").isRequired());
        assertFalse(options.getOption("groupOption").isRequired());
    }

    /**
     * Test parsing of options with values.
     */
    @Test
    public void testParseOptionsWithValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "optionA", "Value A");

        String[] args = {"-a", "Value B"};
        CommandLine cmd = new DefaultParser().parse(options, args);
        assertEquals("Value B", cmd.getOptionValue("a"));
    }

    /**
     * Test parsing of options with no values.
     */
    @Test
    public void testParseOptionsWithNoValues() throws ParseException {
        Options options = new Options();
        options.addOption("a", false, "optionA", "Option A");

        String[] args = {"-a"};
        CommandLine cmd = new DefaultParser().parse(options, args);
        assertTrue(cmd.hasOption("a"));
        assertNull(cmd.getOptionValue("a"));
    }
}