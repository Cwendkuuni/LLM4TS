import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Test for CommandLine class
 */
public class CommandLineTest {

    private CommandLine commandLine;
    private Options options;

    @Before
    public void setup() {
        options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 1, "option B with an integer value");
        options.addOption("c", 3, "option C with an integer value");
        options.addOption("d", 4, "option D with an integer value");
        options.addOption("e", "value", "option E with a string value");
        options.addOption("f", null, "option F with no argument");
        options.addOption("h", "help", "option H with a required argument");
    }

    /**
     * Test method for {@link CommandLine#CommandLine()} .
     */
    @Test
    public void testConstructor() {
        commandLine = new CommandLine();
        // Nothing to test here
    }

    /**
     * Test method for {@link CommandLine#hasOption(String)} .
     */
    @Test
    public void testHasOption() {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"-a", "-b", "-h", "help"});

        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption('a'));
        assertTrue(commandLine.hasOption("h"));
        assertTrue(commandLine.hasOption('h'));

        assertFalse(commandLine.hasOption("c"));
        assertFalse(commandLine.hasOption('c'));
        assertFalse(commandLine.hasOption("e"));
        assertFalse(commandLine.hasOption('e'));
        assertFalse(commandLine.hasOption("f"));
        assertFalse(commandLine.hasOption('f'));
        assertFalse(commandLine.hasOption("g"));
        assertFalse(commandLine.hasOption('g'));
    }

    /**
     * Test method for {@link CommandLine#getOptionObject(String)} .
     */
    @Test
    public void testGetOptionObject() {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"-a", "--d", "4"});

        assertNull(commandLine.getOptionObject("a"));
        assertNull(commandLine.getOptionObject('a'));

        assertEquals(Integer.valueOf(4), commandLine.getOptionObject("d"));
        assertEquals(Integer.valueOf(4), commandLine.getOptionObject('d'));

        // Test for option with a string value
        commandLine = parser.parse(options, new String[]{"-e", "testValue"});
        assertEquals("testValue", commandLine.getOptionObject("e"));
        assertEquals("testValue", commandLine.getOptionObject('e'));

        // Test for option with no value
        commandLine = parser.parse(options, new String[]{"-f"});
        assertNull(commandLine.getOptionObject("f"));
        assertNull(commandLine.getOptionObject('f'));
    }

    /**
     * Test method for {@link CommandLine#getOptionValue(String)} .
     */
    @Test
    public void testGetOptionValue() {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"-a", "--h", "helpValue"});

        assertNull(commandLine.getOptionValue("a"));
        assertNull(commandLine.getOptionValue('a'));

        assertNull(commandLine.getOptionValue("c"));
        assertNull(commandLine.getOptionValue('c'));

        assertNull(commandLine.getOptionValue("e"));
        assertNull(commandLine.getOptionValue('e'));

        assertEquals("helpValue", commandLine.getOptionValue("h"));
        assertEquals("helpValue", commandLine.getOptionValue('h'));

        // Test for option with a string value
        commandLine = parser.parse(options, new String[]{"-e", "testValue"});
        assertEquals("testValue", commandLine.getOptionValue("e"));
        assertEquals("testValue", commandLine.getOptionValue('e'));

        // Test for option with no value
        commandLine = parser.parse(options, new String[]{"-f"});
        assertNull(commandLine.getOptionValue("f"));
        assertNull(commandLine.getOptionValue('f'));
    }

    /**
     * Test method for {@link CommandLine#getOptionValues(String)} .
     */
    @Test
    public void testGetOptionValues() {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"-h", "value1", "value2"});

        String[] values = commandLine.getOptionValues("h");
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);

        values = commandLine.getOptionValues("i");
        assertNull(values);

        values = commandLine.getOptionValues("a");
        assertNull(values);
    }

    /**
     * Test method for {@link CommandLine#getOptionValues(char)} .
     */
    @Test
    public void testGetOptionValuesChar() {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"-h", "value1", "value2"});

        String[] values = commandLine.getOptionValues('h');
        assertEquals(2, values.length);
        assertEquals("value1", values[0]);
        assertEquals("value2", values[1]);

        values = commandLine.getOptionValues('i');
        assertNull(values);

        values = commandLine.getOptionValues('a');
        assertNull(values);
    }

    /**
     * Test method for {@link CommandLine#getArgs()} .
     */
    @Test
    public void testGetArgs() {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"arg1", "-a", "-h", "helpValue"});

        String[] args = commandLine.getArgs();
        List expectedArgs = Arrays.asList("arg1");
        assertEquals(expectedArgs, Arrays.asList(args));
    }

    /**
     * Test method for {@link CommandLine#getArgList()} .
     */
    @Test
    public void testGetArgList() {
        CommandLineParser parser = new DefaultParser();
        commandLine = parser.parse(options, new String[]{"arg1", "-a", "-h", "helpValue"});

        List args = commandLine.getArgList();
        List expectedArgs = Arrays.asList("arg1");
        assertEquals(expectedArgs, args);
    }
}