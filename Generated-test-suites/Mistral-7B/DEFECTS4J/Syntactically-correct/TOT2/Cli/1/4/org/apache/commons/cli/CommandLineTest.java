import static org.junit.Assert.assertTrue;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;

public class CommandLineTest {

    @org.junit.Test
    public void testHasOption() {
        CommandLine cmd = new CommandLine();
        cmd.addOption("option1", true);
        assertTrue(cmd.hasOption("option1"));
    }
}