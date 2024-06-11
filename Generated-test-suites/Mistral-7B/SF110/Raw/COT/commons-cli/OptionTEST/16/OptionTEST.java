package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstructorWithOptAndDescription() {
 Option option = new Option("opt", "description");
 assertEquals("opt", option.getOpt());
 assertEquals("description", option.getDescription());
 }

 @Test
 public void testOptionConstructorWithOptHasArgTrueDescription() {
 Option option = new Option("opt", true, "description");
 assertEquals("opt", option.getOpt());
 assertEquals("description", option.getDescription());
 assertEquals(1, option.getArgs());
 }

 @Test
 public void testOptionConstructorWithOptLongOptHasArgTrueDescription() {
 Option option = new Option("opt", "longOpt", true, "description");
 assertEquals("opt", option.getOpt());
 assertEquals("longOpt", option.getLongOpt());
 assertEquals("description", option.getDescription());
 assertEquals(1, option.getArgs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructorWithInvalidOpt() {
 Option option = new Option(null, "description");
 fail("IllegalArgumentException should be thrown");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructorWithInvalidHasArgValue() {
 Option option = new Option("opt", false, "description");
 fail("IllegalArgumentException should be thrown");
 }

 @Test
 public void testOptionGetId() {
 Option option = new Option("opt", "description");
 assertEquals('o', option.getId());
 }

 @Test
 public void testOptionGetKey() {
 Option option = new Option("opt", "description");
 assertEquals("opt", option.getKey());
 }

 @Test
 public void testOptionGetLongOpt() {
 Option option = new Option("opt", "description");
 assertNull(option.getLongOpt());

 Option optionLongOpt = new Option("opt", "longOpt", "description");
 assertEquals("longOpt", optionLongOpt.getLongOpt());
 }

 @Test
 public void testOptionHasOptionalArg() {
 Option option = new Option("opt", "description");
 assertFalse(option.hasOptionalArg());

 Option optionWithOptionalArg = new Option("opt", "description", true, null);
 assertTrue(optionWithOptionalArg.hasOptionalArg());
 }

 @Test
 public void testOptionHasLongOpt() {
 Option option = new Option("opt", "description");
 assertFalse(option.hasLongOpt());

 Option optionWithLongOpt = new Option("opt", "longOpt", null, "description");
 assertTrue(optionWithLongOpt.hasLongOpt());
 }

 @Test
 public void testOptionHasArg() {
 Option option = new Option("opt", "description");
 assertFalse(option.hasArg());

 Option optionWithArg = new Option("opt", "description", true, null);
 assertTrue(optionWithArg.hasArg());
 }

 @Test
 public void testOptionSetArgName() {
 Option option = new Option("opt", "description");
 assertNull(option.getArgName());

 Option optionWithArgName = new Option("opt", "description", false, null);
 optionWithArgName.setArgName("argName");
 assertEquals("argName", optionWithArgName.getArgName());
 }

 @Test
 public void testOptionHasArgName() {
 Option option = new Option("opt", "description");
 assertFalse(option.hasArgName());

 Option optionWithArgName = new Option("opt", "description", false, null);
 optionWithArgName.setArgName("argName");
 assertTrue(optionWithArgName.hasArgName());
 }

 @Test
 public void testOptionHasArgs() {
 Option option = new Option("opt", "description");
 assertFalse(option.hasArgs());

 Option optionWithArgs = new Option("opt", "description", true, null);
 assertTrue(optionWithArgs.hasArgs());
 }

 @Test
 public void testOptionSetArgs() {
 Option option = new Option("opt", "description");
 assertEquals(-1, option.getArgs());

 Option optionWithArgs = new Option("opt", "description", false, null);
 optionWithArgs.setArgs(2);
 assertEquals(2, optionWithArgs.getArgs());
 }

 @Test
 public void testOptionSetValueSeparator() {
 Option option = new Option("opt", "description");
 assertEquals('\0', option.getValueSeparator());

 Option optionWithSeparator = new Option("opt", "description", false, null);
 optionWithSeparator.setValueSeparator(';');
 assertEquals(';', optionWithSeparator.getValueSeparator());
 }

 @Test
 public void testOptionGetValueSeparator() {
 Option option = new Option("opt", "description");
 assertEquals('\0', option.getValueSeparator());

 Option optionWithSeparator = new Option("opt", "description", false, null);
 optionWithSeparator.setValueSeparator(';');
 assertEquals(';', optionWithSeparator.getValueSeparator());
 }

 @Test
 public void testOptionHasValueSeparator() {
 Option option = new Option("opt", "description");
 assertFalse(option.hasValueSeparator());

 Option optionWithSeparator = new Option("opt", "description", false, null);
 optionWithSeparator.setValueSeparator(';');
 assertTrue(optionWithSeparator.hasValueSeparator());
 }

 @Test
 public void testOptionAddValueForProcessing() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 assertEquals("value2", option.getValues().get(1));
 }

 @Test(expected = RuntimeException.class)
 public void testOptionAddValueForProcessingWithNoArgsAllowed() {
 Option option = new Option("opt", "description", -1, null);
 option.addValueForProcessing("value");
 fail("RuntimeException should be thrown");
 }

 @Test
 public void testOptionGetValue() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals("value1", option.getValue());
 }

 @Test
 public void testOptionGetValueWithIndex() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals("value1", option.getValue(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testOptionGetValueWithInvalidIndex() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 option.addValueForProcessing("value3");
 option.addValueForProcessing("value4");
 option.addValueForProcessing("value5");
 assertEquals(5, option.getValues().size());
 option.getValue(6);
 fail("IndexOutOfBoundsException should be thrown");
 }

 @Test
 public void testOptionGetValueWithDefaultValue() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals("value1", option.getValue("defaultValue"));
 }

 @Test
 public void testOptionGetValues() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 String[] expectedValues = new String[]{"value1", "value2"};
 assertArrayEquals(expectedValues, option.getValues());
 }

 @Test
 public void testOptionGetValuesList() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 List expectedValues = new ArrayList();
 expectedValues.add("value1");
 expectedValues.add("value2");
 assertEquals(expectedValues, option.getValuesList());
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("opt", "description");
 assertEquals("[ option: opt :: description ]", option.toString());
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("opt1", "description1");
 Option option2 = new Option("opt2", "description2");
 assertNotEquals(option1.hashCode(), option2.hashCode());

 Option option3 = new Option("opt1", "description1");
 assertEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("opt1", "description1");
 Option option2 = new Option("opt2", "description2");
 assertFalse(option1.equals(option2));

 Option option3 = new Option("opt1", "description1");
 assertTrue(option1.equals(option3));
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 Option clonedOption = option.clone();
 assertEquals(option.getValues().size(), clonedOption.getValues().size());
 for (int i = 0; i < option.getValues().size(); i++) {
 assertEquals(option.getValue(i), clonedOption.getValue(i));
 }
 }

 @Test
 public void testOptionClearValues() {
 Option option = new Option("opt", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 option.clearValues();
 assertTrue(option.hasNoValues());
 }
}