import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionNoArgsConstructor() {
        Option option = new Option("testOption", "Test Option");
        assertEquals("testOption", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionOptionalArgConstructor() {
        Option option = new Option("testOption", true, "Test Option");
        assertEquals("testOption", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertNull(option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionOptionalArgAndLongOptConstructor() {
        Option option = new Option("testOption", "testLongOption", true, "Test Option");
        assertEquals("testOption", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertEquals("testLongOption", option.getLongOpt());
        assertTrue(option.hasOptionalArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionNullOpt() {
        try {
            new Option(null, "Test Option");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionEmptyDescription() {
        try {
            new Option("testOption", true, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionEmptyOpt() {
        try {
            new Option("", "Test Option");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionEmptyLongOpt() {
        try {
            new Option("testOption", "testLongOption", true, "");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionNullDescription() {
        try {
            new Option("testOption", true, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptionNoArgConstructorWithArgName() {
        Option option = new Option("testOption", "Test Option", "testArgName");
        assertEquals("testOption", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertEquals("testArgName", option.getArgName());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionWithArgs() {
        Option option = new Option("testOption", 2, "Test Option");
        assertEquals("testOption", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertTrue(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionWithNegativeNumberOfArgs() {
        try {
            new Option("testOption", -1, "Test Option");
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testOptionWithUnlimitedValues() {
        Option option = new Option("testOption", Option.UNLIMITED_VALUES, "Test Option");
        assertEquals("testOption", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertNotNull(option.getValue());
        assertTrue(option.hasValues());
    }
}