import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testCommandLineParsing() throws ParseException {
 // Set up the command line options
 Options options = new Options();
 options.addOption("a", "alpha", true, "Alpha option");
 options.addOption("b", "beta", false, "Beta option");
 options.addOption("c", "char", 'c', "Character option");
 options.addOption("d", "delta", 1, "Delta option with an integer value");
 options.addOption("e", "echo", 3, "Echo option with an integer value > 1");

 // Parse the command line arguments
 String[] args = new String[]{"-a", "1", "-b", "--c", "3", "-d", "2", "1", "2", "3"};
 CommandLine cmd = new CommandLine().parse(args, options);

 // Test the parsed command line
 Assert.assertTrue(cmd.hasOption("a"));
 Assert.assertEquals("1", cmd.getOptionValue("a"));
 Assert.assertTrue(cmd.hasOption("b"));
 Assert.assertFalse(cmd.hasOption("b", false)); // false flag
 Assert.assertTrue(cmd.hasOption('c'));
 Assert.assertEquals("3", cmd.getOptionValue('c'));
 Assert.assertTrue(cmd.hasOption("d"));
 Assert.assertEquals("2", cmd.getOptionValue("d"));
 Assert.assertEquals(new Integer(3), cmd.getOptionObject("e"));

 // Test non-existent options
 Assert.assertFalse(cmd.hasOption("z"));

 // Test left-over arguments
 Assert.assertEquals(new String[]{"1", "2", "3"}, cmd.getArgs());
 }

 @Test(expected = ParseException.class)
 public void testParseException() throws ParseException {
 // Set up the command line options with an invalid option
 Options options = new Options();
 options.addOption("a", "alpha", true, "Alpha option");
 options.addOption("b", "beta", false, "Beta option");
 options.addOption("c", "char", 'c', "Character option");
 options.addOption("d", "delta", 1, "Delta option with an integer value");
 options.addOption("e", "echo", 3, "Echo option with an integer value > 1");
 options.addOption("f", "foo", 0, "Non-existent option"); // This option will cause a ParseException

 // Attempt to parse the command line with the non-existent option
 String[] args = new String[]{"-a", "1", "-b", "--c", "3", "-d", "2", "-f", "1", "2", "3"};
 CommandLine cmd = new CommandLine().parse(args, options);
 }
}