import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test for CommandLine class.
 */
public class CommandLineTest {

    private CommandLine commandLine;

    /**
     * Initialize commandLine object.
     */
    @Before
    public void init() {
        commandLine = new CommandLine();
    }

    /**
     * Test for hasOption method.
     */
    @Test
    public void testHasOption() {
        Options options = new Options();
        options.addOption("a", false, false, "An option with no argument.");
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"-a"});
            commandLine = parser.getCommandLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertTrue(commandLine.hasOption("a"));
    }

    /**
     * Test for getOptionValue method with simple option.
     */
    @Test
    public void testGetOptionValueSimpleOption() {
        Options options = new Options();
        options.addOption("o", true, "OptionalValue", "An option with a value.");
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"-o", "testValue"});
            commandLine = parser.getCommandLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        Assert.assertEquals("testValue", commandLine.getOptionValue("o"));
    }

    /**
     * Test for getOptionValue method with no option.
     */
    @Test
    public void testGetOptionValueNoOption() {
        Assert.assertNull(commandLine.getOptionValue("nonexistent"));
    }

    /**
     * Test for getOptionValues method with single value option.
     */
    @Test
    public void testGetOptionValuesSingleValueOption() {
        Options options = new Options();
        options.addOption("o", true, "OptionalValue", "An option with a value.");
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"-o", "testValue1", "--", "arg1", "arg2"});
            commandLine = parser.getCommandLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] values = commandLine.getOptionValues("o");
        Assert.assertNotNull(values);
        Assert.assertEquals(1, values.length);
        Assert.assertEquals("testValue1", values[0]);
    }

    /**
     * Test for getOptionValues method with multiple values option.
     */
    @Test
    public void testGetOptionValuesMultipleValuesOption() {
        Options options = new Options();
        options.addOption("o", true, "OptionalValue", "An option with a value.");
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"-o", "testValue1", "-o", "testValue2", "--", "arg1", "arg2"});
            commandLine = parser.getCommandLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] values = commandLine.getOptionValues("o");
        Assert.assertNotNull(values);
        Assert.assertEquals(2, values.length);
        Assert.assertEquals("testValue1", values[0]);
        Assert.assertEquals("testValue2", values[1]);
    }

    /**
     * Test for getOptionValues method with no value option.
     */
    @Test
    public void testGetOptionValuesNoValueOption() {
        Options options = new Options();
        options.addOption("o", false, false, "An option with no value.");
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"-o"});
            commandLine = parser.getCommandLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] values = commandLine.getOptionValues("o");
        Assert.assertNull(values);
    }

    /**
     * Test for getArgs method.
     */
    @Test
    public void testGetArgs() {
        Options options = new Options();
        options.addOption("o", false, false, "An option with no value.");
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"arg1", "arg2", "-o", "arg3"});
            commandLine = parser.getCommandLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        String[] args = commandLine.getArgs();
        Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
    }

    /**
     * Test for getArgList method.
     */
    @Test
    public void testGetArgList() {
        Options options = new Options();
        options.addOption("o", false, false, "An option with no value.");
        CommandLineParser parser = new DefaultParser();
        try {
            parser.parse(options, new String[]{"arg1", "arg2", "-o", "arg3"});
            commandLine = parser.getCommandLine();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        List<String> argList = commandLine.getArgList();
        Assert.assertEquals(Arrays.asList(Arrays.asList("arg1", "arg2")), argList);
    }
}