import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

public class PosixParserTest {

 private static final String[] ARGS_EMPTY = new String[0];
 private static final String[] ARGS_HELP = {"-h", "--help"};
 private static final String[] ARGS_ONE_OPTION = {"-a"};
 private static final String[] ARGS_ONE_OPTION_VALUE = {"-a", "value1"};
 private static final String[] ARGS_TWO_OPTIONS = {"-a", "--b"};
 private static final String[] ARGS_TWO_OPTIONS_VALUE = {"-a", "value1", "--b", "value2"};
 private static final String[] ARGS_ONE_LONG_OPTION = {"--longOption"};
 private static final String[] ARGS_ONE_LONG_OPTION_VALUE = {"--longOption", "value1"};
 private static final String[] ARGS_INVALID = {"--a=b"};
 private static final String[] ARGS_TOO_MANY = {"-a", "--b", "c"};

 @Test
 public void testEmpty() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_EMPTY, false);
 assertNull(parsedArgs);
 }

 @Test
 public void testHelp() {
 Options options = new Options();
 options.addOption("h", "help", false, "display this help message");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_HELP, false);
 Assert.assertArrayEquals(ARGS_HELP, parsedArgs);
 }

 @Test
 public void testOneOption() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "option A");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_ONE_OPTION, false);
 assertEquals(1, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 }

 @Test
 public void testOneOptionValue() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "option A with value");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_ONE_OPTION_VALUE, false);
 assertEquals(2, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 assertEquals("value1", parsedArgs[1]);
 }

 @Test
 public void testTwoOptions() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "option A");
 options.addOption("b", "optionB", false, "option B");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_TWO_OPTIONS, false);
 assertEquals(2, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 assertEquals("optionB", parsedArgs[1]);
 }

 @Test
 public void testTwoOptionsValues() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "option A with value");
 options.addOption("b", "optionB", true, "option B with value");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_TWO_OPTIONS_VALUE, false);
 assertEquals(4, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 assertEquals("value1", parsedArgs[1]);
 assertEquals("optionB", parsedArgs[2]);
 assertEquals("value2", parsedArgs[3]);
 }

 @Test(expected = ParseException.class)
 public void testInvalid() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_INVALID, false);
 Assert.fail("Expected ParseException");
 }

 @Test
 public void testTooMany() {
 Options options = new Options();
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_TOO_MANY, false);
 assertEquals(3, parsedArgs.length);
 assertEquals("--", parsedArgs[1]);
 assertEquals("c", parsedArgs[2]);
 }

 @Test
 public void testStopAtNonOption() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "option A");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, ARGS_ONE_OPTION, true);
 assertEquals(1, parsedArgs.length);
 assertEquals("a", parsedArgs[0]);
 }

 @Test
 public void testBurstToken() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "option A with value");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, new String[]{"-a--value"}, false);
 assertEquals(3, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 assertEquals("value", parsedArgs[1]);
 assertEquals("--", parsedArgs[2]);
 }

 @Test
 public void testBurstTokenNoValue() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "option A");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, new String[]{"-aa"}, false);
 assertEquals(2, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 assertEquals("--", parsedArgs[1]);
 }

 @Test
 public void testBurstTokenLongOption() {
 Options options = new Options();
 options.addOption("longOption", "longOption", true, "long option with value");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, new String[]{"--longOption=value"}, false);
 assertEquals(2, parsedArgs.length);
 assertEquals("longOption", parsedArgs[0]);
 assertEquals("value", parsedArgs[1]);
 }

 @Test
 public void testBurstTokenLongOptionNoValue() {
 Options options = new Options();
 options.addOption("longOption", "longOption", false, "long option");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, new String[]{"--longOption"}, false);
 assertEquals(1, parsedArgs.length);
 assertEquals("longOption", parsedArgs[0]);
 }

 @Test
 public void testBurstTokenTwoOptions() {
 Options options = new Options();
 options.addOption("a", "optionA", false, "option A");
 options.addOption("b", "optionB", false, "option B");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, new String[]{"-a--b"}, false);
 assertEquals(3, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 assertEquals("optionB", parsedArgs[1]);
 assertEquals("--", parsedArgs[2]);
 }

 @Test
 public void testBurstTokenTwoOptionsValues() {
 Options options = new Options();
 options.addOption("a", "optionA", true, "option A with value");
 options.addOption("b", "optionB", true, "option B with value");
 PosixParser parser = new PosixParser();
 String[] parsedArgs = parser.flatten(options, new String[]{"-a--b=value1--c=value2"}, false);
 assertEquals(5, parsedArgs.length);
 assertEquals("optionA", parsedArgs[0]);
 assertEquals("value1", parsedArgs[1]);
 assertEquals("optionB", parsedArgs[2]);
 assertEquals("value2", parsedArgs[3]);
 assertEquals("--", parsedArgs[4]);
 }
}