import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        String[] args = new String[]{"-a", "--optionA"};
        CommandLine cmd = new CommandLine().parse(new Options().addOption("a", true).addOptions(), args);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption('a'));
        Assert.assertFalse(cmd.hasOption("notAnOption"));
        Assert.assertFalse(cmd.hasOption('z'));
    }
}