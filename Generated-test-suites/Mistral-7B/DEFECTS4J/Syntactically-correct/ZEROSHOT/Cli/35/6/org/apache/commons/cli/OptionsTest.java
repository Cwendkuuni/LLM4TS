import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOption() {
 Option option = new Option(null, "o", false, "option");
 options.addOption(option);
 assertEquals("option", options.getOption("o").getLongOpt());
 }

 @Test
 public void testAddOptionWithDescription() {
 Option option = new Option("o", "option", false, "an option");
 options.addOption(option);
 assertEquals("an option", options.getOption("o").getDescription());
 }

 @Test
 public void testAddOptionWithValue() {
 Option option = new Option(null, "o", true, "an option");
 options.addOption(option);
 assertEquals("an option", options.getOption("o").getLongOpt());
 assertEquals("an option", options.getOption("o").getDescription());
 }

 @Test
 public void testAddOptionWithLongOpt() {
 Option option = new Option("loption", "long-option", false, "a long option");
 options.addOption(option);
 assertEquals("long-option", options.getOption("long-option").getKey());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "a group");
 Option option = new Option("g", "group-option", false, "a group option");
 group.addOption(option);
 options.addOptionGroup(group);
 assertEquals("a group option", options.getOption("g").getDescription());
 assertEquals("group", group.getKey());
 }

 @Test
 public void testGetRequiredOptions() {
 List<Option> options = new ArrayList<>();
 options.add(new Option("o", "option", false, "an option"));
 options.add(new OptionGroup("group", "a group"));
 options.add(options.get(0));
 options.add(new OptionGroup("group", "a group"));
 Options parsedOptions = new Options();
 parsedOptions.addOptions(options);
 assertEquals(2, parsedOptions.getRequiredOptions().size());
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("l", "long1", false, "long option 1");
 Option option2 = new Option("l", "long2", false, "long option 2");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matchingOptions = options.getMatchingOptions("l");
 assertEquals(2, matchingOptions.size());
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "option", false, "an option");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("o", false));
 assertFalse(options.hasOption("o", true));
 assertFalse(options.hasOption("O"));
 }

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 Option option = new Option("o", "option", false, "an option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o"};
 CommandLine cmd = parser.parse(options, args);
 assertEquals("an option", cmd.getOptionValue("o"));
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option option1 = new Option("l", "long1", false, "long option 1");
 Option option2 = new Option("l", "long2", false, "long option 2");
 options.addOption(option1);
 options.addOption(option2);
 String expected = "[ Options: [ short {} ] [ long {long1=long option 1, long2=long option 2} ]]";
 assertEquals(expected, options.toString());
 }
}