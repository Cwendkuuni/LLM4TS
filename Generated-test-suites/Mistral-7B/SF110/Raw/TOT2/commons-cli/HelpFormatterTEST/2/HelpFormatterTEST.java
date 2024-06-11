import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        String cmdLineSyntax = "java MyApp -option1 arg1 -option2 arg2";
        Options options = new Options();
        options.addOption("option1", "option1", true, "Option 1");
        options.addOption("option2", "option2", false, "Option 2");

        HelpFormatter formatter = new HelpFormatter();
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream out = new PrintStream(outContent);
        formatter.printHelp(out, 80, cmdLineSyntax, "", options, 1, 4, "");
        String expectedHelp = "usage: java MyApp [-option1 <arg1>] [-option2]\n\n" +
                "Options:\n" +
                "   -option1 <arg1>\n" +
                "      Option 1\n" +
                "   -option2\n" +
                "      Option 2\n";

        String actualHelp = outContent.toString();
        assertEquals(expectedHelp, actualHelp);
    }
}