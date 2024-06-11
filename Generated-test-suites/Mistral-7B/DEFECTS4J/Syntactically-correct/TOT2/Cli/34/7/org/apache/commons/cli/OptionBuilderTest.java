
import static org.junit.Assert.*;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder withLongOpt = optionBuilder.withLongOpt("--option");
        assertEquals("--option", withLongOpt.getLongOpt());
    }

    // Test cases for other methods and edge cases go here

    // Test method to check create method
    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder optionBuilder = new OptionBuilder();
        OptionBuilder withLongOpt = optionBuilder.withLongOpt("--option");
        OptionBuilder withDescription = withLongOpt.withDescription("Test Option");
        OptionBuilder hasArg = withDescription.hasArg();
        OptionBuilder isRequired = hasArg.isRequired();

        Option option = withLongOpt.create('O');
        assertEquals("--option", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
        assertEquals(1, option.getNumberOfArgs());
        assertTrue(option.isRequired());
    }
}