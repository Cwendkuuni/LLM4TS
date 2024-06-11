import org.apache.commons.cli2.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = Option.builder("optionName").build();

        commandLine.addOption(option);
        List options = commandLine.getOptions();

        assertTrue(options.contains(option));
    }

    // edge case: null option
    @Test(expected = IllegalArgumentException.class)
    public void testAddOptionNullOption() {
        WriteableCommandLine commandLine = Mockito.mock(WriteableCommandLine.class);
        Option option = null;

        commandLine.addOption(option);
    }
}