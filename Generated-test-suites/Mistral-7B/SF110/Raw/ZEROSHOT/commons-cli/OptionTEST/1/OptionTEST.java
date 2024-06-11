package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstructionWithArg() {
 Option option = new Option("-a", "test option with arg");
 assertEquals("-a", option.getOpt());
 assertEquals("test option with arg", option.getDescription());
 assertTrue(option.hasArg());
 assertEquals(1, option.getArgs());
 assertNull(option.getLongOpt());
 }

 @Test
 public void testOptionConstructionWithLongOpt() {
 Option option = new Option("--test", "test option with long opt", true);
 assertEquals("--test", option.getOpt());
 assertEquals("test option with long opt", option.getDescription());
 assertTrue(option.hasLongOpt());
 assertFalse(option.hasArg());
 }

 @Test
 public void testOptionConstructionWithLongOptAndArg() {
 Option option = new Option("--test", "test option with long opt and arg", true, "arg");
 assertEquals("--test", option.getOpt());
 assertEquals("test option with long opt and arg", option.getDescription());
 assertTrue(option.hasLongOpt());
 assertTrue(option.hasArg());
 assertEquals(1, option.getArgs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructionWithInvalidOpt() {
 Option option = new Option("", "test option with empty opt");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructionWithInvalidDescription() {
 Option option = new Option("-a", "");
 fail("IllegalArgumentException expected but not thrown");
 }

 @Test
 public void testOptionId() {
 Option option = new Option("-a", "test option with arg");
 assertEquals('a', option.getId());
 }

 @Test
 public void testOptionKey() {
 Option option = new Option("-a", "test option with arg");
 assertEquals("-a", option.getKey());
 Option optionLong = new Option("--test", "test option with long opt");
 assertEquals("--test", optionLong.getKey());
 }

 @Test
 public void testOptionGetOpt() {
 Option option = new Option("-a", "test option with arg");
 assertEquals("-a", option.getOpt());
 Option optionLong = new Option("--test", "test option with long opt");
 assertEquals("--test", optionLong.getOpt());
 }

 @Test
 public void testOptionGetType() {
 Option option = new Option("-a", "test option with arg");
 assertNull(option.getType());
 Option optionWithType = new Option("-a", "test option with arg", String.class);
 assertEquals(String.class, optionWithType.getType());
 }

 @Test
 public void testOptionSetType() {
 Option option = new Option("-a", "test option with arg");
 assertNull(option.getType());
 option.setType(Integer.class);
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testOptionGetLongOpt() {
 Option option = new Option("-a", "test option with arg");
 assertNull(option.getLongOpt());
 Option optionLong = new Option("--test", "test option with long opt");
 assertEquals("--test", optionLong.getLongOpt());
 }

 @Test
 public void testOptionSetLongOpt() {
 Option option = new Option("-a", "test option with arg");
 assertNull(option.getLongOpt());
 option.setLongOpt("--test");
 assertEquals("--test", option.getLongOpt());
 }

 @Test
 public void testOptionSetOptionalArg() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.hasOptionalArg());
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionHasOptionalArg() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.hasOptionalArg());
 Option optionWithArg = new Option("-a", "test option with arg", true);
 assertTrue(optionWithArg.hasOptionalArg());
 }

 @Test
 public void testOptionHasLongOpt() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.hasLongOpt());
 Option optionLong = new Option("--test", "test option with long opt");
 assertTrue(optionLong.hasLongOpt());
 }

 @Test
 public void testOptionHasArg() {
 Option option = new Option("-a", "test option with arg");
 assertTrue(option.hasArg());
 Option optionNoArg = new Option("-a", "test option with no arg");
 assertFalse(optionNoArg.hasArg());
 }

 @Test
 public void testOptionGetDescription() {
 Option option = new Option("-a", "test option with arg", "", "test option description");
 assertEquals("test option description", option.getDescription());
 }

 @Test
 public void testOptionSetDescription() {
 Option option = new Option("-a", "test option with arg");
 assertEquals("", option.getDescription());
 option.setDescription("test option description");
 assertEquals("test option description", option.getDescription());
 }

 @Test
 public void testOptionIsRequired() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.isRequired());
 Option requiredOption = new Option("-a", "test option with arg", true);
 assertTrue(requiredOption.isRequired());
 }

 @Test
 public void testOptionSetRequired() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.isRequired());
 option.setRequired(true);
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionSetArgName() {
 Option option = new Option("-a", "test option with arg");
 assertEquals("arg", option.getArgName());
 Option optionWithArgName = new Option("-a", "test option with arg", "", "test arg name");
 assertEquals("test arg name", optionWithArgName.getArgName());
 }

 @Test
 public void testOptionHasArgName() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.hasArgName());
 Option optionWithArgName = new Option("-a", "test option with arg", "", "test arg name");
 assertTrue(optionWithArgName.hasArgName());
 }

 @Test
 public void testOptionHasArgs() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.hasArgs());
 Option optionWithArgs = new Option("-a", "test option with multiple args", true, "arg1 arg2");
 assertTrue(optionWithArgs.hasArgs());
 }

 @Test
 public void testOptionSetArgs() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.hasArgs());
 option.setArgs(2);
 assertTrue(option.hasArgs());
 }

 @Test
 public void testOptionSetValueSeparator() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.hasValueSeparator());
 option.setValueSeparator(':');
 assertTrue(option.hasValueSeparator());
 }

 @Test
 public void testOptionGetValueSeparator() {
 Option option = new Option("-a", "test option with arg");
 assertFalse(option.hasValueSeparator());
 option.setValueSeparator(':');
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testOptionGetArgs() {
 Option option = new Option("-a", "test option with arg");
 assertEquals(0, option.getArgs());
 Option optionWithArgs = new Option("-a", "test option with multiple args", true, "arg1 arg2");
 assertEquals(2, optionWithArgs.getArgs());
 }

 @Test
 public void testOptionAddValueForProcessing() {
 Option option = new Option("-a", "test option with arg", true);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 assertEquals("value2", option.getValues().get(1));
 }

 @Test(expected = RuntimeException.class)
 public void testOptionAddValueForProcessingWithNoArgs() {
 Option option = new Option("-a", "test option with no arg");
 option.addValueForProcessing("value");
 fail("RuntimeException expected but not thrown");
 }

 @Test
 public void testOptionGetValue() {
 Option option = new Option("-a", "test option with arg", true, "value");
 assertEquals("value", option.getValue());
 Option optionWithValues = new Option("-a", "test option with multiple values", true, "value1 value2");
 assertEquals("value1", optionWithValues.getValue());
 }

 @Test
 public void testOptionGetValueWithIndexOutOfBoundsException() {
 Option option = new Option("-a", "test option with multiple values", true, "value1 value2");
 try {
 assertEquals("value2", option.getValue(1));
 fail("IndexOutOfBoundsException expected but not thrown");
 }
 catch (IndexOutOfBoundsException e) {
 // expected
 }
 }

 @Test
 public void testOptionGetValueWithDefaultValue() {
 Option option = new Option("-a", "test option with arg", true, "defaultValue");
 assertEquals("defaultValue", option.getValue("defaultValue"));
 }

 @Test
 public void testOptionGetValues() {
 Option option = new Option("-a", "test option with multiple values", true, "value1 value2");
 List<String> values = option.getValues();
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testOptionGetValuesList() {
 Option option = new Option("-a", "test option with multiple values", true, "value1 value2");
 List<String> values = option.getValuesList();
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("-a", "test option with arg");
 assertEquals("[ option: -a :: test option with arg ]", option.toString());
 Option optionWithLongOpt = new Option("--test", "test option with long opt");
 assertEquals("[ option: --test :: test option with long opt ]", optionWithLongOpt.toString());
 Option optionWithArg = new Option("-a", "test option with arg", true, "arg");
 assertEquals("[ option: -a :: test option with arg :: [ARG] ]", optionWithArg.toString());
 Option optionWithValues = new Option("-a", "test option with multiple values", true, "value1 value2");
 assertEquals("[ option: -a :: test option with multiple values :: [ARG1 ARG2] ]", optionWithValues.toString());
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("-a", "test option with arg");
 Option option2 = new Option("-a", "test option with arg");
 assertTrue(option1.equals(option2));
 Option option3 = new Option("--test", "test option with long opt");
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("-a", "test option with arg");
 Option option2 = new Option("-a", "test option with arg");
 assertEquals(option1.hashCode(), option2.hashCode());
 Option option3 = new Option("--test", "test option with long opt");
 assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("-a", "test option with arg");
 Option clonedOption = (Option)option.clone();
 assertEquals(option, clonedOption);
 assertEquals(option.getValues(), clonedOption.getValues());
 }

 @Test
 public void testOptionClearValues() {
 Option option = new Option("-a", "test option with multiple values", true, "value1 value2");
 option.clearValues();
 assertEquals(0, option.getValues().size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testOptionAddValue() {
 Option option = new Option("-a", "test option with arg");
 option.addValue("value");
 fail("UnsupportedOperationException expected but not thrown");
 }
}