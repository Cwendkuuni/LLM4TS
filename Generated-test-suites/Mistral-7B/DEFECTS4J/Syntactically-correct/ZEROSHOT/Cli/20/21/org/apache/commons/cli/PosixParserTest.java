import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private Options options;
 private PosixParser parser;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", 2, "Option B with an argument");
 options.addOption("c", 3, "Option C with an argument");
 }

 @Test
 public void testFlatten_NoOptions() throws ParseException {
 String[] args = new String[0];
 String[] expected = new String[0];
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_SimpleOptions() throws ParseException {
 String[] args = {"--a"};
 String[] expected = {"--a"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_SingleDash() throws ParseException {
 String[] args = {"-"};
 String[] expected = {"--"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_LongOption() throws ParseException {
 String[] args = {"--long-option"};
 String[] expected = {"--long-option"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOption() throws ParseException {
 String[] args = {"-a"};
 String[] expected = {"-a"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithArg() throws ParseException {
 String[] args = {"-a", "arg"};
 String[] expected = {"-a", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_MultipleShortOptions() throws ParseException {
 String[] args = {"-a", "-b", "arg"};
 String[] expected = {"-a", "-b", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_LongOptionWithArg() throws ParseException {
 String[] args = {"--long-option", "arg"};
 String[] expected = {"--long-option", "arg"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithLongOption() throws ParseException {
 String[] args = {"-a", "--long-option"};
 String[] expected = {"-a", "--long-option"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_ShortOptionWithArgAndLongOption() throws ParseException {
 String[] args = {"-a", "arg", "--long-option", "arg2"};
 String[] expected = {"-a", "arg", "--long-option", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_ArgumentConsumingShortOption() throws ParseException {
 String[] args = {"-a", "arg1", "arg2"};
 String[] expected = {"-a", "arg1", "--", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_ArgumentConsumingShortOptionWithLongOption() throws ParseException {
 String[] args = {"-a", "arg1", "--long-option", "arg2"};
 String[] expected = {"-a", "arg1", "--long-option", "arg2"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOption() throws ParseException {
 String[] args = {"--", "foo"};
 String[] expected = {"--", "foo"};
 String[] result = parser.flatten(options, args, true);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOptionWithArguments() throws ParseException {
 String[] args = {"--", "foo", "bar"};
 String[] expected = {"--", "foo", "bar"};
 String[] result = parser.flatten(options, args, true);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOptionWithOption() throws ParseException {
 String[] args = {"--", "-a"};
 String[] expected = {"--", "-a"};
 String[] result = parser.flatten(options, args, true);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_StopAtNonOptionWithOptionAndArguments() throws ParseException {
 String[] args = {"--", "-a", "arg"};
 String[] expected = {"--", "-a", "arg"};
 String[] result = parser.flatten(options, args, true);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_MultipleArguments() throws ParseException {
 String[] args = {"arg1", "arg2", "arg3"};
 String[] expected = {"arg1", "arg2", "arg3"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_MultipleArgumentsConsumingOption() throws ParseException {
 String[] args = {"arg1", "arg2", "--", "arg3"};
 String[] expected = {"arg1", "arg2", "--", "arg3"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_MultipleArgumentsConsumingOptionWithArguments() throws ParseException {
 String[] args = {"arg1", "arg2", "--long-option", "arg3"};
 String[] expected = {"arg1", "arg2", "--long-option", "arg3"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_MultipleArgumentsConsumingOptionWithArgumentsAndShortOption() throws ParseException {
 String[] args = {"arg1", "arg2", "--long-option", "arg3", "-a"};
 String[] expected = {"arg1", "arg2", "--long-option", "arg3", "-a"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_MultipleArgumentsConsumingOptionWithArgumentsAndLongOption() throws ParseException {
 String[] args = {"arg1", "arg2", "--long-option", "arg3", "--longer-option"};
 String[] expected = {"arg1", "arg2", "--long-option", "arg3", "--longer-option"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testFlatten_MultipleArgumentsConsumingOptionWithArgumentsAndLongOptionWithArgument() throws ParseException {
 String[] args = {"arg1", "arg2", "--long-option", "arg3", "--longer-option", "arg4"};
 String[] expected = {"arg1", "arg2", "--long-option", "arg3", "--longer-option", "arg4"};
 String[] result = parser.flatten(options, args, false);
 assertEquals(expected, result);
 }

 @Test
 public void testBurstToken_ValidOption() {
 String token = "-a";
 parser.init();
 parser.processOptionToken(token, false);
 assertNotNull(parser.currentOption);
 assertEquals("Option A", parser.currentOption.getLongOpt());
 assertEquals(true, parser.currentOption.hasArg());
 assertEquals(true, parser.currentOption.isRequired());
 }

 @Test
 public void testBurstToken_InvalidOption() {
 String token = "invalid";
 parser.init();
 parser.processOptionToken(token, false);
 assertNull(parser.currentOption);
 }

 @Test
 public void testBurstToken_InvalidOptionWithStopAtNonOption() {
 String token = "invalid";
 parser.init();
 parser.processOptionToken(token, true);
 assertEquals(Arrays.asList("--", token).toArray(new String[2]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testBurstToken_ValidOptionWithArgument() {
 String token = "-a arg";
 parser.init();
 parser.processOptionToken(token, false);
 assertNotNull(parser.currentOption);
 assertEquals("Option A", parser.currentOption.getLongOpt());
 assertEquals(true, parser.currentOption.hasArg());
 assertEquals(true, parser.currentOption.isRequired());
 assertEquals("arg", parser.tokens.get(parser.tokens.size() - 1));
 }

 @Test
 public void testBurstToken_ValidOptionWithMultipleArguments() {
 String token = "-a arg1 arg2";
 parser.init();
 parser.processOptionToken(token, false);
 assertNotNull(parser.currentOption);
 assertEquals("Option A", parser.currentOption.getLongOpt());
 assertEquals(true, parser.currentOption.hasArgs());
 assertEquals(true, parser.currentOption.isRequired());
 assertEquals("arg1", parser.tokens.get(parser.tokens.size() - 2));
 assertEquals("arg2", parser.tokens.get(parser.tokens.size() - 1));
 }

 @Test
 public void testBurstToken_ValidOptionWithMultipleArgumentsAndStopAtNonOption() {
 String token = "-a arg1 arg2 --";
 parser.init();
 parser.processOptionToken(token, true);
 assertEquals(Arrays.asList("--", "-a", "arg1", "arg2").toArray(new String[4]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testBurstToken_ValidOptionWithMultipleArgumentsAndLongOption() {
 String token = "--long-option arg1 arg2";
 parser.init();
 parser.processOptionToken(token, false);
 assertNotNull(parser.currentOption);
 assertEquals("longOption", parser.currentOption.getLongOpt());
 assertEquals(2, parser.currentOption.getArgCount());
 assertEquals("arg1", parser.tokens.get(parser.tokens.size() - 2));
 assertEquals("arg2", parser.tokens.get(parser.tokens.size() - 1));
 }

 @Test
 public void testBurstToken_ValidOptionWithMultipleArgumentsAndLongOptionWithArgument() {
 String token = "--long-option arg1 arg2 arg3";
 parser.init();
 parser.processOptionToken(token, false);
 assertNotNull(parser.currentOption);
 assertEquals("longOption", parser.currentOption.getLongOpt());
 assertEquals(3, parser.currentOption.getArgCount());
 assertEquals("arg1", parser.tokens.get(parser.tokens.size() - 3));
 assertEquals("arg2", parser.tokens.get(parser.tokens.size() - 2));
 assertEquals("arg3", parser.tokens.get(parser.tokens.size() - 1));
 }

 @Test
 public void testProcess() {
 String token = "arg1 arg2";
 parser.init();
 parser.process(token);
 assertEquals(Arrays.asList("--", token).toArray(new String[2]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithCurrentOption() {
 String token = "-a arg";
 parser.init();
 parser.currentOption = options.getOption("a");
 parser.process(token);
 assertEquals(Arrays.asList("-a", "arg").toArray(new String[2]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithLongOption() {
 String token = "--long-option arg";
 parser.init();
 parser.process(token);
 assertEquals(Arrays.asList("--long-option", "arg").toArray(new String[2]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithLongOptionAndArgument() {
 String token = "--long-option arg1 arg2";
 parser.init();
 parser.process(token);
 assertEquals(Arrays.asList("--long-option", "arg1", "arg2").toArray(new String[3]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithCurrentOptionAndArguments() {
 String token = "-a arg1 arg2";
 parser.init();
 parser.currentOption = options.getOption("a");
 parser.process(token);
 assertEquals(Arrays.asList("-a", "arg1", "arg2", "--").toArray(new String[4]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithLongOptionAndArguments() {
 String token = "--long-option arg1 arg2";
 parser.init();
 parser.process(token);
 assertEquals(Arrays.asList("--long-option", "arg1", "arg2").toArray(new String[3]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithLongOptionAndArgumentsAndCurrentOption() {
 String token = "--long-option arg1 arg2 -a arg3";
 parser.init();
 parser.currentOption = options.getOption("a");
 parser.process(token);
 assertEquals(Arrays.asList("--long-option", "arg1", "arg2", "-a", "arg3").toArray(new String[5]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithLongOptionAndArgumentsAndArguments() {
 String token = "--long-option arg1 arg2 arg3";
 parser.init();
 parser.process(token);
 assertEquals(Arrays.asList("--long-option", "arg1", "arg2", "arg3").toArray(new String[4]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithLongOptionAndArgumentsAndArgumentsAndCurrentOption() {
 String token = "--long-option arg1 arg2 arg3 -a arg4";
 parser.init();
 parser.currentOption = options.getOption("a");
 parser.process(token);
 assertEquals(Arrays.asList("--long-option", "arg1", "arg2", "arg3", "-a", "arg4").toArray(new String[6]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testProcess_WithLongOptionAndArgumentsAndArgumentsAndArguments() {
 String token = "--long-option arg1 arg2 arg3 arg4";
 parser.init();
 parser.process(token);
 assertEquals(Arrays.asList("--long-option", "arg1", "arg2", "arg3", "arg4").toArray(new String[5]), parser.tokens.toArray(new String[0]));
 }

 @Test
 public void testInit() {
 parser = new PosixParser();
 assertTrue(parser.tokens.isEmpty());
 assertFalse(parser.eatTheRest);
 assertNull(parser.currentOption);
 }
}