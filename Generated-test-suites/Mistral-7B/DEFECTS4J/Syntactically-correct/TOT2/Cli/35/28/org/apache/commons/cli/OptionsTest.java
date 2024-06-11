import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 // Basic test cases for each method

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test option");
 options.addOption(option);
 List<Option> optionsList = options.helpOptions();
 assertEquals(1, optionsList.size());
 assertEquals(option, optionsList.get(0));
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = new Options();
 Option option = new Option("o", "option", true, "Test option with value");
 options.addOption(option);
 List<Option> optionsList = options.helpOptions();
 assertEquals(1, optionsList.size());
 assertEquals(option, optionsList.get(0));
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "Test group");
 options.addOptionGroup(optionGroup);
 assertEquals(0, options.shortOpts.size());
 assertEquals(0, options.longOpts.size());
 assertEquals(1, options.optionGroups.size());
 }

 // Test edge cases and exception handling scenarios

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullDescription() {
 Options options = new Options();
 Option option = new Option("o", "option", false, null);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullKey() {
 Options options = new Options();
 Option option = new Option(null, "option", false, "Test option");
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithEmptyKey() {
 Options options = new Options();
 Option option = new Option("", "option", false, "Test option");
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateKey() {
 Options options = new Options();
 Option option1 = new Option("o", "option1", false, "Test option 1");
 Option option2 = new Option("o", "option2", false, "Test option 2");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test option");
 options.addOption(option);
 Option retrievedOption = options.getOption("o");
 assertEquals(option, retrievedOption);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionWithInvalidKey() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test option");
 options.addOption(option);
 options.getOption("x");
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test option");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("o")); // long option
 }

 @Test
 public void testHasOptionWithInvalidKey() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test option");
 options.addOption(option);
 assertFalse(options.hasOption("x"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("option", "longOption", false, "Test long option");
 options.addOption(option);
 assertTrue(options.hasLongOption("longOption"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test short option");
 options.addOption(option);
 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testHasShortOptionWithInvalidKey() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "Test short option");
 options.addOption(option);
 assertFalse(options.hasShortOption("x"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "Test group");
 Option option = new Option("o", "option", false, "Test option");
 option.setOptionGroup(optionGroup);
 options.addOption(option);
 OptionGroup retrievedOptionGroup = options.getOptionGroup(option);
 assertEquals(optionGroup, retrievedOptionGroup);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionGroupWithNullOption() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "Test group");
 options.addOptionGroup(optionGroup);
 options.getOptionGroup(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionGroupWithInvalidOptionKey() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "Test group");
 Option option = new Option("o", "option", false, "Test option");
 options.addOption(option);
 options.getOptionGroup(new Option("x", "invalidOption"));
 }
}