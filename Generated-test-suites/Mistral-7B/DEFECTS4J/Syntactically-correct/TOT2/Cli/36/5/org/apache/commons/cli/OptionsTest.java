import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;

public class OptionsTest {

    @Test
    public void testAddOption() {
        Options opts = new Options();
        Option option = new Option("o", "option", false, "an option");
        opts.addOption(option);
        assertTrue(opts.hasOption("o"));
        assertTrue(opts.hasOption("option"));
    }

    @Test
    public void testAddOptionWithArgument() {
        Options opts = new Options();
        Option option = new Option("i", "inputFile", true, "input file");
        opts.addOption(option);
        assertTrue(opts.hasOption("i"));
        assertTrue(opts.hasOption("inputFile"));
    }

    @Test
    public void testAddOptionGroup() {
        Options opts = new Options();
        OptionGroup group = new OptionGroup("g", "group");
        Option option = new Option("a", "optionA", false, "an option A");
        group.addOption(option);
        opts.addOptionGroup(group);
        assertTrue(opts.hasOption("g"));
        assertTrue(opts.hasOption("group"));
        assertTrue(opts.hasOption("a"));
        assertTrue(opts.hasOption("optionA"));
    }

    @Test
    public void testParseOptions() throws ParseException {
        Options opts = new Options();
        Option option = new Option("i", "inputFile", true, "input file");
        opts.addOption(option);
        CommandLineParser parser = new DefaultParser();
        String[] args = {"-i", "test.txt"};
        CommandLine cmd = parser.parse(opts, args);
        assertEquals("test.txt", cmd.getOptionValue("inputFile"));
    }

    @Test(expected = ParseException.class)
    public void testParseOptionsWithMissingOption() throws ParseException {
        Options opts = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {};
        parser.parse(opts, args);
        fail("Expected ParseException but no exception was thrown.");
    }

    @Test(expected = ParseException.class)
    public void testParseOptionsWithInvalidOption() throws ParseException {
        Options opts = new Options();
        CommandLineParser parser = new DefaultParser();
        String[] args = {"--invalidOption"};
        parser.parse(opts, args);
        fail("Expected ParseException but no exception was thrown.");
    }
}