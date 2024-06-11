package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstruction() {
 Option option = new Option("-t", "Test option", true);
 assertEquals("-t", option.getOpt());
 assertEquals("Test option", option.getDescription());
 assertTrue(option.isRequired());
 assertTrue(option.hasArg());
 assertNull(option.getLongOpt());
 assertNull(option.getArgName());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 assertFalse(option.isSet());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.isUnboundedValues());
 }

 @Test
 public void testOptionConstructionWithLongOpt() {
 Option option = new Option("--test", "Test option", true, "test");
 assertEquals("--test", option.getOpt());
 assertEquals("Test option", option.getDescription());
 assertTrue(option.isRequired());
 assertTrue(option.hasArg());
 assertEquals("test", option.getArgName());
 assertNull(option.getType());
 assertTrue(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.isSet());
 }

 @Test
 public void testOptionConstructionWithNoArg() {
 Option option = new Option("-t", "Test option", false);
 assertEquals("-t", option.getOpt());
 assertEquals("Test option", option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertNull(option.getLongOpt());
 assertNull(option.getArgName());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.isSet());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionConstructionWithArgName() {
 Option option = new Option("-t", "Test option", false, "testArg");
 assertEquals("-t", option.getOpt());
 assertEquals("Test option", option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertEquals("testArg", option.getArgName());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.isSet());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionConstructionWithValueSeparator() {
 Option option = new Option("-t", "Test option", false, null, ';');
 assertEquals("-t", option.getOpt());
 assertEquals("Test option", option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertNull(option.getArgName());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertTrue(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.isSet());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionConstructionWithArgAndValueSeparator() {
 Option option = new Option("-t", "Test option", false, "testArg", null, ';');
 assertEquals("-t", option.getOpt());
 assertEquals("Test option", option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertEquals("testArg", option.getArgName());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertTrue(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.isSet());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionConstructionWithUnlimitedValues() {
 Option option = new Option("-t", "Test option", false, null, null, ';', true);
 assertEquals("-t", option.getOpt());
 assertEquals("Test option", option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasArg());
 assertNull(option.getArgName());
 assertNull(option.getType());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertTrue(option.hasValueSeparator());
 assertTrue(option.isUnboundedValues());
 assertFalse(option.hasArgs());
 assertFalse(option.isSet());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("-t", "Test option", true);
 Option option2 = new Option("-t", "Test option", true);
 assertTrue(option1.equals(option2));
 Option option3 = new Option("-f", "Test option", true);
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("-t", "Test option", true);
 Option option2 = new Option("-t", "Test option", true);
 assertEquals(option1.hashCode(), option2.hashCode());
 Option option3 = new Option("-f", "Test option", true);
 int hashCode1 = option1.hashCode();
 int hashCode2 = option3.hashCode();
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("-t", "Test option", true);
 String expected = "[ option: -t :: Test option :: ]";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("-t", "Test option", true);
 Option clonedOption = option.clone();
 assertEquals(option.toString(), clonedOption.toString());
 }

 @Test
 public void testOptionClearValues() {
 Option option = new Option("-t", "Test option", false);
 option.addValue("testValue1");
 option.addValue("testValue2");
 option.clearValues();
 assertFalse(option.isSet());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionAddValue() {
 Option option = new Option("-t", "Test option", false);
 option.addValue("testValue1");
 List<String> expectedValues = new ArrayList<>();
 expectedValues.add("testValue1");
 assertEquals(expectedValues, option.getValuesList());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testOptionAddValueUnsupported() {
 Option option = new Option("-t", "Test option", false);
 option.addValue("testValue1");
 try {
 option.addValue("testValue2");
 }
 catch (UnsupportedOperationException uoe) {
 // Expected exception
 }
 }

 @Test
 public void testOptionHasArgs() {
 Option optionWithArgs = new Option("-t", "Test option", false, null, null, ';', true);
 assertTrue(optionWithArgs.hasArgs());
 Option optionWithoutArgs = new Option("-t", "Test option", false);
 assertFalse(optionWithoutArgs.hasArgs());
 }
}