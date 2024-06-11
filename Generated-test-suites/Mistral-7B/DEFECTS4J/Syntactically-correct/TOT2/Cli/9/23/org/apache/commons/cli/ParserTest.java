import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.MissingArgumentException;
import org.apache.commons.cli.MissingOptionException;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.UnrecognizedOptionException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 private static final String[] TEST_OPTIONS = new String[]{"--option1", "--option2=value", "--option3"};
 private static final String[] TEST_ARGUMENTS = new String[]{"--argument1", "argument2", "--option4"};

 @Test(expected = ParseException.class)
 public void testParseWithNullOptions() {
 Parser parser = new DefaultParser();
 String[] arguments = new String[0];
 parser.parse(null, arguments);
 }

 @Test(expected = ParseException.class)
 public void testParseWithNullArguments() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 parser.parse(options, null);
 }

 @Test
 public void testParseWithNoOptionsOrArguments() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = new String[0];
 try {
 CommandLine cmd = parser.parse(options, arguments);
 Assert.assertEquals(0, cmd.getArgs().size());
 } catch (ParseException e) {
 // should not reach here
 }
 }

 @Test
 public void testParseWithOptionsOnly() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 options.addOption("option3", true, "o3");
 String[] arguments = new String[0];
 try {
 CommandLine cmd = parser.parse(options, arguments);
 Assert.assertEquals(3, cmd.getOptions().size());
 } catch (ParseException e) {
 // should not reach here
 }
 }

 @Test
 public void testParseWithArgumentsOnly() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 String[] arguments = TEST_ARGUMENTS;
 try {
 CommandLine cmd = parser.parse(options, arguments);
 Assert.assertEquals(3, cmd.getArgs().size());
 Assert.assertEquals(TEST_ARGUMENTS[0], cmd.getArgs().get(0));
 Assert.assertEquals(TEST_ARGUMENTS[1], cmd.getArgs().get(1));
 Assert.assertEquals(TEST_ARGUMENTS[2], cmd.getArgs().get(2));
 } catch (ParseException e) {
 // should not reach here
 }
 }

 @Test
 public void testParseWithOptionsAndArguments() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 options.addOption("option3", true, "o3");
 String[] arguments = TEST_ARGUMENTS;
 try {
 CommandLine cmd = parser.parse(options, arguments);
 Assert.assertEquals(3, cmd.getOptions().size());
 Assert.assertEquals(TEST_ARGUMENTS[0], cmd.getArgs().get(0));
 Assert.assertEquals(TEST_ARGUMENTS[1], cmd.getArgs().get(1));
 Assert.assertEquals(TEST_ARGUMENTS[2], cmd.getArgs().get(2));
 } catch (ParseException e) {
 // should not reach here
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 options.addOption("invalidOption", true, "io");
 String[] arguments = new String[0];
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testParseWithUnrecognizedOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 String[] arguments = new String[]{"--unknownOption"};
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 // should not reach here
 } catch (UnrecognizedOptionException e) {
 // expected
 }
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOption() {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "o1");
 options.addRequiredOption("option2", true, "o2");
 String[] arguments = new String[0];
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 // expected
 Assert.assertTrue(e.getMessage().contains("Missing required option"));
 }
 }

 @Test(expected = MissingArgumentException.class)
 public void testParseMissingOptionArgument() throws ParseException {
 Parser parser = new DefaultParser();
 Options options = new Options();
 options.addOption("option1", true, "o1");
 options.addOption("option2", true, "o2");
 options.addOption("option3", true, "o3").setValueSeparator('=');
 options.addOption("option4", true, "o4").hasArg();
 String[] arguments = TEST_ARGUMENTS;
 try {
 parser.parse(options, arguments);
 } catch (ParseException e) {
 // should not reach here
 }
 CommandLine cmd = parser.parse(options, arguments);
 cmd.getOption("option4").setValue("value4");
 try {
 parser.parse(options, arguments);
 } catch (MissingArgumentException e) {
 // expected
 }
 }
}