import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the Parser abstract class.
 */
public class ParserTest {

    @Test
    public void testParseOptionsWithNoArguments() throws ParseException {
        // given
        String[] arguments = new String[]{"-a", "--optionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, arguments, false);

        // then
        Assert.assertEquals(2, cmd.getNumArgs());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test
    public void testParseOptionsWithRequiredArguments() throws ParseException {
        // given
        String[] arguments = new String[]{"--requiredOptionA=valueA", "--requiredOptionB=valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A").required(true);
        options.addOption("b", true, "Option B").required(true);

        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, arguments, false);

        // then
        Assert.assertEquals(0, cmd.getArgs().length);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("valueA", cmd.getOptionValue("a"));
        Assert.assertEquals("valueB", cmd.getOptionValue("b"));
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseOptionsWithMissingRequiredArgument() throws ParseException {
        // given
        String[] arguments = new String[]{"-a", "--requiredOptionA"};
        Options options = new Options();
        options.addOption("a", true, "Option A").required(true);
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        // when & then (expecting an exception)
        parser.parse(options, arguments, false);
    }

    @Test(expected = MissingOptionException.class)
    public void testParseOptionsWithMissingOption() throws ParseException {
        // given
        String[] arguments = new String[]{"--unknownOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();

        // when & then (expecting an exception)
        parser.parse(options, arguments, false);
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseOptionsWithUnrecognizedOption() throws ParseException {
        // given
        String[] arguments = new String[]{"--unrecognizedOption"};
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();

        // when & then (expecting an exception)
        parser.parse(options, arguments, false);
    }

    @Test
    public void testParseOptionsWithDashArg() throws ParseException {
        // given
        String[] arguments = new String[]{"-a", "--optionB=-valueB"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, arguments, false);

        // then
        Assert.assertEquals(1, cmd.getArgs().length);
        Assert.assertEquals("-valueB", cmd.getArgs()[0]);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
    }

    @Test
    public void testParseOptionsWithDashArgAndOtherArgs() throws ParseException {
        // given
        String[] arguments = new String[]{"-a", "--optionB=-valueB", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options, arguments, false);

        // then
        Assert.assertEquals(2, cmd.getArgs().length);
        Assert.assertEquals("arg1", cmd.getArgs()[0]);
        Assert.assertEquals("arg2", cmd.getArgs()[1]);
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertTrue(cmd.hasOption("b"));
        Assert.assertEquals("-valueB", cmd.getOptionValue("b"));
    }
}