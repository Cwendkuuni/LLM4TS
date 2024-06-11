package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.apache.commons.cli.ParseException;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

public class OptionTest {

 @Test
 public void testOptionCreation() {
 Option option = new Option("a", "arg", false, "this is an argument option");
 assertEquals("a", option.getOpt());
 assertEquals("arg", option.getArgName());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertFalse(option.hasLongOpt());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithLongOpt() {
 Option option = new Option("a", "long-arg", false, "this is an argument option with long name");
 assertEquals("a", option.getOpt());
 assertEquals("long-arg", option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithHasArg() {
 Option option = new Option("b", null, true, "this is an option with argument");
 assertEquals("b", option.getOpt());
 assertEquals("", option.getArgName());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithHasOptionalArg() {
 Option option = new Option("c", null, false, null, true, "this is an optional argument option");
 assertEquals("c", option.getOpt());
 assertEquals("arg", option.getArgName());
 assertFalse(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertNull(option.getType());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithValue() {
 Option option = new Option("d", null, false, null, null, "this is an option with value");
 assertEquals("d", option.getOpt());
 assertEquals("", option.getArgName());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertEquals("this is an option with value", option.getDescription());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithValueAndValueSeparator() {
 Option option = new Option("e", null, false, null, null, "this is an option with value and separator");
 assertEquals("e", option.getOpt());
 assertEquals("", option.getArgName());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertEquals("this is an option with value and separator", option.getDescription());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertFalse(option.isRequired());
 option.setValueSeparator(':');
 }

 @Test
 public void testOptionCreationWithValues() {
 Option option = new Option("f", null, false, null, null, "this is an option with values");
 assertEquals("f", option.getOpt());
 assertEquals("", option.getArgName());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertFalse(option.hasValue());
 assertFalse(option.isRequired());
 option.addValue("value1");
 option.addValue("value2");
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));
 }

 @Test
 public void testOptionCreationWithValuesAndValueSeparator() {
 Option option = new Option("g", null, false, null, null, "this is an option with values and separator");
 assertEquals("g", option.getOpt());
 assertEquals("", option.getArgName());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertFalse(option.hasValue());
 assertFalse(option.isRequired());
 option.setValueSeparator(':');
 option.addValue("value1:arg1");
 option.addValue("value2:arg2");
 assertEquals(2, option.getValues().size());
 assertEquals("value1:arg1", option.getValue(0));
 assertEquals("value2:arg2", option.getValue(1));
 }

 @Test
 public void testOptionCreationWithType() {
 Option option = new Option("h", null, false, null, null, "this is an option with type", new Integer(5));
 assertEquals("h", option.getOpt());
 assertEquals("", option.getArgName());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNotNull(option.getType());
 assertEquals(Integer.class, option.getType().getClass());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithTypeAndValues() {
 Option option = new Option("i", null, false, null, null, "this is an option with type and values", Integer.TYPE, new String[]{"1", "2", "3"});
 assertEquals("i", option.getOpt());
 assertEquals("", option.getArgName());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNotNull(option.getType());
 assertEquals(Integer.class, option.getType().getClass());
 assertEquals(3, option.getValues().size());
 assertEquals("1", option.getValue(0));
 assertEquals("2", option.getValue(1));
 assertEquals("3", option.getValue(2));
 }

 @Test
 public void testOptionCreationWithRequired() {
 Option option = new Option("j", null, false, "this is an option", true);
 assertEquals("j", option.getOpt());
 assertEquals("", option.getArgName());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertEquals("this is an option", option.getDescription());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionCreationWithLongOptAndRequired() {
 Option option = new Option("k", "long-arg", false, "this is an option with long arg name", true);
 assertEquals("k", option.getOpt());
 assertEquals("long-arg", option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getType());
 assertEquals("this is an option with long arg name", option.getDescription());
 assertFalse(option.hasValue());
 assertFalse(option.hasValues());
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionParsingWithInvalidArg() {
 Option option = new Option("l", null, false, null, null, "this is an option with invalid arg");
 ParseException exception = assertThrows(ParseException.class, () -> OptionParser.parse(new String[]{"-l", "invalidArg"}));
 assertEquals("Invalid value 'invalidArg' for option 'l' with arg 'arg'.", exception.getMessage());
 }

 @Test
 public void testOptionParsingWithOneValue() {
 Option option = new Option("m", null, false, null, null, "this is an option with one value");
 OptionParser parser = new OptionParser();
 OptionSet options = parser.parse(new String[]{"-m", "value"}, option);
 assertEquals("value", option.getValue());
 }

 @Test
 public void testOptionParsingWithMultipleValues() {
 Option option = new Option("n", null, false, null, null, "this is an option with multiple values");
 OptionParser parser = new OptionParser();
 OptionSet options = parser.parse(new String[]{"-n", "value1", "-n", "value2"}, option);
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));
 }

 @Test
 public void testOptionParsingWithValueSeparator() {
 Option option = new Option("o", null, false, null, null, "this is an option with value separator");
 option.setValueSeparator(',');
 OptionParser parser = new OptionParser();
 OptionSet options = parser.parse(new String[]{"-o", "value1,value2"}, option);
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));
 }

 @Test
 public void testOptionParsingWithType() {
 Option option = new Option("p", null, false, null, null, "this is an option with type", Integer.TYPE);
 OptionParser parser = new OptionParser();
 OptionSet options = parser.parse(new String[]{"-p", "5"}, option);
 assertEquals(5, ((Integer)option.getValue()).intValue());
 }

 @Test
 public void testOptionParsingWithTypeAndValues() {
 Option option = new Option("q", null, false, null, null, "this is an option with type and values", Integer.TYPE, new String[]{"1", "2", "3"});
 OptionParser parser = new OptionParser();
 OptionSet options = parser.parse(new String[]{"-q", "1", "-q", "2", "-q", "3"}, option);
 assertEquals(3, option.getValues().size());
 assertEquals("1", option.getValue(0).toString());
 assertEquals("2", option.getValue(1).toString());
 assertEquals("3", option.getValue(2).toString());
 }

 @Test
 public void testOptionParsingWithLongOpt() {
 Option option = new Option("r", null, false, "this is an option with long opt", null, null, null);
 option.setLongOpt("long-option");
 OptionParser parser = new OptionParser();
 OptionSet options = parser.parse(new String[]{"--long-option"}, option);
 assertEquals("this is an option with long opt", option.getDescription());
 }
}