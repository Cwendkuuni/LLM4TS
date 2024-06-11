import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        String cmdLineSyntax = "java MyApp [options] arg1 arg2";
        String header = "Options:";
        String footer = "Use --help for more information.";
        Options options = new Options();
        options.addOption("a", false, false, "an option");
        options.addOption("b", true, "num", "a required option with arg");
        options.addOptionGroup(new OptionGroup("group", "A group", "options in a group"));
        Option optionGroup = options.getOptionGroup("group");
        optionGroup.addOption(new Option("c", false, "num", "an option in the group"));
        optionGroup.addOption(new Option("d", false, null, "another option in the group"));
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, 80, cmdLineSyntax, header, options, footer);
        String expected = "..."; // expected output
        String actual = captureOutPut(System.out);
        assertEquals(expected, actual);
    }

    // Util method for capturing the output of System.out
    private String captureOutPut(PrintStream systemOut) {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        PrintStream oldOut = System.out;
        System.setOut(new PrintStream(new RedirectedStream(new PrintWriter(outContent))));
        // your test code here
        System.setOut(oldOut);
        return outContent.toString();
    }
}