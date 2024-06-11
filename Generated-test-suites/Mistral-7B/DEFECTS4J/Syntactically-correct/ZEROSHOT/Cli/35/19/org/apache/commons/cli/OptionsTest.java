import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 @Test
 public void testOptionsCreation() {
 Options options = new Options();
 assertEquals(new LinkedHashMap<String, Option>(), options.getShortOpts());
 assertEquals(new LinkedHashMap<String, Option>(), options.getLongOpts());
 assertEquals(new ArrayList<Object>(), options.getRequiredOpts());
 assertEquals(new HashMap<String, OptionGroup>(), options.getOptionGroups());
 }

 @Test
 public void testAddOptionWithShortOpt() {
 Options options = new Options();
 Option opt = new Option("o", "output", false, "Output file path");
 options.addOption(opt);
 assertEquals(Collections.singletonList(opt), options.helpOptions());
 assertEquals(1, options.getRequiredOptions().size());
 }

 @Test
 public void testAddOptionWithLongOpt() {
 Options options = new Options();
 Option opt = new Option(null, "output-file", false, "Output file path");
 options.addOption(opt);
 assertEquals(Collections.singletonList(opt), options.helpOptions());
 assertEquals(1, options.getRequiredOptions().size());
 }

 @Test
 public void testAddOptionWithShortAndLongOpt() {
 Options options = new Options();
 Option opt = new Option("o", "output", true, "Output file path");
 options.addOption(opt);
 assertEquals(Collections.singletonList(opt), options.helpOptions());
 assertEquals(1, options.getRequiredOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup optionGroup = new OptionGroup("group", "Group options");
 options.addOptionGroup(optionGroup);
 options.addOption(optionGroup.getOptions().get(0));
 assertEquals(Collections.singletonList(optionGroup), options.getOptionGroups());
 }

 @Test
 public void testHasOptionWithShortOpt() {
 Options options = new Options();
 Option opt = new Option("o", "output", false, "Output file path");
 options.addOption(opt);
 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testHasOptionWithLongOpt() {
 Options options = new Options();
 Option opt = new Option(null, "output-file", false, "Output file path");
 options.addOption(opt);
 assertTrue(options.hasLongOption("output-file"));
 }

 @Test
 public void testHasOptionWithShortAndLongOpt() {
 Options options = new Options();
 Option opt = new Option("o", "output", true, "Output file path");
 options.addOption(opt);
 assertTrue(options.hasShortOption("o"));
 assertTrue(options.hasLongOption("output"));
 }

 @Test
 public void testGetOptionWithShortOpt() {
 Options options = new Options();
 Option opt = new Option("o", "output", false, "Output file path");
 options.addOption(opt);
 assertEquals(opt, options.getOption("o"));
 }

 @Test
 public void testGetOptionWithLongOpt() {
 Options options = new Options();
 Option opt = new Option(null, "output-file", false, "Output file path");
 options.addOption(opt);
 assertEquals(opt, options.getOption("output-file"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option opt1 = new Option(null, "output-file", false, "Output file path");
 Option opt2 = new Option(null, "output-dir", false, "Output directory path");
 options.addOption(opt1);
 options.addOption(opt2);
 List<String> matchingOptions = options.getMatchingOptions("output");
 assertEquals(1, matchingOptions.size());
 assertEquals(opt1.getLongOpt(), matchingOptions.get(0));
 }

 @Test
 public void testGetMatchingOptionsWithEmptyString() {
 Options options = new Options();
 Option opt1 = new Option(null, "output-file", false, "Output file path");
 Option opt2 = new Option(null, "output-dir", false, "Output directory path");
 options.addOption(opt1);
 options.addOption(opt2);
 List<String> matchingOptions = options.getMatchingOptions("");
 assertEquals(2, matchingOptions.size());
 }

 @Test
 public void testGetMatchingOptionsWithNull() {
 Options options = new Options();
 Option opt1 = new Option(null, "output-file", false, "Output file path");
 Option opt2 = new Option(null, "output-dir", false, "Output directory path");
 options.addOption(opt1);
 options.addOption(opt2);
 List<String> matchingOptions = options.getMatchingOptions(null);
 assertEquals(2, matchingOptions.size());
 }

 @Test
 public void testGetMatchingOptionsWithNoMatchingOptions() {
 Options options = new Options();
 Option opt1 = new Option(null, "output-file", false, "Output file path");
 Option opt2 = new Option(null, "output-dir", false, "Output directory path");
 options.addOption(opt1);
 options.addOption(opt2);
 List<String> matchingOptions = options.getMatchingOptions("notmatching");
 assertEquals(0, matchingOptions.size());
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option opt = new Option("o", "output", false, "Output file path");
 options.addOption(opt);
 assertEquals("[ Options: [ short {o=Output file path} ] [ long {} ]]", options.toString());
 }
}