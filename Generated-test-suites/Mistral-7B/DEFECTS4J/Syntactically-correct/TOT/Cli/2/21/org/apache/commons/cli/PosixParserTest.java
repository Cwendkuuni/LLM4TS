import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private static final String[] ARGS1 = {"--foo", "-b", "--bar=value", "-a", "--baz", "--qux"};
 private static final String[] ARGS2 = {"--foo=value", "-a", "--qux", "--baz"};
 private static final String[] ARGS3 = {"--foo", "-a"};
 private static final String[] ARGS4 = {"--baz", "--qux"};
 private static final String[] ARGS5 = {"--"};

 @Test
 public void testFlatten1() {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("bar", true, "An option with an argument");
 options.addOption("baz", false, "An option without an argument");
 options.addOption("qux", 1, "An option with an integer argument");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGS1, false);
 assertArrayEquals(new String[]{"--foo", "-b", "--bar", "value", "-a", "--baz", "--qux"}, result);
 }

 @Test
 public void testFlatten2() {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("bar", true, "An option with an argument");
 options.addOption("baz", false, "An option without an argument");
 options.addOption("qux", 1, "An option with an integer argument");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGS2, false);
 assertArrayEquals(new String[]{"--foo=value", "-a", "--qux"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten3() {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("baz", false, "An option without an argument");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGS3, false);
 assertArrayEquals(new String[]{"--foo", "-a"}, result); // expected error: unrecognized option: -a
 }

 @Test
 public void testFlatten4() {
 Options options = new Options();
 options.addOption("baz", false, "An option without an argument");
 options.addOption("qux", 1, "An option with an integer argument");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGS4, false);
 assertArrayEquals(new String[]{"--baz", "--qux"}, result);
 }

 @Test(expected = ParseException.class)
 public void testFlatten5() {
 Options options = new Options();

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGS5, false);
 assertArrayEquals(new String[]{"--", "--"}, result); // expected error: unrecognized option: --
 }

 @Test
 public void testFlatten6() {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("bar", true, "An option with an argument");
 options.addOption("baz", true, "An option with an argument");

 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, ARGS1, true);
 assertArrayEquals(new String[]{"--foo", "-b", "--bar", "value", "-a", "--baz"}, result);
 }
}