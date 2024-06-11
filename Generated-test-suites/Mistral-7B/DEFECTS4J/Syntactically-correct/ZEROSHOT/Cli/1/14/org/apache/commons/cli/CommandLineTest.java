import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class CommandLineTest {

 @Test
 public void testCommandLineParsing() throws ParseException {
 // Set up command line arguments and options
 String[] args = new String[]{"--option1", "value1", "--option2=value2", "--option3"};
 Options options = new Options();
 options.addOption("option1", true, "Option 1 description");
 options.addOption("option2", true, "Option 2 description");
 options.addOption("option3", false, "Option 3 description");

 // Parse command line arguments
 CommandLine commandLine = new CommandLineParser().parse(options, args);

 // Test hasOption method
 Assert.assertTrue(commandLine.hasOption("option1"));
 Assert.assertTrue(commandLine.hasOption("option2"));
 Assert.assertTrue(commandLine.hasOption("option3"));

 // Test getOptionValue method for options with arguments
 Assert.assertEquals("value1", commandLine.getOptionValue("option1"));
 Assert.assertEquals("value2", commandLine.getOptionValue("option2"));

 // Test getOptionValue method for options without arguments
 Assert.assertNull(commandLine.getOptionValue("option3"));

 // Test getOptionObject method
 Option option1 = options.getOption("option1");
 Assert.assertEquals(option1.getType(), String.class);
 Option option2 = options.getOption("option2");
 Assert.assertEquals(option2.getType(), String.class);

 Assert.assertEquals("value1", commandLine.getOptionObject("option1", "defaultValue1").toString());
 Assert.assertEquals("value2", commandLine.getOptionObject("option2", "defaultValue2").toString());
 Assert.assertNull(commandLine.getOptionObject("option3", "defaultValue3"));

 // Test getArgs method
 String[] argsArray = commandLine.getArgs();
 Assert.assertEquals(args.length - options.getOptions().size, argsArray.length);
 for (int i = 0; i < argsArray.length; i++) {
 Assert.assertEquals(args[args.length - options.getOptions().size + i], argsArray[i]);
 }
 }
}