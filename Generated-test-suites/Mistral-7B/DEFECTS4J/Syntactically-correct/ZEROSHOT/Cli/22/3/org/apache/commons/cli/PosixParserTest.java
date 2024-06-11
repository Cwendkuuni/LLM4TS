import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

 @Test
 public void testFlatten() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOption("d", 4, "Option D");
 options.addOption("e", 5, "Option E");

 String[] args = {"-a", "--b", "1", "-c", "--d=4", "-e", "--", "test", "arg1", "arg2"};
 String[] expected = {"-a", "--b", "1", "-c", "--d", "4", "--e", "--", "test", "arg1", "arg2"};
 String[] result = new PosixParser().flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);

 String[] args2 = {"-a", "--b=1", "-c", "--d", "-e", "--", "test", "arg1", "arg2"};
 String[] expected2 = {"-a", "--b", "1", "-c", "--d", "--", "test", "arg1", "arg2"};
 String[] result2 = new PosixParser().flatten(options, args2, false);
 Assert.assertArrayEquals(expected2, result2);

 String[] args3 = {"-a", "--b", "-c", "--d", "-e=5", "--", "test", "arg1", "arg2"};
 String[] expected3 = {"-a", "--b", "-c", "--d", "--e", "5", "--", "test", "arg1", "arg2"};
 String[] result3 = new PosixParser().flatten(options, args3, false);
 Assert.assertArrayEquals(expected3, result3);

 String[] args4 = {"-a", "--b=1", "-c", "--d", "-e", "--", "test", "arg1", "arg2"};
 String[] expected4 = {"-a", "--b", "1", "--d", "--e", "--", "test", "arg1", "arg2"};
 String[] result4 = new PosixParser().flatten(options, args4, true);
 Assert.assertArrayEquals(expected4, result4);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 3, "Option C");
 options.addOption("d", 4, "Option D");
 options.addOption("e", 5, "Option E");

 String[] args = {"-f", "--d=4", "-e", "--", "test", "arg1", "arg2"};
 }
}