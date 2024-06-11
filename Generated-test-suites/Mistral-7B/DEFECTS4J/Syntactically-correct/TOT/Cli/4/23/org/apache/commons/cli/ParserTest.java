import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.NoSuchOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testParseSimpleOptions() throws ParseException {
        String[] args = {"-a", "1", "-b", "2"};
        Options options = new Options();
        Option optA = new Option("a", true, "a help message for option a");
        Option optB = new Option("b", true, "a help message for option b");

        options.addOption(optA);
        options.addOption(optB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("a"));
        assertEquals("2", cmd.getOptionValue("b"));
    }

    @Test(expected = MissingOptionException.class)
    public void testMissingRequiredOption() throws ParseException {
        String[] args = {"-c"};
        Options options = new Options();
        Option optA = new Option("a", true, true, "a help message for option a");
        Option optB = new Option("b", true, true, "a help message for option b");
        Option optC = new Option("c", true, true, "a help message for option c");
        optC.setRequired(true);

        options.addOption(optA);
        options.addOption(optB);
        options.addOption(optC);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        fail("MissingRequiredOptionException expected");
    }

    @Test(expected = MissingArgumentException.class)
    public void testMissingOptionalArgument() throws ParseException {
        String[] args = {"-d"};
        Options options = new Options();
        Option optD = new Option("d", true, "a help message for option d");

        options.addOption(optD);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        fail("MissingArgumentException expected");
    }

    @Test(expected = NoSuchOptionException.class)
    public void testUnrecognizedOption() throws ParseException {
        String[] args = {"-e"};
        Options options = new Options();
        Option optA = new Option("a", true, "a help message for option a");

        options.addOption(optA);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        fail("NoSuchOptionException expected");
    }

    @Test
    public void testParseLongOptions() throws ParseException {
        String[] args = {"--long-option-a", "1", "--long-option-b=2"};
        Options options = new Options();
        Option optLongOptionA = new Option("longOptionA", true, "a help message for long option A");
        Option optLongOptionB = new Option("longOptionB", true, "a help message for long option B");

        options.addOption(optLongOptionA);
        options.addOption(optLongOptionB);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOptionA"));
        assertTrue(cmd.hasOption("longOptionB"));
        assertEquals("1", cmd.getOptionValue("longOptionA"));
        assertEquals("2", cmd.getOptionValue("longOptionB"));
    }

    @Test
    public void testParseWithProperties() throws ParseException {
        String[] args = {"--property1=value1", "--property2=value2"};
        Properties properties = new Properties();
        properties.setProperty("property1", "value1");
        properties.setProperty("property2", "value2");

        Options options = new Options();
        Option propOption1 = new Option("p", false, "a help message for property option 1");
        Option propOption2 = new Option("p", false, "a help message for property option 2");
        propOption1.setLongOpt("property1");
        propOption2.setLongOpt("property2");

        options.addOption(propOption1);
        options.addOption(propOption2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, properties, false);

        assertTrue(cmd.hasOption("property1"));
        assertTrue(cmd.hasOption("property2"));
        assertEquals("value1", cmd.getOptionValue("property1"));
        assertEquals("value2", cmd.getOptionValue("property2"));
    }

    @Test
    public void testParseWithMixedArgumentsAndOptions() throws ParseException {
        String[] args = {"--long-option1", "arg1", "arg2", "--long-option2"};
        Options options = new Options();
        Option longOption1 = new Option("lo1", false, "a help message for long option 1");
        longOption1.setLongOpt("longOption1");
        Option longOption2 = new Option("lo2", false, "a help message for long option 2");
        longOption2.setLongOpt("longOption2");

        options.addOption(longOption1);
        options.addOption(longOption2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args, null, false);

        assertTrue(cmd.hasOption("longOption1"));
        assertTrue(cmd.hasOption("longOption2"));
        assertEquals(2, cmd.getArgs().size());
        assertEquals("arg1", cmd.getArgs().get(0));
        assertEquals("arg2", cmd.getArgs().get(1));
    }
}