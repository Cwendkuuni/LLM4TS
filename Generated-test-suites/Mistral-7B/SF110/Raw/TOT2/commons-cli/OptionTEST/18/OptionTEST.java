import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    @Test
    public void testOptionNoArgsConstructor() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getType());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertNull(option.getArgName());
        assertFalse(option.hasArgs());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertTrue(option.hasNoValues());
        assertEquals("option: opt :: description ", option.toString());
    }
}