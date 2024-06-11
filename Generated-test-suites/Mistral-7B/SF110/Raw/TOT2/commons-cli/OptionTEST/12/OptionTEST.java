import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

    @Test
    public void testOptionConstructorWithOptAndDescription() {
        Option option = new Option("--test", "Test option");
        assertEquals("--test", option.getOpt());
        assertEquals("Test option", option.getDescription());
    }

    // Test other method constructor and getters here

}