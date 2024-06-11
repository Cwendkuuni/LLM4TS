import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 2, "Option C with a double argument");
 options.addOption("d", 3, "Option D with a long argument");
 }

 @After
 public void teardown() {
 parser = null;
 }

 @Test
 public void testEmptyArgs() throws ParseException {
 String[] args = {};
 String[] expected = {};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testSingleArg() throws ParseException {
 String[] args = {"arg1"};
 String[] expected = {"arg1"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testSingleShortOption() throws ParseException {
 String[] args = {"-a"};
 boolean expectedArg = true;
 String[] actual = parser.parse(options, args, false);
 assertEquals(0, actual.length);
 assertSame(options.findOption("a"), parser.getCurrentOption());
 assertEquals(expectedArg, parser.getCurrentOption().isSet());
 }

 @Test
 public void testSingleLongOption() throws ParseException {
 String[] args = {"--optionA"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(0, actual.length);
 assertNull(parser.getCurrentOption());
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() throws ParseException {
 String[] args = {"-z"};
 String[] actual = parser.parse(options, args, false);
 }

 @Test(expected = ParseException.class)
 public void testOptionWithArg() throws ParseException {
 String[] args = {"-a", "arg1"};
 String[] expected = {"-a"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 assertSame(options.findOption("a"), parser.getCurrentOption());
 }

 @Test(expected = ParseException.class)
 public void testOptionWithArgAndSecondArg() throws ParseException {
 String[] args = {"-a", "arg1", "arg2"};
 }

 @Test
 public void testOptionWithArgAndFinalArg() throws ParseException {
 String[] args = {"-a", "arg1", "--"};
 String[] expected = {"-a", "--"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleOptions() throws ParseException {
 String[] args = {"-a", "--optionB=1", "-c", "3.14", "--optionD=42"};
 String[] expected = {"-a", "--optionB", "--optionD"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 assertSame(options.findOption("a"), parser.getCurrentOption());
 assertSame(options.findOption("b"), parser.getCurrentOption());
 assertSame(options.findOption("d"), parser.getCurrentOption());
 }

 @Test(expected = ParseException.class)
 public void testMultipleOptionsWithInvalidArg() throws ParseException {
 String[] args = {"-a", "--optionB=1", "--optionB=2"};
 }

 @Test
 public void testMultipleOptionsWithArgAndNoArg() throws ParseException {
 String[] args = {"-a", "--optionB=1", "-c"};
 String[] expected = {"-a", "--optionB", "-c"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleOptionsWithArgAndArg() throws ParseException {
 String[] args = {"-a", "--optionB=1", "--optionB=2"};
 String[] expected = {"-a", "--optionB", "--optionB"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleOptionsWithArgAndLongArg() throws ParseException {
 String[] args = {"-a", "--optionB=1", "--optionC=3.14"};
 String[] expected = {"-a", "--optionB", "--optionC"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testMultipleOptionsWithLongArgAndArg() throws ParseException {
 String[] args = {"-a", "--optionB=1", "--optionC=3.14", "--optionB=2"};
 String[] expected = {"-a", "--optionB", "--optionB", "--optionC"};
 String[] actual = parser.parse(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 options.addOption("stop", false, "Stop parsing options");
 String[] args = {"-a", "--optionB", "--stop", "--optionD"};
 String[] expected = {"-a", "--optionB"};
 String[] actual = parser.parse(options, args, true);
 assertEquals(expected, actual);
 }
}