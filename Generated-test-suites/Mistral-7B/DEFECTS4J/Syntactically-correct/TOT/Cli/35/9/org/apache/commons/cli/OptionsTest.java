package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testSimpleOption() {
 Options options = new Options();
 options.addOption("a", "example", false, "An example option");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a"});
 assertTrue(cmd.hasOption("a"));
 } catch (ParseException e) {
 fail("Failed to parse: " + e.getMessage());
 }
 }

 @Test
 public void testOptionWithValue() {
 Options options = new Options();
 options.addOption("b", "value", true, "An option with a value");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-b", "test"});
 assertEquals("test", cmd.getOptionValue("b"));
 } catch (ParseException e) {
 fail("Failed to parse: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingOption() {
 Options options = new Options();
 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options.toArray(), new String[]{"-c"});
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }

 @Test
 public void testLongOption() {
 Options options = new Options();
 options.addOption("example", "e", false, "An example long option");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"--example"});
 assertTrue(cmd.hasOption("example"));
 } catch (ParseException e) {
 fail("Failed to parse: " + e.getMessage());
 }
 }

 @Test
 public void testOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("group", "A group of options");
 options.addOptionGroup(group);
 group.addOption("a", true, "A group option");
 options.addOption("b", true, "Another option");

 CommandLineParser parser = new DefaultParser();
 try {
 CommandLine cmd = parser.parse(options.toArray(), new String[]{"-a", "--group", "-b"});
 assertTrue(cmd.hasOption("a"));
 assertTrue(cmd.hasOption("b"));
 } catch (ParseException e) {
 fail("Failed to parse: " + e.getMessage());
 }
 }

 @Test(expected = ParseException.class)
 public void testMissingRequiredOption() {
 Options options = new Options();
 options.addOption("required", true, "A required option");
 CommandLineParser parser = new DefaultParser();
 try {
 parser.parse(options.toArray(), new String[]{"-a"});
 fail("Expected ParseException");
 } catch (ParseException e) {
 // Expected
 }
 }
}