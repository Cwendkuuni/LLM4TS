import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testAddOptionWithShortNameOnly() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals("a", options.getOptions().get(0).getKey());
 }

 @Test
 public void testAddOptionWithShortNameAndLongName() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 option.setLongOpt("long-alias");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals("a", options.getOptions().get(0).getKey());
 assertEquals("long-alias", options.getOptions().get(0).getLongOpt());
 }

 @Test
 public void testAddOptionWithShortNameAndArgument() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option with argument");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals("a", options.getOptions().get(0).getKey());
 assertTrue(options.getOptions().get(0).hasArg());
 }

 @Test
 public void testAddOptionWithLongNameOnly() {
 Options options = new Options();
 Option option = new Option("alias", "An alias option");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 assertEquals("alias", options.getOptions().get(0).getLongOpt());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "A group of options");
 Option option = new Option("g", "group-option", false, "A group option");
 group.addOption(option);
 options.addOptionGroup(group);
 assertEquals(1, options.getOptions().size() + options.getOptionGroups().size());
 assertEquals("group-option", options.getOptions().get(options.getOptions().size() - 1).getKey());
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 Option result = options.getOption("a");
 assertSame(option, result);
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("a", "alias1", false, "Option 1");
 Option option2 = new Option("a", "alias2", false, "Option 2");
 options.addOption(option1);
 options.addOption(option2);
 List<String> matching = options.getMatchingOptions("a");
 assertEquals(2, matching.size());
 assertTrue(matching.contains("alias1"));
 assertTrue(matching.contains("alias2"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithEmptyKey() {
 Options options = new Options();
 Option option = new Option("", "alias", false, "An alias option");
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithNullDescription() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, null);
 options.addOption(option);
 assertEquals("", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullKey() {
 Options options = new Options();
 Option option = new Option(null, "alias", false, "An alias option");
 options.addOption(option);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddRequiredOptionToGroup() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 OptionGroup group = new OptionGroup("group", "A group of options");
 group.addOption(option);
 options.addOptionGroup(group);
 option.setRequired(true);
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 assertEquals(0, options.getOptions().get(0).getValues().size());

 options.parse("--a");
 assertEquals(1, options.getOptions().get(0).getValues().size());
 assertEquals("", options.getOptions().get(0).getValues().get(0));

 options.parse("--a foo");
 assertEquals(1, options.getOptions().get(0).getValues().size());
 assertEquals("foo", options.getOptions().get(0).getValues().get(0));
 }

 @Test
 public void testParseWithDuplicateShortOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--a", "--a", "--b"};
 try {
 parser.parse(options, args, true);
 } catch (ParseException e) {
 // expected
 }
 formatter.printHelp("test", options);
 }

 @Test
 public void testParseWithDuplicateLongOption() {
 Options options = new Options();
 Option option = new Option(null, "alias", false, "An alias option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--alias", "--alias", "--b"};
 try {
 parser.parse(options, args, true);
 } catch (ParseException e) {
 // expected
 }
 formatter.printHelp("test", options);
 }

 @Test
 public void testParseWithShortOptionWithValue() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option with argument");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--a", "value"};
 try {
 Options parsedOptions = parser.parse(options, args, true);
 assertEquals("value", parsedOptions.getOption("a").getValue()[0]);
 } catch (ParseException e) {
 // expected
 }
 formatter.printHelp("test", options);
 }

 @Test
 public void testParseWithLongOptionWithValue() {
 Options options = new Options();
 Option option = new Option("--alias", "An alias option with argument");
 option.setArgName("value");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--alias", "value"};
 try {
 Options parsedOptions = parser.parse(options, args, true);
 assertEquals("value", parsedOptions.getOption("--alias").getValue()[0]);
 } catch (ParseException e) {
 // expected
 }
 formatter.printHelp("test", options);
 }

 @Test
 public void testParseWithMissingOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--b"};
 try {
 parser.parse(options, args, true);
 fail();
 } catch (ParseException e) {
 // expected
 }
 formatter.printHelp("test", options);
 }

 @Test
 public void testParseWithMissingRequiredOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", true, "An alias option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {};
 try {
 parser.parse(options, args, true);
 assertFalse(options.getOption("a").isSet());
 } catch (ParseException e) {
 // expected
 }
 formatter.printHelp("test", options);
 }

 @Test
 public void testParseWithInvalidOption() {
 Options options = new Options();
 Option option = new Option("a", "alias", false, "An alias option");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--c"};
 try {
 parser.parse(options, args, true);
 assertNull(options.getOption("a"));
 } catch (ParseException e) {
 // expected
 }
 formatter.printHelp("test", options);
 }
}