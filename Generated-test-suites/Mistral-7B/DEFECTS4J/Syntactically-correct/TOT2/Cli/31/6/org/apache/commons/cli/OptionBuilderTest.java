package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 // Test case for method: withLongOpt(String newLongopt)
 @Test
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 optionBuilder.withLongOpt("optionNameLong");
 Option actualOption = optionBuilder.create();
 assertEquals(expectedOption.getLongOpt(), actualOption.getLongOpt());
 }

 // Test case for method: hasArg()
 @Test
 public void testHasArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 optionBuilder.hasArg();
 Option actualOption = optionBuilder.create();
 assertEquals(Option.UNINITIALIZED, actualOption.getArgs().length, 0);
 assertFalse(actualOption.isRequired());
 }

 // Test case for method: hasArg(boolean hasArg)
 @Test
 public void testHasArgBoolean() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 expectedOption.setArgs(new String[]{"argName"});
 Option actualOption = optionBuilder.hasArg(true).withArgName("argName").create();
 assertEquals(1, actualOption.getArgs().length);
 assertEquals("argName", actualOption.getArgName());
 }

 // Test case for method: withArgName(String name)
 @Test
 public void testWithArgName() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 expectedOption.setArgName("argName");
 Option actualOption = optionBuilder.withArgName("argName").create();
 assertEquals("argName", actualOption.getArgName());
 }

 // Test case for method: isRequired()
 @Test
 public void testIsRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 Option actualOption = optionBuilder.isRequired().create();
 assertTrue(actualOption.isRequired());
 }

 // Test case for method: withValueSeparator(char sep)
 @Test
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 optionBuilder.withValueSeparator(':');
 Option actualOption = optionBuilder.create();
 assertEquals(Option.VALUE_SEPARATOR_COLON, actualOption.getValueSeparator());
 }

 // Test case for method: withValueSeparator()
 @Test
 public void testWithValueSeparatorEmpty() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 optionBuilder.withValueSeparator();
 Option actualOption = optionBuilder.create();
 assertEquals(Option.VALUE_SEPARATOR_EQUALS, actualOption.getValueSeparator());
 }

 // Test case for method: isRequired(boolean newRequired)
 @Test
 public void testIsRequiredBoolean() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 Option actualOption = optionBuilder.isRequired(false).create();
 assertFalse(actualOption.isRequired());
 }

 // Test case for method: hasArgs()
 @Test
 public void testHasArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 expectedOption.setArgs(new String[]{"arg1", "arg2"});
 Option actualOption = optionBuilder.hasArgs().create();
 assertEquals(Option.UNLIMITED_VALUES, actualOption.getArgs().length);
 }

 // Test case for method: hasArgs(int num)
 @Test
 public void testHasArgsInt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 expectedOption.setArgs(new String[]{"arg1", "arg2", "arg3"});
 Option actualOption = optionBuilder.hasArgs(3).create();
 assertEquals(3, actualOption.getArgs().length);
 }

 // Test case for method: hasOptionalArg()
 @Test
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", false, 'O');
 expectedOption.setArgs(new String[]{"arg"});
 expectedOption.setOptionalArg(true);
 Option actualOption = optionBuilder.hasOptionalArg().create();
 assertFalse(actualOption.isRequired());
 assertEquals(Option.UNINITIALIZED, actualOption.getArgs().length, 0);
 }

 // Test case for method: hasOptionalArgs()
 @Test
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", false, 'O');
 expectedOption.setArgs(new String[]{"arg1", "arg2"});
 expectedOption.setOptionalArg(true);
 Option actualOption = optionBuilder.hasOptionalArgs().create();
 assertFalse(actualOption.isRequired());
 assertEquals(2, actualOption.getArgs().length);
 }

 // Test case for method: hasOptionalArgs(int numArgs)
 @Test
 public void testHasOptionalArgsInt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", false, 'O');
 expectedOption.setArgs(new String[]{"arg1", "arg2", "arg3"});
 expectedOption.setOptionalArg(true);
 Option actualOption = optionBuilder.hasOptionalArgs(3).create();
 assertFalse(actualOption.isRequired());
 assertEquals(3, actualOption.getArgs().length);
 }

 // Test case for method: withType(Object newType)
 @Test
 public void testWithType() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 expectedOption.setType(Integer.class);
 Option actualOption = optionBuilder.withType(Integer.class).create();
 assertEquals(Integer.class, actualOption.getType());
 }

 // Test case for method: withDescription(String newDescription)
 @Test
 public void testWithDescription() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 Option actualOption = optionBuilder.withDescription("New Option description").create();
 assertEquals("New Option description", actualOption.getDescription());
 }

 // Test case for method: create(char opt)
 @Test
 public void testCreateChar() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 Option actualOption = optionBuilder.create('O');
 assertEquals(expectedOption, actualOption);
 }

 // Test case for method: create(String opt)
 @Test
 public void testCreateString() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", "Option description.", true, 'O');
 Option actualOption = optionBuilder.create("optionName");
 assertEquals(expectedOption, actualOption);
 }

 // Test case for method: create()
 @Test(expected = IllegalStateException.class)
 public void testCreateEmpty() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder.reset();
 Option actualOption = optionBuilder.create();
 fail("Expected IllegalStateException");
 }

 // Test case for method: create(char opt) with invalid char
 @Test(expected = IllegalArgumentException.class)
 public void testCreateInvalidChar() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder.reset();
 Option actualOption = optionBuilder.create(' ');
 fail("Expected IllegalArgumentException");
 }

 // Test case for method: create(String opt) with null opt
 @Test(expected = IllegalArgumentException.class)
 public void testCreateNullOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder.reset();
 Option actualOption = optionBuilder.create(null);
 fail("Expected IllegalArgumentException");
 }

 // Test case for method: create(String opt) with null description
 @Test
 public void testCreateNullDescription() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option expectedOption = new Option("optionName", null, true, 'O');
 Option actualOption = optionBuilder.create("optionName");
 assertEquals(expectedOption, actualOption);
 }
}