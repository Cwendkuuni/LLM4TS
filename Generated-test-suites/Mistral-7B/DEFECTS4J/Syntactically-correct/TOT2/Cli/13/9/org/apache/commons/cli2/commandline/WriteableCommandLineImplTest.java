import static org.junit.Assert.*;
import org.apache.commons.cli2.Option;
import org.junit.Test;

public class WriteableCommandLineImplTest {
    // Test case for method addOption
    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = new WriteableCommandLineImpl(null, null);
        Option option = new Option("test", "testOption", false, "Test option");
        writeableCommandLine.addOption(option);
        List options = writeableCommandLine.getOptions();
        assertTrue(options.contains(option));
    }
}