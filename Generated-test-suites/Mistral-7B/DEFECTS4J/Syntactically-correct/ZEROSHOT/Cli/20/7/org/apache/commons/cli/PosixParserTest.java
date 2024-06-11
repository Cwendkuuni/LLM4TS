import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOption("d", 3, "Option D with three arguments");
 }

 @Test
 public void testSimple() throws ParseException {
 String[] args = {"--a", "--b", "argB"};
 String[] expected = {"--a", "--b", "argB"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testLongOptionWithArgument() throws ParseException {
 String[] args = {"--option-b", "argB"};
 String[] expected = {"--option-b", "argB"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 String[] args = {"--invalid"};
 parser.flatten(options, args, false);
 fail("Should have thrown ParseException");
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 String[] args = {"--a", "--b", "argB", "nonOption", "--c", "argC1", "argC2"};
 String[] expected = {"--a", "--b", "argB", "--c", "argC1", "argC2"};
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testBursting() throws ParseException {
 String[] args = {"-abc"};
 String[] expected = {"-a", "-b", "-c"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingWithStopAtNonOption() throws ParseException {
 String[] args = {"-abc", "nonOption"};
 String[] expected = {"--", "nonOption"};
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(expected, result);
 }
}