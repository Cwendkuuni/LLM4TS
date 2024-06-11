package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.BasicParser;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some option");
 options.addOption(opt);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithArgument() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some option with argument");
 options.addOption(opt);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionWithNoArgument() {
 Options options = new Options();
 Option opt = new Option("o", "option", false, "Some option without argument");
 options.addOption(opt);
 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "Group 1");
 Option opt = new Option("o", "option", true, "Some option in group 1");
 group.addOption(opt);
 options.addOptionGroup(group);
 assertEquals(2, options.getOptions().size());
 }

 @Test
 public void testAddRequiredOption() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some required option");
 opt.setRequired(true);
 options.addOption(opt);
 assertTrue(options.getRequiredOptions().contains(opt));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some option");
 options.addOption(opt);
 options.addOption(opt);
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some option");
 options.addOption(opt);
 assertEquals(opt, options.getOption("o"));
 assertNull(options.getOption("n"));
 }

 @Test
 public void testGetMatchingOptions() {
 Options options = new Options();
 Option opt1 = new Option("o", "option1", true, "Option 1");
 Option opt2 = new Option("o", "option2", true, "Option 2");
 options.addOption(opt1);
 options.addOption(opt2);
 List<String> matchingOptions = options.getMatchingOptions("o");
 assertEquals(2, matchingOptions.size());
 assertEquals(opt1.getKey(), matchingOptions.get(0));
 assertEquals(opt2.getKey(), matchingOptions.get(1));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some option");
 options.addOption(opt);
 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("option"));
 assertFalse(options.hasOption("n"));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some option");
 options.addOption(opt);
 assertTrue(options.hasLongOption("option"));
 assertFalse(options.hasLongOption("n"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some option");
 options.addOption(opt);
 assertTrue(options.hasShortOption("o"));
 assertFalse(options.hasShortOption("n"));
 }

 @Test
 public void testParseOptionWithArgument() {
 Options options = new Options();
 Option opt = new Option("o", "option", true, "Some option");
 CommandLineParser parser = new BasicParser();
 String[] args = {"--option", "arg"};
 CommandLine cmd = parser.parse(options.toArray(), args);
 assertEquals("arg", cmd.getOptionValue("option"));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOptionWithoutArgument() {
 Options options = new Options();Option opt = new Option("o", "option", false, "Some option");
 CommandLineParser parser = new BasicParser();
 String[] args = {"--option"};
 CommandLine cmd = parser.parse(options.toArray(), args);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseOptionWithInvalidArgument() {
 Options options = new Options();Option opt = new Option("o", "option", true, "Some option");
 CommandLineParser parser = new BasicParser();
 String[] args = {"--option", "invalid argument"};
 CommandLine cmd = parser.parse(options.toArray(), args);
 }
}