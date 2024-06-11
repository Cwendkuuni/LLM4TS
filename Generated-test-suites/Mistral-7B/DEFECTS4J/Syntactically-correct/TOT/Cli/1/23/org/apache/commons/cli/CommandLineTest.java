import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class CommandLineTest {

 @Test
 public void testEmptyCommandLine() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();

 try {
 commandLine = options.parse(new String[0]);
 } catch (ParseException e) {
 // Empty command line should not throw an exception
 }

 Assertions.assertNull(commandLine.getArgs());
 Assertions.assertEquals(0, commandLine.size());
 }

 @Test
 public void testSimpleCommandLine() {
 Options options = new Options();
 options.addOption("o", "option", false, "this is an option");
 options.addOption("a", "argument", true, "this is an argument");

 String[] args = new String[]{"-o", "value", "--arg", "argValue"};
 CommandLine commandLine = null;
 try {
 commandLine = options.parse(args);
 } catch (ParseException e) {
 // Test should pass
 }

 Assertions.assertEquals("-o", commandLine.hasOption("o"));
 Assertions.assertEquals("value", commandLine.getOptionValue("o"));
 Assertions.assertEquals("argValue", commandLine.getOptionValue("arg"));
 Assertions.assertNull(commandLine.getOptionValue("nonExistentOption"));
 }

 @Test
 public void testMissingOption() {
 Options options = new Options();
 options.addOption("o", "option", false, "this is an option");
 options.addOption("a", "argument", true, "this is an argument");

 String[] args = new String[]{"--missingOption"};
 CommandLine commandLine = null;
 try {
 commandLine = options.parse(args);
 } catch (ParseException e) {
 // Test should fail
 Assertions.assertEquals("Unrecognized option: missingOption", e.getMessage());
 }

 Assertions.assertNull(commandLine.getArgs());
 Assertions.assertEquals(0, commandLine.size());
 }

 @Test
 public void testDuplicateOption() {
 Options options = new Options();
 options.addOption("o", "option", false, "this is an option");
 options.addOption("o", "anotherOption", false, "this is another option");

 String[] args = new String[]{"-o", "value"};
 CommandLine commandLine = null;
 try {
 commandLine = options.parse(args);
 } catch (ParseException e) {
 // Test should fail
 Assertions.assertEquals("Duplicate option: o", e.getMessage());
 }

 Assertions.assertNull(commandLine.getArgs());
 Assertions.assertEquals(0, commandLine.size());
 }

 @Test
 public void testNegativeOption() {
 Options options = new Options();
 options.addOption("o", "option", false, "this is an option");

 String[] args = new String[]{"--no-option"};
 CommandLine commandLine = null;
 try {
 commandLine = options.parse(args);
 } catch (ParseException e) {
 // Test should fail
 Assertions.assertEquals("Unrecognized option: no-option", e.getMessage());
 }

 Assertions.assertNull(commandLine.getArgs());
 Assertions.assertEquals(0, commandLine.size());
 }

 @Test
 public void testCommandLineWithArgs() {
 Options options = new Options();
 options.addOption("o", "option", false, "this is an option");
 options.addOption("a", "argument", true, "this is an argument");

 String[] args = new String[]{"--arg", "argValue", "--option", "value"};
 CommandLine commandLine = null;
 try {
 commandLine = options.parse(args);
 } catch (ParseException e) {
 // Test should pass
 }

 Assertions.assertNull(commandLine.hasOption("nonExistentOption"));
 Assertions.assertEquals("argValue", commandLine.getOptionValue("arg"));
 Assertions.assertEquals("value", commandLine.getOptionValue("option"));
 Assertions.assertArrayEquals(new String[0], commandLine.getArgs());
 }

 @Test
 public void testCommandLineWithMultipleArgsForOption() {
 Options options = new Options();
 options.addOption("a", "args", true, "this is an argument");

 String[] args = new String[]{"--args", "arg1", "arg2", "arg3"};
 CommandLine commandLine = null;
 try {
 commandLine = options.parse(args);
 } catch (ParseException e) {
 // Test should pass
 }

 Assertions.assertArrayEquals(new String[]{"arg1", "arg2", "arg3"}, commandLine.getOptionValues("args"));
 }
}