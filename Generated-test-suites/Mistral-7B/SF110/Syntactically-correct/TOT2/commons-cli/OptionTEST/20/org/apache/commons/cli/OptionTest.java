import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionTest {

    @Test
    public void testOptionWithNoArguments() {
        Option option = new Option("opt", "description");
        assertNull(option.getType());
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertFalse(option.hasArg());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.isRequired());
        assertEquals(0, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasArgs());
        assertEquals(0, option.values.size());
        assertEquals("option: opt :: description ", option.toString());
    }
}