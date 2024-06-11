import org.apache.commons.cli.GnuParser;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class GnuParserTest {

 @Test
 public void testFlattenSingleOption() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Enable debug mode");
 String[] arguments = {"-D"};
 String[] expected = {"-D"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenNoOption() {
 Options options = new Options();
 String[] arguments = {"test"};
 String[] expected = {"test"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOneArgumentOption() {
 Options options = new Options();
 options.addOption("a", "arg", true, "An argument option");
 String[] arguments = {"-a", "value"};
 String[] expected = {"-a", "value"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenTwoArgumentOption() {
 Options options = new Options();
 options.addOption("a", "arg", true, "An argument option");
 String[] arguments = {"-a", "value1", "value2"};
 String[] expected = {"-a", "value1", "value2"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenDoubleDash() {
 Options options = new Options();
 String[] arguments = {"--", "test1", "test2"};
 String[] expected = {"--", "test1", "test2"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownOption() {
 Options options = new Options();
 String[] arguments = {"-x", "value"};
 String[] result = null;
 try {
 result = new GnuParser().flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 assertNull(result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenUnknownLongOption() {
 Options options = new Options();
 String[] arguments = {"--longOption", "value"};
 String[] result = null;
 try {
 result = new GnuParser().flatten(options, arguments, false);
 } catch (ParseException e) {
 // expected
 }
 assertNull(result);
 }

 @Test
 public void testFlattenStopAtNonOption() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Enable debug mode");
 String[] arguments = {"-D", "value", "nonOption"};
 String[] expected = {"-D", "value"};
 String[] result = new GnuParser().flatten(options, arguments, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenMultipleOptions() {
 Options options = new Options();
 options.addOption("D", "debug", false, "Enable debug mode");
 options.addOption("v", "verbose", false, "Enable verbose mode");
 String[] arguments = {"-D", "-v"};
 String[] expected = {"-D", "-v"};
 String[] result = new GnuParser().flatten(options, arguments, false);
 assertArrayEquals(expected, result);
 }
}