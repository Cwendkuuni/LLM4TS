import org.apache.commons.cli2.CommandLine;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

    private WriteableCommandLineImpl commandLine;
    private Option option;

    @Before
    public void setUp() {
        option = new Option("o", "option", true, "an option");
        commandLine = new WriteableCommandLineImpl(new Option("root"), new ArrayList<>());
        commandLine.addOption(option);
    }

    @Test
    public void testAddOption() {
        WriteableCommandLineImpl writeableCommandLine = (WriteableCommandLineImpl) commandLine;
        writeableCommandLine.addOption(option);
        assertTrue(writeableCommandLine.hasOption(option));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddOptionTwice() {
        WriteableCommandLineImpl writeableCommandLine = (WriteableCommandLineImpl) commandLine;
        writeableCommandLine.addOption(option);
        writeableCommandLine.addOption(option);
    }

    @Test
    public void testAddValue() {
        WriteableCommandLineImpl writeableCommandLine = (WriteableCommandLineImpl) commandLine;
        writeableCommandLine.addValue(option, "value");
        List values = writeableCommandLine.getValues(option, null);
        assertEquals("value", values.get(0));
    }

    @Test(expected = IllegalStateException.class)
    public void testAddValueToNonArgumentOption() {
        WriteableCommandLineImpl writeableCommandLine = (WriteableCommandLineImpl) commandLine;
        Option nonArgumentOption = new Option("n", "no-argument");
        writeableCommandLine.addOption(nonArgumentOption);
        writeableCommandLine.addValue(nonArgumentOption, "value");
    }

    @Test
    public void testAddSwitch() {
        WriteableCommandLineImpl writeableCommandLine = (WriteableCommandLineImpl) commandLine;
        writeableCommandLine.addSwitch(option, true);
        Boolean switchValue = writeableCommandLine.getSwitch(option, false);
        assertTrue(switchValue);
    }

    @Test(expected = IllegalStateException.class)
    public void testAddSwitchTwice() {
        WriteableCommandLineImpl writeableCommandLine = (WriteableCommandLineImpl) commandLine;
        writeableCommandLine.addSwitch(option, true);
        writeableCommandLine.addSwitch(option, true);
    }
}