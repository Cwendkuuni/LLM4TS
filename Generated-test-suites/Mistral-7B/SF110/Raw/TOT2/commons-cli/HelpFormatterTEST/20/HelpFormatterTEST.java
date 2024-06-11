import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class HelpFormatterTest {

    @Test
    public void testPrintHelp() throws ParseException {
        String cmdLineSyntax = "java MyApp [options]";
        String header = "Options:";
        Options options = new Options();
        options.addOption("a", "append", false, "Append to a file instead of overwriting it");
        options.addOption("b", "backup", false, "Create a backup of the input file");
        options.addOption("c", "count", true, "Output file size counter");

        HelpFormatter formatter = new HelpFormatter();
        formatter.printHelp(System.out, 80, cmdLineSyntax, header, options, 1, 4, null, false);

        String expected = "Options:\n" +
                "  -a, --append          Append to a file instead of overwriting it\n" +
                "  -b, --backup           Create a backup of the input file\n" +
                "  -c, --count            Output file size counter\n" +
                "usage: java MyApp [options]\n";

        Assert.assertEquals(expected, System.out.getBuffer().toString());
    }
}