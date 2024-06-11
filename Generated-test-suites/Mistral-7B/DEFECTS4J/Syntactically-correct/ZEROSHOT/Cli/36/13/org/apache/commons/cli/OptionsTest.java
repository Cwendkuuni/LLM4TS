import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 private Options options;

 @Test
 public void testOptionsCreation() {
 options = new Options();
 // Test empty Options instance
 Assert.assertEquals(Collections.emptyList(), options.getOptions());
 Assert.assertEquals(Collections.emptyList(), options.getRequiredOptions());
 Assert.assertEquals(Collections.emptyMap(), options.getOptionGroups());
 }

 @Test
 public void testAddOptionSimpleShort() {
 options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 List<Option> optionsList = options.helpOptions();
 Option optionA = optionsList.get(0);
 Assert.assertEquals("a", optionA.getKey());
 Assert.assertEquals("optionA", optionA.getLongOpt());
 Assert.assertFalse(optionA.hasArg());
 Assert.assertEquals("Description for option A", optionA.getDescription());
 }

 @Test
 public void testAddOptionSimpleLong() {
 options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 options.addOption("longA", "optionA", false, "Description for option A");
 List<Option> optionsList = options.helpOptions();
 Option optionALong = optionsList.get(1);
 Assert.assertEquals("longA", optionALong.getLongOpt());
 Assert.assertEquals("optionA", optionALong.getKey());
 }

 @Test
 public void testAddOptionWithArgumentShort() {
 options = new Options();
 options.addOption("a", "optionA", true, "Description for option A");
 List<Option> optionsList = options.helpOptions();
 Option optionA = optionsList.get(0);
 Assert.assertTrue(optionA.hasArg());
 }

 @Test
 public void testAddOptionWithArgumentLong() {
 options = new Options();
 options.addOption("a", "optionA", true, "Description for option A");
 options.addOption("longA", "optionA", true, "Description for option A");
 List<Option> optionsList = options.helpOptions();
 Option optionALong = optionsList.get(1);
 Assert.assertTrue(optionALong.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 options = new Options();
 OptionGroup optionGroup = new OptionGroup("groupA", "Group A");
 options.addOptionGroup(optionGroup);
 options.addOption("optionA", true, "Description for option A", optionGroup);
 List<Option> optionsList = options.helpOptions();
 Option optionA = optionsList.get(0);
 OptionGroup optionGroupFromOptions = optionA.getOptionGroup();
 Assert.assertEquals(optionGroup, optionGroupFromOptions);
 }

 @Test
 public void testGetOption() {
 options = new Options();
 Option option = new Option("optionA", "optionA", false, "Description for option A");
 options.addOption(option);
 Option optionFromOptions = options.getOption("optionA");
 Assert.assertEquals(option, optionFromOptions);
 }

 @Test
 public void testGetMatchingOptions() {
 options = new Options();
 Option option1 = new Option("option1", "option1", false, "Option 1");
 Option option2 = new Option("option2", "option2", false, "Option 2");
 Option option3 = new Option("option3", "option3", false, "Option 3");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> matchingOptions = options.getMatchingOptions("opt");
 Assert.assertEquals(2, matchingOptions.size());
 Assert.assertEquals("option2", matchingOptions.get(1));
 }

 @Test
 public void testHasOption() {
 options = new Options();
 Option option = new Option("optionA", "optionA", false, "Description for option A");
 options.addOption(option);
 Assert.assertTrue(options.hasOption("optionA"));
 Assert.assertTrue(options.hasOption("optionA", true));
 Assert.assertFalse(options.hasOption("notAnOption"));
 }

 @Test
 public void testHasLongOption() {
 options = new Options();
 Option option = new Option("optionA", "optionA", false, "Description for option A");
 options.addOption(option);
 Assert.assertTrue(options.hasLongOption("optionA"));
 Assert.assertFalse(options.hasLongOption("notAnOption"));
 }

 @Test
 public void testHasShortOption() {
 options = new Options();
 Option option = new Option("optionA", "optionA", false, "Description for option A");
 options.addOption(option);
 Assert.assertTrue(options.hasShortOption("a"));
 Assert.assertFalse(options.hasShortOption("notAnOption"));
 }

 @Test
 public void testGetOptionGroup() {
 options = new Options();
 OptionGroup optionGroup = new OptionGroup("groupA", "Group A");
 Option option = new Option("optionA", "optionA", false, "Description for option A", optionGroup);
 options.addOption(option);
 OptionGroup optionGroupFromOptions = options.getOptionGroup(option);
 Assert.assertEquals(optionGroup, optionGroupFromOptions);
 }

 @Test
 public void testParseOptions() throws ParseException {
 options = new Options();
 options.addOption("a", "optionA", false, "Description for option A");
 CommandLineParser parser = new DefaultParser();
 String[] arguments = new String[]{"--optionA"};
 CommandLine cmd = parser.parse(options, arguments);
 Option option = options.getOption("optionA");
 Assert.assertTrue(cmd.hasOption("optionA"));
 Assert.assertFalse(cmd.hasOption("notAnOption"));
 Assert.assertEquals(option.getValue(), cmd.getOptionValue("optionA"));
 }
}