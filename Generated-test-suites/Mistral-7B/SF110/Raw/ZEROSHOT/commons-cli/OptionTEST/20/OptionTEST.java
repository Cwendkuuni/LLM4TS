package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionConstructionWithOnlyShortOption() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertEquals("t", option.getOpt());
 assertEquals("testOption", option.getLongOpt());
 assertTrue(option.hasArg());
 assertEquals(1, option.getArgs());
 assertNull(option.getType());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertEquals("Test option description", option.getDescription());
 }

 @Test
 public void testOptionConstructionWithOnlyLongOption() {
 Option option = new Option("--testOption", "Test long option", true, "Test long option description");
 assertEquals(null, option.getOpt());
 assertEquals("--testOption", option.getLongOpt());
 assertTrue(option.hasArg());
 assertEquals(1, option.getArgs());
 assertNull(option.getType());
 assertNull(option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertEquals("Test long option description", option.getDescription());
 }

 @Test
 public void testOptionConstructionWithShortAndLongOption() {
 Option option = new Option("t", "testOption", true, "Test option description");
 Option optionWithLong = new Option("--testOption", "Test long option", true, "Test long option description");
 assertEquals("t", option.getOpt());
 assertEquals("testOption", option.getLongOpt());
 assertEquals("Test option description", option.getDescription());
 assertEquals(null, optionWithLong.getOpt());
 assertEquals("--testOption", optionWithLong.getLongOpt());
 assertEquals("Test long option description", optionWithLong.getDescription());
 }

 @Test
 public void testOptionId() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertEquals('t', option.getId());
 }

 @Test
 public void testGetOpt() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertEquals("t", option.getOpt());
 }

 @Test
 public void testGetType() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertNull(option.getType());
 }

 @Test
 public void testSetType() {
 Option option = new Option("t", "testOption", true, "Test option description");
 option.setType(String.class);
 assertEquals(String.class, option.getType());
 }

 @Test
 public void testGetLongOpt() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertNull(option.getLongOpt());
 }

 @Test
 public void testSetLongOpt() {
 Option option = new Option("t", "testOption", true, "Test option description");
 option.setLongOpt("testLongOption");
 assertEquals("testLongOption", option.getLongOpt());
 }

 @Test
 public void testSetOptionalArg() {
 Option option = new Option("t", "testOption", true, "Test option description");
 option.setOptionalArg(false);
 assertFalse(option.hasOptionalArg());
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testHasOptionalArg() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.hasOptionalArg());
 Option optionWithArg = new Option("t", "testOption", true, "Test option description", true);
 assertTrue(optionWithArg.hasOptionalArg());
 }

 @Test
 public void testHasLongOpt() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.hasLongOpt());
 Option optionWithLongOpt = new Option("--testOption", "Test long option", true, "Test long option description");
 assertTrue(optionWithLongOpt.hasLongOpt());
 }

 @Test
 public void testHasArg() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.hasArg());
 Option optionWithArg = new Option("t", "testOption", true, "Test option description", "arg");
 assertTrue(optionWithArg.hasArg());
 }

 @Test
 public void testGetDescription() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertEquals("Test option description", option.getDescription());
 }

 @Test
 public void testSetDescription() {
 Option option = new Option("t", "testOption", true, "Test option description");
 option.setDescription("New test option description");
 assertEquals("New test option description", option.getDescription());
 }

 @Test
 public void testIsRequired() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.isRequired());
 Option requiredOption = new Option("t", "testOption", false, "Test option description");
 assertFalse(requiredOption.isRequired());
 Option requiredOptionWithArg = new Option("t", "testOption", false, "Test option description", true);
 assertFalse(requiredOptionWithArg.isRequired());
 Option requiredOptionWithArgAndDescription = new Option("t", "testOption", false, "Test option description", true, "Test option is required");
 assertTrue(requiredOptionWithArgAndDescription.isRequired());
 }

 @Test
 public void testSetRequired() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.isRequired());
 option.setRequired(true);
 assertTrue(option.isRequired());
 }

 @Test
 public void testSetArgName() {
 Option option = new Option("t", "testOption", true, "Test option description");
 option.setArgName("argName");
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testHasArgName() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.hasArgName());
 Option optionWithArgName = new Option("t", "testOption", true, "Test option description", "argName");
 assertTrue(optionWithArgName.hasArgName());
 }

 @Test
 public void testGetArgName() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertNull(option.getArgName());
 Option optionWithArgName = new Option("t", "testOption", true, "Test option description", "argName");
 assertEquals("argName", optionWithArgName.getArgName());
 }

 @Test
 public void testHasArgs() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.hasArgs());
 Option optionWithArgs = new Option("t", "testOption", true, "Test option description", "arg1", "arg2");
 assertTrue(optionWithArgs.hasArgs());
 }

 @Test
 public void testSetArgs() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.hasArgs());
 option.setArgs(2);
 assertTrue(option.hasArgs());
 assertEquals(2, option.getArgs());
 }

 @Test
 public void testSetValueSeparator() {
 Option option = new Option("t", "testOption", true, "Test option description");
 option.setValueSeparator(';');
 assertEquals(';', option.getValueSeparator());
 }

 @Test
 public void testGetValueSeparator() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertFalse(option.hasValueSeparator());
 Option optionWithValueSeparator = new Option("t", "testOption", true, "Test option description", null, null, ';');
 assertTrue(optionWithValueSeparator.hasValueSeparator());
 assertEquals(';', optionWithValueSeparator.getValueSeparator());
 }

 @Test
 public void testAddValueForProcessing() {
 Option option = new Option("t", "testOption", true, "Test option description");
 List values = new ArrayList();
 option.setValues(values);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddValue() {
 Option option = new Option("t", "testOption", true, "Test option description");
 List values = new ArrayList();
 option.setValues(values);
 try {
 option.addValue("value1");
 fail();
 }
 catch (UnsupportedOperationException e) {
 // expected
 }
 }

 @Test
 public void testGetValue() {
 Option option = new Option("t", "testOption", true, "Test option description");
 List values = new ArrayList();
 option.setValues(values);
 option.addValueForProcessing("value1");
 assertEquals("value1", option.getValue());
 option.addValueForProcessing("value2");
 assertNull(option.getValue());
 }

 @Test
 public void testGetValueWithIndex() {
 Option option = new Option("t", "testOption", true, "Test option description");
 List values = new ArrayList();
 option.setValues(values);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));
 }

 @Test
 public void testGetValues() {
 Option option = new Option("t", "testOption", true, "Test option description");
 List values = new ArrayList();
 option.setValues(values);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 String[] expectedValues = {"value1", "value2"};
 assertArrayEquals(expectedValues, option.getValues());
 }

 @Test
 public void testGetValuesList() {
 Option option = new Option("t", "testOption", true, "Test option description");
 List values = new ArrayList();
 option.setValues(values);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals(values, option.getValuesList());
 }

 @Test
 public void testToString() {
 Option option = new Option("t", "testOption", true, "Test option description");
 assertEquals("[ option: t :: Test option description ]", option.toString());
 Option optionWithLong = new Option("--testOption", "Test long option", true, "Test long option description");
 assertEquals("[ option: --testOption :: Test long option description ]", optionWithLong.toString());
 Option optionWithArg = new Option("t", "testOption", true, "Test option description", "arg");
 assertEquals("[ option: t [ARG] :: Test option description ]", optionWithArg.toString());
 Option optionWithLongAndArg = new Option("--testOption", "Test long option description", true, "Test long option description", "arg");
 assertEquals("[ option: --testOption [ARG] :: Test long option description ]", optionWithLongAndArg.toString());
 }

 @Test
 public void testEquals() {
 Option option1 = new Option("t", "testOption1", true, "Test option description 1");
 Option option2 = new Option("t", "testOption1", true, "Test option description 1");
 assertTrue(option1.equals(option2));
 Option option3 = new Option("t", "testOption2", true, "Test option description 1");
 assertFalse(option1.equals(option3));
 Option option4 = new Option("--testOption1", "Test long option 1", true, "Test option description 1");
 assertFalse(option1.equals(option4));
 }

 @Test
 public void testHashCode() {
 Option option1 = new Option("t", "testOption1", true, "Test option description 1");
 Option option2 = new Option("t", "testOption1", true, "Test option description 1");
 assertEquals(option1.hashCode(), option2.hashCode());
 Option option3 = new Option("t", "testOption2", true, "Test option description 1");
 assertNotEquals(option1.hashCode(), option3.hashCode());
 Option option4 = new Option("--testOption1", "Test long option 1", true, "Test option description 1");
 assertNotEquals(option1.hashCode(), option4.hashCode());
 }

 @Test
 public void testClone() {
 Option option = new Option("t", "testOption", true, "Test option description");
 try {
 Option clonedOption = (Option)option.clone();
 assertEquals(option, clonedOption);
 }
 catch (CloneNotSupportedException e) {
 fail();
 }
 }

 @Test
 public void testClearValues() {
 Option option = new Option("t", "testOption", true, "Test option description");
 List values = new ArrayList();
 option.setValues(values);
 option.addValueForProcessing("value1");
 option.clearValues();
 assertTrue(values.isEmpty());
 }
}