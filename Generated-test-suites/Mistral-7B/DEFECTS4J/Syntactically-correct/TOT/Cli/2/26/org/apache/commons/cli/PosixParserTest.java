import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private static final String[] ARGS_CASE_1 = {"--foo", "--bar=baz", "-x", "-y"};
 private static final String[] ARGS_CASE_2 = {"--foo=bar", "--bar", "-x", "-y"};
 private static final String[] ARGS_CASE_3 = {"-abc", "-d", "--def=ghi"};
 private static final String[] ARGS_CASE_4 = {"-a", "--b", "--c", "--d"};
 private static final String[] ARGS_CASE_5 = {"--foo", "--bar", "--baz", "--qux"};
 private static final String[] ARGS_CASE_6 = {"--foo", "--bar=baz", "--qux"};

 @Test
 public void testFlatten_simple() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "Description for --foo");
 options.addOption("bar", 1, "Description for --bar");
 options.addOption("baz", 1, "Description for --baz");
 options.addOption("qux", 1, "Description for --qux");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, ARGS_CASE_1, false);
 assertEquals(new String[]{"--foo", "--bar=baz", "--", "-x", "-y"}, args);
 }

 @Test
 public void testFlatten_simpleArg() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "Description for --foo");
 options.addOption("bar", 1, "Description for --bar");
 options.addOption("baz", 1, "Description for --baz");
 options.addOption("qux", 1, "Description for --qux");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, ARGS_CASE_2, false);
 assertEquals(new String[]{"--foo=bar", "--bar", "--", "-x", "-y"}, args);
 }

 @Test
 public void testFlatten_validOptions() throws ParseException {
 Options options = new Options();
 options.addOption("a", false, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");
 options.addOption("d", false, "Option D");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, ARGS_CASE_4, false);
 assertEquals(new String[]{"-a", "-b", "-c", "-d"}, args);
 }

 @Test
 public void testFlatten_ignore_single_hyphen() throws ParseException {
 Options options = new Options();
 options.addOption("a", false, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");
 options.addOption("d", false, "Option D");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, ARGS_CASE_3, false);
 assertEquals(new String[]{"-a", "-c", "--def=ghi"}, args);
 }

 @Test
 public void testFlatten_multipleOptions() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "Description for --foo");
 options.addOption("bar", 1, "Description for --bar");
 options.addOption("baz", 1, "Description for --baz");
 options.addOption("qux", 1, "Description for --qux");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, ARGS_CASE_5, false);
 assertEquals(new String[]{"--foo", "--bar", "--baz", "--qux"}, args);
 }

 @Test
 public void testFlatten_stopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "Description for --foo");
 options.addOption("bar", 1, "Description for --bar");
 options.addOption("baz", 1, "Description for --baz");
 options.addOption("qux", 1, "Description for --qux");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, ARGS_CASE_1, true);
 assertEquals(new String[]{"--foo", "--bar=baz", "-x", "-y"}, args);
 }

 @Test
 public void testFlatten_stopAtNonOption_invalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "Description for --foo");
 options.addOption("bar", 1, "Description for --bar");
 options.addOption("baz", 1, "Description for --baz");
 options.addOption("qux", 1, "Description for --qux");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, new String[]{"--quux"}, true);
 assertNull(args);
 }

 @Test
 public void testBurstToken() throws ParseException {
 Options options = new Options();
 options.addOption("a", false, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");
 options.addOption("d", false, "Option D");
 options.addOption("e", false, "Option E");
 options.addOption("f", false, "Option F");
 options.addOption("g", false, "Option G");
 options.addOption("h", false, "Option H");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, ARGS_CASE_6, false);
 assertEquals(new String[]{"--foo", "--bar=baz", "--qux"}, args);
 }

 @Test
 public void testBurstToken_validOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", false, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");
 options.addOption("d", false, "Option D");
 options.addOption("e", false, "Option E");
 options.addOption("f", false, "Option F");
 options.addOption("g", false, "Option G");
 options.addOption("h", false, "Option H");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, ARGS_CASE_3, false);
 assertEquals(new String[]{"-a", "-c", "--def=ghi"}, args);
 }

 @Test
 public void testBurstToken_validOptionWithArg() throws ParseException {
 Options options = new Options();
 options.addOption("a", false, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");
 options.addOption("d", false, "Option D");
 options.addOption("e", false, "Option E");
 options.addOption("f", false, "Option F");
 options.addOption("g", false, "Option G");
 options.addOption("h", false, "Option H");
 options.addOption("i", 1, "Option I with an argument");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, new String[]{"-abc", "-d", "--def=ghi"}, false);
 assertEquals(new String[]{"-a", "-c", "--def=ghi"}, args);
 }

 @Test
 public void testBurstToken_invalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", false, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");
 options.addOption("d", false, "Option D");
 options.addOption("e", false, "Option E");
 options.addOption("f", false, "Option F");
 options.addOption("g", false, "Option G");
 options.addOption("h", false, "Option H");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, new String[]{"--quux"}, false);
 assertNull(args);
 }

 @Test
 public void testBurstToken_stopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", false, "Option A");
 options.addOption("b", false, "Option B");
 options.addOption("c", false, "Option C");
 options.addOption("d", false, "Option D");
 options.addOption("e", false, "Option E");
 options.addOption("f", false, "Option F");
 options.addOption("g", false, "Option G");
 options.addOption("h", false, "Option H");

 PosixParser parser = new PosixParser();
 String[] args = parser.flatten(options, new String[]{"--foo", "--bar=baz", "--quux"}, true);
 assertEquals(new String[]{"--foo", "--bar=baz"}, args);
 }
}