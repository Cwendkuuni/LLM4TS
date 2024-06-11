import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("optionLong");
        Option option = optionBuilder.create();
        assertEquals("optionLong", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        Option option = optionBuilder.create();
        assertEquals(Option.UNINITIALIZED, option.getArgs().length);
        OptionBuilder optionBuilderWithArg = OptionBuilder.hasArg(true);
        Option optionWithArg = optionBuilderWithArg.create();
        assertEquals(Option.UNINITIALIZED, optionWithArg.getArgs().length);
        OptionBuilder optionBuilderWithArgName = OptionBuilder.hasArg(true).withArgName("argName");
        Option optionWithArgName = optionBuilderWithArgName.create();
        assertEquals(1, optionWithArgName.getArgs().length);
        assertEquals("argName", optionWithArgName.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        Option option = optionBuilder.create();
        assertFalse(option.isRequired());
        OptionBuilder optionBuilderRequired = OptionBuilder.isRequired(true);
        Option optionRequired = optionBuilderRequired.create();
        assertTrue(optionRequired.isRequired());
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        Option option = optionBuilder.create();
        assertFalse(option.hasOptionalArg());
        OptionBuilder optionBuilderOptionalArg = OptionBuilder.hasOptionalArg(true);
        Option optionOptionalArg = optionBuilderOptionalArg.create();
        assertTrue(optionOptionalArg.hasOptionalArg());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(',');
        Option option = optionBuilder.create();
        assertEquals(',', option.getValueSeparator());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithMissingLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.create();
        Option option = optionBuilder.create();
    }
}