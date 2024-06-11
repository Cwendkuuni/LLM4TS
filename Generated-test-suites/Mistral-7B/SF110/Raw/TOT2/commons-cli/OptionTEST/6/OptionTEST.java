import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionWithValidArguments() {
        Option option = new Option("-o", "Option with valid arguments");
        assertEquals("-o", option.getOpt());
        assertEquals("Option with valid arguments", option.getDescription());
    }
}