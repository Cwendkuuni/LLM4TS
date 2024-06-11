import org.apache.commons.cli.*;
import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        String cmdLineSyntax = "java MyApp <arg1> <arg2>";
        Options options = new Options();
        options.addOption("a", "arg1", true, "Argument for arg1");
        options.addOption("b", "arg2", true, "Argument for arg2");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, 1, 3, null, false);

        // Test valid output
        String expectedHelpText = "usage: java MyApp <arg1> <arg2>\n\nargs:\n\n  <arg1>    Argument for arg1\n  <arg2>    Argument for arg2\n";
        String actualHelpText = new String(System.in.readAllBytes());
        assertEquals(expectedHelpText, actualHelpText);

        // Test edge cases
        formatter.printHelp(System.out, 5, cmdLineSyntax, null, options, 1, 3, null, false); // Invalid width
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, null, 1, 3, null, false); // Empty options
        formatter.printHelp(System.out, 80, cmdLineSyntax, null, options, -1, 3, null, false); // Negative left padding
    }
}