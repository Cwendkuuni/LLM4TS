import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() {
        String cmdLineSyntax = "java MyApplication -optA argA -optB argB";
        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, 80, cmdLineSyntax, true);
    }
}