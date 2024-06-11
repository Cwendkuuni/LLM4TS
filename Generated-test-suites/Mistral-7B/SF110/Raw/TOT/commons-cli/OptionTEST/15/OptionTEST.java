package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionCreation() {
 Option option = new Option("-t", "--type", true, "Specify the input type");
 assertEquals("-t", option.getOpt());
 assertEquals("--type", option.getLongOpt());
 assertEquals(true, option.hasOptionalArg());
 assertEquals("Specify the input type", option.getDescription());
 }

 @Test
 public void testOptionCreationWithArguments() {
 Option option = new Option("-i", "--input", false, "Input file path", "arg");
 assertEquals("-i", option.getOpt());
 assertEquals("--input", option.getLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals("arg", option.getArgName());
 assertEquals("Input file path", option.getDescription());
 }

 @Test
 public void testOptionCreationWithMultipleArguments() {
 Option option = new Option("-p", "--paths", false, "Input file paths", "arg1 arg2");
 assertEquals("-p", option.getOpt());
 assertEquals("--paths", option.getLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals("arg1 arg2", option.getArgName());
 assertEquals("Input file paths", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionCreation() {
 try {
 new Option("-", "--invalid", true, "Invalid option");
 fail("An IllegalArgumentException was not thrown.");
 }
 catch (IllegalArgumentException e) {
 // Expected, test passes
 }
 }

 @Test
 public void testSettingGetType() {
 Option option = new Option("-f", "--file", false, "Input file path");
 option.setType(String[].class);
 assertEquals(String[].class, option.getType());
 }

 @Test
 public void testSettingGetLongOptWithNullValue() {
 Option option = new Option("-l", null, false, "Log file path");
 assertEquals(null, option.getLongOpt());
 }

 @Test
 public void testSettingGetOptionalArgWithFalseValue() {
 Option option = new Option("-n", "--noarg", false, "Option without argument");
 assertEquals(false, option.hasOptionalArg());
 }

 @Test
 public void testSettingGetOptionalArgWithTrueValue() {
 Option option = new Option("-y", "--yes", true, "Option with an optional argument");
 assertEquals(true, option.hasOptionalArg());
 }

 @Test
 public void testSettingGetDescriptionWithNullValue() {
 Option option = new Option("-d", "--description", false, null);
 assertEquals(null, option.getDescription());
 }

 @Test
 public void testSettingGetDescriptionWithEmptyValue() {
 Option option = new Option("-e", "--empty", false, "");
 assertEquals("", option.getDescription());
 }

 @Test
 public void testGetId() {
 Option option = new Option("-a", null, false, "Option A");
 assertEquals('a', option.getId());
 }

 @Test
 public void testGetKey() {
 Option option = new Option("-b", "--big", false, "Option B");
 assertEquals("-b", option.getKey());
 }

 @Test
 public void testGetValue() {
 Option option = new Option("-v", "--value", false, "Option with value");
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals("value1", option.getValue());
 }

 @Test
 public void testGetValueWithNoValues() {
 Option option = new Option("-x", "--x", false, "Option without value");
 assertEquals(null, option.getValue());
 }

 @Test
 public void testGetValueWithMultipleValues() {
 Option option = new Option("-c", "--colors", false, "Option with multiple values");
 option.addValueForProcessing("red");
 option.addValueForProcessing("green");
 option.addValueForProcessing("blue");
 assertEquals(new String[]{"red", "green", "blue"}, option.getValues());
 }

 @Test(expected = RuntimeException.class)
 public void testAddingValueWithWrongNumberOfArgs() {
 Option option = new Option("-m", "--multi-args", false, "Option with multiple arguments");
 option.addValueForProcessing("value1 value2 value3 value4");
 try {
 option.addValueForProcessing("value5");
 fail("A RuntimeException was not thrown.");
 }
 catch (RuntimeException e) {
 // Expected, test passes
 }
}

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGettingValueWithOutOfBoundsIndex() {
 Option option = new Option("-w", "--words", false, "Option with multiple values");
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 try {
 option.getValue(2);
 fail("An IndexOutOfBoundsException was not thrown.");
 }
 catch (IndexOutOfBoundsException e) {
 // Expected, test passes
 }
}
}