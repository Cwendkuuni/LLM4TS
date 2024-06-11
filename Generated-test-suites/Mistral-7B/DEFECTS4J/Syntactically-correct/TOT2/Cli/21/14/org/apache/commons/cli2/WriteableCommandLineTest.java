import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CommandLineParser().parse("--test");
        Option option = Option.builder("t").longOpt("test").build();
        commandLine.addOption(option);
        List values = commandLine.getOptions();
        assertTrue(values.contains(option));
    }
}