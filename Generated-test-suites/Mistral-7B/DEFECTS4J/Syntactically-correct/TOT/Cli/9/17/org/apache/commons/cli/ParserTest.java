import org.apache.commons.cli.*;
import org.junit.Assert.*;
import org.junit.Test;

public class ParserTest {

    @Test
    public void testParseSimple() throws ParseException {
        String[] args = {"-a", "1", "-b", "2", "--"};
        Options options = new Options();
        Option optionA = new Option("a", "a", true, "Set option A");
        Option optionB = new Option("b", "b", true, "Set option B");
        options.addOption(optionA);
        options.addOption(optionB);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args);

        Assert.assertEquals(true, cmd.hasOption("a"));
        Assert.assertEquals(true, cmd.hasOption("b"));
        Assert.assertEquals("1", cmd.getOptionValue("a"));
        Assert.assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--foo", "bar"};
        Properties properties = new Properties();
        properties.setProperty("foo", "qux");

        Options options = new Options();
        Option optionFoo = new Option("f", "foo", true, "Set foo");
        optionFoo.setLongOpt("FOO");
        options.addOption(optionFoo);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, properties, false);

        Assert.assertEquals(true, cmd.hasOption("foo"));
        Assert.assertEquals("qux", cmd.getOptionValue("foo"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingArgument() throws ParseException {
        String[] args = {"--qux"};
        Options options = new Options();
        Option optionQux = new Option("q", "qux", true, "Set qux [arg]");
        options.addOption(optionQux);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.fail("Missing argument exception expected");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"--quux"};
        Options options = new Options();
        Option optionQux = new Option("q", "qux", true, "Set qux [arg]");
        options.addOption(optionQux);

        Parser parser = new DefaultParser();
        parser.setOptions(options);
        CommandLine cmd = parser.parse(options, args, null, false);

        Assert.fail("Unrecognized option exception expected");
    }
}