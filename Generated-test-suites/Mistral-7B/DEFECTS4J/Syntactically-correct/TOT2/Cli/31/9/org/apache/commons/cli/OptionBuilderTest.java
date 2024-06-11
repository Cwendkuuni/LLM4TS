package org.apache.commons.cli.test;

import junit.framework.TestCase;
import org.apache.commons.cli.*;

public class OptionBuilderTest extends TestCase {

 // Test for withLongOpt method
 public void testWithLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.withLongOpt("optionA").create('A');
 assertEquals("optionA", option.getLongOpt());
 assertEquals('A', option.getOpt());
 }

 // Test for hasArg method
 public void testHasArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArg();
 Option option = optionBuilder.create();
 assertEquals(1, option.getArgs().length);
 }

 // Test for hasArg with false argument
 public void testHasArgFalse() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArg(false);
 Option option = optionBuilder.create();
 assertEquals(Option.UNINITIALIZED, option.getArgs().length);
 }

 // Test for withArgName method
 public void testWithArgName() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withArgName("argName");
 Option option = optionBuilder.create();
 assertEquals("argName", option.getArgName());
 }

 // Test for isRequired method
 public void testIsRequired() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.isRequired();
 Option option = optionBuilder.create();
 assertTrue(option.isRequired());
 }

 // Test for withValueSeparator method
 public void testWithValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withValueSeparator(':');
 Option option = optionBuilder.create();
 assertEquals(':', option.getValueSeparator());
 }

 // Test for hasArgs method
 public void testHasArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArgs();
 Option option = optionBuilder.create();
 assertNotNull(option.getArgs());
 assertEquals(Option.UNLIMITED_VALUES, option.getArgs().length);
 }

 // Test for hasArgs with specified number of arguments
 public void testHasArgsWithSpecifiedNumber() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArgs(3);
 Option option = optionBuilder.create();
 assertNotNull(option.getArgs());
 assertEquals(3, option.getArgs().length);
 }

 // Test for hasOptionalArg method
 public void testHasOptionalArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasOptionalArg();
 Option option = optionBuilder.create();
 assertTrue(option.isOptional());
 }

 // Test for hasOptionalArgs method
 public void testHasOptionalArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasOptionalArgs();
 Option option = optionBuilder.create();
 assertTrue(option.isOptional());
 }

 // Test for hasOptionalArgs with specified number of optional arguments
 public void testHasOptionalArgsWithSpecifiedNumber() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasOptionalArgs(5);
 Option option = optionBuilder.create();
 assertTrue(option.isOptional());
 assertEquals(5, option.getArgs().length);
 }

 // Test for withType method
 public void testWithType() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionType optionType = new OptionType() {
 @Override
 public Class<?> getType() {
 return String.class;
 }
 };
 optionBuilder.withType(optionType);
 Option option = optionBuilder.create();
 assertEquals(String.class, option.getType());
 }

 // Test for withDescription method
 public void testWithDescription() {
 OptionBuilder optionBuilder = new OptionBuilder();
 String description = "This is an option description";
 optionBuilder.withDescription(description);
 Option option = optionBuilder.create();
 assertEquals(description, option.getLongOpt());
 }

 // Test for create method with no settings
 public void testCreateNoSettings() {
 OptionBuilder optionBuilder = new OptionBuilder();
 Option option = optionBuilder.create();
 assertNull(option);
 }

 // Test for create method with invalid long opt
 public void testCreateInvalidLongOpt() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withLongOpt("invalidLongOpt");
 try {
 Option option = optionBuilder.create();
 fail("An IllegalArgumentException should have been thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for create method with required option and no argument
 public void testCreateRequiredNoArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.isRequired();
 Option option = optionBuilder.create();
 try {
 option.parseValue("value");
 fail("An IllegalArgumentException should have been thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for create method with optional option and one argument
 public void testCreateOptionalOneArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArg().isRequired(false);
 Option option = optionBuilder.create();
 String[] args = {"-A", "value"};
 assertTrue(option.parseValue(args));
 assertEquals("value", option.getValue());
 }

 // Test for create method with optional option and multiple arguments
 public void testCreateOptionalMultipleArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArgs().isRequired(false);
 Option option = optionBuilder.create();
 String[] args = {"-A", "value1", "value2"};
 assertTrue(option.parseValue(args));
 String[] values = option.getValues();
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 }

 // Test for create method with required option and multiple arguments
 public void testCreateRequiredMultipleArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasArgs();
 Option option = optionBuilder.create();
 String[] args = {"-A", "value1", "value2"};
 try {
 option.parseValue(args);
 fail("An IllegalArgumentException should have been thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for create method with invalid value separator
 public void testCreateInvalidValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withValueSeparator(';');
 Option option = optionBuilder.create();
 try {
 option.parseValue("value1;value2");
 fail("An IllegalArgumentException should have been thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for create method with valid value separator
 public void testCreateValidValueSeparator() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withValueSeparator('=');
 Option option = optionBuilder.create();
 String[] args = {"-A=value1=value2"};
 assertTrue(option.parseValue(args));
 String[] values = option.getValues();
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 }

 // Test for create method with invalid argument name
 public void testCreateInvalidArgName() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withArgName("invalidArgName");
 Option option = optionBuilder.create();
 try {
 option.parseValue("value");
 fail("An IllegalArgumentException should have been thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 }

 // Test for create method with valid argument name
 public void testCreateValidArgName() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.withArgName("argName");
 Option option = optionBuilder.create();
 String[] args = {"-A", "argName", "value"};
 assertTrue(option.parseValue(args));
 assertEquals("value", option.getValue());
 }

 // Test for create method with optional option and no argument
 public void testCreateOptionalNoArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasOptionalArg();
 Option option = optionBuilder.create();
 String[] args = {"-A"};
 assertTrue(option.parseValue(args));
 assertNull(option.getValue());
 }

 // Test for create method with optional option and one argument
 public void testCreateOptionalOneArg() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasOptionalArg();
 Option option = optionBuilder.create();
 String[] args = {"-A", "value"};
 assertTrue(option.parseValue(args));
 assertEquals("value", option.getValue());
 }

 // Test for create method with optional option and multiple arguments
 public void testCreateOptionalMultipleArgs() {
 OptionBuilder optionBuilder = new OptionBuilder();
 optionBuilder.hasOptionalArgs();
 Option option = optionBuilder.create();
 String[] args = {"-A", "value1", "value2"};
 assertTrue(option.parseValue(args));
 String[] values = option.getValues();
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 }

 // Test for create method with required option and no argument
 public void testCreateRequiredNoArgWithValue() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder optionBuilder2 = new OptionBuilder();
 optionBuilder.isRequired();
 optionBuilder2.hasArg().isRequired(false);
 Option option = optionBuilder.create();
 Option option2 = optionBuilder2.create();
 String[] args = {"-A", "value"};
 try {
 option.parseValue(args);
 fail("An IllegalArgumentException should have been thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 assertEquals("value", option2.getValue());
 }

 // Test for create method with optional option and no argument
 public void testCreateOptionalNoArgWithValue() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder optionBuilder2 = new OptionBuilder();
 optionBuilder.hasOptionalArg();
 optionBuilder2.hasArg().isRequired(false);
 Option option = optionBuilder.create();
 Option option2 = optionBuilder2.create();
 String[] args = {"-A"};
 assertFalse(option.parseValue(args));
 assertEquals("", option.getValue());
 assertEquals("value", option2.getValue());
 }

 // Test for create method with optional option and one argument
 public void testCreateOptionalOneArgWithValue() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder optionBuilder2 = new OptionBuilder();
 optionBuilder.hasOptionalArg();
 optionBuilder2.hasArg().isRequired(false);
 Option option = optionBuilder.create();
 Option option2 = optionBuilder2.create();
 String[] args = {"-A", "value1"};
 assertTrue(option.parseValue(args));
 assertEquals("value1", option.getValue());
 assertEquals("value", option2.getValue());
 }

 // Test for create method with optional option and multiple arguments
 public void testCreateOptionalMultipleArgsWithValue() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder optionBuilder2 = new OptionBuilder();
 optionBuilder.hasOptionalArgs();
 optionBuilder2.hasArgs().isRequired(false);
 Option option = optionBuilder.create();
 Option option2 = optionBuilder2.create();
 String[] args = {"-A", "value1", "value2"};
 assertTrue(option.parseValue(args));
 String[] values = option.getValues();
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 assertEquals("value", option2.getValue());
 }

 // Test for create method with required option and multiple arguments
 public void testCreateRequiredMultipleArgsWithValue() {
 OptionBuilder optionBuilder = new OptionBuilder();
 OptionBuilder optionBuilder2 = new OptionBuilder();
 optionBuilder.hasArgs();
 optionBuilder2.hasArgs().isRequired(false);
 Option option = optionBuilder.create();
 Option option2 = optionBuilder2.create();
 String[] args = {"-A", "value1", "value2"};
 try {
 option.parseValue(args);
 fail("An IllegalArgumentException should have been thrown");
 } catch (IllegalArgumentException e) {
 // Expected
 }
 assertEquals("value1", option2.getValue());
 assertEquals("value2", option2.getArgs()[0]);
 }
}