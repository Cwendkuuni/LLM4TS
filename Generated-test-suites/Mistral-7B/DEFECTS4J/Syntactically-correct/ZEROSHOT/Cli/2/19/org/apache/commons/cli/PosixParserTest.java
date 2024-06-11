import org.apache.commons.cli. Options;
import org.apache.commons.cli.Parser;
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
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 1, "Option C with a required argument");
 options.addOption("d", "Default option D");
 }

 @After
 public void tearDown() {
 parser = null;
 options = null;
 }

 @Test
 public void testFlatten() {
 String[] arguments = {"-a", "--b", "argB", "-c", "argC", "--d", "--", "argD"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false);
 String[] expected = {"-a", "--b", "argB", "-c", "argC", "--d", "--", "argD"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 String[] arguments = {"-a", "--b", "argB", "-c", "argC", "--d", "--foo", "argFoo"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, true);
 String[] expected = {"-a", "--b", "argB", "--", "--foo", "argFoo"};
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenWithInvalidOption() {
 String[] arguments = {"-z", "--foo", "argFoo"};
 parser = new PosixParser();
 parser.flatten(options, arguments, false);
 }

 @Test
 public void testBurstToken() {
 String[] arguments = {"--abc"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false)[0].split(" ");
 String[] expected = {"--", "abc"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithArg() {
 String[] arguments = {"--abc=def"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false)[0].split(" ");
 String[] expected = {"--", "abc=def"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 String[] arguments = {"--", "abc", "--foo"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, true)[0].split(" ");
 String[] expected = {"--", "abc", "--foo"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithValidOption() {
 String[] arguments = {"-a", "-b", "--abc"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false)[0].split(" ");
 String[] expected = {"-a", "--b", "--abc"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithValidOptionAndArg() {
 String[] arguments = {"-a", "-b", "--abc=def"};
 parser = new PosixParser();
 String[] result = parser.flatten(options, arguments, false)[0].split(" ");
 String[] expected = {"-a", "--b", "--abc=def"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithValidOptionAndRequiredArg() {
 String[] arguments = {"-a", "-b", "--abc"};
 parser = new PosixParser();
 options.getOption("abc").setRequired(true);
 String[] result = parser.flatten(options, arguments, false)[0].split(" ");
 String[] expected = {"-a", "--b", "--abc"};
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testBurstTokenWithInvalidOptionAndStopAtNonOption() {
 String[] arguments = {"-z", "--abc"};
 parser = new PosixParser();
 parser.flatten(options, arguments, true);
 }
}