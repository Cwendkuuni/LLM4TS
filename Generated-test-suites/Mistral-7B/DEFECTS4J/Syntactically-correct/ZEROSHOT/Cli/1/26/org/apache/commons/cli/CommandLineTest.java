import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testCreateCommandLine() throws ParseException {
 CommandLine commandLine = new CommandLine();
 Assert.assertNull(commandLine.getOptionValue("opt"));
 Assert.assertFalse(commandLine.hasOption("opt"));
 }

 @Test
 public void testHasOption() throws ParseException {
 Options options = new Options();
 options.addOption("opt", true, "This is an option");
 CommandLine commandLine = new CommandLine(options, new String[]{"--opt"});
 Assert.assertTrue(commandLine.hasOption("opt"));
 }

 @Test
 public void testHasOptionWithArgument() throws ParseException {
 Options options = new Options();
 options.addOption("opt", true, "This is an option with argument");
 options.addOption("optArg", true, "This is the argument for opt");
 CommandLine commandLine = new CommandLine(options, new String[]{"--opt", "arg"});
 Assert.assertTrue(commandLine.hasOption("opt"));
 Assert.assertTrue(commandLine.hasOption("optArg"));
 Assert.assertEquals("arg", commandLine.getOptionValue("optArg"));
 }

 @Test(expected = AssertionError.class)
 public void testHasOptionWithInvalidOption() throws ParseException {
 Options options = new Options();
 CommandLine commandLine = new CommandLine(options, new String[]{"--invalid"});
 Assert.assertFalse(commandLine.hasOption("invalid"));
 }

 @Test
 public void testGetOptionValue() throws ParseException {
 Options options = new Options();
 options.addOption("opt", true, "This is an option");
 CommandLine commandLine = new CommandLine(options, new String[]{"--opt", "value"});
 Assert.assertEquals("value", commandLine.getOptionValue("opt"));
 }

 @Test(expected = AssertionError.class)
 public void testGetOptionValueWithInvalidOption() throws ParseException {
 CommandLine commandLine = new CommandLine();
 Assert.assertNull(commandLine.getOptionValue("opt"));
 }

 @Test
 public void testGetOptionObject() throws ParseException {
 Options options = new Options();
 options.addOption("opt", true, Integer.TYPE, "An integer option");
 CommandLine commandLine = new CommandLine(options, new String[]{"--opt", "123"});
 Assert.assertEquals(Integer.valueOf(123), commandLine.getOptionObject("opt"));
 }

 @Test(expected = AssertionError.class)
 public void testGetOptionObjectWithInvalidOption() throws ParseException {
 CommandLine commandLine = new CommandLine();
 Assert.assertNull(commandLine.getOptionObject("opt"));
 }

 @Test
 public void testGetOptionValues() throws ParseException {
 Options options = new Options();
 options.addOption("opt", true, "This is an option");
 CommandLine commandLine = new CommandLine(options, new String[]{"--opt", "value1", "--opt", "value2"});
 String[] values = commandLine.getOptionValues("opt");
 Assert.assertArrayEquals(new String[]{"value1", "value2"}, values);
 }

 @Test(expected = AssertionError.class)
 public void testGetOptionValuesWithInvalidOption() throws ParseException {
 CommandLine commandLine = new CommandLine();
 String[] values = commandLine.getOptionValues("opt");
 Assert.assertNull(values);
 }

 @Test
 public void testGetArgs() {
 CommandLine commandLine = new CommandLine();
 commandLine.addArg("arg1");
 commandLine.addArg("arg2");
 String[] args = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"arg1", "arg2"}, args);
 }

 @Test
 public void testToString() throws ParseException {
 Options options = new Options();
 options.addOption("opt", true, "This is an option");
 CommandLine commandLine = new CommandLine(options, new String[]{"--opt", "value"});
 String toString = commandLine.toString();
 Assert.assertTrue(toString.contains("--opt:true"));
 }
}