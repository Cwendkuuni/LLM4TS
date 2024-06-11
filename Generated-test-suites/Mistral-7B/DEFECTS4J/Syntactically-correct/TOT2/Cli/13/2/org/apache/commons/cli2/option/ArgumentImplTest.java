import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.OptionPrinter;
import org.apache.commons.cli2.TestUtils;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ArgumentImplTest {

    private Argument arg;
    private CommandLine cmd;

    @Before
    public void setUp() {
        arg = new ArgumentImpl("arg", "description", 1, 1, '-', ' ', null, "default", null, null, 0);
        cmd = new TestableCommandLine();
    }

    @Test
    public void testBasicUsage() throws OptionException {
        arg.process(cmd, new String[]{"arg", "value"});
        assertEquals("value", cmd.getValue(arg));
    }

    @Test
    public void testMinimumUsage() throws OptionException {
        arg.process(cmd, new String[]{"arg"});
        assertEquals(1, cmd.getValues(arg).size());
    }

    @Test(expected = OptionException.class)
    public void testMaximumUsage() throws OptionException {
        arg.process(cmd, new String[]{"arg", "value1", "value2"});
        assertEquals(2, cmd.getValues(arg).size());
    }

    @Test(expected = OptionException.class)
    public void testInvalidInput() throws OptionException {
        arg.process(cmd, new String[]{"arg", "invalidInput"});
        fail("Should have failed with OptionException");
    }

    @Test
    public void testConsumeRemaining() throws OptionException {
        arg = new ArgumentImpl("arg", "description", 1, 1, '-', ' ', null, "default", null, null, 0);
        arg.setConsumeRemaining(true);

        String[] args = {"arg", "--", "value1", "value2"};
        arg.process(cmd, args);
        assertEquals(2, cmd.size());
        assertEquals("value1", cmd.getValue(arg));
        assertEquals("value2", cmd.getValue(new ArgumentImpl("otherArg", null, 0, 1, '-', ' ', null, null, null, 1)));
    }

    @Test
    public void testHelp() {
        HelpFormatter formatter = new HelpFormatter();
        arg.appendUsage(TestUtils.getPrintWriter(System.out), null, null);
        formatter.printHelp("Test", "", arg.getHelpLines(0, null, null), true);
    }
}

public class TestableCommandLine extends WriteableCommandLine {
    @Override
    public void addValue(Option option, String value) {
        super.addValue(option, value);
        values.add(value);
    }
}