import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.junit.Test;
import static org.junit.Assert.*;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        String cmdLineSyntax = "java MyApp --optionA argA --optionB argB";
        Options options = new Options();
        options.addOption("optionA", true, "Option A description");
        options.addOption("optionB", false, "Option B description");
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, cmdLineSyntax, options, true);
    }
}