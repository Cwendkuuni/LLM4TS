import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B with an integer argument");
 options.addOption("c", 2, "Option C with a required integer argument");
 options.addOption("d", 3, "Option D with a required integer argument and a long argument name");
 }

 @Test
 public void testEmptyArguments() {
 String[] arguments = {};
 String[] expected = {};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 // empty arguments should not cause a parse exception
 }
 }

 @Test
 public void testShortOption() {
 String[] arguments = {"-a"};
 String[] expected = {"-a"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testShortOptionWithValue() {
 String[] arguments = {"-a", "value"};
 String[] expected = {"-a", "value"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testLongOption() {
 String[] arguments = {"--option-a"};
 String[] expected = {"--option-a"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testLongOptionWithValue() {
 String[] arguments = {"--option-a", "value"};
 String[] expected = {"--option-a", "value"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testShortOptionWithArgument() {
 String[] arguments = {"-a", "arg"};
 String[] expected = {"-a", "arg"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testShortOptionWithRequiredArgument() {
 String[] arguments = {"-c"};
 String[] expected = {"-c"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 fail("Test should not throw a parse exception");
 }
 }

 @Test(expected = ParseException.class)
 public void testShortOptionWithRequiredArgumentMissing() {
 String[] arguments = {"-c"};
 String[] expected = {"-c"};
 try {
 String[] result = parser.parse(options, arguments);
 fail("Test should throw a parse exception");
 } catch (ParseException e) {
 }
 }

 @Test
 public void testShortOptionWithRequiredArgumentValue() {
 String[] arguments = {"-c", "1"};
 String[] expected = {"-c", "1"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test(expected = ParseException.class)
 public void testShortOptionWithOptionalArgumentMissing() {
 String[] arguments = {"-a"};
 String[] expected = {"-a"};
 try {
 String[] result = parser.parse(options, arguments);
 fail("Test should throw a parse exception");
 } catch (ParseException e) {
 }
 }

 @Test
 public void testShortOptionWithOptionalArgumentValue() {
 String[] arguments = {"-a", "value"};
 String[] expected = {"-a", "value"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testLongOptionWithRequiredArgument() {
 String[] arguments = {"--option-c", "1"};
 String[] expected = {"--option-c", "1"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test(expected = ParseException.class)
 public void testLongOptionWithRequiredArgumentMissing() {
 String[] arguments = {"--option-c"};
 String[] expected = {"--option-c"};
 try {
 String[] result = parser.parse(options, arguments);
 fail("Test should throw a parse exception");
 } catch (ParseException e) {
 }
 }

 @Test(expected = ParseException.class)
 public void testLongOptionWithOptionalArgumentMissing() {
 String[] arguments = {"--option-b"};
 String[] expected = {"--option-b"};
 try {
 String[] result = parser.parse(options, arguments);
 fail("Test should throw a parse exception");
 } catch (ParseException e) {
 }
 }

 @Test
 public void testLongOptionWithOptionalArgumentValue() {
 String[] arguments = {"--option-b", "value"};
 String[] expected = {"--option-b", "value"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testLongOptionWithLongArgumentName() {
 String[] arguments = {"--my-long-option-d", "1", "2"};
 String[] expected = {"--my-long-option-d", "1", "2"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testBursting() {
 String[] arguments = {"-abc"};
 String[] expected = {"-a", "-c"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testBurstingWithStopAtNonOption() {
 Options optionsWithStopAtNonOption = new Options();
 optionsWithStopAtNonOption.addOption("a", true, "Option A");
 optionsWithStopAtNonOption.addOption("b", 1, "Option B with an integer argument");
 optionsWithStopAtNonOption.addOption("c", 2, "Option C with a required integer argument");
 optionsWithStopAtNonOption.setAllowUnknownOptions(false);

 PosixParser parserWithStopAtNonOption = new PosixParser();

 String[] arguments = {"-abc"};
 String[] expected = {"-a", "-c"};
 try {
 String[] result = parserWithStopAtNonOption.parse(optionsWithStopAtNonOption, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }

 String[] remainingArguments = {"d"};
 String[] expectedRemainingArguments = {"d"};
 try {
 String[] result = parserWithStopAtNonOption.parse(optionsWithStopAtNonOption, remainingArguments);
 fail("Test should throw a parse exception");
 } catch (ParseException e) {
 }

 assertEquals(1, parserWithStopAtNonOption.tokens.size());
 String[] parsedTokens = (String[]) parserWithStopAtNonOption.tokens.toArray(new String[parserWithStopAtNonOption.tokens.size()]);
 assertArrayEquals(new String[]{"--"}, parsedTokens);
 }

 @Test
 public void testBurstingWithLongArgumentName() {
 String[] arguments = {"--my-long-option-abcd"};
 String[] expected = {"--my-long-option-ab", "--my-long-option-cd"};
 try {
 String[] result = parser.parse(options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 }

 @Test
 public void testNullOptions() {
 PosixParser parser = new PosixParser();
 assertNull(parser.options);
 }

 @Test
 public void testNullArguments() {
 PosixParser parser = new PosixParser();
 String[] arguments = null;
 assertArrayEquals(new String[0], parser.parse(parser.options, arguments));
 }

 @Test(expected = NullPointerException.class)
 public void testNullOptionsAndArguments() {
 PosixParser parser = new PosixParser();
 assertNull(parser.options);
 String[] arguments = null;
 try {
 parser.parse(parser.options, arguments);
 fail("Test should throw a NullPointerException");
 } catch (NullPointerException e) {
 }
 }

 @Test
 public void testNullCurrentOption() {
 PosixParser parser = new PosixParser();
 parser.currentOption = null;
 assertNull(parser.currentOption);
 }

 @Test
 public void testTokensCleared() {
 PosixParser parser = new PosixParser();
 String[] arguments = {"-a", "-b"};
 String[] expected = {"-a", "-b"};
 try {
 String[] result = parser.parse(parser.options, arguments);
 assertArrayEquals(expected, result);
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test should not throw a parse exception");
 }
 assertEquals(2, parser.tokens.size());
 String[] parsedTokens = (String[]) parser.tokens.toArray(new String[parser.tokens.size()]);
 assertArrayEquals(expected, parsedTokens);

 parser.init();
 assertEquals(0, parser.tokens.size());
 }
}