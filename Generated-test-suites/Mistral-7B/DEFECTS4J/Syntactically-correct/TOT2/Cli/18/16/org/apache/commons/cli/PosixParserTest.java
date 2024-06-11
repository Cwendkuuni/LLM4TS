import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 private static final String[] ARGS = {"--foo", "--bar=value", "-c", "-d", "-e foo=value", "--", "arg1", "arg2"};

 @Test
 public void testFlatten() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] expected = {"--foo", "--bar", "value", "-c", "-d", "-e", "foo", "value", "arg1", "arg2"};
 String[] result = parser.flatten(options, ARGS, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithStopAtNonOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"--foo", "--bar=value", "-c", "-d", "-e foo=value", "--", "arg1", "arg2"};
 String[] result = parser.flatten(options, ARGS, true);
 Assert.fail("Expected ParseException but got: " + Arrays.toString(result));
 }

 @Test
 public void testBurstToken() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String token = "--foo=bar--baz";
 String[] expected = {"--foo", "bar--baz"};
 String[] result = parser.burstToken(token, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testBurstTokenWithStopAtNonOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String token = "--foo=bar--baz";
 String[] result = parser.burstToken(token, true);
 Assert.fail("Expected ParseException but got: " + Arrays.toString(result));
 }

 @Test
 public void testProcessOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 options.addOption("bar", true, "b");
 String[] args = {"-f", "--foo", "-b", "--bar", "arg1", "arg2"};
 String[] expected = {"-f", "--foo", "--bar", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testProcessOptionMissing() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("foo", true, "f");
 String[] args = {"-f", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, false);
 Assert.fail("Expected ParseException but got: " + Arrays.toString(result));
 }

 @Test
 public void testProcessSingleHyphen() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"-", "--"};
 String[] expected = {"-", "--"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testProcessSingleHyphenWithStopAtNonOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] args = {"-", "arg1", "arg2"};
 String[] expected = {"-", "--", "arg1", "arg2"};
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(expected, result);
 }

}