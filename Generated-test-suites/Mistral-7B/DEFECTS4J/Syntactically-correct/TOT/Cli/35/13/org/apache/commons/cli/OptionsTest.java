package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionsTest {

 @Test
 public void testAddOptionWithShortNameAndNoArgument() {
 Options options = new Options();
 options.addOption("a", "alias", false, "Alias option");

 assertEquals(1, options.getOptions().size());
 Option option = options.getOption("a");
 assertTrue(option.isRequired());
 assertFalse(option.hasArg());
 assertEquals("Alias option", option.getDescription());
 }

 @Test
 public void testAddOptionWithShortNameAndArgument() {
 Options options = new Options();
 options.addOption("b", "booleanOption", true, "Boolean option with argument");

 assertEquals(1, options.getOptions().size());
 Option option = options.getOption("b");
 assertTrue(option.isRequired());
 assertTrue(option.hasArg());
 assertEquals("Boolean option with argument", option.getDescription());
 }

 @Test
 public void testAddOptionWithLongNameAndNoArgument() {
 Options options = new Options();
 options.addOption("anOption", "anOption", false, "An option with a long name");

 assertEquals(1, options.getOptions().size());
 Option option = options.getOption("anOption");
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertEquals("An option with a long name", option.getDescription());
 }

 @Test
 public void testAddOptionWithLongNameAndArgument() {
 Options options = new Options();
 options.addOption("aLongOption", "aLongOption", true, "A long option with an argument");

 assertEquals(1, options.getOptions().size());
 Option option = options.getOption("aLongOption");
 assertFalse(option.isRequired());
 assertTrue(option.hasArg());
 assertEquals("A long option with an argument", option.getDescription());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddOptionWithDuplicateName() {
 Options options = new Options();
 options.addOption("a", "alias", false, "Alias option");
 options.addOption("a", "anotherAlias", false, "Another alias option");

 assertEquals(1, options.getOptions().size());
 }

 @Test
 public void testAddOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("Group", "A test group");
 Option option1 = new Option("option1", "option1", true, "Option 1");
 Option option2 = new Option("option2", "option2", true, "Option 2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);

 assertEquals(2, options.getOptions().size());
 assertFalse(options.getOption("option1").isRequired());
 assertFalse(options.getOption("option1").hasArg());
 assertTrue(options.getOption("option2").isRequired());
 assertFalse(options.getOption("option2").hasArg());
 }

 @Test
 public void testAddOptionsWithOptionGroup() {
 Options options = new Options();
 OptionGroup group = new OptionGroup("Group", "A test group");
 Option option1 = new Option("option1", "option1", true, "Option 1");
 Option option2 = new Option("option2", "option2", true, "Option 2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);

 assertEquals(3, options.getOptions().size());
 assertFalse(options.getOption("option1").isRequired());
 assertFalse(options.getOption("option1").hasArg());
 assertTrue(options.getOption("option2").isRequired());
 assertFalse(options.getOption("option2").hasArg());
 }

 @Test (expected = IllegalArgumentException.class)
 public void testAddOptionGroupWithDuplicateName() {
 Options options = new Options();
 OptionGroup group1 = new OptionGroup("Group", "A test group");
 OptionGroup group2 = new OptionGroup("Group", "Another test group");
 options.addOptionGroup(group1);
 options.addOptionGroup(group2);

 assertEquals(0, options.getOptions().size());
 }

 @Test
 public void testParse() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alias", false, "Alias option");
 options.parse(new String[]{"-a"});

 assertEquals(1, options.getOptions().size());
 Option option = options.getOption("a");
 assertTrue(option.isSet());
 }

 @Test (expected = ParseException.class)
 public void testParseWithUnknownOption() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alias", false, "Alias option");
 options.parse(new String[]{"-z"});

 assertEquals(0, options.getOptions().size());
 }

 @Test (expected = ParseException.class)
 public void testParseWithRequiredOptionMissing() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alias", true, "Alias option");
 options.parse(new String[]{});

 assertEquals(0, options.getOptions().size());
 }

 @Test
 public void testParseWithOptionValue() throws ParseException {
 Options options = new Options();
 options.addOption("a", "alias", false, "Alias option");
 options.addOption("b", "booleanOption", false, "Boolean option");
 options.parse(new String[]{"-a", "--booleanOption=true"});

 assertEquals(2, options.getOptions().size());
 Option optionA = options.getOption("a");
 assertTrue(optionA.isSet());
 Option optionB = options.getOption("booleanOption");
 assertTrue(optionB.getValue().equals("true"));
 }

 @Test
 public void testParseWithOptionGroup() throws ParseException {
 Options options = new Options();
 OptionGroup group = new OptionGroup("Group", "A test group");
 Option option1 = new Option("option1", "option1", true, "Option 1");
 Option option2 = new Option("option2", "option2", true, "Option 2");
 group.addOption(option1);
 group.addOption(option2);
 options.addOptionGroup(group);
 options.parse(new String[]{"--group"});

 assertEquals(3, options.getOptions().size());
 assertTrue(options.getOption("option1").isSet());
 assertTrue(options.getOption("option2").isSet());
 }
}