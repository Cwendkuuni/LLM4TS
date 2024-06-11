import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;

public class OptionTest {

    @Test
    public void testOptionCreationWithOnlyShortOption() {
        Option option = new Option("o", "output", false, "Output the result to a file");
        assertEquals("o", option.getOpt());
        assertEquals("output", option.getLongOpt());
        assertFalse(option.hasArg());
        assertEquals(false, option.isRequired());
        assertEquals("Output the result to a file", option.getDescription());
        assertEquals(6, option.toString().length());
    }

    @Test
    public void testOptionCreationWithBothShortAndLongOptions() {
        Option option = new Option("o", "output", true, "Output the result to a file [FILE]");
        assertEquals("o", option.getOpt());
        assertEquals("output", option.getLongOpt());
        assertEquals("FILE", option.getArgName());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("Output the result to a file [FILE]", option.getDescription());
        assertEquals(12, option.toString().length());
    }

    @Test
    public void testOptionCreationWithOptionalArgument() {
        Option option = new Option("i", "input", true, "Input file [FILE]");
        assertEquals("i", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("FILE", option.getArgName());
        assertTrue(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("Input file [FILE]", option.getDescription());
        assertEquals(10, option.toString().length());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithInvalidShortOption() {
        Option option = new Option("1", "option", false, "Invalid option");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithEmptyDescription() {
        Option option = new Option("e", "empty", false, "");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithNegativeNumberOfArgs() {
        Option option = new Option("n", "number", -3, "Number of arguments");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionCreationWithUnlimitedNumberOfArgs() {
        Option option = new Option("u", "unlimited", Option.UNLIMITED_VALUES, "Unlimited number of arguments");
    }

    @Test
    public void testIdMethod() {
        Option option = new Option("t", "template", false, "Template option");
        assertEquals('t', option.getId());
    }

    @Test
    public void testKeyMethod() {
        Option option = new Option("t", "template", false, "Template option");
        assertEquals("t", option.getKey());
        Option optionWithLongOpt = new Option("t", null, false, "Template option");
        assertEquals("template", optionWithLongOpt.getKey());
    }

    @Test
    public void testValueSeparatorMethod() {
        Option option = new Option("c", "color", false, "Color option");
        Option optionWithSeparator = new Option("c", "color", false, "Color option", ',');
        assertEquals(false, option.hasValueSeparator());
        assertEquals(true, optionWithSeparator.hasValueSeparator());
        assertEquals(',', optionWithSeparator.getValueSeparator());
    }
}