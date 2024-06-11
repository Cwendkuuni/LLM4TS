package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.HelpFormatter;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOption() {
 Options options = new Options();
 Option option = new Option("o", "outputDir", true, "output directory");
 options.addOption(option);

 assertEquals(1, options.getOptions().size());
 assertEquals("output directory", options.getOptions().get(0).getLongOpt());
 assertTrue(options.getOptions().get(0).isOptional());
 }

 @Test
 public void testAddOptionWithValue() {
 Options options = new Options();
 Option option = new Option("o", "outputFile", true, "output file");
 options.addOption(option);

 assertEquals(1, options.getOptions().size());
 assertEquals("output file", options.getOptions().get(0).getLongOpt());
 assertTrue(options.getOptions().get(0).isOptional());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "group1");
 Option option1 = new Option("o1", "option1", true, "option1");
 Option option2 = new Option("o2", "option2", true, "option2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);

 assertEquals(4, options.getOptions().size());
 assertEquals("group1", options.getOptionGroup("group1").getName());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddDuplicateOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);
 options.addOption(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateLongOpt() {
 Options options = new Options();
 Option option1 = new Option("o1", "output1", true, "output1");
 Option option2 = new Option("o1", "output2", true, "output2");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateShortOpt() {
 Options options = new Options();
 Option option1 = new Option('o', "output1", true, "output1");
 Option option2 = new Option('o', "output2", true, "output2");
 options.addOption(option1);
 options.addOption(option2);
 }

 @Test
 public void testGetOption() {
 Options options = new Options();
 Option option = new Option("o", "outputDir", true, "output directory");
 options.addOption(option);

 assertEquals(option, options.getOption("o"));
 assertEquals(option, options.getOption("outputDir"));
 }

 @Test(expected = IllegalStateException.class)
 public void testGetOptionMissing() {
 Options options = new Options();

 assertNull(options.getOption("missing"));
 }

 @Test
 public void testHasOption() {
 Options options = new Options();
 Option option = new Option("o", "outputDir", true, "output directory");
 options.addOption(option);

 assertTrue(options.hasOption("o"));
 assertTrue(options.hasOption("outputDir"));
 }

 @Test
 public void testHasShortOption() {
 Options options = new Options();
 Option option = new Option('o', "outputDir", true, "output directory");
 options.addOption(option);

 assertTrue(options.hasShortOption('o'));
 }

 @Test
 public void testHasLongOption() {
 Options options = new Options();
 Option option = new Option("outputDir", "output directory");
 options.addOption(option);

 assertTrue(options.hasLongOption("outputDir"));
 }

 @Test
 public void testHasOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "group1");
 Option option = new Option("o1", "option1", true, "option1");
 group.addOption(option);
 options.addOptionGroup(group);

 assertTrue(options.hasOptionGroup("group1"));
 }

 @Test
 public void testParseOptions() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "output.txt"};

 try {
 CommandLine cmd = parser.parse(options, args);
 assertEquals("output.txt", cmd.getOptionValue("output"));
 } catch (ParseException e) {
 fail("parse failed");
 }
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsMissingOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 String[] args = {};

 try {
 CommandLine cmd = parser.parse(options, args);
 fail("parse succeeded even though an option was missing");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsInvalidOption() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-x", "output.txt"};

 try {
 CommandLine cmd = parser.parse(options, args);
 fail("parse succeeded even though an invalid option was provided");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test(expected = ParseException.class)
 public void testParseOptionsMissingValue() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o"};

 try {
 CommandLine cmd = parser.parse(options, args);
 fail("parse succeeded even though an option was missing a value");
 } catch (ParseException e) {
 // expected
 }
 }

 @Test
 public void testParseOptionsWithValue() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "output.txt"};

 try {
 CommandLine cmd = parser.parse(options, args);
 assertEquals("output.txt", cmd.getOptionValue("output"));
 } catch (ParseException e) {
 fail("parse failed");
 }
 }

 @Test
 public void testParseOptionsWithValues() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "output1.txt", "--output", "output2.txt"};

 try {
 CommandLine cmd = parser.parse(options, args);
 assertEquals("output1.txt", cmd.getOptionValue("output"));
 assertEquals("output2.txt", cmd.getOptionValue("output"));
 } catch (ParseException e) {
 fail("parse failed");
 }
 }

 @Test
 public void testGetOptionValues() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);

 CommandLineParser parser = new DefaultParser();
 String[] args = {"-o", "output1.txt", "--output", "output2.txt"};

 try {
 CommandLine cmd = parser.parse(options, args);
 assertEquals(2, cmd.getOptionValues("output").length);
 assertEquals("output1.txt", cmd.getOptionValues("output")[0]);
 assertEquals("output2.txt", cmd.getOptionValues("output")[1]);
 } catch (ParseException e) {
 fail("parse failed");
 }
 }

 @Test
 public void testHelpFormatter() {
 Options options = new Options();
 Option option = new Option("o", "output", true, "output");
 options.addOption(option);

 HelpFormatter formatter = new HelpFormatter();
 String help = formatter.printHelp("OptionsTest", options);

 assertTrue(help.contains("output\toutput\toutput"));
 assertTrue(help.contains("output [value]"));
 }
}