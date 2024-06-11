import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @After
 public void tearDown() {
 // Reset the options for the next test
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "opt", false, "Test option");
 options.addOption(option);
 assertEquals("Test option", options.getOption("opt"));
 }

 @Test
 public void testAddOptionWithValue() {
 Option option = new Option("o", "opt", true, "Test option with value");
 options.addOption(option);
 assertEquals("Test option with value", options.getOptionValue("opt"));
 assertNull(options.getOptionValue("nonExistentOption"));
 }

 @Test
 public void testAddRequiredOption() {
 Option option = new Option("o", "opt", true, "Test required option");
 options.addOption(option);
 assertFalse(options.hasOption("opt"));

 try {
 options.parse("--no-opt");
 fail("Expected an IllegalArgumentException when a required option is missing");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 Option optionValue = options.getOptionValue("opt");
 assertNotNull(optionValue);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Option option = new Option("o", "opt", false, "Test duplicate option");
 options.addOption(option);
 options.addOption(option);
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Test option group");
 Option option = new Option("g", "opt", false, "Test option in group", group);
 options.addOptionGroup(group);
 options.addOption(option);

 assertEquals("Test option in group", options.getOption("g"));
 assertEquals("Test option group", options.getOptionGroup("group"));
 }

 @Test
 public void testAddOptionGroupWithRequiredOption() {
 OptionGroup group = new OptionGroup("group", "Test option group");
 Option option = new Option("g", "opt", true, "Test required option in group", group);
 options.addOptionGroup(group);

 try {
 options.parse("--group");
 fail("Expected an IllegalArgumentException when a required option is missing");
 } catch (IllegalArgumentException e) {
 // Expected
 }

 Option optionValue = options.getOptionValue("opt");
 assertNotNull(optionValue);
 }

 @Test
 public void testParse() {
 Option option = new Option("o", "opt", false, "Test option");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--opt"});
 assertNotNull(cmd);
 } catch (ParseException e) {
 fail("Expected a valid command line to be parsed");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() {
 Option option = new Option("o", "opt", false, "Test option");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options.toArray(), new String[]{"--nonExistentOption"});
 fail("Expected a ParseException when a missing option is given");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testParseWithOptionValue() {
 Option option = new Option("o", "opt", true, "Test option");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--opt", "value"});
 assertEquals("value", cmd.getOptionValue("opt"));
 } catch (ParseException e) {
 fail("Expected a valid command line to be parsed");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOptionValue() {
 Option option = new Option("o", "opt", true, "Test option with value");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options.toArray(), new String[]{"--opt", "invalidValue"});
 fail("Expected a ParseException when an invalid option value is given");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testGetOptions() {
 List<Option> optionsList = options.getOptions();
 assertTrue(optionsList.isEmpty());

 Option option = new Option("o", "opt", false, "Test option");
 options.addOption(option);
 optionsList = options.getOptions();
 assertTrue(optionsList.size() == 1);
 assertEquals(option, optionsList.get(0));
 }

 @Test
 public void testGetRequiredOptions() {
 List<Option> requiredOptions = options.getRequiredOptions();
 assertTrue(requiredOptions.isEmpty());

 Option option = new Option("o", "opt", true, "Test required option");
 options.addOption(option);
 requiredOptions = options.getRequiredOptions();
 assertTrue(requiredOptions.size() == 1);
 assertEquals(option, requiredOptions.get(0));
 }

 @Test
 public void testGetOption() {
 Option option = new Option("o", "opt", false, "Test option");
 options.addOption(option);
 assertEquals(option, options.getOption("opt"));
 assertNull(options.getOption("nonExistentOption"));
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("o", "opt1", false, "Test option 1");
 Option option2 = new Option("o", "opt2", false, "Test option 2");
 options.addOption(option1);
 options.addOption(option2);

 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertTrue(matchingOptions.contains("opt1"));
 assertTrue(matchingOptions.contains("opt2"));
 }

 @Test
 public void testHasOption() {
 Option option = new Option("o", "opt", false, "Test option");
 options.addOption(option);
 assertTrue(options.hasOption("opt"));
 assertFalse(options.hasOption("nonExistentOption"));
 }

 @Test
 public void testHasLongOption() {
 Option option = new Option("l", "longOpt", false, "Test long option");
 options.addOption(option);
 assertTrue(options.hasLongOption("longOpt"));
 assertFalse(options.hasLongOption("nonExistentOption"));
 }

 @Test
 public void testHasShortOption() {
 Option option = new Option("o", "opt", false, "Test short option");
 options.addOption(option);
 assertTrue(options.hasShortOption("o"));
 assertFalse(options.hasShortOption("n"));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Test option group");
 Option option = new Option("g", "opt", false, "Test option in group", group);
 options.addOptionGroup(group);
 options.addOption(option);

 OptionGroup result = options.getOptionGroup(group.getKey());
 assertEquals(group, result);
 }
}