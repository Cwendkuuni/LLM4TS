import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

 private String[] getTestArgs() {
 return new String[]{"--foo", "-b", "-c", "--foo=bar", "--", "-d", "-e", "-f arg"};
 }

 @Test
 public void testFlatten() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("d", false, "An optional boolean flag");
 options.addOption("e", false, "Another optional boolean flag");

 PosixParser parser = new PosixParser();
 String[] args = getTestArgs();
 String[] flattenedArgs = parser.flatten(options, args, false);

 List<String> expected = new ArrayList<>();
 expected.add("--foo");
 expected.add("true");
 expected.add("-b");
 expected.add("-c");
 expected.add("--foo");
 expected.add("bar");
 expected.add("--");
 expected.add("-d");
 expected.add("");
 expected.add("-e");
 expected.add("");
 expected.add("-f");
 expected.add("arg");

 assertArrayEquals(expected.toArray(), flattenedArgs);
 }

 @Test(expected = ParseException.class)
 public void testFlattenWithStopAtNonOption() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("d", false, "An optional boolean flag");
 options.addOption("e", false, "Another optional boolean flag");

 PosixParser parser = new PosixParser();
 String[] args = getTestArgs();
 String[] flattenedArgs = parser.flatten(options, args, true);

 fail("Expected ParseException but got: " + Arrays.toString(flattenedArgs));
 }

 @Test(expected = ParseException.class)
 public void testBurstingWithInvalidOption() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("d", false, "An optional boolean flag");
 options.addOption("e", false, "Another optional boolean flag");

 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--badOption"};
 String[] flattenedArgs = parser.flatten(options, args, false);

 fail("Expected ParseException but got: " + Arrays.toString(flattenedArgs));
 }

 @Test(expected = ParseException.class)
 public void testBurstingWithMultipleInvalidOptions() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("d", false, "An optional boolean flag");
 options.addOption("e", false, "Another optional boolean flag");

 PosixParser parser = new PosixParser();
 String[] args = new String[]{"--badOption1", "--badOption2"};
 String[] flattenedArgs = parser.flatten(options, args, false);

 fail("Expected ParseException but got: " + Arrays.toString(flattenedArgs));
 }

 @Test
 public void testGobble() throws ParseException {
 Options options = new Options();
 options.addOption("foo", true, "A boolean option");
 options.addOption("d", false, "An optional boolean flag");
 options.addOption("e", false, "Another optional boolean flag");

 PosixParser parser = new PosixParser();
 String[] args = getTestArgs();
 String[] flattenedArgs = parser.flatten(options, args, false);
 String[] remainingArgs = new String[]{"--", "-x", "-y"};

 parser.init();
 String[] finalArgs = parser.flatten(options, remainingArgs, true);

 List<String> expected = new ArrayList<>();
 expected.addAll(Arrays.asList(flattenedArgs));
 expected.addAll(Arrays.asList(finalArgs));

 assertArrayEquals(expected.toArray(), parser.getTokens().toArray());
 }
}