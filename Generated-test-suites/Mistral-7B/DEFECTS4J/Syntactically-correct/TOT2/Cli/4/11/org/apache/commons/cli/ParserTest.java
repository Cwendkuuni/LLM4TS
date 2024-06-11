import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

/**
 * Test class for Parser.
 */
public class ParserTest {

    /**
     * Test parse method with valid options and arguments.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseValidOptionsAndArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", 3, "Option C");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.parse(new String[]{"-a", "--b", "1", "2", "3"}));

        assertNotNull(cmd);
        assertEquals(3, cmd.getArgs().size());
        assertTrue(cmd.hasOption("a"));
        assertTrue(cmd.hasOption("b"));
        assertEquals("1", cmd.getOptionValue("c"));
    }

    /**
     * Test parse method with missing required option.
     */
    @Test(expected = MissingOptionException.class)
    public void testParseMissingRequiredOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.parse(new String[]{"-a"}));
    }

    /**
     * Test parse method with invalid option.
     */
    @Test(expected = UnrecognizedOptionException.class)
    public void testParseInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.parse(new String[]{"-z"}));
    }

    /**
     * Test parse method with missing argument for an option.
     */
    @Test(expected = MissingArgumentException.class)
    public void testParseMissingArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.parse(new String[]{"-a"}));
    }

    /**
     * Test parse method with extra arguments.
     */
    @Test
    public void testParseExtraArguments() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.parse(new String[]{"-a", "1", "2", "3"}));

        assertEquals(2, cmd.getArgs().size());
    }

    /**
     * Test parse method with properties.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseProperties() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Properties properties = new Properties();
        properties.setProperty("a", "Value A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, properties.stringPropertyNames(), new String[]{"--a"});

        assertEquals("Value A", cmd.getOptionValue("a"));
    }

    /**
     * Test parse method with stopAtNonOption.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseStopAtNonOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.parse(new String[]{"-a", "-"}));

        assertEquals(1, cmd.getArgs().size());
        assertTrue(cmd.getArgs().get(0).startsWith("-"));
    }

    /**
     * Test parse method with an option group and required options.
     *
     * @throws ParseException if an error occurs during parsing.
     */
    @Test
    public void testParseOptionGroupAndRequiredOptions() throws ParseException {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Option Group");
        group.addOption(new Option("a", true, "Option A"));
        group.addOption(new Option("b", true, "Option B"));
        group.addRequiredOption(new Option("c", true, "Option C"));
        options.addOptionGroup(group);

        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options.parse(new String[]{"--group", "-c", "-a"}));

        assertEquals(1, cmd.getArgs().size());
        assertTrue(cmd.hasOption("c"));
        assertTrue(cmd.hasOption("a"));
    }
}