import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {
    @Test
    public void testOptionConstruction() {
        Option option = new Option("opt", "opt description");
        assertEquals("opt", option.getOpt());
        assertEquals("opt description", option.getDescription());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArg());
        assertNull(option.getLongOpt());
        assertFalse(option.isRequired());
        assertEquals(-1, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertTrue(option.hasNoValues());
        assertNull(option.getValue());
        assertEquals(0, option.values.size());
    }
}