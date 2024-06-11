import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithNoArguments() {
        Option option = new Option("testOption", "Test Option");
        assertEquals("testOption", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
    }
}