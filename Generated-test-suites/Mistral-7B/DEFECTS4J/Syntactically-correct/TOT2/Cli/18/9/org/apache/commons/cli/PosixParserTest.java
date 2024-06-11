import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

public class PosixParserTest {

 private PosixParser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", 1, "Option C with an integer argument");
 }

 /**
 * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
 * with basic usage
 */
 @Test
 public void testBasicUsage() {
 String[] args = {"-a", "--b", "-c", "1"};
 String[] expected = {"-a", "--b", "-c", "1"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
 * with single hyphen
 */
 @Test
 public void testSingleHyphen() {
 String[] args = {"-"};
 String[] expected = {"-"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
 * with two characters option
 */
 @Test
 public void testTwoCharOption() {
 String[] args = {"--ab"};
 String[] expected = {"--ab"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
 * with long option and value
 */
 @Test
 public void testLongOptionWithValue() {
 String[] args = {"--longOption", "value"};
 String[] expected = {"--longOption", "value"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
 * with multiple options
 */
 @Test
 public void testMultipleOptions() {
 String[] args = {"-a", "--b", "-c", "1"};
 String[] expected = {"-a", "--b", "-c", "1"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
 * with stopAtNonOption
 */
 @Test
 public void testStopAtNonOption() {
 String[] args = {"-a", "--nonOption", "1"};
 String[] expected = {"-a", "--nonOption"};
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
 * with bursting
 */
 @Test
 public void testBursting() {
 String[] args = {"-abc"};
 String[] expected = {"-a", "-b", "-c"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 /**
 * Test method for {@link PosixParser#flatten(Options, String[], boolean)}
 * with bursting and stopAtNonOption
 */
 @Test (expected = ParseException.class)
 public void testBurstingAndStopAtNonOption() {
 String[] args = {"-abc"};
 String[] expected = {"--", "a", "b", "c"};
 options.addOption("--", "Unknown option");
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(expected, result);
 }
}