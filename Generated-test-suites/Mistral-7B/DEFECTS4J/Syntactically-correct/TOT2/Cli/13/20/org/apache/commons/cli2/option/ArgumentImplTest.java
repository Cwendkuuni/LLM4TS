import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class ArgumentImplTest {

    private CommandLine cmd;
    private Argument arg;

    @Before
    public void setUp() {
        arg = new ArgumentImpl("arg", "an argument", 0, Integer.MAX_VALUE, 'a', 'b', null, null, null, 0);
    }

    @Test
    public void testBasicUsage() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"arg1", "arg2"});
        arg.process(cmd, cmd.getArgs().listIterator());

        assertEquals(2, cmd.getValues(arg).size());
        assertEquals("arg1", cmd.getValues(arg).get(0));
        assertEquals("arg2", cmd.getValues(arg).get(1));
    }

    @Test(expected = OptionException.class)
    public void testInvalidUsage1() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"arg1"});
        arg.process(cmd, cmd.getArgs().listIterator());

        assertEquals(1, cmd.getValues(arg).size());
        assertEquals("arg1", cmd.getValues(arg).get(0));

        arg.process(cmd, cmd.getArgs().listIterator());

        throw new OptionException(arg, "Invalid usage");
    }

    @Test(expected = OptionException.class)
    public void testInvalidUsage2() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"arg1", "arg2arg3"});

        arg.process(cmd, cmd.getArgs().listIterator());
        arg.process(cmd, cmd.getArgs().listIterator());

        throw new OptionException(arg, "Invalid usage");
    }

    @Test
    public void testEmptyArgumentList() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{});
        arg.process(cmd, cmd.getArgs().listIterator());

        assertNull(cmd.getValues(arg));
    }

    @Test(expected = OptionException.class)
    public void testMinimumNumberOfArguments() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"arg1"});
        arg.process(cmd, cmd.getArgs().listIterator());

        throw new OptionException(arg, "Missing argument value");
    }

    @Test(expected = OptionException.class)
    public void testMaximumNumberOfArguments() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"arg1", "arg2", "arg3", "arg4"});
        arg.process(cmd, cmd.getArgs().listIterator());

        throw new OptionException(arg, "Too many argument values");
    }

    @Test
    public void testConsumingArguments() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"--consume"});
        arg.process(cmd, cmd.getArgs().listIterator());

        assertEquals(0, cmd.getArgs().size());
    }

    @Test(expected = OptionException.class)
    public void testInvalidInitialSeparator() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"--arg:arg1"});
        arg = new ArgumentImpl("arg", "an argument", 0, Integer.MAX_VALUE, ':', 'b', null, null, null, 0);

        arg.process(cmd, cmd.getArgs().listIterator());

        throw new OptionException(arg, "Invalid argument separator");
    }

    @Test(expected = OptionException.class)
    public void testInvalidSubsequentSeparator() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"--arg:arg1:arg2"});
        arg = new ArgumentImpl("arg", "an argument", 0, Integer.MAX_VALUE, ':', ':', null, null, null, 0);

        arg.process(cmd, cmd.getArgs().listIterator());

        throw new OptionException(arg, "Invalid argument separator");
    }

    @Test
    public void testEmptyDescription() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"arg1"});
        arg = new ArgumentImpl("arg", "", 0, Integer.MAX_VALUE, 'a', 'b', null, null, null, 0);

        arg.process(cmd, cmd.getArgs().listIterator());

        assertEquals(1, cmd.getValues(arg).size());
        assertEquals("arg1", cmd.getValues(arg).get(0));
    }

    @Test
    public void testMinimumAndMaximumValues() {
        cmd = TestUtils.createCommandLine("junit.test", new String[]{"arg1", "arg2"});
        arg = new ArgumentImpl("arg", "an argument", 1, 2, 'a', 'b', null, null, null, 0);

        arg.process(cmd, cmd.getArgs().listIterator());
        arg.process(cmd, cmd.getArgs().listIterator());

        assertEquals(2, cmd.getValues(arg).size());
        assertEquals("arg1", cmd.getValues(arg).get(0));
        assertEquals("arg2", cmd.getValues(arg).get(1));
    }

    // Add test cases for edge cases and exceptions here
}