import org.apache.commons.cli.*;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionBuilderTest {
    // Test case for method withLongOpt
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("optionA");
        assertEquals("optionA", instance.longopt);
    }

    // Test case for method hasArg
    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg();
        assertEquals(1, instance.numberOfArgs);
    }

    // Test case for method hasArg with false
    @Test
    public void testHasArgWithFalse() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.hasArg(false);
        assertEquals(Option.UNINITIALIZED, instance.numberOfArgs);
    }

    // Test case for method withArgName
    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withArgName("argName");
        assertEquals("argName", instance.argName);
    }

    // Test case for method isRequired with true
    @Test
    public void testIsRequiredWithTrue() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.isRequired();
        assertTrue(instance.required);
    }

    // Test case for method isRequired with false
    @Test
    public void testIsRequiredWithFalse() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.isRequired(false);
        assertFalse(instance.required);
    }

    // Test case for method withValueSeparator with ':'
    @Test
    public void testWithValueSeparatorWith() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator(':');
        assertEquals(':', instance.valuesep);
    }

    // Test case for method withValueSeparator with no argument
    @Test
    public void testWithValueSeparatorNoArg() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withValueSeparator();
        assertEquals('=', instance.valuesep);
    }

    // Test case for method create with valid arguments
    @Test
    public void testCreateValidArguments() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("optionA")
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .hasArg();
        Option option = instance.create('A');
        assertNotNull(option);
        assertEquals("optionA", option.getLongOpt());
        assertEquals("argName", option.getArgName());
        assertTrue(option.isRequired());
        assertEquals(':', option.getValueSeparator());
        assertEquals(1, option.getArgs().length);
    }

    // Test case for method create with invalid arguments
    @Test(expected = IllegalArgumentException.class)
    public void testCreateInvalidArguments() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder instance = optionBuilder.withLongOpt("optionA")
                .withArgName("argName")
                .isRequired()
                .withValueSeparator(':')
                .hasArg();
        Option option = instance.create('B');
        assertNull(option);
    }
}