import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testOptionsInstance() {
 Options options = new Options();
 assertNotNull(options);
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("groupName", "group description");
 options.addOptionGroup(optionGroup);
 assertEquals(1, options.getOptionGroups().size());
 }

 @Test
 public void testAddSingleShortOption() {
 Options options = new Options();
 options.addOption("a", "option A description");
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddSingleShortOptionWithArgument() {
 Options options = new Options();
 options.addOption("a", true, "option A description");
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddSingleLongOption() {
 Options options = new Options();
 options.addOption("a", "long option A description", true);
 options.addOption("a", "short option A", false, "short option A description");
 assertEquals(2, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Options options = new Options();
 options.addOption("a", "short option A", false, "long option A description");
 options.addOption("b", "long option B description", false);
 assertEquals(2, options.getOptions().size());
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("a", "option A description");
 options.addOption(option);
 Option retrievedOption = options.getOption("a");
 assertEquals(option, retrievedOption);
 }

 @Test(expected = NullPointerException.class)
 public void testGetNullOption() {
 Options options = new Options();
 assertNull(options.getOption(null));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option optA = new Option("optA", "Option A");
 Option optB = new Option("optB", "Option B");
 Option optC = new Option("optC", "Option C");
 options.addOption(optA);
 options.addOption(optB);
 options.addOption(optC);
 List<String> matchingOptions = options.getMatchingOptions("opt");
 assertEquals(2, matchingOptions.size());
 assertEquals(optA.getLongOpt(), matchingOptions.get(0));
 assertEquals(optB.getLongOpt(), matchingOptions.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option optA = new Option("optA", "Option A");
 Option optB = new Option("optB", "Option B");
 options.addOption(optA);
 options.addOption(optB);
 assertTrue(options.hasOption("optA"));
 assertTrue(options.hasOption("optB"));
 assertFalse(options.hasOption("nonExistentOption"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option optLong = new Option("longOpt", "Long Option");
 Option optShort = new Option("s", "Short Option");
 options.addOption(optLong);
 options.addOption(optShort);
 assertTrue(options.hasLongOption("longOpt"));
 assertFalse(options.hasLongOption("nonExistentOption"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option optShort = new Option("s", "Short Option");
 Option optLong = new Option("longOpt", "Long Option");
 options.addOption(optShort);
 options.addOption(optLong);
 assertTrue(options.hasShortOption("s"));
 assertFalse(options.hasShortOption("nonExistentOption"));
 }

 @Test
 public void testGetOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("groupName", "group description");
 Option option = new Option("opt", "Option");
 group.addOption(option);
 options.addOptionGroup(group);
 OptionGroup retrievedGroup = options.getOptionGroup(option);
 assertEquals(group, retrievedGroup);
 }

 @Test
 public void testGetOptionGroupNullOption() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("groupName", "group description");
 Option option = new Option("opt", "Option");
 options.addOptionGroup(group);
 assertNull(options.getOptionGroup(null));
 }

 @Test
 public void testParsingOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", false, "Option B");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a", "--b"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertFalse(cmd.hasOption("b"));
 } catch (ParseException e) {
 fail("Parsing should have succeeded.");
 }
 }

 @Test
 public void testParsingOptionsWithValues() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 options.addOption("b", true, "Option B with value");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a", "valueA", "--b", "valueB"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 } catch (ParseException e) {
 fail("Parsing should have succeeded.");
 }
 }

 @Test
 public void testParsingOptionsWithInvalidArgs() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a", "valueA", "--c"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parsing should have failed due to invalid argument --c.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParsingOptionsWithMissingArgument() {
 Options options = new Options();
 options.addOption("a", true, "Option A with value");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parsing should have failed due to missing argument for option A.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParsingOptionsWithMixedArgs() {
 Options options = new Options();
 options.addOption("a", true, "Option A with value");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a", "valueA", "--b", "valueB"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("valueB", cmd.getOptionValue("b"));
 } catch (ParseException e) {
 fail("Parsing should have succeeded.");
 }
 }

 @Test
 public void testParsingOptionsWithNoArgs() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = {};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.numArgs() == 0);
 } catch (ParseException e) {
 fail("Parsing should have succeeded with no arguments.");
 }
 }

 @Test
 public void testParsingOptionsWithMultipleArgs() {
 Options options = new Options();
 options.addOption("a", true, "Option A with value");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a", "valueA", "--a", "valueB"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parsing should have failed due to multiple values for option A.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParsingOptionsWithInvalidShortOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--x", "--a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parsing should have failed due to invalid short option --x.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParsingOptionsWithInvalidLongOption() {
 Options options = new Options();
 options.addOption("a", true, "Option A");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--invalidOption", "--a"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parsing should have failed due to invalid long option --invalidOption.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParsingOptionsWithMixedShortLongOptions() {
 Options options = new Options();
 options.addOption("a", true, "Option A with value");
 options.addOption("b", false, "Option B");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--a", "valueA", "--b", "--a", "valueB"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("a"));
 assertEquals("valueA", cmd.getOptionValue("a"));
 assertTrue(cmd.hasOption("b"));
 assertEquals("valueB", cmd.getOptionValue("a")); // getValue assumes short option
 } catch (ParseException e) {
 fail("Parsing should have succeeded.");
 }
 }

 @Test
 public void testParsingOptionsWithEmptyArgs() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 String[] args = {};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.numArgs() == 0);
 } catch (ParseException e) {
 fail("Parsing should have succeeded with no arguments.");
 }
 }

 @Test
 public void testParsingOptionsWithHelp() {
 Options options = new Options();
 options.addOption("h", false, "Print help");
 options.addOption("a", true, "Option A");
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--help"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("h"));
 assertTrue(cmd.hasOption("help"));
 formatter.printHelp("test", options);
 } catch (ParseException e) {
 fail("Parsing should have succeeded with help.");
 }
 }

 @Test
 public void testParsingOptionsWithInvalidHelpOption() {
 Options options = new Options();
 options.addOption("h", false, "Print help");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--invalidHelpOption"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parsing should have failed with invalid help option.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParsingOptionsWithInvalidHelpOptionAndHelp() {
 Options options = new Options();
 options.addOption("h", false, "Print help");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--help", "--invalidHelpOption"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parsing should have failed with invalid help option.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParsingOptionsWithInvalidHelpOptionAndHelpArgument() {
 Options options = new Options();
 options.addOption("h", false, "Print help");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--help", "--invalidHelpOption", "arg"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Parsing should have failed with invalid help option.");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParsingOptionsWithLongHelp() {
 Options options = new Options();
 options.addOption("h", false, "Print help");
 options.addOption("a", true, "Option A");
 HelpFormatter formatter = new HelpFormatter();
 String[] args = {"--help"};
 try {
 CommandLine cmd = new DefaultParser().parse(options, args);
 formatter.printHelp("test", options);
 } catch (ParseException e) {
 fail("Parsing should have succeeded with help.");
 }
 }
}