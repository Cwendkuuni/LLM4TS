import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options options;

 @Before
 public void setup() {
 options = new Options();
 }

 @Test
 public void testAddOptionWithShortNameAndDescription() {
 Option option = new Option("a", "optionA", false, "This is an option with short name 'a' and description 'This is an option with short name 'a' and description'");
 options.addOption(option);
 assertEquals("This is an option with short name 'a' and description 'This is an option with short name 'a' and description'", options.getOption("a").getDescription());
 }

 @Test
 public void testAddOptionWithShortNameAndValue() {
 Option option = new Option("a", "optionA", true, "This is an option with short name 'a' and value");
 options.addOption(option);
 assertEquals("This is an option with short name 'a' and value", options.getOption("a").getDescription());
 assertTrue(options.getOption("a").hasArg());
 }

 @Test
 public void testAddOptionWithShortNameLongNameAndDescription() {
 Option option = new Option("a", "optionA", false, "This is an option with short name 'a' and long name 'optionA' and description 'This is an option with short name 'a' and long name 'optionA' and description'");
 options.addOption(option);
 assertEquals("This is an option with short name 'a' and long name 'optionA' and description 'This is an option with short name 'a' and long name 'optionA' and description'", options.getOption("a").getDescription());
 assertEquals("optionA", options.getOption("a").getLongOpt());
 }

 @Test
 public void testAddOptionWithShortNameLongNameAndValue() {
 Option option = new Option("a", "optionA", true, "This is an option with short name 'a' and long name 'optionA' and value");
 options.addOption(option);
 assertEquals("This is an option with short name 'a' and long name 'optionA' and value", options.getOption("a").getDescription());
 assertEquals("optionA", options.getOption("a").getLongOpt());
 assertTrue(options.getOption("a").hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("groupName", "This is a group");
 Option option = new Option("g", "groupOption", false, "This is an option in group 'groupName'");
 option.setRequired(true);
 optionGroup.addOption(option);
 options.addOptionGroup(optionGroup);
 assertTrue(options.getOption("g").isRequired());
 assertEquals("groupName", options.getOptionGroup(options.getOption("g")).getName());
 assertTrue(options.getOptionGroup("groupName").getOptions().contains(options.getOption("g")));
 }

 @Test
 public void testGetOptions() {
 CommandLineParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(options.toArray(), new String[]{"-a", "--optionA", "--group", "-g"});
 } catch (ParseException e) {
 // do nothing
 }
 List<Option> optionsList = options.helpOptions();
 assertEquals(3, optionsList.size());
 Option optionA = optionsList.get(0);
 Option optionGroupOption = optionsList.get(1);
 Option groupOption = optionsList.get(2);
 assertEquals("optionA", optionA.getLongOpt());
 assertEquals("a", optionA.getKey());
 assertNull(optionA.getValue());
 assertNull(optionA.getOptionGroup());
 assertEquals("groupOption", groupOption.getKey());
 assertEquals("g", groupOption.getOpt());
 assertNull(groupOption.getValue());
 OptionGroup optionGroup = options.getOptionGroup(groupOption);
 assertEquals("groupName", optionGroup.getName());
 }

 @Test
 public void testGetRequiredOptions() {
 CommandLineParser parser = new DefaultParser();
 CommandLine commandLine = null;
 try {
 commandLine = parser.parse(options.toArray(), new String[]{"-a", "--optionA", "--group", "-g", "--requiredOption"});
 } catch (ParseException e) {
 // do nothing
 }
 List<Option> requiredOptionsList = options.getRequiredOptions();
 assertEquals(1, requiredOptionsList.size());
 Option requiredOption = requiredOptionsList.get(0);
 assertEquals("requiredOption", requiredOption.getKey());
 assertTrue(requiredOption.isRequired());
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionWithNonExistentOption() {
 options.getOption("nonexistentOption");
 }

 @Test(expected = NullPointerException.class)
 public void testGetOptionGroupWithNonExistentOptionGroup() {
 options.getOptionGroup(new Option("nonexistentOptionGroup", "nonexistentOptionGroup"));
 }

 @Test
 public void testToString() {
 assertEquals("[ Options: [ short {a=}, {g=groupOption}}, long {} ], long options={}, requiredOptions=[], optionGroups=map]", options.toString());
 }
}