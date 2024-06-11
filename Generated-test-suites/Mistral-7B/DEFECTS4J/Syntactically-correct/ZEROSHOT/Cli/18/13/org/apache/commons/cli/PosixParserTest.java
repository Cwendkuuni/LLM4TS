import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;

public class PosixParserTest {

 private Options options;
 private String[] args;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOption("d", 4, "Option D");
 }

 @After
 public void tearDown() {
 options = null;
 }

 @Test
 public void testFlattenEmpty() {
 String[] result = new PosixParser().flatten(options, new String[0], false);
 String[] expected = new String[0];
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenSingleOption() {
 args = new String[]{"-a"};
 String[] result = new PosixParser().flatten(options, args, false);
 String[] expected = {"-a"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOptionWithArgument() {
 args = new String[]{"-a", "arg"};
 String[] result = new PosixParser().flatten(options, args, false);
 String[] expected = {"-a", "arg"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMultipleOptions() {
 args = new String[]{"-a", "-b", "-c"};
 String[] result = new PosixParser().flatten(options, args, false);
 String[] expected = {"-a", "-b", "-c"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMultipleOptionsWithArguments() {
 args = new String[]{"-a", "arg1", "-b", "arg2", "-c", "arg3"};
 String[] result = new PosixParser().flatten(options, args, false);
 String[] expected = {"-a", "arg1", "-b", "arg2", "-c", "arg3"};
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 args = new String[]{"-i", "value"};
 new PosixParser().flatten(options, args, false);
 }

 @Test
 public void testFlattenWithLongOption() {
 args = new String[]{"--long-option"};
 String[] result = new PosixParser().flatten(options, args, false);
 String[] expected = {"--long-option"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptionAndValue() {
 args = new String[]{"--long-option", "value"};
 String[] result = new PosixParser().flatten(options, args, false);
 String[] expected = {"--long-option", "value"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptionAndTwoValues() {
 args = new String[]{"--long-option", "value1", "--long-option", "value2"};
 String[] result = new PosixParser().flatten(options, args, false);
 String[] expected = {"--long-option", "value1", "--long-option", "value2"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 args = new String[]{"--", "-a", "arg", "-b"};
 String[] result = new PosixParser().flatten(options, args, true);
 String[] expected = {"--", "-a", "arg", "-b"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionAndInvalidOption() {
 args = new String[]{"--", "-i", "value"};
 try {
 new PosixParser().flatten(options, args, true);
 } catch (ParseException e) {
 // expected
 }
 }
}