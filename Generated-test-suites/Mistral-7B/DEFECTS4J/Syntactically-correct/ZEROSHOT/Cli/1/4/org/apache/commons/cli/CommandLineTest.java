import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class CommandLineTest {

 @Before
 public void setUp() {
 // Set up your Options object here
 Options options = new Options();
 options.addOption("a", "apple", true, "Apple type");
 options.addOption("b", "banana", false);
 }

 @Test
 public void testHasOption() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a", "Fuji"});

 // Test hasOption method
 Assert.assertTrue(commandLine.hasOption("a"));
 Assert.assertFalse(commandLine.hasOption("z"));

 // Test hasOption method with char argument
 Assert.assertTrue(commandLine.hasOption('a'));
 Assert.assertFalse(commandLine.hasOption('z'));
 }

 @Test
 public void testGetOptionValue() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a", "Fuji"});

 // Test getOptionValue method with String argument
 Assert.assertEquals("Fuji", commandLine.getOptionValue("a"));
 Assert.assertNull(commandLine.getOptionValue("z"));

 // Test getOptionValue method with char argument
 Assert.assertEquals("Fuji", commandLine.getOptionValue('a'));
 Assert.assertNull(commandLine.getOptionValue('z'));
 }

 @Test
 public void testGetOptionValues() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a", "Fuji", "-a", "Gala"});

 // Test getOptionValues method with String argument
 String[] values = commandLine.getOptionValues("a");
 Assert.assertArrayEquals(new String[]{"Fuji", "Gala"}, values);

 // Test getOptionValues method with char argument
 values = commandLine.getOptionValues('a');
 Assert.assertArrayEquals(new String[]{"Fuji", "Gala"}, values);
 }

 @Test
 public void testGetArgs() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a", "Fuji", "Orange", "Banana"});

 // Test getArgs method
 String[] args = commandLine.getArgs();
 Assert.assertArrayEquals(new String[]{"Orange", "Banana"}, args);
 }

 @Test
 public void testGetArgList() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-a", "Fuji", "Orange", "Banana"});

 // Test getArgList method
 List argList = commandLine.getArgList();
 Assert.assertEquals(Arrays.asList(new String[]{"Orange", "Banana"}), argList);
 }

 @Test(expected = AssertionError.class)
 public void testUnknownOption() throws ParseException {
 CommandLine commandLine = new CommandLineParser().parse(options, new String[]{"-z", "Fuji"});
 }
}