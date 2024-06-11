import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addBooleanOption("test", false);
        CommandLine commandLine = new CommandLine();
        commandLine.parse(options.parse(new String[]{"--test"}));
        Assert.assertTrue(commandLine.hasOption("test"));
    }
}