import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.HelpFormatter;
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
 public void testAddOptionShortNameOnly() {
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 assertEquals("Output file path", option.getLongOpt());
 assertFalse(option.hasArg());
 assertNull(option.getValue());
 }

 @Test
 public void testAddOptionShortNameWithArg() {
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 assertEquals("output", option.getOpt());
 assertTrue(option.hasArg());
 assertNull(option.getValue());
 }

 @Test
 public void testAddOptionShortNameWithLongNameAndArg() {
 Option option = new Option("o", "output", true, "Output file path");
 option.setLongOpt("outputFile");
 options.addOption(option);
 assertEquals("outputFile", option.getLongOpt());
 assertTrue(option.hasArg());
 assertNull(option.getValue());
 }

 @Test
 public void testAddOptionLongNameOnly() {
 Option option = new Option("outputFile", "Output file path", false, null);
 options.addOption(option);
 assertEquals("outputFile", option.getLongOpt());
 assertFalse(option.hasArg());
 assertNull(option.getValue());
 }

 @Test
 public void testAddOptionLongNameWithArg() {
 Option option = new Option("outputFile", "Output file path", true, null);
 options.addOption(option);
 assertEquals("outputFile", option.getLongOpt());
 assertTrue(option.hasArg());
 assertNull(option.getValue());
 }

 @Test
 public void testAddOptionShortNameAndLongName() {
 Option option = new Option("o", "output", false, "Output file path");
 option.setLongOpt("outputFile");
 options.addOption(option);
 assertEquals("outputFile", option.getLongOpt());
 assertFalse(option.hasArg());
 assertNull(option.getValue());
 }

 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("group", "Group name");
 Option option = new Option("g", "group", false, "Group flag", group);
 options.addOption(option);
 options.addOptionGroup(group);
 Option opt = options.getOption("g");
 assertNotNull(opt);
 assertEquals("group", opt.getLongOpt());
 assertEquals("Group flag", opt.getDescription());
 assertNull(opt.getValue());
 assertNotNull(opt.getOptionGroup());
 assertEquals("group", opt.getOptionGroup().getName());
 }

 @Test
 public void testHasOptionShortName() {
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 assertTrue(options.hasOption("o"));
 }

 @Test
 public void testHasOptionLongName() {
 Option option = new Option("outputFile", "Output file path", false, null);
 options.addOption(option);
 assertTrue(options.hasOption("outputFile"));
 }

 @Test
 public void testHasOptionWrongName() {
 Option option = new Option("outputFile", "Output file path", false, null);
 options.addOption(option);
 assertFalse(options.hasOption("output"));
 }

 @Test
 public void testParse() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"-o", "output.txt"});
 Option option = options.getOption("o");
 assertNotNull(option);
 assertEquals("Output file path", option.getDescription());
 assertEquals("output.txt", option.getValue());
 }

 @Test(expected = ParseException.class)
 public void testParseWithMissingOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[0]);
 }

 @Test(expected = ParseException.class)
 public void testParseWithInvalidOption() throws ParseException {
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.parseOptions(), new String[]{"-x"});
 }

 @Test
 public void testGetOptions() {
 List<Option> optionsList = options.getOptions();
 assertFalse(optionsList.isEmpty());
 }

 @Test
 public void testGetRequiredOptions() {
 List<Option> optionsList = options.getRequiredOptions();
 assertFalse(optionsList.isEmpty());
 }

 @Test
 public void testGetOption() {
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 Option optionRet = options.getOption("o");
 assertNotNull(optionRet);
 assertEquals(option, optionRet);
 }

 @Test
 public void testGetMatchingOptions() {
 Option option1 = new Option("o", "option1", false, "Option 1");
 Option option2 = new Option("output", "option2", false, "Option 2");
 Option option3 = new Option("outputFile", "option3", false, "Option 3");
 options.addOption(option1);
 options.addOption(option2);
 options.addOption(option3);
 List<String> matchingOptions = options.getMatchingOptions("output");
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.contains("output"));
 assertTrue(matchingOptions.contains("outputFile"));
 }

 @Test
 public void testGetHelp() {
 HelpFormatter formatter = new HelpFormatter();
 String help = formatter.printHelp("Program", options.parseOptions(), true);
 assertNotNull(help);
 }

}