import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithNoArguments() {
        Option option = new Option("optionName", "description");
        assertEquals("optionName", option.getOpt());
        assertEquals("description", option.getDescription());
        assertNull(option.getType());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArg());
        assertNull(option.getValue());
        assertFalse(option.hasArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.isRequired());
        assertTrue(option.hasArgName());
        assertNull(option.getArgName());
        assertFalse(option.equals(null));
        assertFalse(option.equals("not an Option object"));
        Option anotherOption = new Option("optionName", "description");
        assertTrue(option.equals(anotherOption));
    }
}