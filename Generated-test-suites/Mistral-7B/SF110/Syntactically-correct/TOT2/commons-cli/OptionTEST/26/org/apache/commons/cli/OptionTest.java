import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    @Test
    public void testOptionWithNoArguments() {
        Option option = new Option("opt", "description");
        assertEquals("option", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getValue());
        assertNull(option.getValues());
        assertFalse(option.hasArg());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertTrue(option.hasNoValues());
    }
}