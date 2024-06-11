import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOneArgument() {
        Option option = new Option("test", "Test Option", false);
        assertEquals("test", option.getOpt());
        assertEquals("Test Option", option.getDescription());
        assertEquals(false, option.hasArg());
        assertEquals(null, option.getLongOpt());
        assertEquals(null, option.getValue());
    }
}