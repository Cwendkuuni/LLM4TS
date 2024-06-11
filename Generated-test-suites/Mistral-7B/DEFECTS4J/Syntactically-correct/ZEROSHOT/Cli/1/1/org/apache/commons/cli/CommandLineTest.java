import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    private CommandLine commandLine;
    private String[] args;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "optionA", false, "Option A");
        options.addOption("b", "optionB", true, "Option B");
        args = new String[]{"-a", "--optionB", "test"};
    }

    @Test
    public void testHasOption() {
        commandLine = new CommandLine();
        commandLine.parse(options, args);
        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption('a'));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertTrue(commandLine.hasOption('b'));
        Assert.assertFalse(commandLine.hasOption("c"));
        Assert.assertFalse(commandLine.hasOption('c'));
    }

    @Test
    public void testGetOptionValue() {
        commandLine = new CommandLine();
        commandLine.parse(options, args);
        Assert.assertNull(commandLine.getOptionValue("c"));
        Assert.assertNull(commandLine.getOptionValue('c'));
        Assert.assertNull(commandLine.getOptionValue("a"));
        Assert.assertNull(commandLine.getOptionValue('a'));
        Assert.assertEquals("test", commandLine.getOptionValue("optionB"));
        Assert.assertEquals("test", commandLine.getOptionValue('b'));
    }

    @Test
    public void testGetOptionValues() {
        commandLine = new CommandLine();
        options.addOption("c", "optionC", true, "Option C");
        args = new String[]{"-a", "--optionB", "test1", "--optionB", "test2", "-c", "value1", "-c", "value2"};
        commandLine.parse(options, args);
        String[] optionCVals = commandLine.getOptionValues("c");
        Assert.assertArrayEquals(new String[]{"value1", "value2"}, optionCVals);
    }

    @Test(expected = AssertionError.class)
    public void testGetOptionValueWithNoOption() {
        commandLine = new CommandLine();
        commandLine.parse(options, args);
        Assert.assertNull(commandLine.getOptionValue("d"));
        Assert.assertNull(commandLine.getOptionValue('d'));
    }

    @Test
    public void testGetArgs() {
        commandLine = new CommandLine();
        commandLine.parse(options, args);
        String[] argsArray = commandLine.getArgs();
        Assert.assertArrayEquals(new String[]{"test1", "test2"}, argsArray);
    }

    @Test
    public void testParseException() {
        options.addOption("z", "optionZ", false, "Option Z");
        args = new String[]{"-z"};
        try {
            commandLine = new CommandLine();
            commandLine.parse(options, args);
        } catch (ParseException e) {
            Assert.assertEquals("Unrecognized option: z", e.getMessage());
        }
    }
}