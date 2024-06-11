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
 public void testAddOptionWithShortKey() {
 Option option = new Option("o", "output", true, "Specify output file");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithShortAndLongKey() {
 Option option = new Option("o", "output,file", true, "Specify output file");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithShortKeyAndArgument() {
 Option option = new Option("o", "output", true, "Specify output file");
 options.addOption(option);
 Option parserOption = new OptionParser();
 parserOption.parse(new String[]{"-o", "test.txt"}, options);
 assertTrue(parserOption.hasOption("output"));
 assertEquals("test.txt", parserOption.getOptionValue("output"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortKey() {
 Option option1 = new Option("o", "output", true, "Specify output file");
 Option option2 = new Option("o", "output", true, "Specify output file");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test
 public void testAddOptionWithLongKey() {
 Option option = new Option("output", "outFile", true, "Specify output file");
 options.addOption(option);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithLongKeyAndArgument() {
 Option option = new Option("output", "outFile", true, "Specify output file");
 options.addOption(option);
 Option parserOption = new OptionParser();
 parserOption.parse(new String[]{"--outFile", "test.txt"}, options);
 assertTrue(parserOption.hasOption("outFile"));
 assertEquals("test.txt", parserOption.getOptionValue("outFile"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongKey() {
 Option option1 = new Option("output", "outFile", true, "Specify output file");
 Option option2 = new Option("output", "outFile", true, "Specify output file");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("group", "Group options");
 Option option = new Option("o", "output", true, "Specify output file", optionGroup);
 options.addOptionGroup(optionGroup);
 options.addOption(option);
 Option parserOption = new OptionParser();
 parserOption.parse(new String[]{"-o", "test.txt"}, options);
 assertTrue(parserOption.hasOption("output"));
 assertEquals("test.txt", parserOption.getOptionValue("output"));
 }

 @Test
 public void testGetMatchingOptions() {
 OptionGroup optionGroup = new OptionGroup("group", "Group options");
 Option option1 = new Option("o1", "option1", true, "Option 1", optionGroup);
 Option option2 = new Option("o2", "option2", true, "Option 2", optionGroup);
 Option option3 = new Option("op", "option", true, "Option", optionGroup);
 options.addOptionGroup(optionGroup);
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> optionsWithPrefix = options.getMatchingOptions("op");
 assertEquals(2, optionsWithPrefix.size());
 List<String> optionsWithPrefix2 = options.getMatchingOptions("group");
 assertEquals(1, optionsWithPrefix2.size());
 }

 @Test
 public void testHasOption() {
 OptionGroup optionGroup = new OptionGroup("group", "Group options");
 Option option1 = new Option("o1", "option1", true, "Option 1", optionGroup);
 Option option2 = new Option("o2", "option2", true, "Option 2", optionGroup);
 Option option3 = new Option("op", "option", true, "Option", optionGroup);
 options.addOptionGroup(optionGroup);
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 assertTrue(options.hasOption("o1"));
 assertTrue(options.hasOption("option"));
 assertFalse(options.hasOption("nonexistent"));
 }

 @Test
 public void testHasLongOption() {
 OptionGroup optionGroup = new OptionGroup("group", "Group options");
 Option option1 = new Option("o1", "option1", true, "Option 1", optionGroup);
 Option option2 = new Option("o2", "option2", true, "Option 2", optionGroup);
 Option option3 = new Option("op", "option", true, "Option", optionGroup);
 options.addOptionGroup(optionGroup);
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 assertTrue(options.hasLongOption("option1"));
 assertFalse(options.hasLongOption("nonexistent"));
 }

 @Test
 public void testHasShortOption() {
 OptionGroup optionGroup = new OptionGroup("group", "Group options");
 Option option1 = new Option("o1", "option1", true, "Option 1", optionGroup);
 Option option2 = new Option("o2", "option2", true, "Option 2", optionGroup);
 Option option3 = new Option("op", "option", true, "Option", optionGroup);
 options.addOptionGroup(optionGroup);
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 assertTrue(options.hasShortOption("o1"));
 assertFalse(options.hasShortOption("nonexistent"));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup optionGroup = new OptionGroup("group", "Group options");
 Option option = new Option("o", "option", true, "Option", optionGroup);
 options.addOptionGroup(optionGroup);
 options.addOption(option);
 OptionGroup parsedOptionGroup = options.getOptionGroup(option);
 assertEquals(optionGroup, parsedOptionGroup);
 }

 @Test
 public void testToString() {
 Options options = new Options();
 Option option1 = new Option("o1", "option1", true, "Option 1");
 Option option2 = new Option("o2", "option2", true, "Option 2");
 options.addOption(option1);
 options.addOption(option2);
 assertEquals("[ Options: [ short {o1=}, {} ] [ long {} {} ]]", options.toString());
 }

}