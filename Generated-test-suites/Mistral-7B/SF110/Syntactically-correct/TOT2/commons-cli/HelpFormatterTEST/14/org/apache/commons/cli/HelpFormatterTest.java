import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class HelpFormatterTest {
    @Test
    public void testPrintHelp() throws Exception {
        Options options = new Options();
        options.addOption("h", "help", false, "Print this message.");
        options.addOption("p", "print", true, "Print a message.");

        HelpFormatter formatter = new HelpFormatter();
        PrintWriter pw = new PrintWriter(System.out);
        String cmdLineSyntax = "MyApp [options]";
        formatter.printHelp(pw, 80, cmdLineSyntax, "MyApp", options, 1, 4, "");

        // Check if the help message was printed correctly.
        String expectedHelpMessage = "MyApp [options]\n" +
                "Options:\n" +
                "   -h, --help          Print this message.\n" +
                "   -p, --print <message>Print a message.\n";
        String actualHelpMessage = pw.getBuffer().toString();
        assertEquals(expectedHelpMessage, actualHelpMessage);
    }
}