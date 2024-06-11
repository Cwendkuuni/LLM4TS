import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options createTestOptions() {
 Options options = new Options();

 // Test short option
 options.addOption("t", "test", false, "Run tests");

 // Test long option
 options.addOption("t", "test-long", false, "Run tests (long form)");
 options.addOption("l", "long-test-long", false, "Run long tests (long form)");

 // Test option with argument
 options.addOption("i", "input", true, "Input file name");

 // Test required option
 options.addOptionGroup(new OptionGroup("required-opts")
 .required(true)
 .addOption("r", "required", true, "Required option"));

 // Test option group
 OptionGroup optionGroup = new OptionGroup("group")
 .addOption(new Option("g", "group", false, "Test group"))
 .addOption(new Option("p", "group-property", true, "Group property"));
 options.addOptionGroup(optionGroup);

 return options;
 }

 @Test
 public void testBasicUsage() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options, new String[] {"-t"});
 assertTrue(cmd.hasOption("test"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test
 public void testLongOptionUsage() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options, new String[] {"--test-long"});
 assertTrue(cmd.hasOption("test-long"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test
 public void testOptionWithArgument() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options, new String[] {"-i", "input.txt"});
 assertTrue(cmd.hasOption("input"));
 assertEquals("input.txt", cmd.getOptionValue("input"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[0]);
 fail("No required option was provided");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 parser.parse(options, new String[] {"-r"});
 fail("No required option group was provided");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testOptionGroup() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options, new String[] {"-g"});
 assertTrue(cmd.hasOption("group"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test
 public void testOptionGroupWithArgument() {
 Options options = createTestOptions();
 CommandLineParser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(options, new String[] {"-g", "value"});
 assertEquals("value", cmd.getOptionValue("group-property"));
 } catch (ParseException e) {
 fail("Parsing failed: " + e.getMessage());
 }
 }
}