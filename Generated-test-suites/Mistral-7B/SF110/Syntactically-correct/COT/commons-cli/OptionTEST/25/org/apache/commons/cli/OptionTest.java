package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.Option;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

 // Test constructor with required flag and no arguments
 @Test
 public void testOptionConstructor1() {
 Option option = new Option("-o", "Option test");
 assertEquals("Option test", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 }

 // Test constructor with required flag and one argument
 @Test
 public void testOptionConstructor2() {
 Option option = new Option("-o", "Option test", true, "arg");
 assertEquals("Option test", option.getDescription());
 assertTrue(option.hasArg());
 assertTrue(option.isRequired());
 }

 // Test constructor with optional flag and one argument
 @Test
 public void testOptionConstructor3() {
 Option option = new Option("-o", "Option test", false, "arg");
 assertEquals("Option test", option.getDescription());
 assertTrue(option.hasArg());
 assertFalse(option.isRequired());
 }

 // Test constructor with long option and no arguments
 @Test
 public void testOptionConstructor4() {
 Option option = new Option("--long-option", "Long option test");
 assertEquals("Long option test", option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 }

 // Test constructor with long option and one argument
 @Test
 public void testOptionConstructor5() {
 Option option = new Option("--long-option", "Long option test", true, "arg");
 assertEquals("Long option test", option.getLongOpt());
 assertTrue(option.hasArg());
 assertTrue(option.isRequired());
 }

 // Test constructor with long option and optional argument
 @Test
 public void testOptionConstructor6() {
 Option option = new Option("--long-option", "Long option test", false, "arg");
 assertEquals("Long option test", option.getLongOpt());
 assertTrue(option.hasArg());
 assertFalse(option.isRequired());
 }

 // Test getter methods
 @Test
 public void testGetterMethods() {
 Option option = new Option("-o", "Option test", false, "arg");
 assertEquals("-o", option.getOpt());
 assertEquals("Option test", option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 }

 // Test setter methods
 @Test
 public void testSetterMethods() {
 Option option = new Option("-o", "Option test", false, "arg");
 option.setDescription("New description");
 assertEquals("New description", option.getDescription());
 option.setLongOpt("--long-option");
 assertEquals("--long-option", option.getLongOpt());
 option.setRequired(true);
 assertTrue(option.isRequired());
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 }

 // Test validation of number of arguments
 @Test(expected = IllegalArgumentException.class)
 public void testInvalidNumberOfArgs() {
 new Option("-o", "Option test", 3);
 }

 // Test adding a value
 @Test
 public void testAddValue() {
 Option option = new Option("-o", "Option test", false, "arg");
 option.addValue("test1");
 assertEquals("test1", option.getValue());
 option.addValue("test2");
 String[] expectedValues = {"test1", "test2"};
 assertArrayEquals(expectedValues, option.getValues());
 }

 // Test adding multiple values with value separator
 @Test
 public void testAddValuesWithSeparator() {
 Option option = new Option("-o", "Option test", false, "arg", ',');
 option.addValue("test1");
 option.addValue("test2");
 option.addValue("test3");
 String[] expectedValues = {"test1", "test2", "test3"};
 assertArrayEquals(expectedValues, option.getValues());
 }

 // Test adding multiple values without value separator
 @Test
 public void testAddValuesWithoutSeparator() {
 Option option = new Option("-o", "Option test", false, "arg", ' ');
 option.addValue("test1 test2");
 String[] expectedValues = {"test1 test2"};
 assertArrayEquals(expectedValues, option.getValues());
 }

 // Test adding more values than allowed
 @Test(expected = RuntimeException.class)
 public void testAddMoreValuesThanAllowed() {
 Option option = new Option("-o", "Option test", 1, "arg");
 option.addValue("test1");
 option.addValue("test2");
 option.addValue("test3");
 }

 // Test adding empty value
 @Test
 public void testAddEmptyValue() {
 Option option = new Option("-o", "Option test", false, "arg");
 option.addValue("");
 assertEquals("", option.getValue());
 }

 // Test equals method
 @Test
 public void testEqualsMethod() {
 Option option1 = new Option("-o", "Option test", false, "arg");
 Option option2 = new Option("-o", "Option test", false, "arg");
 Option option3 = new Option("-p", "Option test", false, "arg");
 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 // Test hashCode method
 @Test
 public void testHashCodeMethod() {
 Option option1 = new Option("-o", "Option test", false, "arg");
 Option option2 = new Option("-o", "Option test", false, "arg");
 Option option3 = new Option("-p", "Option test", false, "arg");
 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 int hashCode3 = option3.hashCode();
 assertEquals(hashCode1, hashCode2);
 assertNotEquals(hashCode1, hashCode3);
 }

 // Test clone method
 @Test
 public void testCloneMethod() throws CloneNotSupportedException {
 Option option = new Option("-o", "Option test", false, "arg");
 Option clonedOption = (Option)option.clone();
 assertEquals(option.getOpt(), clonedOption.getOpt());
 assertEquals(option.getDescription(), clonedOption.getDescription());
 assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
 assertEquals(option.isRequired(), clonedOption.isRequired());
 assertEquals(option.hasArg(), clonedOption.hasArg());
 assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
 assertEquals(option.getArgs(), clonedOption.getArgs());
 assertEquals(option.getValueSeparator(), clonedOption.getValueSeparator());
 assertEquals(option.getValues(), clonedOption.getValues());
 }

 // Test clearValues method
 @Test
 public void testClearValuesMethod() {
 Option option = new Option("-o", "Option test", false, "arg");
 option.clearValues();
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

}