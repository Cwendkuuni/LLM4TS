import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("a", true, "An option with a short name.");

        commandLine.parse(options.parse(new String[]{"--a"}));
        Assert.assertTrue(commandLine.hasOption("a"));

        commandLine.parse(options.parse(new String[]{"--b"}));
        Assert.assertFalse(commandLine.hasOption("a"));
        Assert.assertFalse(commandLine.hasOption("b"));

        commandLine.parse(options.parse(new String[]{"--a", "--b"}));
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
    }
}