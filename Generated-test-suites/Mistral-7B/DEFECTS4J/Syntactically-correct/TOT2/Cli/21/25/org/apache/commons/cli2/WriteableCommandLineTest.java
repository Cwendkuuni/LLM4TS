import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine commandLine = new CliCommandLineParser().parse("-a, --foo");
        Option option = Option.builder("a").longOpt("foo").build();
        commandLine.addOption(option);
        assert(commandLine.getOptions().contains(option));
    }
}