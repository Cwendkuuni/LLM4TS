package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testBasicUsage() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "input file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse(new String[]{"-i", "/path/to/input.txt"});
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Failed to parse command line.");
 }

 assertTrue(cmd.hasOption("i"));
 assertEquals("/path/to/input.txt", cmd.getOptionValue("i"));
 }

 @Test
 public void testLongOption() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "input file path");
 options.addOption("f", "fileName", true, "file name");

 CommandLine cmd = null;
 try {
 cmd = options.parse(new String[]{"--fileName", "/path/to/file.txt"});
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Failed to parse command line.");
 }

 assertTrue(cmd.hasOption("fileName"));
 assertEquals("/path/to/file.txt", cmd.getOptionValue("fileName"));
 assertFalse(cmd.hasOption("i"));
 }

 @Test
 public void testMissingOption() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "input file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse(new String[]{"--outputFile", "/path/to/output.txt"});
 } catch (ParseException e) {
 // Expected, missing option "i"
 }

 assertFalse(cmd.hasOption("i"));
 }

 @Test(expected = ParseException.class)
 public void testInvalidOption() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "input file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse(new String[]{"--invalid", "/path/to/input.txt"});
 } catch (ParseException e) {
 // Expected, invalid option "--invalid"
 }
 }

 @Test
 public void testMultipleOptions() {
 Options options = new Options();
 options.addOption("i", "inputFile", true, "input file path");
 options.addOption("o", "outputFile", true, "output file path");

 CommandLine cmd = null;
 try {
 cmd = options.parse(new String[]{"-i", "/path/to/input.txt", "--outputFile", "/path/to/output.txt"});
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Failed to parse command line.");
 }

 assertTrue(cmd.hasOption("inputFile"));
 assertEquals("/path/to/input.txt", cmd.getOptionValue("inputFile"));
 assertTrue(cmd.hasOption("outputFile"));
 assertEquals("/path/to/output.txt", cmd.getOptionValue("outputFile"));
 }

 @Test
 public void testOptionGroupUsage() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group1", "group1");
 group.setRequired(true);

 options.addOptionGroup(group);
 group.addOption(new Option("g", "group", false, "group option"));
 group.addOption(new Option("h", false, false, "help"));

 CommandLine cmd = null;
 try {
 cmd = options.parse(new String[]{"--group"});
 } catch (ParseException e) {
 e.printStackTrace();
 fail("Failed to parse command line.");
 }

 assertTrue(cmd.hasOptionGroup("group1"));
 assertTrue(cmd.hasOption("group"));
 assertFalse(cmd.hasOption("help"));
 }
}