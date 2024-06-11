import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {
    @Test
    public void testDefaultSettings() {
        HelpFormatter formatter = new HelpFormatter();
        String cmdLineSyntax = "myCommand [options]";
        Options options = new Options();
        formatter.printHelp(cmdLineSyntax, options);
        String printedHelp = System.out.getBuffer().toString();
        String expectedHelp = "usage: myCommand [options]\n" +
                "\n" +
                "options:\n" +
                "  -h, --help            show detailed help\n" +
                "  --version             show version information\n";
        assertEquals(expectedHelp, printedHelp);
    }
}