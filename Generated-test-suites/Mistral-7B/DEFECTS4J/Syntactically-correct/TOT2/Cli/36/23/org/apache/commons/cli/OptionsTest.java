package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option opt = new Option("t", "test", false, "Run tests.");
 options.addOption(opt);

 Assert.assertTrue(options.hasOption("t"));
 Assert.assertFalse(options.hasOption("f")); // Should not contain an option with a different letter
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = new Options();
 Option opt = new Option("o", "output", true, "Output directory.");
 options.addOption(opt);

 Assert.assertTrue(options.hasOption("o"));
 Assert.assertNull(options.getOption("o").getValue());
 }

 @Test
 public void testAddOptionWithLongName() {
 Options options = new Options();
 Option opt = new Option("t", "test", false, "Run tests.");
 options.addOption(opt);
 Option longOpt = new Option("testing", "test", false, "Run tests.");
 options.addOption(longOpt);

 Assert.assertTrue(options.hasOption("testing"));
 Assert.assertTrue(options.hasOption("t")); // Both options should be present
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("testGroup", "Test options:");
 Option opt = new Option("t", "test", false, "Run tests.");
 group.addOption(opt);
 options.addOptionGroup(group);

 Assert.assertTrue(options.hasOption("t"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongName() {
 Options options = new Options();
 Option opt1 = new Option("t", "test", false, "Run tests.");
 options.addOption(opt1);
 Option opt2 = new Option("t", "test", false, "Run tests.");
 options.addOption(opt2);
 }

 @Test
 public void testGetOptions() {
 Options options = new Options();
 Option opt1 = new Option("t", "test", false, "Run tests.");
 options.addOption(opt1);
 Option opt2 = new Option("o", "output", true, "Output directory.");
 options.addOption(opt2);
 Option opt3 = new Option("g", "group", true, "Test group.");
 options.addOptionGroup(new OptionGroup("testGroup", "Test options:"));
 options.addOption(new Option("h", "help", false, "Print this help message."));

 List<Option> optionsList = options.getOptions();

 Assert.assertEquals(4, optionsList.size());
 Assert.assertEquals(opt1, optionsList.get(0));
 Assert.assertEquals(opt2, optionsList.get(1));
 Assert.assertEquals(opt3, optionsList.get(2));
 Assert.assertEquals(options.getOption("h"), optionsList.get(3));
 }

 @Test
 public void testParseRequiredOption() {
 Options options = new Options();
 Option opt = new Option("t", "test", true, "Run tests.");
 options.addOption(opt);

 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"-t"};
 try {
 CommandLine cmd = parser.parse(options, args);
 Assert.assertNotNull(cmd.getOptionValue("t"));
 } catch (ParseException e) {
 Assert.fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequiredOption() {
 Options options = new Options();
 Option opt = new Option("t", "test", true, "Run tests.");
 options.addOption(opt);

 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{};
 try {
 CommandLine cmd = parser.parse(options, args);
 } catch (ParseException e) {
 String errorMessage = e.getMessage();
 Assert.assertTrue(errorMessage.contains("required option 't' is missing"));
 }
 }

 @Test
 public void testParseLongOptionWithArgument() {
 Options options = new Options();
 Option opt = new Option("o", "output", true, "Output directory.");
 options.addOption(opt);

 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--output", "test"};
 try {
 CommandLine cmd = parser.parse(options, args);
 Assert.assertEquals("test", cmd.getOptionValue("output"));
 } catch (ParseException e) {
 Assert.fail("Parsing failed: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidOption() {
 Options options = new Options();
 Option opt = new Option("o", "output", true, "Output directory.");
 options.addOption(opt);

 CommandLineParser parser = new DefaultParser();
 String[] args = new String[]{"--invalid"};
 try {
 CommandLine cmd = parser.parse(options, args);
 } catch (ParseException e) {
 String errorMessage = e.getMessage();
 Assert.assertTrue(errorMessage.contains("invalid option: 'invalid'"));
 }
 }
}