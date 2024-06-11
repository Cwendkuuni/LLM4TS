import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 @Test
 public void testEmptyArgumentArray() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 String[] arguments = {};
 String[] expected = {};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testSimpleOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 String[] arguments = {"-a"};
 String[] expected = {"-a"};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testOptionWithArgument() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 options.addOption("b", 1, "A help message for option b");
 String[] arguments = {"-a", "valueForA", "-b", "123"};
 String[] expected = {"-a", "valueForA", "-b", "123"};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testTwoOptions() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 options.addOption("b", 1, "A help message for option b");
 String[] arguments = {"-a", "valueForA", "-b", "123", "--"};
 String[] expected = {"-a", "valueForA", "--", "-b", "123"};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testMultipleArguments() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 options.addOption("b", 1, "A help message for option b");
 String[] arguments = {"--", "-a", "valueForA", "-b", "valueForB"};
 String[] expected = {"--", "-a", "valueForA", "-b", "valueForB"};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testLongOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOptionName", true, "A help message for long option name");
 String[] arguments = {"--longOptionName"};
 String[] expected = {"--longOptionName"};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testLongOptionWithArgument() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("longOptionName", true, "A help message for long option name");
 options.addOption("b", 1, "A help message for option b");
 String[] arguments = {"--longOptionName=valueForLongOption", "-b", "123"};
 String[] expected = {"--longOptionName=valueForLongOption", "-b", "123"};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 String[] arguments = {"-a", "--", "nonOption"};
 String[] expected = {"-a"};
 String[] result = parser.flatten(options, arguments, true);
 assertEquals(expected, result);
 }

 @Test
 public void testInvalidOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 String[] arguments = {"-z"};
 String[] expected = {};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBursting() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 options.addOption("ab", true, "A help message for option ab");
 String[] arguments = {"-ab", "valueForAb"};
 String[] expected = {"-a", "-b", "valueForAb"};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstingWithArgument() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 options.addOption("ab", true, "A help message for option ab");
 options.addOption("c", 1, "A help message for option c");
 String[] arguments = {"-ab=valueForAbC", "123"};
 String[] expected = {"-a", "-b", "valueForAbC", "-c", "123"};
 String[] result = parser.flatten(options, arguments, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstingWithStopAtNonOption() throws ParseException {
 PosixParser parser = new PosixParser();
 Options options = new Options();
 options.addOption("a", true, "A help message for option a");
 options.addOption("ab", true, "A help message for option ab");
 String[] arguments = {"-ab=valueForAb", "--", "nonOption"};
 String[] expected = {"-a", "-b", "valueForAb"};
 String[] result = parser.flatten(options, arguments, true);
 assertEquals(expected, result);
 }
}