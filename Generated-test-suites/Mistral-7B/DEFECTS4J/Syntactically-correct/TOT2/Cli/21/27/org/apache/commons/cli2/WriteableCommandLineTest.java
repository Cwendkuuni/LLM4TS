import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("--test", new DefaultParser());
        Option option = new Option("test", "test", false, "Test option");
        commandLine.addOption(option);
        assertTrue(commandLine.getOptions().contains(option));
    }
}