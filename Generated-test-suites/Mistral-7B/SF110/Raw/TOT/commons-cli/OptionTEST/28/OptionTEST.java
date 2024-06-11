import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionCreationWithSingleCharacterOption() {
        Option option = new Option("a", "option-a", false, "This is an option with a single character short name.");
        assertEquals("option-a", option.getLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertEquals("This is an option with a single character short name.", option.getDescription());
        assertEquals('a', option.getId());
        assertEquals("a", option.getKey());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionCreationWithLongOption() {
        Option option = new Option("my-long-option", "my-long-option-description", false, "This is a long option.");
        assertEquals("my-long-option", option.getOpt());
        assertEquals("my-long-option-description", option.getDescription());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertEquals("my-long-option", option.getKey());
        assertEquals("my-long-option", option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionCreationWithOptionalSingleCharacterArgument() {
        Option option = new Option("b", "option-b", true, "This is an option with a single character short name and an optional argument.");
        assertEquals("option-b", option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertEquals("This is an option with a single character short name and an optional argument.", option.getDescription());
        assertEquals('b', option.getId());
        assertEquals("b", option.getKey());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionCreationWithMandatorySingleCharacterArgument() {
        try {
            new Option("c", "option-c", true, "This is an option with a single character short name and a mandatory argument.");
            fail("An IllegalArgumentException should have been thrown.");
        } catch (IllegalArgumentException e) {
            assertEquals("Option 'c' must not have an argument with mandatory value.", e.getMessage());
        }
    }

    @Test
    public void testOptionCreationWithOptionalLongArgument() {
        Option option = new Option("d", null, true, "This is an option with a long name and an optional argument.");
        assertEquals(null, option.getOpt());
        assertTrue(option.hasOptionalArg());
        assertNull(option.getValue());
        assertFalse(option.isRequired());
        assertEquals("This is an option with a long name and an optional argument.", option.getDescription());
        assertEquals("d", option.getKey());
        assertEquals(null, option.getLongOpt());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
    }

    @Test
    public void testOptionCreationWithMandatoryLongArgument() {
        Option option = new Option("e", "mandatory-option", true, "This is an option with a long name and a mandatory argument.");
        assertEquals("mandatory-option", option.getLongOpt());
        assertTrue(option.hasArg());
        assertNull(option.getValue());
        assertTrue(option.isRequired());
        assertEquals("This is an option with a long name and a mandatory argument.", option.getDescription());
        assertEquals("e", option.getKey());
        assertEquals("mandatory-option", option.getLongOpt());
        assertEquals(1, option.getArgs());
    }

    @Test
    public void testOptionCreationWithListOfValues() {
        Option option = OptionBuilder.withLongOpt("values")
                .withDescription("This is an option with a list of values.")
                .hasArgs(3)
                .isRequired(true)
                .create();
        assertEquals("values", option.getOpt());
        assertTrue(option.hasArgs());
        assertTrue(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals("This is an option with a list of values.", option.getDescription());
        assertEquals("values", option.getKey());
        assertEquals("values", option.getLongOpt());
        assertEquals(3, option.getArgs());
    }

    @Test
    public void testAddingValuesToOption() {
        Option option = new Option("f", "option-f", true, "This is an option with a list of values.");
        option.setArgs(3);
        option.setValueSeparator(';');
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        option.addValueForProcessing("val3");
        List<String> values = option.getValuesList();
        assertEquals("val1", values.get(0));
        assertEquals("val2", values.get(1));
        assertEquals("val3", values.get(2));
        assertEquals(3, values.size());
    }

    @Test(expected = RuntimeException.class)
    public void testAddingMoreValuesToOptionThanSpecified() {
        Option option = new Option("g", "option-g", true, "This is an option with a list of values.");
        option.setArgs(3);
        option.setValueSeparator(';');
        option.addValueForProcessing("val1");
        option.addValueForProcessing("val2");
        option.addValueForProcessing("val3");
        option.addValueForProcessing("val4");
    }
}