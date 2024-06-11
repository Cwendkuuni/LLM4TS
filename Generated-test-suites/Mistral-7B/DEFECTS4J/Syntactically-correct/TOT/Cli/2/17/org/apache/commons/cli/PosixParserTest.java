import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 @Test
 public void testFlattenBasic() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C");

 String[] args = {"-a", "--b", "-c", "1", "--", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 String[] expected = {"-a", "--b", "-c", "1", "--", "value"};
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"--z", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 // ParseException should be thrown here
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("longOption", true, "Long Option");

 String[] args = {"--longOption", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 String[] expected = {"--longOption"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionNoValue() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("longOption", false, "Long Option");

 String[] args = {"--longOption"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 String[] expected = {"--longOption"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"-a", "--", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 String[] expected = {"-a", "--", "value"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenStopAtNonOptionInvalidOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");

 String[] args = {"--z", "--", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 String[] expected = {"--z"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstTokenSingleCharacter() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C");
 options.addOption("d", 3, "Option D");

 String[] args = {"-acd"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 String[] expected = {"-a", "-c", "-d"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstTokenLongOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("longOption", true, "Long Option");

 String[] args = {"--longOption"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 String[] expected = {"--longOption"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstTokenLongOptionWithValue() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("longOption", true, "Long Option");

 String[] args = {"--longOption", "value"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 String[] expected = {"--longOption"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstTokenLongOptionWithValueAndArguments() {
 Options options = new Options();options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("longOption", true, "Long Option");

 String[] args = {"--longOption", "value", "arg1", "arg2"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 String[] expected = {"--longOption", "value", "arg1", "arg2"};
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenBurstTokenInvalidOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C");
 options.addOption("d", 3, "Option D");

 String[] args = {"--z"};
 PosixParser parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 String[] expected = {"--z"};
 assertArrayEquals(expected, result);
 }
}