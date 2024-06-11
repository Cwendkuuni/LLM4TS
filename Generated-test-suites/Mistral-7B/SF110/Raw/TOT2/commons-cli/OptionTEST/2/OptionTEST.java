import org.apache.commons.cli.Option;
import org.junit.Test;

public class OptionTest {
    @Test
    public void testOptionWithOptAndDescription() {
        Option option = new Option("opt", "description");
        assertNotNull(option);
        assertEquals("opt", option.getOpt());
        assertEquals("description", option.getDescription());
    }
}