import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with argument");
 }

 @Test
 public void testFlattenSimple() {
 String[] args = {"--foo", "-a", "-b", "argB", "-c", "argC"};
 String[] expected = {"--foo", "-a", "--", "-b", "argB", "-c", "--", "argC"};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 String[] args = {"--foo", "-a", "-b", "argB", "-c", "argC", "argD"};
 String[] expected = {"--foo", "-a", "--", "-b", "argB", "-c", "--", "argC", "--", "argD"};
 String[] result = parser.parse(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithSingleDash() {
 String[] args = {"--foo", "-", "-b", "argB", "-c", "argC"};
 String[] expected = {"--foo", "-", "--", "-b", "argB", "-c", "--", "argC"};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOption() {
 String[] args = {"--foo", "--bar"};
 String[] expected = {"--foo", "--bar"};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidOption() {
 String[] args = {"--foo", "--invalid"};
 parser = new PosixParser(); // use PosixParser instead of default Parser
 String[] expected = {"--foo"};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 assertEquals(1, parser.getOptions().getOptions().length);
 }

 @Test
 public void testBurstToken() {
 String[] args = {"-abc"};
 String[] expected = {"-a", "-b", "-c"};
 String[] result = parser.parse(options, args, false)[0];
 assertEquals(Arrays.toString(expected), Arrays.toString(result));
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 String[] args = {"-abc", "d"};
 String[] expected = {"-a", "-b", "-c", "--", "d"};
 String[] result = parser.parse(options, args, true)[0];
 assertEquals(Arrays.toString(expected), Arrays.toString(result));
 }

 @Before
 @SuppressWarnings("unchecked")
 public void setUpParser() {
 parser = new PosixParser();
 }

}