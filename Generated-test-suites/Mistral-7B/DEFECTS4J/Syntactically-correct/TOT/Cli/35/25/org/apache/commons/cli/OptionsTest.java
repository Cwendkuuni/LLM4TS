import org.apache.commons.cli.Options;
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

        // test adding a simple option with no argument
        options.addOption("a", "option A");
        assertEquals("option A", options.getOption("a").getLongOpt());

        // test adding a simple option with an argument
        options.addOption("b", "option B", true, "argument B");
        Option optionB = options.getOption("b");
        assertTrue(optionB.hasArg());
        assertEquals("argument B", optionB.getArgName());

        // test adding an option with a short and long name
        options.addOption("c", "option C", false, "option C");
        options.addOption("d", "option D", false, "option D");
        Option optionC = options.getOption("c");
        assertEquals("option C", optionC.getLongOpt());
        Option optionD = options.getOption("d");
        assertEquals("option D", optionD.getLongOpt());

        // test adding an option group
        OptionGroup group = new OptionGroup("group", "group description");
        group.addOption(new Option("g", "group option", false, "group option"));
        options.addOptionGroup(group);
        Option groupOption = options.getOption("g");
        assertEquals("group option", groupOption.getLongOpt());
        assertEquals("group description", groupOption.getDescription());
    }

    @Test(expected = ParseException.class)
    public void testAddDuplicateOption() {
        Options options = new Options();

        // test adding a duplicate option
        options.addOption("a", "option A");
        options.addOption("a", "option A");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullDescription() {
        Options options = new Options();

        // test adding an option with a null description
        options.addOption("a", "option A", true, null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionWithNullArgName() {
        Options options = new Options();

        // test adding an option with a null arg name
        options.addOption("a", "option A", true, null);
    }
}