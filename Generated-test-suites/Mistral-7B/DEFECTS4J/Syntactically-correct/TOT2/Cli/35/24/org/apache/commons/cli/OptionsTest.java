import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 @Test
 public void testAddOptions() {
 Options options = new Options();

 // Test adding options with short name only
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");

 // Test adding options with long name only
 options.addOption("c", "Consumer", true, "Option C");
 options.addOption("d", "Data", false, "Option D");

 // Test adding options with short and long name
 options.addOption("e", "Enable", true, "Option E");
 options.addOption("f", "Feature", false, "Option F");
 options.addOption("e", "Enable,enable", true, "Option E (long name)");

 // Test adding an OptionGroup
 OptionGroup group = new OptionGroup("Group1", "Group 1");
 group.setRequired(true);
 group.addOption(new Option("g", null, true, "Group Option G"));
 options.addOptionGroup(group);

 // Test getOptions()
 List<Option> optionsList = options.getOptions();
 Assert.assertEquals(6, optionsList.size());

 // Test toString()
 String optionsString = options.toString();
 Assert.assertNotNull(optionsString);
 }

 @Test(expected = ParseException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("a", false, "Option A (different)");
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option optionA = new Option("a", null, true, "Option A");
 Option optionB = new Option("b", null, false, "Option B");

 options.addOption(optionA);
 options.addOption(optionB);

 Option actualOptionA = options.getOption("a");
 Option actualOptionB = options.getOption("b");

 Assert.assertEquals(optionA, actualOptionA);
 Assert.assertEquals(optionB, actualOptionB);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionNull() {
 Options options = new Options();
 options.getOption(null);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 options.addOption("foo");
 options.addOption("bar.baz");
 options.addOption("bat");

 List<String> matchingOptions = options.getMatchingOptions("ba");
 Assert.assertFalse(matchingOptions.isEmpty());
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertTrue(matchingOptions.contains("bar.baz"));
 Assert.assertTrue(matchingOptions.contains("bat"));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Options testOptions = new Options();

 options.addOption("a", true, "Option A");
 testOptions.addOption("b", true, "Option B");

 Assert.assertTrue(options.hasOption("a"));
 Assert.assertTrue(options.hasOption("a", true));
 Assert.assertFalse(options.hasOption("b"));
 Assert.assertFalse(options.hasOption("c"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 options.addOption("a--Activate", true, "Option A");

 Assert.assertTrue(options.hasLongOption("--Activate"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 Assert.assertTrue(options.hasShortOption("a"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasOptionNull() {
 Options options = new Options();
 options.hasOption(null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testHasOptionWithWrongArgument() {
 Options options = new Options();
 options.addOption("a", true, "Option A");

 options.hasOption("a", "wrongArg");
 }

 @Test
 public void testGetOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("Group1", "Group 1");
 Option groupOptionG = new Option("g", null, true, "Group Option G");
 group.addOption(groupOptionG);
 options.addOptionGroup(group);

 OptionGroup actualGroup = options.getOptionGroup(groupOptionG);
 Assert.assertEquals(group, actualGroup);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testGetOptionGroupNull() {
 Options options = new Options();
 options.getOptionGroup(null);
 }

 @Test
 public void testToString() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");

 Assert.assertNotNull(options.toString());
 }
}