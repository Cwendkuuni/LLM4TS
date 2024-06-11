import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class CommandLineTest {

    private CommandLine commandLine;
    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", "testOptionA", false, "Test Option A");
        options.addOption("b", "testOptionB", true, "Test Option B");
        options.addOption("c", "testOptionC", true, "Test Option C");
        options.addOption("d", "testOptionD", true, "Test Option D");
        options.addOption("e", "testOptionE", false, "Test Option E");
    }

    @After
    public void tearDown() {
        commandLine = null;
    }

    @Test
    public void testHasOption() throws ParseException {
        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-a", "--testOptionB", "arg1", "arg2"});

        Assert.assertTrue(commandLine.hasOption("a"));
        Assert.assertTrue(commandLine.hasOption("b"));
        Assert.assertFalse(commandLine.hasOption("z"));

        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--testOptionC=value1", "--testOptionE"});

        Assert.assertTrue(commandLine.hasOption("b", false)); // false option 'a' not present
        Assert.assertTrue(commandLine.hasOption("c"));
        Assert.assertTrue(commandLine.hasOption("e"));
        Assert.assertFalse(commandLine.hasOption("z"));
    }

    @Test
    public void testGetOptionValue() throws ParseException {
        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-a", "--testOptionB=value1", "arg1", "arg2"});

        Assert.assertEquals("value1", commandLine.getOptionValue("b"));
        Assert.assertNull(commandLine.getOptionValue("z"));

        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--testOptionC=value1", "--testOptionE"});

        Assert.assertNull(commandLine.getOptionValue("a"));
        Assert.assertEquals("value1", commandLine.getOptionValue("c"));
        Assert.assertNull(commandLine.getOptionValue("z"));
    }

    @Test
    public void testGetOptionValues() throws ParseException {
        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-a", "-b", "--testOptionB=value1", "--testOptionB=value2", "arg1", "arg2"});

        String[] values = commandLine.getOptionValues("b");
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.length);
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);

        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--testOptionD=value1,value2"});

        values = commandLine.getOptionValues("d");
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.length);
        Assert.assertEquals("value1", values[0]);
        Assert.assertEquals("value2", values[1]);
    }

    @Test
    public void testGetArgs() throws ParseException {
        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-a", "--testOptionB=value1", "arg1", "arg2"});

        String[] args = commandLine.getArgs();
        Assert.assertNotNull(args);
        Assert.assertEquals(2, args.length);
        Assert.assertEquals("arg1", args[0]);
        Assert.assertEquals("arg2", args[1]);

        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--testOptionC=value1", "--testOptionE", "arg1", "arg2", "arg3"});

        args = commandLine.getArgs();
        Assert.assertNotNull(args);
        Assert.assertEquals(3, args.length);
        Assert.assertEquals("arg1", args[0]);
        Assert.assertEquals("arg2", args[1]);
        Assert.assertEquals("arg3", args[2]);
    }

    @Test
    public void testGetArgList() throws ParseException {
        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-a", "--testOptionB=value1", "arg1", "arg2"});

        List argList = commandLine.getArgList();
        Assert.assertNotNull(argList);
        Assert.assertEquals(2, argList.size());
        Assert.assertEquals("arg1", argList.get(0));
        Assert.assertEquals("arg2", argList.get(1));

        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--testOptionC=value1", "--testOptionE", "arg1", "arg2", "arg3"});

        argList = commandLine.getArgList();
        Assert.assertNotNull(argList);
        Assert.assertEquals(3, argList.size());
        Assert.assertEquals("arg1", argList.get(0));
        Assert.assertEquals("arg2", argList.get(1));
        Assert.assertEquals("arg3", argList.get(2));
    }

    @Test
    public void testToString() throws ParseException {
        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-a", "--testOptionB=value1", "arg1", "arg2"});

        String toString = commandLine.toString();
        Assert.assertNotNull(toString);
        Assert.assertTrue(toString.contains("CommandLine: [ options:"));
        Assert.assertTrue(toString.contains("args: [arg1, arg2] ] ]"));

        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"--testOptionC=value1", "--testOptionE", "arg1", "arg2", "arg3"});

        toString = commandLine.toString();
        Assert.assertNotNull(toString);
        Assert.assertTrue(toString.contains("CommandLine: [ options:"));
        Assert.assertTrue(toString.contains("args: [arg1, arg2, arg3] ] ]"));
    }
}