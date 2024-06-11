import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

/**
 * Test class for the Parser class.
 */
public class ParserTest {

    /**
     * Test parsing a command line with valid options and arguments.
     */
    @Test
    public void testValidCommandLine() {
        Options options = new Options();
        options.addOption("a", true, "an option with no value");
        options.addOption("b", 1, "an option with an integer value");
        options.addOption("c", 3, "an option with a short value");
        options.addOption("d", "default", "an option with a default value");
        options.addOption("e", "value", "an option with a value");
        options.addOption("f", "value1,value2", "an option with multiple values");

        String[] arguments = {"-a", "-b", "3", "--c", "--d", "--e", "--f", "value3"};
        CommandLine cmd = new Parser().parse(options, arguments);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("x"));
        Assert.assertEquals(3, cmd.getOptionValue("b", (short) 0));
        Assert.assertEquals("value", cmd.getOptionValue("e"));
        Assert.assertArrayEquals(new String[]{"value1", "value2"},
                cmd.getOptionValues("f"));
        Assert.assertNull(cmd.getOptionValue("g"));
    }

    /**
     * Test parsing a command line with required options.
     */
    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() {
        Options options = new Options();
        options.addRequiredOption("a");
        String[] arguments = {};
        new Parser().parse(options, arguments);
    }

    /**
     * Test parsing a command line with invalid options.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "an option with no value");
        String[] arguments = {"-x", "--a"};
        new Parser().parse(options, arguments);
    }

    /**
     * Test parsing a command line with unrecognized arguments.
     */
    @Test
    public void testUnrecognizedArgument() {
        Options options = new Options();
        options.addOption("a", true, "an option with no value");
        String[] arguments = {"--a", "value"};
        CommandLine cmd = new Parser().parse(options, arguments);
        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals("value", cmd.getOptionValue("a"));
        Assert.assertEquals(0, cmd.getArgs().length);
    }
}