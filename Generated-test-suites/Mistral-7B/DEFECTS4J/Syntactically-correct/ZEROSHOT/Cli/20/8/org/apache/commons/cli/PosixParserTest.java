import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options options;
 private String[] args;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A with boolean value.");
 options.addOption("b", 1, "Option B with integer value.");
 options.addOption("c", 3, "Option C with integer value.");
 }

 @After
 public void tearDown() {
 options = null;
 args = null;
 }

 @Test
 public void testEmptyArgs() {
 String[] expected = new String[0];
 String[] result = PosixParser.parseArgs(options, new String[0], false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatteningSimpleArgs() {
 args = new String[]{"--foo", "-a", "--bar", "-b", "3", "-c"};
 String[] expected = new String[]{"--foo", "-a", "--bar", "-b", "3", "-c"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatteningLongOptions() {
 args = new String[]{"--foo=bar", "--baz=qux"};
 String[] expected = new String[]{"--foo=bar", "--baz=qux"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatteningShortOptionsWithValue() {
 args = new String[]{"-a", "foo", "-b=3", "-c", "qux"};
 String[] expected = new String[]{"-a", "--", "foo", "-b", "3", "--", "c", "qux"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatteningShortOptionsWithoutValue() {
 args = new String[]{"-a", "-b", "-c"};
 String[] expected = new String[]{"-a", "--", "-b", "--", "-c"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlatteningShortOptionsWithValueAndLongOptions() {
 args = new String[]{"-a", "--foo=bar", "-b=3", "--baz=qux", "quux"};
 String[] expected = new String[]{"-a", "--", "--foo=bar", "-b", "3", "--", "--baz=qux", "--", "quux"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatteningInvalidOption() {
 args = new String[]{"--invalidOption"};
 try {
 PosixParser.parseArgs(options, args, false);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testFlatteningStopAtNonOption() {
 args = new String[]{"--foo", "-a", "-b", "nonOption", "-c"};
 String[] expected = new String[]{"--foo", "-a", "--", "nonOption"};
 String[] result = PosixParser.parseArgs(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingOption() {
 args = new String[]{"-ab"};
 String[] expected = new String[]{"-a", "--", "b"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingOptionWithValue() {
 args = new String[]{"-abc"};
 String[] expected = new String[]{"-a", "--", "b", "c"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingOptionWithLongValue() {
 args = new String[]{"-abcdef"};
 String[] expected = new String[]{"-a", "--", "b", "--", "c", "--", "d", "--", "e", "--", "f"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingOptionWithArgument() {
 args = new String[]{"-abc arg"};
 String[] expected = new String[]{"-a", "--", "b", "--", "arg"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingOptionWithArgumentAndValue() {
 args = new String[]{"-abc arg=value"};
 String[] expected = new String[]{"-a", "--", "b", "--", "arg=value"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingOptionWithMultipleArguments() {
 args = new String[]{"-abc arg1 arg2"};
 String[] expected = new String[]{"-a", "--", "b", "--", "arg1", "--", "arg2"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingOptionWithMultipleArgumentsAndValues() {
 args = new String[]{"-abc arg1 arg2=value1 value2"};
 String[] expected = new String[]{"-a", "--", "b", "--", "arg1", "--", "arg2=value1", "--", "arg2=value2"};
 String[] result = PosixParser.parseArgs(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstingOptionWithStopAtNonOption() {
 args = new String[]{"-abc arg1 arg2 nonOption"};
 String[] expected = new String[]{"-a", "--", "b", "--", "arg1", "--", "arg2", "--"};
 String[] result = PosixParser.parseArgs(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testEmptyOptions() {
 Options emptyOptions = new Options();
 String[] emptyArgs = new String[0][];
 String[] result = PosixParser.parseArgs(emptyOptions, emptyArgs, false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testNullOptions() {
 String[] result = PosixParser.parseArgs(null, new String[0], false);
 assertArrayEquals(new String[0], result);
 }

 @Test
 public void testNullArgs() {
 Options options = new Options();
 String[] result = PosixParser.parseArgs(options, null, false);
 assertArrayEquals(new String[0], result);
 }
}