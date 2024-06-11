package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionCreationWithNoArg() {
 Option option = new Option("opt", "Description for option opt");
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertNull(option.getValue());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithOptionalArg() {
 Option option = new Option("opt", true, "Description for option opt");
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertNull(option.getValue());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithMandatoryArg() {
 Option option = new Option("opt", false, "Description for option opt", "arg");
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertNull(option.getValue());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithLongOption() {
 Option option = new Option("opt", null, false, "long-opt", "Description for option opt");
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertEquals("long-opt", option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertNull(option.getValue());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithOptionalArgAndValues() {
 Option option = new Option("opt", true, "Description for option opt", "arg");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithMandatoryArgAndValues() {
 Option option = new Option("opt", false, "Description for option opt", "arg");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithLongOptAndValues() {
 Option option = new Option("opt", null, false, "long-opt", "Description for option opt");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertEquals("long-opt", option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithOptionalArgAndValuesAndValueSeparator() {
 Option option = new Option("opt", true, "Description for option opt", "arg", ',');
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertTrue(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithMandatoryArgAndValuesAndValueSeparator() {
 Option option = new Option("opt", false, "Description for option opt", "arg", ',');
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertTrue(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithOptionalArgAndValuesAndValuesSeparatorIsSpecifiedButEmpty() {
 Option option = new Option("opt", true, "Description for option opt", "arg", "");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithOptionalArgAndValuesAndValuesSeparatorIsSpecified() {
 Option option = new Option("opt", true, "Description for option opt", "arg", ';');
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertTrue(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithOptionalArgAndValuesAndValuesSeparatorIsSpecifiedAndMoreThanOneValue() {
 Option option = new Option("opt", true, "Description for option opt", "arg", ';');
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertTrue(option.hasValueSeparator());
 assertTrue(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithMandatoryArgAndValuesAndValuesSeparatorIsSpecified() {
 Option option = new Option("opt", false, "Description for option opt", "arg", ';');
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 assertEquals("opt", option.getOpt());
 assertEquals("Description for option opt", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertEquals("arg", option.getArgName());
 assertNull(option.getType());
 assertEquals(expectedValues, option.getValuesList());
 assertTrue(option.hasValueSeparator());
 assertTrue(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("opt1", false, null, null, null);
 Option option2 = new Option("opt1", false, null, null, null);
 Option option3 = new Option("opt2", false, null, null, null);
 Option option4 = new Option("opt1", false, "Description for option1", null, null);
 assertEquals(option1, option1);
 assertEquals(option1, option2);
 assertNotEquals(option1, option3);
 assertNotEquals(option1, option4);
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("opt1", false, null, null, null);
 Option option2 = new Option("opt1", false, null, null, null);
 Option option3 = new Option("opt2", false, null, null, null);
 Option option4 = new Option("opt1", false, "Description for option1", null, null);
 int hashCodeOption1 = option1.hashCode();
 int hashCodeOption2 = option2.hashCode();
 int hashCodeOption3 = option3.hashCode();
 int hashCodeOption4 = option4.hashCode();
 assertEquals(hashCodeOption1, hashCodeOption2);
 assertNotEquals(hashCodeOption1, hashCodeOption3);
 assertNotEquals(hashCodeOption1, hashCodeOption4);
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("opt", false, "Description for option opt", null, null);
 assertEquals("[ option: opt :: Description for option opt ]", option.toString());
 }

 @Test
 public void testOptionClearValues() {
 Option option = new Option("opt", false, "Description for option opt", "arg", null);
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("value1");
 expectedValues.add("value2");
 option.setValues(expectedValues);
 option.clearValues();
 assertEquals(0, option.getValuesList().size());
 }
}