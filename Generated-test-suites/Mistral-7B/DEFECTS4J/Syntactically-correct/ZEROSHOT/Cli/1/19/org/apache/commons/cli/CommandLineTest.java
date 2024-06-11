import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testCreateCommandLine() {
 CommandLine commandLine = new CommandLine();
 }

 @Test(expected = AssertionError.class)
 public void testHasOptionWithNonExistentOption() {
 CommandLine commandLine = new CommandLine();
 Assert.assertFalse(commandLine.hasOption("nonExistentOption"));
 }

 @Test
 public void testHasOptionWithExistentOption() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("o", "option", false, "Test option");
 commandLine.parse(options.parse("o"));
 Assert.assertTrue(commandLine.hasOption("o"));
 }

 @Test(expected = AssertionError.class)
 public void testGetOptionValueWithNonExistentOption() {
 CommandLine commandLine = new CommandLine();
 Assert.assertNull(commandLine.getOptionValue("nonExistentOption"));
 }

 @Test
 public void testGetOptionValueWithExistentOption() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("o", "option", false, "Test option");
 commandLine.parse(options.parse("o"));
 Assert.assertNull(commandLine.getOptionValue("o"));
 }

 @Test
 public void testGetOptionValueWithExistentOptionAndArgument() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "arg", true, "Test argument");
 commandLine.parse(options.parse("a", new String[]{"test"}));
 Assert.assertEquals("test", commandLine.getOptionValue("a"));
 }

 @Test(expected = AssertionError.class)
 public void testGetOptionValueWithNonExistentOptionAndArgument() {
 CommandLine commandLine = new CommandLine();
 Assert.assertNull(commandLine.getOptionValue("nonExistentOption", "defaultValue"));
 }

 @Test
 public void testGetOptionValueWithExistentOptionAndDefaultArgument() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "arg", true, "Test argument");
 commandLine.parse(options.parse(""));
 Assert.assertEquals("", commandLine.getOptionValue("a", "defaultValue"));
 }

 @Test
 public void testGetOptionValueWithExistentOptionAndArgumentSupplied() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "arg", true, "Test argument");
 commandLine.parse(options.parse("a", new String[]{"test"}));
 Assert.assertEquals("test", commandLine.getOptionValue("a"));
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "arg", true, "Test argument");
 commandLine.parse(options.parse("a", new String[]{"test", "arg1", "arg2"}));
 String[] args = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 @Test
 public void testAddArg() {
 CommandLine commandLine = new CommandLine();
 commandLine.parse(new String[0][0]);
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 String[] args = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 @Test
 public void testIterator() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "arg", true, "Test argument");
 commandLine.parse(options.parse("a", new String[]{"test"}));
 Option option = null;
 for (Object o : commandLine) {
 if (o instanceof Option) {
 option = (Option) o;
 break;
 }
 }
 Assert.assertNotNull(option);
 Assert.assertEquals("arg", option.getLongOpt());
 Assert.assertEquals("test", option.getValue()[0]);
 }

 @Test
 public void testGetOptions() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "arg", true, "Test argument");
 commandLine.parse(options.parse("a", new String[]{"test"}));
 Option[] optionsArray = commandLine.getOptions();
 Assert.assertArrayEquals(new Option[]{options.findOption("arg")}, optionsArray);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidInput() {
 CommandLine commandLine = new CommandLine();
 Options options = new Options();
 options.addOption("a", "arg", true, "Test argument");
 commandLine.parse(options.parse("invalidInput"));
 }
}