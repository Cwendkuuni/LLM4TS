import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class CommandLineTest {

    private CommandLine commandLine;
    private Options options;

    @Before
    public void setUp() throws ParseException {
        options = new Options();
        options.addOption("a", "optionA", true, "Option A");
        options.addOption("b", "optionB", false, "Option B");
        options.addOption("c", "optionC", 1, "Option C with an integer argument");
        commandLine = new CommandLine();
        commandLine.parse(options, new String[]{"-a", "1", "-b", "-c", "3"});
    }

    @After
    public void tearDown() {
        commandLine = null;
        options = null;
    }

    @Test
    public void testHasOption() {
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption("d"));
    }

    @Test
    public void testGetOptionValue() {
        assertSame("1", commandLine.getOptionValue("a"));
        assertTrue(commandLine.getOptionValue("b") == null);
    }

    @Test
    public void testGetOptionValues() {
        assertArrayEquals(new String[]{"3"}, commandLine.getOptionValues("c"));
        assertNull(commandLine.getOptionValues("d"));
    }

    @Test
    public void testGetArgList() {
        List args = commandLine.getArgList();
        assertArrayEquals(new String[]{}, args.toArray());
    }

    @Test
    public void testGetArgs() {
        String[] args = commandLine.getArgs();
        assertArrayEquals(new String[]{}, args);
    }

    @Test
    public void testGetOptionObject() {
        Option optionA = options.getOption("a");
        Option optionC = options.getOption("c");
        assertSame(optionA, commandLine.getOptionObject("a"));
        assertSame(optionC, commandLine.getOptionObject("c"));
        assertNull(commandLine.getOptionObject("d"));
    }

    @Test
    public void testGetOptionObjectWithType() {
        Option optionC = options.getOption("c");
        assertSame(optionC, commandLine.getOptionObject("c", new Integer(1)));
        assertNull(commandLine.getOptionObject("d", new Integer(1)));
    }

    @Test(expected = NullPointerException.class)
    public void testGetOptionObjectWithNullOption() {
        commandLine.getOptionObject(null);
    }
}