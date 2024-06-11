import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class CommandLineTest {

    @Test
    public void testEmptyCommandLine() {
        CommandLine commandLine = new CommandLine();
        assertNull(commandLine.getOptionValue("option1"));
        assertFalse(commandLine.hasOption("option1"));
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testSimpleOption() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option value");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, new String[]{"-o", "value"});
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        assertEquals("value", commandLine.getOptionValue("o"));
        assertEquals("value", commandLine.getOptionValue('o'));
        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.hasOption('o'));
        assertEquals(0, commandLine.getArgs().length);
    }

    @Test
    public void testOptionWithArg() {
        Options options = new Options();
        options.addOption("o", "option", true, "Option value");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;
        try {
            commandLine = parser.parse(options, new String[]{"-o", "value1", "value2"});
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        String[] args = commandLine.getArgs();
        assertEquals(1, args.length);
        assertEquals("value2", args[0]);
        assertEquals("value1", commandLine.getOptionValue("o"));
        assertEquals("value1", commandLine.getOptionValue('o'));
        assertTrue(commandLine.hasOption("o"));
        assertTrue(commandLine.hasOption('o'));
    }

    @Test(expected = ParseException.class)
    public void testMissingOption() {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, new String[]{"--non-existent"});
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNullOption() {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        parser.parse(options, (String[]) null);
    }

    @Test
    public void testUnrecognizedOption() {
        Options options = new Options();
        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(options, new String[]{"--unknown"});
        } catch (ParseException e) {
            commandLine = e.getParsedCommandLine();
        }

        String[] args = commandLine.getArgs();
        assertEquals(1, args.length);
        assertEquals("--unknown", args[0]);
        assertFalse(commandLine.hasOption("option"));
        assertFalse(commandLine.hasOption('o'));
    }

    @Test
    public void testHelp() {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this message.");

        CommandLineParser parser = new DefaultParser();
        CommandLine commandLine;

        try {
            commandLine = parser.parse(options, new String[]{"--help"});
        } catch (ParseException e) {
            HelpFormatter formatter = new HelpFormatter();
            formatter.printHelp("javac", options);
            assertEquals(formatter.getHelpMessage(), commandLine.getArgList().toString());
        }

        String[] args = commandLine.getArgs();
        assertNull(args);
        assertFalse(commandLine.hasOption("h"));
        assertFalse(commandLine.hasOption('h'));
    }
}