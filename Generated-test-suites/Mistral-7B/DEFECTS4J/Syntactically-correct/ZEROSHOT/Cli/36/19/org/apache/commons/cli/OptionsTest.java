import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOptionWithShortOption() {
 Option option = new Option("o", "opt", false, "Option with short name");
 options.addOption(option);
 assertEquals(option, options.getOption("o"));
 }

 @Test
 public void testAddOptionWithLongOption() {
 Option option = new Option("longOpt", "long option", false, "Option with long name");
 options.addOption(option);
 assertEquals(option, options.getOption("longOpt"));
 }

 @Test
 public void testAddOptionWithBothShortAndLongOptions() {
 Option option = new Option("o", "longOpt", false, "Option with both short and long names");
 options.addOption(option);
 assertEquals(option, options.getOption("o"));
 assertEquals(option, options.getOption("longOpt"));
 }

 @Test
 public void testAddOptionWithShortRequiredOption() {
 Option option = new Option("o", "opt", true, "Required option with short name");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertFalse(options.hasOption("opt", null)); // null for unspecified value
 }

 @Test
 public void testAddOptionWithLongRequiredOption() {
 Option option = new Option("longOpt", "long opt", true, "Required option with long name");
 options.addOption(option);
 assertTrue(options.hasOption("longOpt"));
 assertFalse(options.hasOption("longOpt", null)); // null for unspecified value
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("group", "Group name");
 Option option = new Option("g", "opt", false, "Group option", optionGroup);
 options.addOptionGroup(optionGroup);
 optionGroup.addOption(option);
 assertEquals(option, options.getOption("g"));
 assertEquals(option, options.getOption("opt"));
 }

 @Test
 public void testGetRequiredOptions() {
 Option option = new Option("o", "opt", false, "Option with short name");
 options.addOption(option);
 options.addOption(option); // add another instance for testing
 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(2, requiredOptions.size());
 }

 @Test
 public void testHelpOptions() {
 Option option = new Option("o", "opt", false, "Option with short name");
 options.addOption(option);
 List<Option> helpOptions = options.helpOptions();
 assertEquals(1, helpOptions.size());
 assertEquals(option, helpOptions.get(0));
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("opt1", "opt1", false, "Option 1");
 Option option2 = new Option("opt2", "opt2", false, "Option 2");
 Option option3 = new Option("opt1long", "opt1 long", false, "Option 1 long");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> matchingOptions = options.getMatchingOptions("opt1");
 assertEquals(2, matchingOptions.size());
 assertEquals("opt1", matchingOptions.get(0));
 assertEquals("opt1long", matchingOptions.get(1));
 }

 @Test
 public void testHasOptionWithShortOption() {
 Option option = new Option("o", "opt", false, "Option with short name");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testHasOptionWithLongOption() {
 Option option = new Option("longOpt", "long option", false, "Option with long name");
 options.addOption(option);
 assertTrue(options.hasOption("longOpt"));
 }

 @Test
 public void testHasOptionWithNonExistentOption() {
 assertFalse(options.hasOption("nonExistentOption"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullOption() {
 options.addOption(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithNullOptionGroup() {
 options.addOptionGroup(null);
 }

 @Test
 public void testToString() {
 String expected = "[ Options: [ short {} ] [ long {} ]]";
 assertEquals(expected, options.toString());
 }
}