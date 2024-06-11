import static org.junit.Assert.*;
import org.apache.commons.cli.Option;

public class OptionTest {

    @Test
    public void testOptionDefaultConstructor() {
        Option option = new Option();
        assertNull(option.getOpt());
        assertNull(option.getLongOpt());
        assertNull(option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValuesList());
    }

    @Test
    public void testOptionStringConstructor() {
        Option option = new Option("testOpt", "Test Option");
        assertEquals("testOpt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValuesList());
    }

    @Test
    public void testOptionStringBooleanConstructor() {
        Option option = new Option("testOpt", false, "Test Option");
        assertEquals("testOpt", option.getOpt());
        assertNull(option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValuesList());
    }

    @Test
    public void testOptionStringStringBooleanConstructor() {
        Option option = new Option("testOpt", "longTestOpt", false, "Test Option");
        assertEquals("testOpt", option.getOpt());
        assertEquals("longTestOpt", option.getLongOpt());
        assertEquals("Test Option", option.getDescription());
        assertFalse(option.isRequired());
        assertTrue(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getType());
        assertNull(option.getValuesList());
    }
}