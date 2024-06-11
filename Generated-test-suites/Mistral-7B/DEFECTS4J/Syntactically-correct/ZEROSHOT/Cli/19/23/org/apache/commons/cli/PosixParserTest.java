import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 2, "Option C with a long argument");
 }

 @After
 public void tearDown() {
 parser = null;
 }

 @Test
 public void testEmpty() {
 String[] args = {};
 String[] expected = {};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testSimple() {
 String[] args = {"--a"};
 String[] expected = {"--a"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testOptionWithValue() {
 String[] args = {"--a", "value"};
 String[] expected = {"--a", "value"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testOptionWithArgument() {
 String[] args = {"--a", "value1", "value2"};
 String[] expected = {"--a", "value1", "value2"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() {
 String[] args = {"--x"};
 String[] expected = {};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 //expected
 }
 }

 @Test(expected = ParseException.class)
 public void testInvalidOptionWithArguments() {
 String[] args = {"--x", "value"};
 String[] expected = {};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 //expected
 }
 }

 @Test(expected = ParseException.class)
 public void testOptionWithNoArgument() {
 String[] args = {"--a", "value", "value2"};
 String[] expected = {"--a", "value"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 //expected
 }
 }

 @Test
 public void testLongOption() {
 String[] args = {"--option-c", "123"};
 String[] expected = {"--option-c", "123"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ParseException.class)
 public void testLongOptionWithValue() {
 String[] args = {"--option-c", "123", "value"};
 String[] expected = {};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 //expected
 }
 }

 @Test
 public void testShortOption() {
 String[] args = {"-a"};
 String[] expected = {"-a"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testShortOptionWithArgument() {
 String[] args = {"-a", "value"};
 String[] expected = {"-a", "value"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ParseException.class)
 public void testShortOptionWithNoArgument() {
 String[] args = {"-a", "value", "value2"};
 String[] expected = {"-a", "value"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 //expected
 }
 }

 @Test
 public void testBursting() {
 String[] args = {"-abc"};
 String[] expected = {"-a", "-c"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ParseException.class)
 public void testBurstingWithInvalidOption() {
 String[] args = {"-xabc"};
 String[] expected = {};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 //expected
 }
 }

 @Test(expected = ParseException.class)
 public void testBurstingWithNoOption() {
 String[] args = {"abcd"};
 String[] expected = {};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 //expected
 }
 }

 @Test
 public void testStopAtNonOption() {
 String[] args = {"-a", "value", "x"};
 String[] expected = {"-a", "value"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test(expected = ParseException.class)
 public void testStopAtNonOptionWithNoOption() {
 String[] args = {"x"};
 String[] expected = {};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 //expected
 }
 }

 @Test
 public void testStopAtNonOptionWithArguments() {
 String[] args = {"x", "value"};
 String[] expected = {"x"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testStopAtNonOptionWithOption() {
 String[] args = {"-a", "value", "x"};
 String[] expected = {"-a", "value"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testStopAtNonOptionWithOptionAndArguments() {
 String[] args = {"-a", "value1", "value2", "x"};
 String[] expected = {"-a", "value1", "value2"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }

 @Test
 public void testCurrentOption() {
 String[] args = {"-a", "value1", "-b", "value2"};
 String[] expected = {"-a", "value1", "-b", "value2"};
 try {
 parser = new PosixParser();
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);

 assertEquals(options.getOption("a"), parser.getCurrentOption());
 assertNull(parser.getCurrentOption());
 } catch (ParseException e) {
 throw new RuntimeException(e);
 }
 }
}