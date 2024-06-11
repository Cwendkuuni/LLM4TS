package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import org.apache.commons.cli.utils.HelpFormatter;
import org.junit.Test;

public class ParserTest {

 private static final String[] ARGS_OPTIONS = {"-a", "--optionA", "-b=valueB", "--optionB=valueB", "--no-option"};
 private static final String[] ARGS_NO_OPTIONS = {"--no-option"};
 private static final String[] ARGS_INVALID = {"--invalid"};

 @Test
 public void testParser() {
 Parser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(new Options(), ARGS_OPTIONS);

 assertEquals("Expected 6 arguments, found: " + cmd.getArgs().size(), 6, cmd.getArgs().size());
 assertEquals("-a", cmd.getOptionValue("optionA"));
 assertEquals("valueB", cmd.getOptionValue("optionB"));
 assertNull("Expected no value for option without value", cmd.getOptionValue("noOption"));

 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testParseInvalidArguments() {
 Parser parser = new DefaultParser();

 try {
 parser.parse(new Options(), ARGS_INVALID);
 fail("Expected ParseException but no exception was thrown");
 } catch (ParseException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseNoOptions() {
 Parser parser = new DefaultParser();

 try {
 CommandLine cmd = parser.parse(new Options(), ARGS_NO_OPTIONS);
 assertTrue("Expected empty arguments list, found: " + cmd.getArgs().size(), cmd.getArgs().isEmpty());
 } catch (ParseException e) {
 fail("Failed to parse command line: " + e.getMessage());
 }
 }

 @Test
 public void testParseHelp() {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 opts.addRequiredOption("h", "help", false, "Print this help message.");

 HelpFormatter formatter = new HelpFormatter();
 formatter.printHelp("test", opts);
 }

 @Test
 public void testParseRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 opts.addRequiredOption("r", "requiredOption", true, "Required option");

 CommandLine cmd = parser.parse(opts, new String[0]);
 fail("Expected MissingOptionException but no exception was thrown");
 }

 @Test(expected = MissingOptionException.class)
 public void testParseMissingRequiredOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 opts.addRequiredOption("r", "requiredOption", true, "Required option");

 parser.parse(opts, ARGS_OPTIONS);
 }

 @Test
 public void testParseOptionalOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 opts.addOption("o", "optionalOption", true, "Optional option");

 CommandLine cmd = parser.parse(opts, ARGS_OPTIONS);
 assertNotNull("Expected optional option to be present", cmd.getOptionValue("optionalOption"));
 }

 @Test
 public void testParseLongOptions() throws ParseException {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 opts.addOption("l", "longOption", true, "Long option");

 CommandLine cmd = parser.parse(opts, ARGS_OPTIONS);
 assertEquals("Expected 'longOption' value to be present", "valueB", cmd.getOptionValue("longOption"));
 }

 @Test
 public void testParseGroups() throws ParseException {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 OptionGroup group = new OptionGroup("group", "Group options");
 group.addOption(new Option("g", "groupOption", false, "Group option"));
 opts.addOptionGroup(group);

 CommandLine cmd = parser.parse(opts, ARGS_OPTIONS);
 assertTrue("Expected group option to be present", cmd.hasOption("groupOption"));
 }

 @Test
 public void testParseMultipleValuesForOption() throws ParseException {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 opts.addOption("v", "verbose", true, "Verbose output");

 CommandLine cmd = parser.parse(opts, ARGS_OPTIONS);
 assertArrayEquals("Expected 'verbose' option to have two values", new String[]{"valueB", "valueB"}, cmd.getOptionValues("verbose"));
 }

 @Test
 public void testParseInvalidValuesForOption() {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 opts.addOption("i", "invalid", true, "Invalid option");

 try {
 parser.parse(opts, ARGS_OPTIONS);
 fail("Expected ParseException but no exception was thrown");
 } catch (ParseException e) {
 // Expected exception
 }
 }

 @Test
 public void testParseInvalidValueForOptionWithArg() {
 Parser parser = new DefaultParser();
 Options opts = new Options();
 opts.addOption("a", "argOption", true, "Argument option");

 try {
 parser.parse(opts, new String[]{"--argOption=invalid"});
 fail("Expected ParseException but no exception was thrown");
 } catch (ParseException e) {
 // Expected exception
 }
 }
}