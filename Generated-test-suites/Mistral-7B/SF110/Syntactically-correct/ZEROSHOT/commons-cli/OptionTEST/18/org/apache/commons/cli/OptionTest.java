package org.apache.commons.cli;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionWithNoArg() {
 Option option = new Option("-o", "Option with no argument");
 assertEquals("-o", option.getOpt());
 assertEquals("Option with no argument", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionWithInvalidOpt() {
 Option option = new Option("", "Option with empty opt");
 }

 @Test
 public void testOptionWithArg() {
 Option option = new Option("-a", "Option with argument", true, "Argument for option -a");
 assertEquals("-a", option.getOpt());
 assertEquals("Argument for option -a", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionWithLongOpt() {
 Option option = new Option("--arg", "Long option with argument", true, "Long argument for option --arg");
 assertEquals("--arg", option.getLongOpt());
 assertEquals("Long argument for option --arg", option.getDescription());
 assertEquals("--arg", option.getOpt());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionWithOptionalArg() {
 Option option = new Option("-b", "Option with optional argument", false, "Optional argument for option -b");
 assertEquals("-b", option.getOpt());
 assertEquals("Optional argument for option -b", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionWithRequiredArg() {
 Option option = new Option("-c", "Option with required argument", false, "Required argument for option -c");
 assertEquals("-c", option.getOpt());
 assertEquals("Required argument for option -c", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionWithLongOptAndArg() {
 Option option = new Option("--data", "Long option with argument", true, "Data to be set");
 assertEquals("--data", option.getLongOpt());
 assertEquals("Data to be set", option.getDescription());
 assertNull(option.getOpt());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testSetOptionalArg() {
 Option option = new Option("-d", "Option with optional argument", false, "Optional argument for option -d");
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testSetArgName() {
 Option option = new Option("-e", "Option with argument name", true, "Argument name for option -e");
 option.setArgName("name");
 assertEquals("name", option.getArgName());
 }

 @Test
 public void testSetDescription() {
 Option option = new Option("-f", "Option with description", false, "Description for option -f");
 option.setDescription("Updated description for option -f");
 assertEquals("Updated description for option -f", option.getDescription());
 }

 @Test
 public void testSetArgNumber() {
 Option option = new Option("-g", "Option with argument number", false, "Argument for option -g");
 option.setArgs(2);
 assertEquals(2, option.getArgs());
 }

 @Test
 public void testSetValueSeparator() {
 Option option = new Option("-h", "Option with value separator", false, "Value separator for option -h");
 option.setValueSeparator(',');
 assertEquals(',', option.getValueSeparator());
 }

 @Test
 public void testAddValue() {
 Option option = new Option("-i", "Option with value", false, "Value for option -i");
 option.addValue("value1");
 option.addValue("value2");
 List valuesList = option.getValuesList();
 assertEquals(2, valuesList.size());
 assertEquals("value1", valuesList.get(0));
 assertEquals("value2", valuesList.get(1));
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueWithLimitReached() {
 Option option = new Option("-j", "Option with value", false, "Value for option -j");
 option.setArgs(1);
 option.addValue("value1");
 option.addValue("value2");
 }

 @Test
 public void testGetValue() {
 Option option = new Option("-k", "Option with value", false, "Value for option -k");
 option.addValue("value1");
 assertEquals("value1", option.getValue());
 }

 @Test
 public void testGetValueWithIndex() {
 Option option = new Option("-l", "Option with multiple values", false, "Multiple values for option -l");
 option.addValue("value1");
 option.addValue("value2");
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));
 }

 @Test
 public void testToString() {
 Option option = new Option("-m", "Option with string representation", false, "String representation for option -m");
 assertEquals("[ option: -m :: Option with string representation :: ]", option.toString());
 }

 @Test
 public void testClearValues() {
 Option option = new Option("-n", "Option with clear values", false, "Clear values for option -n");
 option.addValue("value1");
 option.addValue("value2");
 option.clearValues();
 assertTrue(option.hasNoValues());
 }
}