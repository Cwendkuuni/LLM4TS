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
 options.addOption("c", 2, "Option C with arg");
 options.addOption("d", 3, "Option D with arg");
 }

 @Test
 public void testFlattenBasic() {
 String[] args = {"--a", "-b", "-c", "arg1", "--d", "arg2"};
 String[] expected = {"--a", "-c", "-d"};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithArg() {
 String[] args = {"--a", "-c", "arg1", "--d=arg2"};
 String[] expected = {"--a", "-c", "--d=arg2"};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 String[] args = {"--a", "-c", "arg1", "-x", "--d=arg2"};
 String[] expected = {"--a", "-c", "--d=arg2"};
 String[] result = parser.parse(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBursting() {
 String[] args = {"--a", "-abc", "arg1", "--d=arg2"};
 String[] expected = {"--a", "--d=arg2"};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstingWithStopAtNonOption() {
 String[] args = {"--a", "-abc", "arg1", "-x", "--d=arg2"};
 String[] expected = {"--a", "--d=arg2"};
 String[] result = parser.parse(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenEmptyArgs() {
 String[] args = {};
 String[] expected = {};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenEmptyArgsStopAtNonOption() {
 String[] args = {};
 String[] expected = {};
 String[] result = parser.parse(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenNullOptions() {
 String[] args = {"--a", "-c", "arg1", "--d=arg2"};
 Options nullOptions = null;
 String[] result = parser.parse(nullOptions, args, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenNullArgs() {
 Options options = new Options();
 String[] args = null;
 String[] result = parser.parse(options, args, false);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenNullArgsStopAtNonOption() {
 Options options = new Options();
 String[] args = null;
 String[] result = parser.parse(options, args, true);
 }

 @Test
 public void testFlattenLongOptionWithArg() {
 String[] args = {"--long-option=arg"};
 String[] expected = {"--long-option=arg"};
 String[] result = parser.parse(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithArgStopAtNonOption() {
 String[] args = {"--long-option=arg", "-x"};
 String[] expected = {"--long-option=arg"};
 String[] result = parser.parse(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Before
 public void setUpParser() {
 parser = new PosixParser();
 }
}