import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 3, "Option C with an optional argument");
 }

 @After
 public void tearDown() {
 parser = null;
 options = null;
 }

 @Test
 public void testBasicFlattening() {
 String[] args = {"-a", "-b", "1", "--", "--help", "C:/path/to/file", "-c", "3"};
 String[] expected = {"-a", "-b", "1", "--", "--help", "C:/path/to/file", "-c", "3"};

 parser = new PosixParser();
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testStopAtNonOptionFlattening() {
 String[] args = {"-a", "-b", "1", "--", "--help", "C:/path/to/file", "-c", "3", "nonOption"};
 String[] expected = {"-a", "-b", "1", "--help", "C:/path/to/file", "-c", "3", "nonOption"};

 parser = new PosixParser();
 String[] actual = parser.flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testBursting() {
 String[] args = {"-abc", "123"};
 String[] expected = {"-a", "-b", "-c", "123"};

 parser = new PosixParser();
 String[] actual = parser.flatten(options, args, false);
 assertArrayEquals(expected, actual);
 }

 @Test
 public void testBurstingWithStopAtNonOption() {
 String[] args = {"-abc", "123", "nonOption"};
 String[] expected = {"-a", "-b", "-c", "123", "nonOption"};

 parser = new PosixParser();
 String[] actual = parser.flatten(options, args, true);
 assertArrayEquals(expected, actual);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testUnknownOption() {
 String[] args = {"-x", "value"};

 parser = new PosixParser();
 parser.flatten(options, args, false);
 }

 @Test
 public void testHelp() {
 HelpFormatter formatter = new HelpFormatter();
 String help = formatter.printHelp("Sample", "Sample program", options, true);
 assertEquals(
 "Sample\n" +
 "----------------------------------------------------------------------------\n" +
 "Sample program\n" +
 "\n" +
 "usage: Sample [-a] [-b <arg>] [-c <arg>]\n" +
 "\n" +
 "options:\n" +
 "  -a, --option-a\n" +
 "      Option A\n" +
 "  -b, --option-b <arg>\n" +
 "      Option B with an argument\n" +
 "  -c, --option-c <arg>\n" +
 "      Option C with an optional argument\n" +
 "\n" +
 "----------------------------------------------------------------------------\n", help);
 }
}