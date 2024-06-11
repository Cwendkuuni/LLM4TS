import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testOptionsCreation() {
 Options options = new Options();
 }

 @Test
 public void testAddOptionWithShortName() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "option for alpha");
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "option for alpha");
 options.addOption(option);
 Option option2 = new Option("l", "long-alpha", false, "long option for alpha");
 options.addOption(option2);
 }

 @Test
 public void testAddOptionWithShortNameAndArgument() {
 Options options = new Options();
 Option option = new Option("a", "alpha", true, "option for alpha with argument");
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithShortNameAndRequired() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "required option alpha");
 option.setRequired(true);
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = new Options();
 Option option = new Option("long-alpha", "long option for alpha", false, "long option for alpha");
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithLongNameAndArgument() {
 Options options = new Options();
 Option option = new Option("long-alpha", "long option for alpha", true, "long option for alpha with argument");
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithLongNameAndRequired() {
 Options options = new Options();
 Option option = new Option("long-alpha", "long option for alpha", false, "required long option for alpha");
 option.setRequired(true);
 options.addOption(option);
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "option group");
 Option option = new Option("g", "group-opt", false, "option for group");
 optionGroup.addOption(option);
 options.addOptionGroup(optionGroup);
 }

 @Test
 public void testGetOptions() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "option for alpha");
 Option option2 = new Option("l", "long-alpha", false, "long option for alpha");
 options.addOption(option);
 options.addOption(option2);
 List<Option> optionsList = options.getOptions();
 assertEquals(2, optionsList.size());
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "option for alpha");
 options.addOption(option);
 assertTrue(options.hasOption("a"));
 assertTrue(options.hasOption("l")); // long options are also checked
 assertFalse(options.hasOption("z"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "option group");
 Option option = new Option("g", "group-opt", false, "option for group");
 optionGroup.addOption(option);
 options.addOptionGroup(optionGroup);
 OptionGroup returnedOptionGroup = options.getOptionGroup(option);
 assertNotNull(returnedOptionGroup);
 assertEquals("group", returnedOptionGroup.getName());
 }

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 Option option = new Option("a", "alpha", false, "option for alpha");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-a"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("alpha", cmd.getOptionValue("a"));
 assertNull(cmd.getOptionValue("z"));
 }

 @Test (expected = ParseException.class)
 public void testParseWithRequiredOptionMissing() throws ParseException {
 Options options = new Options();Option option = new Option("a", "alpha", false, "required option alpha");
 option.setRequired(true);
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{};
 CommandLine cmd = parser.parse(options, args);
 }

 @Test
 public void testParseWithArgument() throws ParseException {
 Options options = new Options();
 Option option = new Option("a", "alpha", true, "option for alpha with argument");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-a", "value"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("value", cmd.getOptionValue("a"));
 }

 @Test
 public void testParseWithLongOption() throws ParseException {
 Options options = new Options();
 Option option = new Option("long-alpha", "long option for alpha", false, "long option for alpha");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--long-alpha"};
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("long-alpha"));
 assertNull(cmd.getOptionValue("a"));
 }

 @Test
 public void testParseWithLongOptionAndArgument() throws ParseException {
 Options options = new Options();
 Option option = new Option("long-alpha", "long option for alpha", true, "long option for alpha with argument");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--long-alpha", "value"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("value", cmd.getOptionValue("long-alpha"));
 }
}