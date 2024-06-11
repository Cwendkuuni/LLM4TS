import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testOptionsCreation() {
 // Create an empty Options object
 Options options = new Options();

 // Test size of empty Options object
 assertEquals(0, options.getOptions().size());
 assertEquals(0, options.getRequiredOptions().size());
 assertNull(options.getOptionGroup("groupName"));

 // Test adding options
 options.addOption("opt1", "Option 1");
 options.addOption("opt2", "Option 2", true);
 options.addOption("opt3", "Option 3", false, "Description for Option 3");

 // Test adding option groups
 OptionGroup group1 = new OptionGroup("Group 1", "Group 1 description");
 OptionGroup group2 = new OptionGroup("Group 2", "Group 2 description");
 options.addOptionGroup(group1);
 options.addOptionGroup(group2);

 // Test adding options to option groups
 group1.addOption("optG1", "Option in Group 1");
 group2.addOption("optG2", "Option in Group 2", true);

 // Test option count
 assertEquals(5, options.getOptions().size());
 assertEquals(2, options.getRequiredOptions().size());
 assertNotNull(options.getOptionGroup("Group 1"));
 assertNotNull(options.getOptionGroup("Group 2"));
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 options.addOption("opt1", "Option 1");

 Option opt = options.getOption("opt1");
 assertNotNull(opt);
 assertEquals("Option 1", opt.getLongOpt());
 assertEquals(false, opt.hasArg());
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 options.addOption("opt1", "Option 1");
 options.addOption("opt2", "Option 2");
 options.addOption("opt3", "Option 3");

 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.contains("opt1"));
 assertTrue(matchingOptions.contains("opt3"));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 options.addOption("opt1", "Option 1");
 options.addOption("opt2", "Option 2");

 assertTrue(options.hasOption("opt1"));
 assertTrue(options.hasOption("opt2"));
 assertTrue(options.hasLongOption("opt1"));
 assertTrue(options.hasLongOption("opt2"));
 assertFalse(options.hasOption("nonexistentOpt"));
 }

 @Test
 public void testParseOptions() {
 Options options = new Options();
 options.addOption("opt1", "Option 1");
 options.addOption("opt2", "Option 2", true);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"--opt1", "value1", "--opt2", "value2"};

 try {
 CommandLine cmd = parser.parse(options, args);

 assertEquals(2, cmd.getArgs().length);
 assertEquals("value1", cmd.getOptionValue("opt1"));
 assertEquals("value2", cmd.getOptionValue("opt2"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Test failed: " + e.getMessage());
 }
 }

 @Test
 public void testParseOptionsWithInvalidArgument() {
 Options options = new Options();
 options.addOption("opt1", "Option 1");
 options.addOption("opt2", "Option 2", true);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"--opt1", "value1", "--opt2", "invalidArg"};

 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Test failed: parse should have thrown a ParseException");
 } catch (ParseException e) {
 assertEquals("Invalid value 'invalidArg' for option '--opt2'.", e.getMessage());
 }
 }

 @Test
 public void testParseOptionsWithMissingOption() {
 Options options = new Options();
 options.addOption("opt1", "Option 1");
 options.addOption("opt2", "Option 2", true);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"--opt2"};

 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Test failed: parse should have thrown a ParseException");
 } catch (ParseException e) {
 assertEquals("The following options are missing: [opt1]", e.getMessage());
 }
 }

 @Test
 public void testParseOptionsWithMissingValue() {
 Options options = new Options();
 options.addOption("opt1", "Option 1", true);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"--opt1"};

 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Test failed: parse should have thrown a ParseException");
 } catch (ParseException e) {
 assertEquals("The option '--opt1' requires an argument.", e.getMessage());
 }
 }

 @Test
 public void testToString() {
 Options options = new Options();
 options.addOption("opt1", "Option 1");
 options.addOption("opt2", "Option 2");

 assertEquals("[ Options: [ short {opt1=} ] [ long {opt1=,opt2=}] ]", options.toString());
 }

}