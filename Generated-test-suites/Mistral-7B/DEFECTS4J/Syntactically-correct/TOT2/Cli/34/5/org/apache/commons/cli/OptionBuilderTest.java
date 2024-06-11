import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option option = optionBuilder.create('o');
        assertEquals("optionLong", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create('a');
        assertTrue(option.hasArg());
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create('r');
        assertTrue(option.isRequired());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder optionBuilder = new OptionBuilder();
        Option option = optionBuilder.create('x');
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        Option option = optionBuilder.create('s');
        assertEquals(":", option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        Option option = optionBuilder.create('g');
        assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testCreateWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("This is a test option");
        Option option = optionBuilder.create('t');
        assertEquals("This is a test option", option.getDescription());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create('h');
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testCreateWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
        Option option = optionBuilder.create('i');
        assertEquals(Integer.class, option.getType());
    }
}