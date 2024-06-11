import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class OptionsTest {

 @Test
 public void testAddOptionWithShortName() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();

 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "test.txt"});
 Option parsedOption = cmd.getOption("o");
 assertEquals("output", parsedOption.getLongOpt());
 assertEquals("Output file path", parsedOption.getDescription());
 assertNull(parsedOption.getValue());
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = new Options();
 Option option = new Option("outputFile", false, "Output file path");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();

 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--outputFile"});
 Option parsedOption = cmd.getOption("outputFile");
 assertEquals("outputFile", parsedOption.getLongOpt());
 assertEquals("Output file path", parsedOption.getDescription());
 assertNull(parsedOption.getValue());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithInvalidShortName() {
 Options options = new Options();
 Option option = new Option("1", "output", false, "Output file path");
 options.addOption(option);
 }

 @Test
 public void testAddOptionWithShortNameAndValue() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();

 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "test.txt"});
 Option parsedOption = cmd.getOption("o");
 assertEquals("output", parsedOption.getLongOpt());
 assertEquals("Output file path", parsedOption.getDescription());
 assertEquals("test.txt", parsedOption.getValue());
 }

 @Test
 public void testAddOptionWithLongNameAndValue() {
 Options options = new Options();
 Option option = new Option("outputFile", true, "Output file path");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();

 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--outputFile", "test.txt"});
 Option parsedOption = cmd.getOption("outputFile");
 assertEquals("outputFile", parsedOption.getLongOpt());
 assertEquals("Output file path", parsedOption.getDescription());
 assertEquals("test.txt", parsedOption.getValue());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "Group of options");
 Option option = new Option("o", "output", false, "Output file path", group);
 options.addOptionGroup(group);
 CommandLineParser parser = new DefaultParser();

 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "test.txt"});
 Option parsedOption = cmd.getOption("o");
 assertEquals("group", parsedOption.getOptionGroup().getName());
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("opt1", false, "Option 1");
 Option option2 = new Option("opt2", false, "Option 2");
 Option option3 = new Option("opt", false, "Option");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);

 List<Option> matchingOptions = options.getMatchingOptions("opt");
 assertEquals(2, matchingOptions.size());
 assertEquals(option2, matchingOptions.get(0));
 assertEquals(option3, matchingOptions.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("output"));
 assertTrue(options.hasLongOption("output"));
 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 assertTrue(options.hasLongOption("output"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 assertTrue(options.hasShortOption("o"));
 }

 @Test
 public void testGetOption() {
 Options options = new Options();Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);

 Option parsedOption = options.getOption("o");
 assertEquals(option, parsedOption);
 }

 @Test
 public void testGetRequiredOptions() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 options.addOptionGroup(new OptionGroup("group", "Group"));
 options.addOptionGroup(new OptionGroup("group2", "Group2"));
 options.addOptionGroup(new OptionGroup("group3", "Group3"));
 options.addOptionGroup(new OptionGroup("group4", "Group4", true));

 List<Option> requiredOptions = options.getRequiredOptions();
 assertEquals(1, requiredOptions.size());
 assertEquals(options.getOption("group4"), requiredOptions.get(0));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testGetOptionsWithNullArgument() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();

 List<Option> optionsList = options.helpOptions();
 parser.parse(optionsList.toArray(), null);
 }
}