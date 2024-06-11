import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("a", "alert", false, "Display an alert message.");
 options.addOption(option);
 List<Option> optionsList = options.helpOptions();
 assertTrue(optionsList.contains(option));
 }

 @Test
 public void testAddShortOption() {
 Options options = new Options();
 options.addOption("a", true, "Display an alert message.");
 List<Option> optionsList = options.helpOptions();
 Option option = new Option("a", null, false, "Display an alert message.");
 assertTrue(optionsList.contains(option));
 }

 @Test
 public void testAddLongOption() {
 Options options = new Options();
 options.addOption("alert", true, "Display an alert message.");
 List<Option> optionsList = options.helpOptions();
 Option option = new Option("alert", "display", false, "Display an alert message.");
 assertTrue(optionsList.contains(option));
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = new Options();
 Option option = new Option("a", "alert", true, "Display an alert message [MESSAGE].");
 options.addOption(option);
 List<Option> optionsList = options.helpOptions();
 assertTrue(optionsList.contains(option));
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Some group.");
 Option option = new Option("g", null, false, "Join group1.");
 group.addOption(option);
 options.addOptionGroup(group);
 List<Option> optionsList = options.helpOptions();
 assertTrue(optionsList.contains(option));
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("a", "alert", false, "Display an alert message.");
 options.addOption(option);
 Option optionRetrieved = options.getOption("a");
 assertTrue(optionRetrieved.equals(option));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("alert", "enable", false, "Enable alerts.");
 Option option2 = new Option("alert", "disable", false, "Disable alerts.");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("alert");
 assertTrue(matchingOptions.contains("enable"));
 assertTrue(matchingOptions.contains("disable"));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("a", "alert", false, "Display an alert message.");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("alert"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("alert", "display", false, "Display an alert message.");
 options.addOption(option);
 assertTrue(options.hasLongOption("alert"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("a", null, false, "Display an alert message.");
 options.addOption(option);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Some group.");
 Option option = new Option("g", null, false, "Join group1.");
 group.addOption(option);
 options.addOptionGroup(group);
 OptionGroup groupRetrieved = options.getOptionGroup(option);
 assertTrue(groupRetrieved.equals(group));
 }

 @Test
 public void testParse() {
 Options options = new Options();
 options.addOption("a", true);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 } catch (ParseException e) {
 assertFalse(true);
 }
 }

 @Test
 public void testParseWithArgument() {
 Options options = new Options();
 options.addOption("a", true, "arg");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "test"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.getOptionValue("a").equals("test"));
 } catch (ParseException e) {
 assertFalse(true);
 }
 }

 @Test
 public void testParseWithoutOption() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = {};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertNull(cmd);
 } catch (ParseException e) {
 assertFalse(true);
 }
 }

 @Test
 public void testParseWithInvalidOption() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-z"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertNull(cmd);
 } catch (ParseException e) {
 assertTrue(e.getMessage().contains("Unrecognised option: -z"));
 }
 }

 @Test
 public void testParseWithMandatoryOptionMissing() {
 Options options = new Options();
 Option option = new Option("a", "alert", true, "Display an alert message.");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertFalse(true);
 } catch (ParseException e) {
 assertTrue(e.getMessage().contains("Missing required option 'alert'."));
 }
 }

 @Test
 public void testParseWithMandatoryOptionPresent() {
 Options options = new Options();
 Option option = new Option("a", "alert", true, "Display an alert message.");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("alert"));
 } catch (ParseException e) {
 assertFalse(true);
 }
 }
}