import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class WriteableCommandLineTest {

    @Test
    public void testAddOption() {
        WriteableCommandLine cmd = new CommandLineParser().parse(-1, new String[]{"--test"});
        Option option = Option.builder("t").longOpt("test").build();
        cmd.addOption(option);
        assertNotNull(cmd.getOption("t"));
    }
}