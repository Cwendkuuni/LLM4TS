import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("a", "optionA"));
        assertTrue(commandLine.hasOption("a"));
        assertTrue(commandLine.hasOption('a'));
        assertFalse(commandLine.hasOption("b"));
        assertFalse(commandLine.hasOption('b'));
    }
}