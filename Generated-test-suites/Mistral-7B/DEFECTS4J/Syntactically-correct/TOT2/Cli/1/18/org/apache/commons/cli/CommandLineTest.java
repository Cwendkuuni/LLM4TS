import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("h", "help", false, "Print help message.");
        commandLine.parse(options.parse("--help"));
        Assert.assertTrue(commandLine.hasOption("h"));
        Assert.assertTrue(commandLine.hasOption('h'));
    }

    @Test
    public void testGetOptionValue() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path.");
        commandLine.parse(options.parse("--output,output.txt"));
        Assert.assertEquals("output.txt", commandLine.getOptionValue("output"));
        Assert.assertEquals("output.txt", commandLine.getOptionValue('o'));
    }

    @Test
    public void testGetOptionValues() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("l", "list", true, "List of items.");
        commandLine.parse(options.parse("--list,item1,item2"));
        String[] values = commandLine.getOptionValues("list");
        Assert.assertArrayEquals(new String[]{"item1", "item2"}, values);
    }

    @Test
    public void testGetArgs() {
        CommandLine commandLine = new CommandLine();
        String[] args = new String[]{"arg1", "arg2"};
        commandLine.parse(args);
        String[] expectedArgs = new String[]{"arg1", "arg2"};
        Assert.assertArrayEquals(expectedArgs, commandLine.getArgs());
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path.");
        commandLine.parse(new String[]{"--badOption"});
    }

    @Test(expected = ParseException.class)
    public void testMissingRequiredOption() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path.");
        commandLine.parse(new String[]{"--"});
    }

    @Test(expected = ParseException.class)
    public void testInvalidOption() {
        CommandLine commandLine = new CommandLine();
        Options options = new Options();
        options.addOption("o", "output", true, "Output file path.");
        commandLine.parse(new String[]{"--notAnOption"});
    }
}