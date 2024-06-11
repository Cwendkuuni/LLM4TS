import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 @Test
 public void testAddOptionWithShortName() {
 Option option = new Option("o", "output", false, "Output file");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testAddOptionWithShortAndLongName() {
 Option option = new Option("o", "output,f", false, "Output file");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("output"));
 }

 @Test
 public void testAddOptionWithShortNameAndValue() {
 Option option = new Option("o", "output", true, "Output file");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testAddOptionWithLongName() {
 Option option = new Option("output", "outputFile", false, "Output file");
 options.addOption(option);
 assertTrue(options.hasOption("outputFile"));
 }

 @Test
 public void testAddOptionWithLongNameAndValue() {
 Option option = new Option("output", "outputFile", true, "Output file");
 options.addOption(option);
 assertTrue(options.hasOption("outputFile"));
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group options");
 Option option = new Option("g", "groupOption", false, "Group option");
 group.addOption(option);
 options.addOptionGroup(group);
 assertTrue(options.hasOption("g"));
 assertTrue(options.hasOption("groupOption"));
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("longOption1", "longOption1", false, "Long option 1");
 Option option2 = new Option("longOption2", "longOption2", false, "Long option 2");
 Option option3 = new Option("longOption1", "longOption3", false, "Long option 3");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);

 List<String> optionsMatched = options.getMatchingOptions("longOption");
 assertEquals(2, optionsMatched.size());
 assertTrue(optionsMatched.contains("longOption1"));
 assertTrue(optionsMatched.contains("longOption2"));
 }

 @Test
 public void testParse() {
 CommandLineParser parser = new DefaultParser();
 HelpFormatter formatter = new HelpFormatter();

 String[] args = {"-o", "test.txt", "--longOption", "value"};
 try {
 CommandLine cmd = parser.parse(options.parseOptions(args), true);
 } catch (ParseException e) {
 fail("Parsing failed. " + e.getMessage());
 }

 assertTrue(cmd.hasOption("o"));
 assertEquals("test.txt", cmd.getOptionValue("o"));
 assertTrue(cmd.hasOption("longOption"));
 assertEquals("value", cmd.getOptionValue("longOption"));
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"--missingOption"};
 try {
 CommandLine cmd = parser.parse(options.parseOptions(args), true);
 } catch (ParseException e) {
 // Expected
 }
 fail("Parsing did not fail.");
 }
}