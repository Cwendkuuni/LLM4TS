import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

 private PosixParser parser;
 private Options options;

 @Before
 public void setup() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B");
 options.addOption("c", 3, "Option C with an int value");
 }

 @Test
 public void testFlattenWithEmptyArgs() {
 String[] args = {};
 String[] expected = {};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenSimpleOptions() {
 String[] args = {"-a", "--b"};
 String[] expected = {"-a", "--b"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOptionWithValue() {
 String[] args = {"-a", "--b=value"};
 String[] expected = {"-a", "--b=value"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenOptionWithMultiValue() {
 String[] args = {"-a", "--b=value1", "--b=value2"};
 String[] expected = {"-a", "--b=value1", "--b=value2"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test(expected = ParseException.class)
 public void testFlattenInvalidOption() {
 String[] args = {"-z"};
 String[] expected = {};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 Assert.assertEquals("Invalid option: z", parser.getParsingErrorMessage());
 }

 @Test(expected = ParseException.class)
 public void testFlattenMissingArgument() {
 String[] args = {"-c"};
 String[] expected = {};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 Assert.assertEquals("Missing value for option: c", parser.getParsingErrorMessage());
 }

 @Test
 public void testFlattenLongOption() {
 String[] args = {"--longOption"};
 String[] expected = {"--longOption"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValue() {
 String[] args = {"--longOption=value"};
 String[] expected = {"--longOption=value"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValueAndMultipleOccurrences() {
 String[] args = {"--longOption=value1", "--longOption=value2"};
 String[] expected = {"--longOption=value1", "--longOption=value2"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValueAndSingleHyphen() {
 String[] args = {"--longOption=value", "--"};
 String[] expected = {"--longOption=value", "--"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValueAndDoubleHyphen() {
 String[] args = {"--longOption=value", "--"};
 String[] expected = {"--longOption=value", "--"};
 parser.flatten(options, args, false);
 String[] args2 = {"--longOption=value", "--longOption", "--"};
 String[] result2 = parser.flatten(options, args2, false);
 Assert.assertArrayEquals(Arrays.concat(expected, new String[]{"--longOption"}), Arrays.concat(result, new String[]{"--longOption"}));
 }

 @Test
 public void testFlattenLongOptionWithValuesAndSingleHyphen() {
 String[] args = {"--longOption=value1,value2", "--"};
 String[] expected = {"--longOption=value1", "--longOption=value2", "--"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenLongOptionWithValuesAndDoubleHyphen() {
 String[] args = {"--longOption=value1,value2", "--longOption", "--"};
 String[] expected = {"--longOption=value1", "--longOption=value2", "--longOption"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(Arrays.concat(expected, new String[]{"--longOption"}), Arrays.concat(result, new String[]{"--longOption"}));
 }

 @Test
 public void testFlattenLongOptionWithValuesMultipleOccurrences() {
 String[] args = {"--longOption=value1,value2", "--longOption=value3,value4"};
 String[] expected = {"--longOption=value1", "--longOption=value2", "--longOption=value3", "--longOption=value4"};
 String[] result = parser.flatten(options, args, false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 String[] args = {"-a", "nonOption", "--b"};
 String[] expected = {"-a", "nonOption"};
 String[] result = parser.flatten(options, args, true);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstToken() {
 String[] args = {"--option-a=value1", "--option-a=value2", "--option-b"};
 String[] expected = {"-option-a", "--option-a=value1", "--option-a=value2", "--option-b"};
 String[] result = parser.burstToken("--option-a=value1--option-a=value2--option-b", false);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 String[] args = {"--option-a=value1--option-a=value2--option-b"};
 String[] expected = {"-option-a", "--option-a=value1", "--option-a=value2", "--option-b"};
 String[] result = parser.burstToken("--option-a=value1--option-a=value2--option-b", true);
 Assert.assertArrayEquals(expected, result);
 }

 @Test
 public void testProcessOptionToken() {
 String[] args = {"-a", "--b"};
 parser.init();
 parser.processOptionToken("-a", false);
 Assert.assertNull(parser.currentOption);
 Assert.assertEquals(1, parser.tokens.size());
 Assert.assertEquals("-a", parser.tokens.get(0));
 parser.init();
 parser.processOptionToken("--b", false);
 Assert.assertNotNull(parser.currentOption);
 Assert.assertEquals("b", parser.currentOption.getKey());
 Assert.assertEquals(2, parser.tokens.size());
 Assert.assertEquals("-b", parser.tokens.get(1));
 }

 @Test
 public void testProcess() {
 String[] args = {"-a", "--b=value1", "--b=value2", "nonOption"};
 parser.init();
 parser.process(args[0]);
 Assert.assertEquals(1, parser.tokens.size());
 Assert.assertEquals("-a", parser.tokens.get(0));
 parser.init();
 parser.process(args[1]);
 parser.process(args[2]);
 Assert.assertEquals(4, parser.tokens.size());
 Assert.assertEquals("-b=value1", parser.tokens.get(2).toString());
 Assert.assertEquals("--b=value2", parser.tokens.get(3).toString());
 parser.init();
 parser.process("nonOption");
 Assert.assertEquals(5, parser.tokens.size());
 }

 @Test
 public void testGobble() {
 String[] args = {"--option-a=value1--option-a=value2--option-b"};
 parser.init();
 parser.burstToken("--option-a=value1--option-a=value2--option-b", false);
 parser.gobble(Arrays.asList(args).iterator());
 Assert.assertEquals(5, parser.tokens.size());
 Assert.assertEquals("--option-b", parser.tokens.get(parser.tokens.size() - 1).toString());
 parser.init();
 parser.burstToken("--option-a=value1--option-a=value2--option-b", true);
 parser.gobble(Arrays.asList(args).iterator());
 Assert.assertEquals(5, parser.tokens.size());
 }
}