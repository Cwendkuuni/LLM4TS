package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class OptionsTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 options.addOption("a", "optionA", false, "Option A");
 options.addOption("b", "optionB", true, "Option B");
 options.addOptionGroup(new OptionGroup("group", "Group"));
 options.addOptionGroup(new OptionGroup("group2", "Group2"));
 options.getOptionGroup("group").addOption("c", "optionC", false, "Option C in Group");
 options.getOptionGroup("group2").addOption("d", "optionD", false, "Option D in Group2");
 }

 @Test
 public void testGetOptions() {
 List<Option> options = this.options.helpOptions();
 assertEquals(4, options.size());
 }

 @Test
 public void testGetRequiredOptions() {
 List<Option> requiredOptions = this.options.getRequiredOptions();
 assertEquals(0, requiredOptions.size());
 }

 @Test
 public void testGetOption() {
 Option optionA = this.options.getOption("a");
 assertTrue(optionA.isRequired());
 }

 @Test
 public void testGetMatchingOptions() {
 List<String> matchingOptions = this.options.getMatchingOptions("o");
 assertEquals(2, matchingOptions.size());
 assertTrue(matchingOptions.contains("optionB"));
 assertTrue(matchingOptions.contains("optionGroup"));
 }

 @Test
 public void testHasOption() {
 assertTrue(this.options.hasOption("a"));
 assertTrue(this.options.hasOption("b"));
 assertTrue(this.options.hasOption("c"));
 }

 @Test
 public void testHasLongOption() {
 assertTrue(this.options.hasLongOption("optionA"));
 assertTrue(this.options.hasLongOption("optionB"));
 assertTrue(this.options.hasLongOption("optionC"));
 }

 @Test
 public void testHasShortOption() {
 assertTrue(this.options.hasShortOption("a"));
 assertTrue(this.options.hasShortOption("b"));
 }

 @Test
 public void testGetOptionGroup() {
 OptionGroup group = this.options.getOptionGroup("group");
 OptionGroup group2 = this.options.getOptionGroup("group2");
 Option optionC = group.getOption("c");
 Option optionD = group2.getOption("d");
 assertTrue(optionC != null);
 assertTrue(optionD != null);
 }

 @Test
 public void testParse() {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "--optionB", "-d"};
 try {
 CommandLine cmd = parser.parse(this.options, args);
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("optionB"));
 assertTrue(cmd.hasOption("d"));
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Parsing failed");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseInvalid() {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a", "--invalidOption"};
 try {
 CommandLine cmd = parser.parse(this.options, args);
 fail("Parsing did not fail");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseMissingRequired() {
 CommandLineParser parser = new DefaultParser();
 String[] args = {"-a"};
 try {
 CommandLine cmd = parser.parse(this.options, args);
 fail("Parsing did not fail");
 } catch (ParseException e) {
 // expected
 }
 }
}