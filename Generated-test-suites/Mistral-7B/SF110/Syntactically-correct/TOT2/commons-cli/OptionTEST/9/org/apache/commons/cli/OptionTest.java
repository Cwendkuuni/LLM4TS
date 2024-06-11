import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionTest {
    @Test
    public void testOptionWithNoArguments() {
        Option option = new Option("optionName", "description");
        assertNotNull(option);
        assertEquals("optionName", option.getOpt());
        assertEquals("description", option.getDescription());
    }
}