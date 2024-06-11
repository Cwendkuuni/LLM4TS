import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionTest {

    @Test
    public void testOptionConstructorWithNoArguments() {
        Option option = new Option("optA", "Description of optA");
        assertNotNull(option);
        assertEquals("optA", option.getOpt());
        assertEquals("Description of optA", option.getDescription());
        assertNull(option.getLongOpt());
        assertFalse(option.hasArg());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertFalse(option.hasLongOpt());
        assertFalse(option.hasArgName());
        assertFalse(option.hasArgs());
        assertEquals(-1, option.getArgs());
        assertFalse(option.hasValueSeparator());
        assertFalse(option.hasNoValues());
        assertEquals(0, option.values.size());
    }
}