import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the CommandLine class.
 */
public class CommandLineTest {

 @Test
 public void testEmptyCommandLine() {
 CommandLine commandLine = new CommandLine();
 Assert.assertEquals(new String[0], commandLine.getArgs());
 Assert.assertEquals(0, commandLine.getOptions().length);
 }

 @Test
 public void testCommandLineWithOptions() throws ParseException {
 String[] args = new String[]{"-a", "1", "-b", "2", "--optionC", "C"};
 CommandLine commandLine = new DefaultParser().parse(getOptions(), args);

 // Test hasOption
 Assert.assertTrue(commandLine.hasOption("a"));
 Assert.assertTrue(commandLine.hasOption('a'));
 Assert.assertTrue(commandLine.hasOption("b"));
 Assert.assertTrue(commandLine.hasOption('b'));
 Assert.assertTrue(commandLine.hasOption("optionC"));
 Assert.assertFalse(commandLine.hasOption("nonexistentOption"));

 // Test getOptionValue
 Assert.assertEquals("1", commandLine.getOptionValue("a"));
 Assert.assertEquals("1", commandLine.getOptionValue('a'));
 Assert.assertEquals("2", commandLine.getOptionValue("b"));
 Assert.assertEquals("2", commandLine.getOptionValue('b'));
 Assert.assertEquals("C", commandLine.getOptionValue("optionC"));
 Assert.assertNull(commandLine.getOptionValue("nonexistentOption"));

 // Test getOptionValues
 String[] values = commandLine.getOptionValues("b");
 Assert.assertNotNull(values);
 Assert.assertEquals(2, values.length);
 Assert.assertEquals("2", values[0]);

 // Test getArgs
 String[] args2 = commandLine.getArgs();
 Assert.assertNotNull(args2);
 Assert.assertEquals(0, args2.length);
 }

 @Test
 public void testCommandLineWithUnrecognizedArguments() throws ParseException {
 String[] args = new String[]{"-a", "1", "unrecognized", "--optionC", "C"};
 CommandLine commandLine = new DefaultParser().parse(getOptions(), args);

 // Test getArgs
 String[] args2 = commandLine.getArgs();
 Assert.assertNotNull(args2);
 Assert.assertEquals(1, args2.length);
 Assert.assertEquals("unrecognized", args2[0]);
 }

 private Options getOptions() {
 Options options = new Options();
 Option optionA = new Option("a", "anOption", true, "default value");
 Option optionB = new Option("b", "anotherOption", true, "default value");
 Option optionC = new Option("c", "optionC", false, "default value");
 options.addOption(optionA);
 options.addOption(optionB);
 options.addOption(optionC);
 return options;
 }

}