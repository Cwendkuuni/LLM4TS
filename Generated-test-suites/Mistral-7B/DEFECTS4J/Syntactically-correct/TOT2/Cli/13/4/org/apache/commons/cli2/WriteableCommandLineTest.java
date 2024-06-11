import org.junit.Test;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test");
        Option option = new Option("t", "test", true, "Test option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
        assertFalse(commandLine.getOptions().isEmpty());
    }
}