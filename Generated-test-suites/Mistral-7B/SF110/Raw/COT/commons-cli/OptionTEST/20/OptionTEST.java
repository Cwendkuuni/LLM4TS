package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

public class OptionTest {

 // Basic test cases

 @Test
 public void testOptionConstructionWithArg() {
     Option option = new Option("-a", "option A", true);
     assertEquals("-a", option.getOpt());
     assertEquals("option A", option.getDescription());
     assertTrue(option.hasArg());
     assertFalse(option.hasLongOpt());
     assertNull(option.getLongOpt());
     assertFalse(option.isRequired());
     assertFalse(option.hasOptionalArg());
     assertFalse(option.hasArgs());
     assertArrayEquals(new String[0], option.getValues());
     assertNull(option.getValue());
     assertNull(option.getValue(0));
     assertFalse(option.getValueList().isEmpty());
     assertFalse(option.getValueSeparator() > '\0');
     assertFalse(option.hasValueSeparator());
     assertEquals(0, option.getArgs());
 }

 @Test
 public void testOptionConstructionWithoutArg() {
     Option option = new Option("-b", "option B");
     assertEquals("-b", option.getOpt());
     assertEquals("option B", option.getDescription());
     assertFalse(option.hasArg());
     assertFalse(option.hasLongOpt());
     assertNull(option.getLongOpt());
     assertFalse(option.isRequired());
     assertFalse(option.hasOptionalArg());
     assertFalse(option.hasArgs());
     assertArrayEquals(new String[0], option.getValues());
     assertNull(option.getValue());
     assertNull(option.getValue(0));
     assertFalse(option.getValueList().isEmpty());
     assertFalse(option.getValueSeparator() > '\0');
     assertFalse(option.hasValueSeparator());
     assertEquals(0, option.getArgs());
 }

 @Test
 public void testOptionConstructionWithLongOpt() {
     Option option = new Option("--optionC", "option C", false);
     assertEquals("--optionC", option.getOpt());
     assertEquals("option C", option.getDescription());
     assertFalse(option.hasArg());
     assertTrue(option.hasLongOpt());
     assertEquals("--optionC", option.getLongOpt());
     assertFalse(option.isRequired());
     assertFalse(option.hasOptionalArg());
     assertFalse(option.hasArgs());
     assertArrayEquals(new String[0], option.getValues());
     assertNull(option.getValue());
     assertNull(option.getValue(0));
     assertFalse(option.getValueList().isEmpty());
     assertFalse(option.getValueSeparator() > '\0');
     assertFalse(option.hasValueSeparator());
     assertEquals(0, option.getArgs());
 }

 // Test cases for edge cases and exceptions

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionConstruction() {
     new Option(null, "description");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidOptionChar() {
     new Option("\u0000", "description");
 }

 @Test
 public void testOptionWithMultipleArgs() {
     Option option = new Option("-a", "option A", false, 2);
     assertEquals("-a", option.getOpt());
     assertEquals("option A", option.getDescription());
     assertFalse(option.hasArg());
     assertFalse(option.hasLongOpt());
     assertNull(option.getLongOpt());
     assertFalse(option.isRequired());
     assertFalse(option.hasOptionalArg());
     assertFalse(option.hasArgs());
     assertArrayEquals(new String[0], option.getValues());
     assertNull(option.getValue());
     assertNull(option.getValue(0));
     assertFalse(option.getValueList().isEmpty());
     assertFalse(option.getValueSeparator() > '\0');
     assertFalse(option.hasValueSeparator());
     assertEquals(2, option.getArgs());
 }

 @Test
 public void testSettingAndGettingType() {
     Option option = new Option("-a", "option A");
     option.setType(String.class);
     assertEquals(String.class, option.getType());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidType() {
     Option option = new Option("-a", "option A");
     option.setType(new Object(){});
 }

 @Test
 public void testSettingAndGettingValues() {
     Option option = new Option("-a", "option A");
     option.addValueForProcessing("value1");
     option.addValueForProcessing("value2");
     List<String> expected = new ArrayList<String>(Arrays.asList("value1", "value2"));
     assertEquals(expected, option.getValuesList());
     assertEquals("value1", option.getValue(0));
     assertEquals("value2", option.getValue(1));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGettingValueOutOfBounds() {
     Option option = new Option("-a", "option A");
     option.addValueForProcessing("value1");
     option.addValueForProcessing("value2");
     option.addValueForProcessing("value3");
     option.addValueForProcessing("value4");
     option.getValue(3);
 }

 @Test
 public void testGettingValueWithDefault() {
     Option option = new Option("-a", "option A");
     option.addValueForProcessing("value1");
     option.addValueForProcessing("value2");
     assertEquals("value1", option.getValue("defaultValue"));
 }

 @Test
 public void testSettingAndGettingDescription() {
     Option option = new Option("-a", "option A");
     option.setDescription("new description");
     assertEquals("new description", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testInvalidArgumentNumber() {
     Option option = new Option("-a", "option A");
     option.setArgs(-1);
 }

 @Test
 public void testSettingAndGettingValueSeparator() {
     Option option = new Option("-a", "option A");
     option.setValueSeparator(';');
     assertEquals(';', option.getValueSeparator());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddingValue() {
     Option option = new Option("-a", "option A");
     option.addValue("value1");
 }

 @Test
 public void testSettingAndClearingValues() {
     Option option = new Option("-a", "option A");
     option.addValueForProcessing("value1");
     option.addValueForProcessing("value2");
     option.clearValues();
     assertArrayEquals(new String[0], option.getValues());
     assertNull(option.getValue());
     assertNull(option.getValue(0));
     assertTrue(option.getValueList().isEmpty());
 }

}