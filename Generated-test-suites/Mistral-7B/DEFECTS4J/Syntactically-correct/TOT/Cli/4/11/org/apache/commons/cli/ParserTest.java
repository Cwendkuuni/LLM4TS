import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Test;

import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test class for Parser.
 */
public class ParserTest {

    /**
     * Test parsing command line arguments with no options or arguments.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testEmpty() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[0]);
        assertEquals(new CommandLine(), cmd);
    }

    /**
     * Test parsing command line arguments with only options.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testOptions() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--a", "--b"});
        Option aOption = options.getOption("a");
        Option bOption = options.getOption("b");
        assertEquals(true, aOption.isSet());
        assertEquals(true, bOption.isSet());
        assertEquals(new String[0], cmd.getArgs());
    }

    /**
     * Test parsing command line arguments with only arguments.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testArgs() throws Exception {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"arg1", "arg2"});
        assertEquals(new String[]{"arg1", "arg2"}, cmd.getArgs());
        assertEquals(new CommandLine(), cmd.getOptions());
    }

    /**
     * Test parsing command line arguments with options and arguments.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testOptionsAndArgs() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", 1, "Option C");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--a", "arg1", "--b", "arg2", "--c", "arg3"});
        Option aOption = options.getOption("a");
        Option bOption = options.getOption("b");
        Option cOption = options.getOption("c");
        assertEquals(true, aOption.isSet());
        assertEquals(true, bOption.isSet());
        assertEquals("arg1", aOption.getValue());
        assertEquals(true, cOption.isSet());
        assertEquals("arg3", cOption.getValue());
        assertEquals(new String[]{"arg2"}, cmd.getArgs());
    }

    /**
     * Test parsing command line arguments with required options.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testRequiredOptions() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        options.addRequiredOption("c", true, "Option C");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, new String[]{"--a", "--b"});
            fail("MissingRequiredOptionException expected");
        } catch (MissingOptionException e) {
            // Expected
        }
        try {
            CommandLine cmd = parser.parse(options, new String[]{"--a", "--b", "--c", "arg"});
            Option cOption = options.getOption("c");
            assertEquals(true, cOption.isSet());
            assertEquals("arg", cOption.getValue());
            assertEquals(new String[]{"--a"}, cmd.getArgs());
        } catch (Exception e) {
            fail("Unexpected exception");
        }
    }

    /**
     * Test parsing command line arguments with a group of options.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testOptionGroup() throws Exception {
        Options options = new Options();
        OptionGroup group = new OptionGroup("group", "Group Options");
        group.addOption("a", true, "Option A");
        group.addOption("b", true, "Option B");
        options.addOptionGroup(group);
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--group", "--a"});
        Option aOption = group.getOption("a");
        assertEquals(true, aOption.isSet());
        assertEquals(new String[0], cmd.getArgs());
    }

    /**
     * Test parsing command line arguments with invalid options.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testInvalidOptions() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        CommandLineParser parser = new DefaultParser();
        try {
            CommandLine cmd = parser.parse(options, new String[]{"--b"});
            fail("UnrecognizedOptionException expected");
        } catch (UnrecognizedOptionException e) {
            // Expected
        }
        try {
            CommandLine cmd = parser.parse(options, new String[]{"--a", "--b"});
            fail("MultipleUnrecognizedOptionException expected");
        } catch (ParseException e) {
            // Expected
        }
    }

    /**
     * Test parsing command line arguments with specified help.
     * @throws IOException if an error occurs during testing.
     */
    @Test
    public void testHelp() throws IOException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--help"});
        HelpFormatter formatter = new HelpFormatter();
        String help = formatter.printHelp("Test", options);
        System.out.println(help);
    }

    /**
     * Test parsing command line arguments with a mixture of valid and
     * invalid options.
     * @throws Exception if an error occurs during testing.
     */
    @Test
    public void testMixedOptions() throws Exception {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", true, "Option B");
        CommandLineParser parser = new DefaultParser();
        CommandLine cmd = parser.parse(options, new String[]{"--a", "--b", "--c"});
        Option aOption = options.getOption("a");
        Option bOption = options.getOption("b");
        Option cOption = null;
        try {
            cOption = options.getOption("c");
        } catch (UnrecognizedOptionException e) {
            // Expected
        }
        assertEquals(true, aOption.isSet());
        assertEquals(true, bOption.isSet());
        assertEquals(false, cOption == null);
        assertEquals(new String[0], cmd.getArgs());
    }
}