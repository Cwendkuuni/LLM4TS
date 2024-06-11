import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 @Test
 public void testFlattenEmpty() {
 Parser parser = new PosixParser();
 String[] args = {};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[0], result);
 }

 @Test
 public void testFlattenSingleOption() {
 Parser parser = new PosixParser();
 String[] args = {"--foo"};
 Options options = new Options();
 options.addOption("foo", false, null, "Foo option");
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--foo"}, result);
 }

 @Test
 public void testFlattenSingleValueOption() {
 Parser parser = new PosixParser();
 String[] args = {"--foo=bar"};
 Options options = new Options();
 options.addOption("foo", true, null, "Foo option");
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--foo", "bar"}, result);
 }

 @Test
 public void testFlattenMultipleOptions() {
 Parser parser = new PosixParser();
 String[] args = {"--foo", "--bar", "--baz=qux"};
 Options options = new Options();
 options.addOption("foo", false, null, "Foo option");
 options.addOption("bar", false, null, "Bar option");
 options.addOption("baz", true, null, "Baz option");
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--foo", "--bar", "--baz", "qux"}, result);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 Parser parser = new PosixParser();
 String[] args = {"--long-option-with-value=value"};
 Options options = new Options();
 options.addOption("longOptionWithValue", true, null, "Long option with value");
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOptionWithValue", "value"}, result);
 }

 @Test
 public void testFlattenMultipleLongOptions() {
 Parser parser = new PosixParser();
 String[] args = {"--longOption1", "--longOption2=value2", "--longOption3"};
 Options options = new Options();
 options.addOption("longOption1", false, null, "Long option 1");
 options.addOption("longOption2", true, null, "Long option 2");
 options.addOption("longOption3", false, null, "Long option 3");
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"--longOption1", "--longOption2", "value2", "--longOption3"}, result);
 }

 @Test
 public void testFlattenSingleHyphen() {
 Parser parser = new PosixParser();
 String[] args = {"-"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-"}, result);
 }

 @Test
 public void testFlattenMultipleSingleHyphens() {
 Parser parser = new PosixParser();
 String[] args = {"-", "-"};
 Options options = new Options();
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-", "--"}, result);
 }

 @Test
 public void testFlattenSingleCharacterOption() {
 Parser parser = new PosixParser();
 String[] args = {"-f"};
 Options options = new Options();
 options.addOption("f", true, null, "Force option");
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-f"}, result);
 }

 @Test
 public void testFlattenTwoCharacterOptionNoArgument() {
 Parser parser = new PosixParser();
 String[] args = {"-ab"};
 Options options = new Options();
 options.addOption("ab", false, null, "Option ab");
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-ab"}, result);
 }

 @Test
 public void testFlattenTwoCharacterOptionWithArgument() {
 Parser parser = new PosixParser();
 String[] args = {"-ab=xyz"};
 Options options = new Options();
 options.addOption("ab", true, null, "Option ab");
 String[] result = parser.flatten(options, args, false);
 assertEquals(new String[]{"-ab", "xyz"}, result);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testFlattenInvalidOption() {
 Parser parser = new PosixParser();
 String[] args = {"--invalidOption"};
 Options options = new Options();
 parser.flatten(options, args, false);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Parser parser = new PosixParser();
 String[] args = {"--foo", "bar--baz"};
 Options options = new Options();
 options.addOption("foo", true, null, "Foo option");
 String[] result = parser.flatten(options, args, true);
 assertEquals(new String[]{"--foo", "bar"}, result);
 }
}