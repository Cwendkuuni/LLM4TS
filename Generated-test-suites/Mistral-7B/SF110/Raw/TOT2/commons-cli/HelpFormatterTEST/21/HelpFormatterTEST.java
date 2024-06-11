import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HelpFormatterTest {

    @Test
    public void testPrintHelpWithAutoUsage() {
        String cmdLineSyntax = "java MyApp [args]";
        String header = "Options:";
        String footer = "";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, 80, cmdLineSyntax, header, null, footer, false);
        String expectedHelp = "usage: java MyApp [args]\nOptions:\n";
        String actualHelp = System.out.getBuffer().toString();
        assertEquals(expectedHelp, actualHelp);
    }

    // Edge cases and exception handling tests will be added in the next steps
}