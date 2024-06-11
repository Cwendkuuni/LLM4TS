package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 // Test case for addOption method with single character short option
 @Test
 public void testAddOptionShort() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "test.txt"});
 assertTrue(cmd.hasOption("output"));
 assertEquals("test.txt", cmd.getOptionValue("output"));
 }

 // Test case for addOption method with long option
 @Test
 public void testAddOptionLong() {
 Options options = new Options();
 Option option = new Option(null, "outputFile", false, "Output file path");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--outputFile", "test.txt"});
 assertTrue(cmd.hasOption("outputFile"));
 assertEquals("test.txt", cmd.getOptionValue("outputFile"));
 }

 // Test case for addOption method with short and long options
 @Test
 public void testAddOptionBoth() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 option.setLongOpt("outputFile");
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-o", "test.txt", "--outputFile", "test.txt.bak"});
 assertTrue(cmd.hasOption("output"));
 assertTrue(cmd.hasOption("outputFile"));
 assertEquals("test.txt", cmd.getOptionValue("output"));
 assertEquals("test.txt.bak", cmd.getOptionValue("outputFile"));
 }

 // Test case for addOptionGroup method
 @Test
 public void testAddOptionGroup() {
 OptionGroup group = new OptionGroup("network", "Networking related options");
 Option opt = new Option("n", "host", true, "Network host to connect to");
 group.addOption(opt);
 Options options = new Options();
 options.addOptionGroup(group);
 CommandLineParser parser = new DefaultParser();
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-n", "localhost"});
 Option networkOption = options.getOption("network");
 assertTrue(networkOption.isRequired());
 assertNull(networkOption.getValue());
 assertEquals("localhost", cmd.getOptionValue("host"));
 }

 // Test case for getMatchingOptions method
 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option option1 = new Option("l", "list", false, "List all available options");
 Option option2 = new Option("l", "limit", true, "Maximum number of options to show");
 options.addOption(option1);
 options.addOption(option2);
 List<String> optionsMatched = options.getMatchingOptions("l");
 assertEquals(2, optionsMatched.size());
 assertEquals(option1.getLongOpt(), optionsMatched.get(0));
 assertEquals(option2.getLongOpt(), optionsMatched.get(1));
 }

 // Test case for hasOption method
 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 assertTrue(options.hasOption("output"));
 }

 // Test case for hasLongOption method
 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option(null, "outputFile", false, "Output file path");
 options.addOption(option);
 assertTrue(options.hasLongOption("outputFile"));
 }

 // Test case for hasShortOption method
 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option("o", "output", false, "Output file path");
 options.addOption(option);
 assertTrue(options.hasShortOption("o"));
 }

 // Test case for getOptionGroup method
 @Test
 public void testGetOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("network", "Networking related options");
 Option opt = new Option("n", "host", true, "Network host to connect to");
 group.addOption(opt);
 options.addOptionGroup(group);
 Option option = options.getOption("network");
 assertEquals(group, options.getOptionGroup(option));
 }

 // Test case for parse method
 @Test(expected = ParseException.class)
 public void testParseException() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 parser.parse(options.toArray(), new String[]{"--invalid"});
 }

 // Test case for parse method with invalid option
 @Test(expected = ParseException.class)
 public void testParseExceptionInvalidOption() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 parser.parse(options.toArray(), new String[]{"--invalidOption"});
 }

 // Test case for parse method with missing required option
 @Test(expected = ParseException.class)
 public void testParseExceptionMissingRequiredOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "Output file path");
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options.toArray(), new String[]{});
 fail();
 } catch (ParseException e) {
 assertEquals("Required option 'output' is missing", e.getMessage());
 }
 }
}