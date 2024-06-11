import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Test
 public void testAddOptionsWithShortName() {
 options = new Options();
 options.addOption("i", "inputFile", true, "input file");
 Option option = options.getOption("i");
 assertEquals("i", option.getOpt());
 assertEquals("inputFile", option.getLongOpt());
 assertEquals(true, option.hasArg());
 }

 @Test
 public void testAddOptionsWithLongName() {
 options = new Options();
 options.addOption("inputFile", true, "input file option");
 Option option = options.getOption("inputFile");
 assertEquals(null, option.getOpt());
 assertEquals("inputFile", option.getLongOpt());
 assertEquals(true, option.hasArg());
 }

 @Test
 public void testAddOptionsWithShortAndLongName() {
 options = new Options();
 options.addOption("i", "inputFile", true, "input file option");
 Option option = options.getOption("i");
 assertEquals("i", option.getOpt());
 assertEquals("inputFile", option.getLongOpt());
 assertEquals(true, option.hasArg());
 }

 @Test
 public void testAddOptionGroup() {
 options = new Options();
 OptionGroup group = new OptionGroup("group", "group");
 options.addOptionGroup(group);
 assertEquals(Collections.singletonList(group), options.getOptionGroups());
 }

 @Test
 public void testAddRequiredOption() {
 options = new Options();
 Option option = new Option("i", "inputFile", true, "input file option");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("group", "group").required(true));
 List<Object> requiredOpts = options.getRequiredOpts();
 assertEquals(2, requiredOpts.size());
 assertTrue(requiredOpts.contains(option));
 assertTrue(requiredOpts.contains(group));
 }

 @Test
 public void testGetMatchingOptions() {
 options = new Options();
 options.addOption("input");
 options.addOption("inputFile");
 options.addOption("outputFile");
 List<String> matchingOpts = options.getMatchingOptions("input");
 assertEquals(2, matchingOpts.size());
 assertTrue(matchingOpts.contains("input"));
 assertTrue(matchingOpts.contains("inputFile"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidOptionName() {
 options = new Options();
 options.addOption("", "", false, "");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithNullOptionDescription() {
 options = new Options();
 options.addOption("i", "", true, null);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddOptionWithDuplicateOptionName() {
 options = new Options();
 options.addOption("i", "inputFile", true, "input file option");
 options.addOption("i", "inputFile", true, "input file option");
 }

 @Test
 public void testParseOptions() {
 options = new Options();
 options.addOption("i", "inputFile", true, "input file option");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-i", "/path/to/file.txt"};
 try {
 CommandLine cmd = parser.parse(options, args);
 Option option = options.getOption("i");
 assertEquals("/path/to/file.txt", cmd.getOptionValue(option.getOpt()));
 } catch (ParseException e) {
 fail("Failed to parse command-line");
 }
 }

 @Test
 public void testParseOptionsWithUnrecognizedOption() {
 options = new Options();
 options.addOption("i", "inputFile", true, "input file option");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--unknown"};
 try {
 CommandLine cmd = parser.parse(options, args);
 } catch (ParseException e) {
 assertEquals("Unrecognized option: --unknown", e.getMessage());
 }
 }

 @Test
 public void testParseOptionsWithInvalidBooleanOption() {
 options = new Options();
 options.addOption("d", "debug", false, "debug mode");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-d"};
 try {
 CommandLine cmd = parser.parse(options, args);
 assertTrue(cmd.hasOption("d"));
 } catch (ParseException e) {
 fail("Failed to parse command-line");
 }
 }

 @Test
 public void testParseOptionsWithInvalidNumericOption() {
 options = new Options();
 options.addOption("n", "num", true, "number of items");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-n", "abc"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Failed to parse command-line with invalid numeric option");
 } catch (ParseException e) {
 assertEquals("Invalid value for option '--num': 'abc'. Expected a number.", e.getMessage());
 }
 }

 @Test
 public void testParseOptionsWithMissingValue() {
 options = new Options();
 options.addOption("i", "inputFile", true, "input file option");
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-i"};
 try {
 CommandLine cmd = parser.parse(options, args);
 fail("Failed to parse command-line with missing option value");
 } catch (ParseException e) {
 assertEquals("The option '--inputFile' requires an argument.", e.getMessage());
 }
 }
}