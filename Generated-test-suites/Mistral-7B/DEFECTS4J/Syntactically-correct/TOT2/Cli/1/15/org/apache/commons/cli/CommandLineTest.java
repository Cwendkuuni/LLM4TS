import org.junit.Test;
import static org.junit.Assert.*;

public class CommandLineTest {

    @Test
    public void testHasOption() {
        CommandLine commandLine = new CommandLine();
        commandLine.addOption(new Option("opt", true, "Optional option"));
        assertTrue(commandLine.hasOption("opt"));
    }

    @Test
    public void testHasOptionFalse() {
        CommandLine commandLine = new CommandLine();
        assertFalse(commandLine.hasOption("nonexistent"));
    }
}