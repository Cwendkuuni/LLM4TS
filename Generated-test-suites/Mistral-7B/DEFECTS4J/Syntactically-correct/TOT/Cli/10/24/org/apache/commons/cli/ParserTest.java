import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {

    @Test
    public void testSimpleParse() throws ParseException {
        String[] args = {"-a", "1", "--optionB", "2"};
        Options options = new Options();
        options.addOption("a", true, "help a");
        options.addOption("b", false, "help b");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.getOptionValue("a").equals("1"));
        assertTrue(cmd.hasOption("b"));
        assertNull(cmd.getOptionValue("b"));
    }

    @Test
    public void testParseWithRequiredOptions() throws ParseException {
        String[] args = {"--optionA", "optionB"};
        Options options = new Options();
        options.addOption("a", true, "help a").required(true);
        options.addOption("b", false, "help b");

        CommandLineParser parser = new DefaultParser();

        try {
            CommandLine cmd = parser.parse(options, args);
            fail();
        } catch (MissingOptionException e) {
            String message = e.getMessage();
            assertTrue(message.contains("Missing required option"));
            assertTrue(message.contains("optionA"));
        }
    }

    @Test
    public void testParseWithOptionalOptions() throws ParseException {
        String[] args = {"--optionA", "valueA", "--optionB", "valueB"};
        Options options = new Options();
        options.addOption("a", true, "help a");
        options.addOption("b", true, "help b");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals(cmd.getOptionValue("a"), "valueA");
        assertTrue(cmd.hasOption("b"));
        assertEquals(cmd.getOptionValue("b"), "valueB");
    }

    @Test
    public void testParseWithLongOptions() throws ParseException {
        String[] args = {"--longOptionA", "valueA", "--long-optionB", "valueB"};
        Options options = new Options();
        options.addOption("longOptionA", true, "help long option A");
        options.addOption("longOptionB", true, "help long option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("longOptionA"));
        assertEquals(cmd.getOptionValue("longOptionA"), "valueA");
        assertTrue(cmd.hasOption("longOptionB"));
        assertEquals(cmd.getOptionValue("longOptionB"), "valueB");
    }

    @Test
    public void testParseWithArgument() throws ParseException {
        String[] args = {"--optionA", "valueA", "valueB"};
        Options options = new Options();
        options.addOption("a", true, "help a").hasArgs(1);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        assertEquals(cmd.getOptionValue("a"), "valueB");
    }

    @Test
    public void testParseWithMultipleArguments() throws ParseException {
        String[] args = {"--optionA", "valueA1", "valueA2"};
        Options options = new Options();
        options.addOption("a", true, "help a").hasArgs(2);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);

        assertTrue(cmd.hasOption("a"));
        String[] values = cmd.getOptionValues("a");
        assertEquals(values[0], "valueA1");
        assertEquals(values[1], "valueA2");
    }

    @Test(expected = UnrecognizedOptionException.class)
    public void testParseWithUnrecognizedOption() throws ParseException {
        String[] args = {"--unknownOption", "value"};
        Options options = new Options();
        options.addOption("a", true, "help a");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, args);
    }
}