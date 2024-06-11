import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options options;
 private String[] args;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with an argument");
 options.addOption("c", 3, "Option C with an optional argument");
 }

 @After
 public void tearDown() {
 parser = null;
 args = null;
 options = null;
 }

 @Test
 public void testFlattenEmpty() throws ParseException {
 String[] expected = new String[0];
 String[] actual = parser.flatten(options, new String[0], false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenNoOption() throws ParseException {
 args = new String[]{"arg1", "arg2"};
 String[] expected = new String[]{"arg1", "arg2"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenSingleOption() throws ParseException {
 args = new String[]{"-a"};
 String[] expected = new String[]{"-a"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenOptionWithArgument() throws ParseException {
 args = new String[]{"-a", "value"};
 String[] expected = new String[]{"-a", "value"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenOptionWithOptionalArgument() throws ParseException {
 args = new String[]{"-c", "value"};
 String[] expected = new String[]{"-c", "value"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenOptionWithLongArgument() throws ParseException {
 args = new String[]{"-c", "long value"};
 String[] expected = new String[]{"-c", "long value"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenLongOption() throws ParseException {
 args = new String[]{"--longOption"};
 String[] expected = new String[]{"--longOption"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenLongOptionWithArgument() throws ParseException {
 args = new String[]{"--longOption", "value"};
 String[] expected = new String[]{"--longOption", "value"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenDoubleHyphen() throws ParseException {
 args = new String[]{"--", "value"};
 String[] expected = new String[]{"--", "value"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testFlattenDoubleHyphenWithArguments() throws ParseException {
 args = new String[]{"--", "value1", "value2"};
 String[] expected = new String[]{"--", "value1", "value2"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testStopAtNonOption() throws ParseException {
 args = new String[]{"arg1", "-a", "-b", "arg3"};
 String[] expected = new String[]{"arg1", "-a", "-b"};
 String[] actual = parser.flatten(options, args, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testBurstTokenSimple() throws ParseException {
 args = new String[]{"-abc"};
 String[] expected = new String[]{"-a", "-c"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testBurstTokenOptionWithArgument() throws ParseException {
 args = new String[]{"-ab", "value"};
 String[] expected = new String[]{"-a", "-b", "value"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testBurstTokenOptionWithOptionalArgument() throws ParseException {
 args = new String[]{"-c", "value1", "value2"};
 String[] expected = new String[]{"-c", "value1", "value2"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testBurstTokenLongOption() throws ParseException {
 args = new String[]{"--longOption", "value1", "value2"};
 String[] expected = new String[]{"--longOption", "value1", "value2"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testBurstTokenLongOptionWithArgument() throws ParseException {
 args = new String[]{"--longOptionValue", "value"};
 String[] expected = new String[]{"--longOptionValue", "value"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testBurstTokenDoubleHyphen() throws ParseException {
 args = new String[]{"--", "arg1", "arg2"};
 String[] expected = new String[]{"--", "arg1", "arg2"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testBurstTokenDoubleHyphenWithArguments() throws ParseException {
 args = new String[]{"--", "arg1", "arg2", "arg3"};
 String[] expected = new String[]{"--", "arg1", "arg2", "arg3"};
 String[] actual = parser.flatten(options, args, false);
 assertEquals(expected, actual);
 }

 @Test
 public void testBurstTokenStopAtNonOption() throws ParseException {
 args = new String[]{"arg1", "-a", "-b", "arg3"};
 String[] expected = new String[]{"arg1", "-a"};
 String[] actual = parser.flatten(options, args, true);
 assertEquals(expected, actual);
 }

 @Test
 public void testInitialize() {
 parser = new PosixParser();
 parser.init();
 assertNull(parser.getCurrentOption());
 assertTrue(parser.getTokens().isEmpty());
 assertFalse(parser.isEatTheRest());
 }

 @Test
 public void testSetCurrentOption() {
 Option option = options.getOption("a");
 parser = new PosixParser();
 parser.init();
 parser.setCurrentOption(option);
 assertSame(option, parser.getCurrentOption());
 }

 @Test
 public void testProcessSingleHyphen() {
 parser = new PosixParser();
 parser.init();
 parser.processSingleHyphen("-");
 assertTrue(parser.getTokens().contains("-"));
 }

 @Test
 public void testProcessSingleHyphenTwice() {
 parser = new PosixParser();
 parser.init();
 parser.processSingleHyphen("-");
 parser.processSingleHyphen("-");
 assertTrue(parser.getTokens().contains("-"));
 assertTrue(parser.getTokens().contains("-"));
 }

 @Test
 public void testProcess() {
 parser = new PosixParser();
 parser.init();
 parser.process("value");
 assertTrue(parser.getTokens().contains("value"));
 }

 @Test
 public void testProcessNull() {
 parser = new PosixParser();
 parser.init();
 parser.process(null);
 assertTrue(parser.getTokens().isEmpty());
 }

 @Test
 public void testProcessOptionToken() {
 Option option = options.getOption("a");
 parser = new PosixParser();
 parser.init();
 parser.processOptionToken("-a", false);
 assertSame(option, parser.getCurrentOption());
 assertTrue(parser.getTokens().contains("-a"));
 assertNull(parser.getCurrentOption());
 }

 @Test
 public void testProcessOptionTokenWithArgument() {
 Option option = options.getOption("b");
 parser = new PosixParser();
 parser.init();
 parser.processOptionToken("-b", false);
 assertNull(parser.getCurrentOption());
 assertTrue(parser.getTokens().contains("-b"));
 parser.process("value");
 assertSame(option, parser.getCurrentOption());
 assertTrue(parser.getTokens().contains("-b"));
 assertTrue(parser.getTokens().contains("value"));
 }

 @Test
 public void testProcessOptionTokenWithOptionalArgument() {
 Option option = options.getOption("c");
 parser = new PosixParser();
 parser.init();
 parser.processOptionToken("-c", false);
 assertNull(parser.getCurrentOption());
 assertTrue(parser.getTokens().contains("-c"));
 parser.process("value1");
 parser.process("value2");
 assertSame(option, parser.getCurrentOption());
 assertTrue(parser.getTokens().contains("-c"));
 assertTrue(parser.getTokens().contains("value1"));
 assertTrue(parser.getTokens().contains("value2"));
 }

 @Test
 public void testBurstToken() {
 parser = new PosixParser();
 parser.init();
 parser.burstToken("-abc", false);
 assertTrue(parser.getTokens().contains("-a"));
 assertTrue(parser.getTokens().contains("-c"));
 }

 @Test
 public void testBurstTokenWithOptionalArgument() {
 parser = new PosixParser();
 parser.init();
 parser.burstToken("-cvalue1value2", false);
 assertTrue(parser.getTokens().contains("-c"));
 assertTrue(parser.getTokens().contains("value1"));
 assertTrue(parser.getTokens().contains("value2"));
 }

 @Test
 public void testBurstTokenLongOption() {
 parser = new PosixParser();
 parser.init();
 parser.burstToken("--longOption", false);
 assertTrue(parser.getTokens().contains("--longOption"));
 }

 @Test
 public void testBurstTokenLongOptionWithArgument() {
 parser = new PosixParser();
 parser.init();
 parser.burstToken("--longOptionValue", false);
 assertTrue(parser.getTokens().contains("--longOptionValue"));
 }

 @Test
 public void testBurstTokenDoubleHyphen() {
 parser = new PosixParser();
 parser.init();
 parser.burstToken("--", false);
 assertTrue(parser.getTokens().contains("--"));
 }

 @Test
 public void testBurstTokenDoubleHyphenWithArguments() {
 parser = new PosixParser();
 parser.init();
 parser.burstToken("--arg1arg2arg3", false);
 assertTrue(parser.getTokens().contains("--"));
 assertTrue(parser.getTokens().contains("arg1"));
 assertTrue(parser.getTokens().contains("arg2"));
 assertTrue(parser.getTokens().contains("arg3"));
 }

 @Test
 public void testBurstTokenStopAtNonOption() {
 parser = new PosixParser();
 parser.init();
 parser.burstToken("-a-b-c", true);
 assertTrue(parser.getTokens().contains("-a"));
 assertTrue(parser.getTokens().contains("-b"));
 assertTrue(parser.getTokens().contains("-c"));
 }
}