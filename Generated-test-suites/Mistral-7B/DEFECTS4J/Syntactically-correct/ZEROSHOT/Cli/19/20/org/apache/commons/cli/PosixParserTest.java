import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testFlatten() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with two args");
 options.addOption("d", 3, "Option D with three args");

 String[] args = {"-a", "--b", "-c", "value1", "value2", "value3", "--d"};
 String[] expected = {"-a", "-a", "--b", "-c", "value1", "value2", "value3", "--d"};

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with two args");
 options.addOption("d", 3, "Option D with three args");

 String[] args = {"-a", "--b", "-c", "value1", "value2", "value3", "--", "nonOption"};
 String[] expected = {"-a", "--b", "--", "nonOption"};

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);

 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstToken() {
 Options options = new Options();
 options.addOption("ab", true, "Option AB");
 options.addOption("cd", 2, "Option CD with two args");
 options.addOption("ef", 3, "Option EF with three args");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, new String[]{"-abcd", "value1", "value2", "value3"}, false);
 String[] expected = {"-ab", "-c", "value1", "value2", "value3"};

 Assert.assertArrayEquals(expected, result);
 }
}