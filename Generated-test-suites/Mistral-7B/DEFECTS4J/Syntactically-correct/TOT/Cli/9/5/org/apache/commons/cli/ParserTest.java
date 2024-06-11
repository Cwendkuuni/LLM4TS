import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import org.junit.Assert;

public class ParserTest {

    @Test
    public void testParseWithNoOptionsOrArguments() throws ParseException {
        // given
        Options options = new Options();
        Option noArgOpt = new Option("a", true, false, "no argument option");
        options.addOption(noArgOpt);
        CommandLineParser parser = new DefaultParser();

        // when
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), new String[0]);

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("a"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("a"));

        // verify
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
        System.out.println(formatter.getHelpText());
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        // given
        Options options = new Options();
        Option reqOpt1 = new Option("r1", true, true, "required option 1");
        Option reqOpt2 = new Option("r2", true, true, "required option 2");
        options.addOption(reqOpt1);
        options.addOption(reqOpt2);

        // when
        String[] args = {"--r1", "--r2", "--help"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), args);

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("r1"));
        Assert.assertTrue(cmd.hasOption("r2"));
        Assert.assertFalse(cmd.hasOption("b"));
        Assert.assertNull(cmd.getOptionValue("r1"));
        Assert.assertNull(cmd.getOptionValue("r2"));

        // verify
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
        System.out.println(formatter.getHelpText());
    }

    @Test(expected = MissingOptionException.class)
    public void testParseWithMissingRequiredOption() throws ParseException {
        // given
        Options options = new Options();
        Option reqOpt = new Option("r", true, true, "required option");
        options.addOption(reqOpt);

        // when
        String[] args = new String[0];
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), args);

        // then
        // Assert.fail("Test should fail with MissingOptionException");
        parser.parse(options.toArray(new Option[0]), args);
    }

    @Test
    public void testParseWithOptionalArgument() throws ParseException {
        // given
        Options options = new Options();
        Option optArgOpt = new Option("o", true, false, "optional argument");
        options.addOption(optArgOpt);

        // when
        String[] args = {"--o", "value"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), args);

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("o"));
        Assert.assertEquals("value", cmd.getOptionValue("o"));

        // when
        String[] args2 = {"--o"};
        CommandLine cmd2 = parser.parse(options.toArray(new Option[0]), args2);

        // then
        Assert.assertEquals(0, cmd2.getArgs().size());
        Assert.assertTrue(cmd2.hasOption("o"));
        Assert.assertNull(cmd2.getOptionValue("o"));

        // verify
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
        System.out.println(formatter.getHelpText());
    }

    @Test(expected = MissingArgumentException.class)
    public void testParseWithMissingArgument() throws ParseException {
        // given
        Options options = new Options();
        Option optArgOpt = new Option("o", true, false, "optional argument");
        options.addOption(optArgOpt);

        // when
        String[] args = {"--o"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), args);

        // then
        // Assert.fail("Test should fail with MissingArgumentException");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        // given
        Options options = new Options();
        Option opt1 = new Option("o1", true, false, "option1");
        Option opt2 = new Option("o2", true, false, "option2");
        options.addOption(opt1);
        options.addOption(opt2);

        // when
        String[] args = {"--b", "--o1", "arg1", "--o3"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), args);

        // then
        // Assert.fail("Test should fail with UnrecognizedOptionException");
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        // given
        Options options = new Options();
        Option longOpt = new Option("longOpt", true, false, "long option");
        options.addOption(longOpt);

        // when
        String[] args = {"--longOpt", "arg1"};
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.toArray(new Option[0]), args);

        // then
        Assert.assertEquals(0, cmd.getArgs().size());
        Assert.assertTrue(cmd.hasOption("longOpt"));
        Assert.assertEquals("arg1", cmd.getOptionValue("longOpt"));

        // verify
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp("test", options);
        System.out.println(formatter.getHelpText());
    }
}