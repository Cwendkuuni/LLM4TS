import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("i", "input", true, "input file");
 options.addOption("o", "output", true, "output file");
 options.addOption("h", "help", false, "print help");
 }

 @Test
 public void testEmptyArgs() {
 String[] args = new String[0];
 String[] parsedArgs = parser.parse(options, args, true);
 assertArrayEquals(new String[0], parsedArgs);
 }

 @Test
 public void testSingleArg() {
 String[] args = {"test"};
 String[] parsedArgs = parser.parse(options, args, true);
 assertEquals("test", parsedArgs[0]);
 }

 @Test
 public void testSingleOption() {
 String[] args = {"-i", "input.txt"};
 String[] parsedArgs = parser.parse(options, args, true);
 assertEquals("-i", parsedArgs[0]);
 assertEquals("input.txt", parsedArgs[1]);
 }

 @Test
 public void testMultipleOptions() {
 String[] args = {"-i", "input.txt", "-o", "output.txt"};
 String[] parsedArgs = parser.parse(options, args, true);
 assertEquals("-i", parsedArgs[0]);
 assertEquals("input.txt", parsedArgs[1]);
 assertEquals("-o", parsedArgs[2]);
 assertEquals("output.txt", parsedArgs[3]);
 }

 @Test
 public void testMultipleOptionsWithArg() {
 String[] args = {"-i", "input.txt", "--output", "output.txt"};
 String[] parsedArgs = parser.parse(options, args, true);
 assertEquals("-i", parsedArgs[0]);
 assertEquals("input.txt", parsedArgs[1]);
 assertEquals("--output", parsedArgs[2]);
 assertEquals("output.txt", parsedArgs[3]);
 }

 @Test
 public void testMultipleOptionsWithShortArg() {
 String[] args = {"-i", "input.txt", "-o", "output.txt"};
 options.addOption("o", "output", true, "output file");
 String[] parsedArgs = parser.parse(options, args, true);
 assertEquals("-i", parsedArgs[0]);
 assertEquals("input.txt", parsedArgs[1]);
 assertEquals("-o", parsedArgs[2]);
 assertEquals("output.txt", parsedArgs[3]);
 }

 @Test
 public void testMultipleOptionsWithShortArgAndArg() {
 String[] args = {"-i", "input.txt", "-o", "output.txt"};
 options.addOption("o", "output", true, "output file");
 String[] parsedArgs = parser.parse(options, args, false);
 assertEquals("-i", parsedArgs[0]);
 assertEquals("input.txt", parsedArgs[1]);
 assertEquals("-o", parsedArgs[2]);
 assertEquals("output.txt", parsedArgs[3]);
 }

 @Test
 public void testMultipleOptionsWithLongArg() {
 String[] args = {"--input", "input.txt", "--output", "output.txt"};
 String[] parsedArgs = parser.parse(options, args, true);
 assertEquals("--input", parsedArgs[0]);
 assertEquals("input.txt", parsedArgs[1]);
 assertEquals("--output", parsedArgs[2]);
 assertEquals("output.txt", parsedArgs[3]);
 }

 @Test
 public void testMultipleOptionsWithLongArgAndArg() {
 String[] args = {"--input", "input.txt", "--output", "output.txt"};
 options.addOption("output", true, "output file");
 String[] parsedArgs = parser.parse(options, args, true);
 assertEquals("--input", parsedArgs[0]);
 assertEquals("input.txt", parsedArgs[1]);
 assertEquals("--output", parsedArgs[2]);
 assertEquals("output.txt", parsedArgs[3]);
 }

 @Test
 public void testMultipleOptionsWithArgAndNoValue() {
 String[] args = {"-i", "input.txt", "-o"};
 String[] parsedArgs = parser.parse(options, args, true);
 assertEquals("--output", parsedArgs[2]);
 assertNull(parsedArgs[3]);
 }

 @Test
 public void testMultipleOptionsWithArgAndValue() {
 String[] args = {"-i", "input.txt", "-o", "output.txt"};
 String[] parsedArgs = parser.parse(options, args, false);
 assertEquals("--input", parsedArgs[0]);
 assertEquals("input.txt", parsedArgs[1]);
 assertEquals("--output", parsedArgs[2]);
 assertEquals("output.txt", parsedArgs[3]);
 }

 @Test
 public void testHelpOption() { helpFormatter = new HelpFormatter(); StringWriter sw = new StringWriter(); HelpFormatter.PrintHelp(sw, 80, options, true); String helpMsg = sw.toString(); assertTrue(helpMsg.contains("--input <input>")); assertTrue(helpMsg.contains("--output <output>")); }

 @Test (expected = IllegalArgumentException.class) public void testUnknownOption() { String[] args = {"-x", "input.txt"}; parser.parse(options, args, true); }

 @Test (expected = IllegalArgumentException.class) public void testMissingArg() { String[] args = {"-i"}; parser.parse(options, args, false); }

 @Test (expected = IllegalArgumentException.class) public void testMissingArgWithHelp() { String[] args = {"-h"}; parser.parse(options, args, false); }

 @Test (expected = IllegalArgumentException.class) public void testMissingArgWithStopAtNonOption() { options.setAllowUnknownOption(false); String[] args = {"-i"}; parser.parse(options, args, true); }
}