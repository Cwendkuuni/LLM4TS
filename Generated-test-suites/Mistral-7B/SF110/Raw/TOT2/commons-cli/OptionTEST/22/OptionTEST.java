import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithNoArgumentsAndNoLongOption() {
        Option option = new Option("opt", "description");
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
        assertEquals(false, option.hasLongOpt());
        assertEquals(false, option.hasArg());
        assertEquals(false, option.isRequired());
        assertEquals(false, option.hasOptionalArg());
        assertEquals(-1, option.getArgs());
        assertEquals(false, option.hasValueSeparator());
        assertEquals(false, option.hasArgName());
        assertTrue(option.hasNoValues());
    }
}