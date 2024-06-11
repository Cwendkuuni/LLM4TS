import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options createOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an argument");
 options.addOption("c", 2, "Option C with two arguments");
 options.addOption("d", 3, "Option D with three arguments");
 return options;
 }

 @Test
 public void testEmptyArgs() throws ParseException {
 String[] args = {};
 String[] expected = {};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testSingleArg() throws ParseException {
 String[] args = {"--a"};
 String[] expected = {"--a"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testSingleArgWithValue() throws ParseException {
 String[] args = {"--a", "value"};
 String[] expected = {"--a", "value"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testDoubleHyphen() throws ParseException {
 String[] args = {"--", "--a"};
 String[] expected = {"--a"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testSingleOption() throws ParseException {
 String[] args = {"-a"};
 String[] expected = {"-a"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testOptionWithArg() throws ParseException {
 String[] args = {"-a", "value"};
 String[] expected = {"-a", "value"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testOptionWithTwoArgs() throws ParseException {
 String[] args = {"-c", "value1", "value2"};
 String[] expected = {"-c", "value1", "value2"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testOptionWithThreeArgs() throws ParseException {
 String[] args = {"-d", "value1", "value2", "value3"};
 String[] expected = {"-d", "value1", "value2", "value3"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testMultipleOptions() throws ParseException {
 String[] args = {"-a", "-b", "valueB", "-c", "valueC1", "valueC2"};
 String[] expected = {"-a", "-b", "valueB", "-c", "valueC1", "valueC2"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testOptionWithLongArg() throws ParseException {
 String[] args = {"-d", "longOptionValue"};
 String[] expected = {"-d", "longOptionValue"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 String[] args = {"-i", "value"}; // invalid option
 String[] options = createOptions().toArray(new String[0]);
 new PosixParser().parse(options, args);
 }

 @Test(expected = ParseException.class)
 public void testInvalidOptionWithValue() throws ParseException {
 String[] args = {"-i", "value"}; // invalid option
 String[] expected = {"--"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 String[] args = {"--", "-a", "-b", "valueB", "nonOption"};
 String[] expected = {"-a", "valueB", "nonOption"};
 String[] result = new PosixParser().flatten(createOptions(), args, true);
 assertEquals(expected, result);
 }

 @Test
 public void testBursting() throws ParseException {
 String[] args = {"-abc"};
 String[] expected = {"-a", "-c"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstingWithStopAtNonOption() throws ParseException {
 String[] args = {"-abc", "nonOption"};
 String[] expected = {"-a", "-c", "nonOption"};
 String[] result = new PosixParser().flatten(createOptions(), args, true);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstingLongOption() throws ParseException {
 String[] args = {"-dlongOptionName"};
 String[] expected = {"-d", "longOptionName"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstingLongOptionWithValue() throws ParseException {
 String[] args = {"-dlongOptionName", "value"};
 String[] expected = {"-d", "longOptionName", "value"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstingLongOptionWithArgs() throws ParseException {
 String[] args = {"-dlongOptionName", "value1", "value2"};
 String[] expected = {"-d", "longOptionName", "value1", "value2"};
 String[] result = new PosixParser().flatten(createOptions(), args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testGetCurrentOption() throws ParseException {
 String[] args = {"-a", "valueA", "-b", "valueB"};
 Option currentOption = new PosixParser().flatten(createOptions(), args, false)[0];
 assertEquals("Option A", currentOption.getLongOpt());
 assertEquals(true, currentOption.hasArg());
 assertEquals("valueA", currentOption.getValue());
 }

 @Test
 public void testGetCurrentOptionWithLongName() throws ParseException {
 String[] args = {"-longOptionName", "value"};
 Option currentOption = new PosixParser().flatten(createOptions(), args, false)[0];
 assertEquals("longOptionName", currentOption.getLongOpt());
 assertEquals(true, currentOption.hasArg());
 assertEquals("value", currentOption.getValue());
 }

 @Test
 public void testGetCurrentOptionWithNoArgs() throws ParseException {
 String[] args = {"-a"};
 Option currentOption = new PosixParser().flatten(createOptions(), args, false)[0];
 assertEquals("Option A", currentOption.getLongOpt());
 assertEquals(false, currentOption.hasArg());
 assertNull(currentOption.getValue());
 }

 @Test
 public void testGetCurrentOptionWithValueAndArgs() throws ParseException {
 String[] args = {"-c", "valueC1", "valueC2"};
 Option currentOption = new PosixParser().flatten(createOptions(), args, false)[0];
 assertEquals("Option C", currentOption.getLongOpt());
 assertEquals(2, currentOption.getArgs().length);
 assertEquals("valueC1", currentOption.getArgs()[0]);
 assertEquals("valueC2", currentOption.getArgs()[1]);
 }
}