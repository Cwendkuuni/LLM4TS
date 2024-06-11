import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertTrue;

public class CommandLineTest {
    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this message.");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"-h"});
        assertTrue(cmd.hasOption("h"));
    }
}