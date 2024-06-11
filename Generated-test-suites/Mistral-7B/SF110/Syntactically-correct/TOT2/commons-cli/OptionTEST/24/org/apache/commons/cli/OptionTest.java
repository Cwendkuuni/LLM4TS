import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Option;

public class OptionTest {

    @Test
    public void testOptionConstructionWithNoArguments() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.hasArg());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionConstructionWithOneArgument() {
        Option option = new Option("opt", "description", true, "arg");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertEquals("arg", option.getArgName());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }

    @Test
    public void testOptionConstructionWithOneArgumentAndDescription() {
        Option option = new Option("opt", "option long name", true, "arg", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("option long name", option.getLongOpt());
        assertEquals("arg", option.getArgName());
        assertTrue(option.hasArg());
        assertEquals(1, option.getArgs());
        assertNull(option.getType());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertTrue(option.hasLongOpt());
        assertFalse(option.hasValueSeparator());
        assertNull(option.getValue());
        assertFalse(option.hasValues());
    }
}