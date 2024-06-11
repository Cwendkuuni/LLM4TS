package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionWithSingleArgument() {
 Option option = new Option("o", "opt", false, "Description for option o");
 option.setArgName("arg");
 option.setNumberOfArgs(1);

 assertEquals("opt", option.getOpt());
 assertEquals("opt", option.getKey());
 assertEquals(false, option.hasLongOpt());
 assertEquals("Description for option o", option.getDescription());
 assertEquals(1, option.getArgs());
 assertEquals("arg", option.getArgName());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());

 String argValue = "value";
 option.addValueForProcessing(argValue);
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add(argValue);
 List<String> actualValues = option.getValuesList();
 assertEquals(expectedValues, actualValues);
 }

 @Test(expected = RuntimeException.class)
 public void testOptionWithNoArguments() {
 Option option = new Option("o", "opt");
 option.setNumberOfArgs(0);
 option.addValueForProcessing("value");
 }

 @Test
 public void testOptionWithMultipleArguments() {
 Option option = new Option("o", "opt", false, "Description for option o");
 option.setArgName("arg");
 option.setNumberOfArgs(-2);

 assertEquals("opt", option.getOpt());
 assertEquals("opt", option.getKey());
 assertEquals(false, option.hasLongOpt());
 assertEquals("Description for option o", option.getDescription());
 assertEquals(-2, option.getArgs());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());

 String argValue1 = "value1";
 String argValue2 = "value2";
 option.addValueForProcessing(argValue1);
 option.addValueForProcessing(argValue2);
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add(argValue1);
 expectedValues.add(argValue2);
 List<String> actualValues = option.getValuesList();
 assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testOptionWithValueSeparator() {
 Option option = new Option("o", "opt", false, "Description for option o");
 option.setArgName("arg");
 option.setNumberOfArgs(1);
 option.setValueSeparator(';');

 assertEquals("opt", option.getOpt());
 assertEquals("opt", option.getKey());
 assertEquals(false, option.hasLongOpt());
 assertEquals("Description for option o", option.getDescription());
 assertEquals(1, option.getArgs());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());

 String argValue1 = "value1";
 String argValue2 = "value2";
 option.addValueForProcessing(argValue1 + ";" + argValue2);
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add(argValue1);
 expectedValues.add(argValue2);
 List<String> actualValues = option.getValuesList();
 assertEquals(expectedValues, actualValues);
 }

 @Test
 public void testOptionWithLongOptions() {
 Option option = new Option("l", "long-opt", false, "Description for long option");
 option.setArgName("arg");
 option.setNumberOfArgs(1);

 assertEquals("long-opt", option.getOpt());
 assertEquals("long-opt", option.getKey());
 assertEquals(true, option.hasLongOpt());
 assertEquals("Description for long option", option.getDescription());
 assertEquals(1, option.getArgs());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionIsRequired() {
 Option option = new Option("o", "opt", true, "Description for option o");
 option.setArgName("arg");
 option.setNumberOfArgs(1);

 assertEquals("opt", option.getOpt());
 assertEquals("opt", option.getKey());
 assertEquals(false, option.hasLongOpt());
 assertEquals("Description for option o", option.getDescription());
 assertEquals(1, option.getArgs());
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionWithOptionalArgument() {
 Option option = new Option("o", "opt", false, "Description for option o");
 option.setArgName("arg");
 option.setOptionalArg(true);
 option.setNumberOfArgs(1);

 assertEquals("opt", option.getOpt());
 assertEquals("opt", option.getKey());
 assertEquals(false, option.hasLongOpt());
 assertEquals("Description for option o", option.getDescription());
 assertEquals(1, option.getArgs());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionConstruction() {
 Option option = new Option("", "opt");
 }
}