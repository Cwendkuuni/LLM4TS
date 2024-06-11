import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 // Basic test cases for addOption method
 @Test
 public void testAddOptionShort() {
 Options options = new Options();
 Option opt = new Option("a", "alert", false, "Display alert message");
 options.addOption(opt);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testAddOptionLong() {
 Options options = new Options();
 Option opt = new Option("alert", "showAlertMessage", false, "Display alert message");
 options.addOption(opt);
 assertTrue(options.hasOption("alert"));
 }

 @Test
 public void testAddOptionWithArg() {
 Options options = new Options();
 Option opt = new Option("b", "baseDir", true, "Path to base directory");
 options.addOption(opt);
 assertTrue(options.hasOption("b"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionNullOption() {
 Options options = new Options();
 Option opt = null;
 options.addOption(opt);
 fail("An IllegalArgumentException should be thrown when trying to add a null option.");
 }

 // Test for addOptionGroup method
 @Test
 public void testAddOptionGroupSimple() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Test Group 1");
 Option opt1 = new Option("g1", "group1Option1", false, "Test group 1 option 1");
 Option opt2 = new Option("g2", "group1Option2", false, "Test group 1 option 2");
 group.addOption(opt1);
 group.addOption(opt2);
 options.addOptionGroup(group);
 assertTrue(options.hasOptionGroup("group1"));
 }

 @Test
 public void testAddOptionGroupRequired() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Test Group 1", true, "Test group options");
 Option opt1 = new Option("g1", "group1Option1", false, "Test group 1 option 1");
 Option opt2 = new Option("g2", "group1Option2", false, "Test group 1 option 2");
 group.addOption(opt1);
 group.addOption(opt2);
 options.addOptionGroup(group);
 options.addOption("testOption", true, "Test option"); //Should fail as testOption is not part of the required group

 try {
 CommandLine cmdLine = new DefaultParser().parse(options, new String[]{"--testOption"});
 fail("A ParseException should be thrown when trying to parse an invalid option.");
 } catch (ParseException e) {
 //Expected
 }
 }

 // Edge cases and error scenarios
 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupNullGroup() {
 Options options = new Options();
 OptionGroup group = null;
 options.addOptionGroup(group);
 fail("An IllegalArgumentException should be thrown when trying to add a null OptionGroup.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupDuplicateOption() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Test Group 1");
 Option opt1 = new Option("g1", "group1Option1", false, "Test group 1 option 1");
 Option opt2 = new Option("g1", "group1Option1", false, "Test group 1 option 1");
 group.addOption(opt1);
 group.addOption(opt2);
 options.addOptionGroup(group);
 fail("An IllegalArgumentException should be thrown when trying to add a duplicate option to a group.");
 }

 @Test
 public void testGetOptions() {
 Options options = new Options();
 Option opt1 = new Option("a", "alert", false, "Display alert message");
 Option opt2 = new Option("b", "baseDir", true, "Path to base directory");
 options.addOption(opt1);
 options.addOption(opt2);
 List<Option> optionsList = options.getOptions();
 assertEquals(2, optionsList.size());
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 Option opt = new Option("r", "requiredOption", true, "Required option");
 options.addOption(opt);
 options.addOptionGroup(new OptionGroup("group1", "Test Group 1", true, "Test group options"));
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option opt = new Option("a", "alert", false, "Display alert message");
 options.addOption(opt);
 Option option = options.getOption("a");
 assertEquals(opt, option);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();Option opt1 = new Option("opt1", "option1", false, "Option 1");
 Option opt2 = new Option("opt2", "option2", false, "Option 2");
 Option opt3 = new Option("opt3", "option3", false, "Option 3");
 Option opt4 = new Option("opt4", "option4", false, "Option 4");
 options.addOption(opt1);
 options.addOption(opt2);
 options.addOption(opt3);
 options.addOption(opt4);
 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertEquals(3, matchingOptions.size());
 assertEquals("opt1", matchingOptions.get(0));
 assertEquals("opt2", matchingOptions.get(1));
 assertEquals("opt3", matchingOptions.get(2));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option opt = new Option("a", "alert", false, "Display alert message");
 options.addOption(opt);
 assertTrue(options.hasOption("a"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option opt = new Option("alert", "showAlertMessage", false, "Display alert message");
 options.addOption(opt);
 assertTrue(options.hasLongOption("showAlertMessage"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option opt = new Option("a", "alert", false, "Display alert message");
 options.addOption(opt);
 assertTrue(options.hasShortOption("a"));
 }

 @Test
 public void testHasOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Test Group 1");
 Option opt1 = new Option("g1", "group1Option1", false, "Test group 1 option 1");
 Option opt2 = new Option("g2", "group1Option2", false, "Test group 1 option 2");
 group.addOption(opt1);
 group.addOption(opt2);
 options.addOptionGroup(group);
 assertTrue(options.hasOptionGroup("group1"));
 }
}