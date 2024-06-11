import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;

public class PosixParserTest {

 private Parser parser;
 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 1, "Option B");
 options.addOption("c", 2, "Option C with arg");
 options.addOption("d", 3, "Option D with args");
 parser = new PosixParser();
 }

 @Test
 public void testFlattenSimple() {
 String[] args = {"--a", "--b", "-c", "value", "-d"};
 String[] expected = {"--a", "--b", "-c", "value", "-d"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOption() {
 String[] args = {"--a", "--b", "-c", "value", "-d", "nonOption"};
 String[] expected = {"--a", "--b", "-c", "value", "--", "nonOption"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOption() {
 String[] args = {"--longOptionWithArg=value"};
 String[] expected = {"--longOptionWithArg", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongAndShortOption() {
 String[] args = {"--longOptionWithArg=value", "-a", "-c", "value"};
 String[] expected = {"--longOptionWithArg", "value", "--a", "-c", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultipleLongOptions() {
 String[] args = {"--longOptionA", "--longOptionB"};
 String[] expected = {"--longOptionA", "--longOptionB"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultipleLongOptionsAndArgs() {
 String[] args = {"--longOptionA", "--longOptionB=value"};
 String[] expected = {"--longOptionA", "--longOptionB", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptionAndShortOption() {
 String[] args = {"--longOptionA=value", "-a"};
 String[] expected = {"--longOptionA", "value", "--a"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptionAndShortOptionWithArgs() {
 String[] args = {"--longOptionA=value", "-a", "value2"};
 String[] expected = {"--longOptionA", "value", "--a", "value2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptionAndMultipleShortOptions() {
 String[] args = {"--longOptionA=value", "-a", "-b", "-c"};
 String[] expected = {"--longOptionA", "value", "--a", "--b", "--c"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithLongOptionAndMultipleShortOptionsWithArgs() {
 String[] args = {"--longOptionA=value", "-a", "value2", "-b", "value3"};
 String[] expected = {"--longOptionA", "value", "--a", "value2", "--b", "value3"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithOptionWithArgs() {
 String[] args = {"-c", "value"};
 String[] expected = {"-c", "value"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithOptionWithArgsAndOtherOptions() {
 String[] args = {"-c", "value", "--a", "-b"};
 String[] expected = {"-c", "value", "--a", "--b"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithComplexArguments() {
 String[] args = {"--longOptionWithArg=value--longOptionWithArgs=value2", "-a", "-c"};
 String[] expected = {"--longOptionWithArg", "value--longOptionWithArgs", "value2", "--a", "-c"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultiArgOption() {
 String[] args = {"-d", "value1", "value2"};
 String[] expected = {"-d", "value1", "value2"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultiArgOptionAndOtherOptions() {
 String[] args = {"-d", "value1", "value2", "--a", "-b"};
 String[] expected = {"-d", "value1", "value2", "--a", "--b"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultiArgOptionAndValues() {
 String[] args = {"-d", "value1", "value2", "value3"};
 String[] expected = {"-d", "value1", "value2", "value3"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithMultiArgOptionAndValuesAndOtherOptions() {
 String[] args = {"-d", "value1", "value2", "value3", "--a", "-b"};
 String[] expected = {"-d", "value1", "value2", "value3", "--a", "--b"};
 String[] result = parser.flatten(options, args, false);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionAndEmptyArgs() {
 String[] args = {};
 String[] expected = {};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionAndArgs() {
 String[] args = {"--a", "--b", "-c", "value", "-d", "nonOption"};
 String[] expected = {"--a", "--b", "--", "nonOption"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionAndArgsWithOptions() {
 String[] args = {"--a", "--b", "-c", "value", "-d", "--longOptionWithArg=value--longOptionWithArgs=value2", "nonOption"};
 String[] expected = {"--a", "--b", "--", "--longOptionWithArg", "value--longOptionWithArgs", "value2", "nonOption"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionAndArgsWithSingleArgOption() {
 String[] args = {"--a", "--b", "-c", "value", "-d", "nonOption", "value3"};
 String[] expected = {"--a", "--b", "--", "--longOptionWithArg", "value--longOptionWithArgs", "value2", "nonOption", "value3"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testFlattenWithStopAtNonOptionAndArgsWithMultipleArgOption() {
 String[] args = {"--a", "--b", "-d", "value1", "value2", "value3", "nonOption"};
 String[] expected = {"--a", "--b", "--", "--longOptionWithArg", "value1--longOptionWithArgs", "value2--longOptionWithArgs", "value3", "nonOption"};
 String[] result = parser.flatten(options, args, true);
 assertArrayEquals(expected, result);
 }

 @Test
 public void testProcessOptionToken() {
 String[] args = {"-a", "--longOptionA", "-c", "--longOptionB"};
 String[] expected = {"-a", "--longOptionA", "-c", "--longOptionB"};
 String[] result = parser.flatten(options, args, false);

 parser.init();
 parser.setOptions(options);
 int index = 0;
 for (String token : result) {
 parser.processOptionToken(token, false);
 index++;
 assertEquals(null, parser.getCurrentOption());
 if (index < result.length - 1) {
 String nextToken = result[index + 1];
 switch (nextToken) {
 case "-":
 case "--":
 break;
 default:
 assertEquals(token, nextToken.substring(0, 1));
 }
 index++;
 }
 }
 assertEquals(0, parser.getTokens().size());
 }

 @Test
 public void testProcessOptionTokenWithStopAtNonOption() {
 String[] args = {"-a", "--longOptionA", "-c", "--longOptionB", "nonOption"};
 String[] expected = {"-a", "--longOptionA", "--", "nonOption"};
 String[] result = parser.flatten(options, args, true);

 parser.init();
 parser.setOptions(options);
 int index = 0;
 for (String token : result) {
 parser.processOptionToken(token, true);
 index++;
 if (index < result.length - 1 && !"--".equals(result[index + 1])) {
 assertNull(parser.getCurrentOption());
 }
 else {
 assertEquals(null, parser.getCurrentOption());
 }
 }
 assertEquals(result.length - 1, parser.getTokens().size());
 }

 @Test
 public void testBurstToken() {
 String[] args = {"--longOptionWithArg", "value--longOptionWithArgs", "value3"};
 String[] expected = {"--longOptionWithArg", "value--longOptionWithArgs", "value3"};
 String[] result = parser.flatten(options, args, false);

 parser.init();
 parser.setOptions(options);
 parser.burstToken("--longOptionWithArg", false);
 assertEquals("--longOptionWithArg", parser.getTokens().get(0));
 assertEquals("-longOptionWithArg", parser.getTokens().get(1));
 assertEquals("value", parser.getTokens().get(2));
 assertEquals("--longOptionWithArgs", parser.getTokens().get(3));
 assertEquals("value2", parser.getTokens().get(4));
 assertEquals("value3", parser.getTokens().get(5));
 assertEquals(6, parser.getTokens().size());
 }

 @Test
 public void testBurstTokenWithStopAtNonOption() {
 String[] args = {"--longOptionWithArg", "value--longOptionWithArgs", "value3", "nonOption"};
 String[] expected = {"--longOptionWithArg", "value--longOptionWithArgs", "--", "nonOption"};
 String[] result = parser.flatten(options, args, true);

 parser.init();
 parser.setOptions(options);
 parser.burstToken("--longOptionWithArg", true);
 assertEquals("--longOptionWithArg", parser.getTokens().get(0));
 assertEquals("-longOptionWithArg", parser.getTokens().get(1));
 assertEquals("value", parser.getTokens().get(2));
 assertEquals("--", parser.getTokens().get(3));
 assertEquals("nonOption", parser.getTokens().get(4));
 assertEquals(5, parser.getTokens().size());
 }
}