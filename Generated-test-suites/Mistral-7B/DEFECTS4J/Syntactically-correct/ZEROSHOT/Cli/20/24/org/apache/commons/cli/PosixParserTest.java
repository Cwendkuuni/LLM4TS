import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "An option with a flag");
 options.addOption("b", 1, "An option with an integer value");
 options.addOption("c", 2, "An option with a short argument and an integer value");
 options.addOption("d", "default", "An option with a long argument and a default value");
 }

 @Test
 public void testFlattenWithFlags() throws ParseException {
 String[] args = {"-a", "-b", "--d", "value"};
 String[] expected = {"-a", "-b", "--d", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithShortAndLongArguments() throws ParseException {
 String[] args = {"-ac", "arg1", "--d", "value"};
 String[] expected = {"-a", "--c", "arg1", "--d", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 String[] args = {"-a", "-b", "--c", "arg1", "--d", "value"};
 String[] expected = {"-a", "-b", "--c", "arg1"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 String[] args = {"-z", "arg"};
 parser = new PosixParser(); // use default Parser instance
 parser.flatten(options, args, false);
 }

 @Test
 public void testBursting() throws ParseException {
 String[] args = {"-abcde"};
 String[] expected = {"-a", "--c", "--e"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testEmptyArguments() throws ParseException {
 String[] args = {};
 String[] expected = {};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testNullArguments() throws ParseException {
 String[] args = null;
 String[] expected = {};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testCurrentOption() {
 parser = new PosixParser();
 parser.flatten(options, new String[]{"-a"}, false);
 assertEquals(options.getOption("a"), parser.currentOption);
 assertNull(parser.currentOption); // reset
 }
}