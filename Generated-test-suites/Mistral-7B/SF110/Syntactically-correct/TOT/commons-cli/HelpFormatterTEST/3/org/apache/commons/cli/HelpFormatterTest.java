import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class HelpFormatterTest {

 private HelpFormatter formatter;
 private String cmdLineSyntax;
 private Options options;

 @Before
 public void setUp() {
 formatter = new HelpFormatter();
 cmdLineSyntax = "java MyApp -p <port> --db-url <db-url>";
 options = new Options();
 options.addRequiredOption("p", "port", true, "Port number");
 options.addRequiredOption("db-url", "database-url", true, "Database URL");
 }

 @Test
 public void testPrintHelp() {
 final PrintWriter pw = new PrintWriter(System.out);
 formatter.printHelp(pw, 75, cmdLineSyntax, options, null, false);
 pw.flush();
 String expectedHelp = "usage: java MyApp [-p <port>] [-db-url <db-url>]\n" +
 "Options:\n" +
 "   -p, --port\n" +
 "       Port number\n" +
 "   --db-url, --database-url\n" +
 "       Database URL\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullCmdLineSyntax() {
 formatter.printHelp(null, 75, cmdLineSyntax, options, null, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testNullOptions() {
 formatter.printHelp(System.out, 75, cmdLineSyntax, null, null, false);
 }

 @Test
 public void testPrintHelpWithAutoUsage() {
 final PrintWriter pw = new PrintWriter(System.out);
 formatter.printHelp(pw, 75, cmdLineSyntax, options);
 pw.flush();
 String expectedHelp = "usage: java MyApp [-p <port>] [-db-url <db-url>]\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testPrintHelpWithHeader() {
 final PrintWriter pw = new PrintWriter(System.out);
 String header = "MyApp Options";
 formatter.printHelp(pw, 75, cmdLineSyntax, header, options, null, false);
 pw.flush();
 String expectedHelp = "MyApp Options\n" +
 "usage: java MyApp [-p <port>] [-db-url <db-url>]\n" +
 "Options:\n" +
 "   -p, --port\n" +
 "       Port number\n" +
 "   --db-url, --database-url\n" +
 "       Database URL\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testPrintHelpWithFooter() {
 final PrintWriter pw = new PrintWriter(System.out);
 String footer = "For more information, see MyApp User Guide.";
 formatter.printHelp(pw, 75, cmdLineSyntax, null, options, null, false, footer);
 pw.flush();
 String expectedHelp = "usage: java MyApp [-p <port>] [-db-url <db-url>]\n" +
 "Options:\n" +
 "   -p, --port\n" +
 "       Port number\n" +
 "   --db-url, --database-url\n" +
 "       Database URL\n" +
 "For more information, see MyApp User Guide.";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testPrintHelpWithLeftPadding() {
 final PrintWriter pw = new PrintWriter(System.out);
 int leftPad = 5;
 formatter.printHelp(pw, 75, cmdLineSyntax, null, options, leftPad, 3, null);
 pw.flush();
 String expectedHelp = "    usage: java MyApp [-p <port>] [-db-url <db-url>]\n" +
 "    Options:\n" +
 "        -p, --port\n" +
 "            Port number\n" +
 "        --db-url, --database-url\n" +
 "            Database URL\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testPrintHelpWithDescPadding() {
 final PrintWriter pw = new PrintWriter(System.out);
 int descPad = 5;
 formatter.printHelp(pw, 75, cmdLineSyntax, null, options, 1, descPad, null);
 pw.flush();
 String expectedHelp = "usage: java MyApp [-p <port>] [-db-url <db-url>]\n" +
 " Options:\n" +
 "   -p, --port\n" +
 "       Port number\n" +
 "       Description: Port number\n" +
 "   --db-url, --database-url\n" +
 "       Database URL\n" +
 "       Description: Database URL\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testPrintHelpWithSyntaxPrefix() {
 final PrintWriter pw = new PrintWriter(System.out);
 String syntaxPrefix = "usage: myapp --";
 formatter.setSyntaxPrefix(syntaxPrefix);
 formatter.printHelp(pw, 75, cmdLineSyntax, options, null, false);
 pw.flush();
 String expectedHelp = "usage: myapp --java-MyApp -- [-p <port>] [-db-url <db-url>]\n" +
 "Options:\n" +
 "   --port\n" +
 "       Port number\n" +
 "   --database-url\n" +
 "       Database URL\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testPrintHelpWithOptPrefix() {
 final PrintWriter pw = new PrintWriter(System.out);
 String optPrefix = "-c";
 formatter.setOptPrefix(optPrefix);
 formatter.printHelp(pw, 75, cmdLineSyntax, options, null, false);
 pw.flush();
 String expectedHelp = "usage: java MyApp [-c-p <port>] [-c--db-url <db-url>]\n" +
 "Options:\n" +
 "   -c-p\n" +
 "       Port number\n" +
 "   -c--db-url\n" +
 "       Database URL\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testPrintHelpWithLongOptPrefix() {
 final PrintWriter pw = new PrintWriter(System.out);
 String longOptPrefix = "--config";
 formatter.setLongOptPrefix(longOptPrefix);
 formatter.printHelp(pw, 75, cmdLineSyntax, options, null, false);
 pw.flush();
 String expectedHelp = "usage: java MyApp [--config--no-port] [--config--db-url <db-url>]\n" +
 "Options:\n" +
 "   --config--no-port\n" +
 "       Port number\n" +
 "       Aliases: -p\n" +
 "   --config--db-url\n" +
 "       Database URL\n" +
 "       Aliases: --database-url\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 @Test
 public void testPrintHelpWithArgName() {
 final PrintWriter pw = new PrintWriter(System.out);
 String argName = "database";
 formatter.setArgName(argName);
 formatter.printHelp(pw, 75, cmdLineSyntax, options, null, false);
 pw.flush();
 String expectedHelp = "usage: java MyApp [-p <port>] [-db <db-url>]\n" +
 "Options:\n" +
 "   -p, --port\n" +
 "       Port number\n" +
 "   -db, --database-url\n" +
 "       Database URL\n";
 String actualHelp = this.getOutputAsString();
 assertEquals(expectedHelp, actualHelp);
 }

 private String getOutputAsString() {
 final ByteArrayOutputStream baos = new ByteArrayOutputStream();
 System.setOut(new PrintStream(baos));
 return baos.toString();
 }
}