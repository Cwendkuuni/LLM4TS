package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstructionWithNoArguments() {
 Option option = new Option("optionName", "description");
 assertEquals("optionName", option.getOpt());
 assertEquals("optionName", option.getKey());
 assertEquals("description", option.getDescription());
 assertEquals(false, option.hasArg());
 assertEquals(false, option.hasOptionalArg());
 assertEquals(false, option.isRequired());
 assertEquals(false, option.hasLongOpt());
 assertEquals(0, option.getArgs());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionConstructionWithOneArgument() {
 Option option = new Option("optionName", "description", true, "argumentName");
 assertEquals("optionName", option.getOpt());
 assertEquals("optionName", option.getKey());
 assertEquals("description", option.getDescription());
 assertEquals("argumentName", option.getArgName());
 assertEquals(true, option.hasArg());
 assertEquals(true, option.hasOptionalArg());
 assertEquals(false, option.isRequired());
 assertEquals(false, option.hasLongOpt());
 assertEquals(1, option.getArgs());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionConstructionWithLongOption() {
 Option option = new Option("optionName", "longOptionName", true, "argumentName");
 assertEquals("optionName", option.getOpt());
 assertEquals("longOptionName", option.getLongOpt());
 assertEquals("description", option.getDescription());
 assertEquals("argumentName", option.getArgName());
 assertEquals(true, option.hasArg());
 assertEquals(true, option.hasOptionalArg());
 assertEquals(false, option.isRequired());
 assertEquals(true, option.hasLongOpt());
 assertEquals(1, option.getArgs());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionConstructionWithNoArgLongOption() {
 Option option = new Option("optionName", "description");
 Option optionWithLongName = new Option("optionName", "longOptionName", false, null);
 assertEquals("optionName", option.getOpt());
 assertEquals("optionName", option.getKey());
 assertEquals("description", option.getDescription());
 assertEquals("optionName", optionWithLongName.getOpt());
 assertEquals("longOptionName", optionWithLongName.getLongOpt());
 assertEquals(false, option.hasArg());
 assertEquals(false, option.hasOptionalArg());
 assertEquals(false, option.isRequired());
 assertEquals(true, optionWithLongName.hasLongOpt());
 assertEquals(0, option.getArgs());
 assertEquals(0, optionWithLongName.getArgs());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertNull(optionWithLongName.getValue());
 assertNull(optionWithLongName.getValues());
 }

 @Test
 public void testOptionId() {
 Option option = new Option("optionName", "description");
 assertEquals('o', option.getId());
 }

 @Test
 public void testOptionGetType() {
 Option option = new Option("optionName", "description");
 option.setType(new Integer(1));
 assertEquals(Integer.class, option.getType().getClass());
 }

 @Test
 public void testOptionHasOptionalArg() {
 Option option = new Option("optionName", "description");
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 Option option2 = new Option("optionName", "description");
 assertFalse(option2.hasOptionalArg());
 }

 @Test
 public void testOptionHasLongOpt() {
 Option option = new Option("optionName", "description");
 assertFalse(option.hasLongOpt());
 Option optionWithLongName = new Option("optionName", "longOptionName", false, null);
 assertTrue(optionWithLongName.hasLongOpt());
 }

 @Test
 public void testOptionHasArg() {
 Option option = new Option("optionName", "description");
 assertFalse(option.hasArg());
 Option optionWithArg = new Option("optionName", "description", true, "argName");
 assertTrue(optionWithArg.hasArg());
 }

 @Test
 public void testOptionDescription() {
 Option option = new Option("optionName", "description");
 assertEquals("description", option.getDescription());
 }

 @Test
 public void testOptionSetDescription() {
 Option option = new Option("optionName", "description");
 option.setDescription("newDescription");
 assertEquals("newDescription", option.getDescription());
 }

 @Test
 public void testOptionIsRequired() {
 Option option = new Option("optionName", "description");
 assertFalse(option.isRequired());
 Option optionRequired = new Option("optionName", "description", true, null);
 assertTrue(optionRequired.isRequired());
 }

 @Test
 public void testOptionArgName() {
 Option option = new Option("optionName", "description");
 assertNull(option.getArgName());
 Option optionWithArgName = new Option("optionName", "description", true, "argName");
 assertEquals("argName", optionWithArgName.getArgName());
 }

 @Test
 public void testOptionSetArgName() {
 Option option = new Option("optionName", "description");
 option.setArgName("argName");
 assertEquals("argName", option.getArgName());
 }

 @Test
 public void testOptionHasArgs() {
 Option option = new Option("optionName", "description");
 assertFalse(option.hasArgs());
 Option optionWithArgs = new Option("optionName", "description", true, "arg1", "arg2");
 assertTrue(optionWithArgs.hasArgs());
 }

 @Test
 public void testOptionSetArgs() {
 Option option = new Option("optionName", "description");
 option.setArgs(2);
 assertEquals(2, option.getArgs());
 }

 @Test
 public void testOptionValueSeparator() {
 Option option = new Option("optionName", "description");
 option.setValueSeparator(';');
 assertEquals(';', option.getValueSeparator());
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueForProcessingWithNoArgsAllowed() {
 Option option = new Option("optionName", "description", false, null);
 option.addValueForProcessing("value");
 }

 @Test
 public void testAddValueForProcessing() {
 Option option = new Option("optionName", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 List<String> values = option.getValuesList();
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetValue() {
 Option option = new Option("optionName", "description", false, null);
 option.addValueForProcessing("value1");
 assertEquals("value1", option.getValue());
 }

 @Test
 public void testGetValueWithNullValue() {
 Option option = new Option("optionName", "description", false, null);
 assertEquals(null, option.getValue());
 }

 @Test
 public void testGetValues() {
 Option option = new Option("optionName", "description", false, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 String[] values = option.getValues();
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 }

 @Test
 public void testGetValuesWithNoValues() {
 Option option = new Option("optionName", "description");
 String[] values = option.getValues();
 assertEquals(0, values.length);
 }

 @Test
 public void testToString() {
 Option option = new Option("optionName", "description");
 assertEquals("[ option: optionName :: description ]", option.toString());
 }

 @Test
 public void testEquals() {
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 Option option3 = new Option("option1", "description1");
 Option option4 = new Option("option1", "description2");
 Option option5 = new Option("option1", "description1");
 assertTrue(option1.equals(option5));
 assertFalse(option1.equals(option2));
 assertFalse(option1.equals(option3));
 assertFalse(option1.equals(new Object()));
 }

 @Test
 public void testHashCode() {
 Option option1 = new Option("option1", "description1");
 Option option2 = new Option("option2", "description2");
 int hashCode1 = option1.hashCode();
 Option option3 = new Option("option1", "description1");
 assertEquals(hashCode1, option3.hashCode());
 assertNotSame(hashCode1, option2.hashCode());
 }

 @Test
 public void testClone() {
 Option option = new Option("optionName", "description");
 Option clonedOption = option.clone();
 assertEquals(option, clonedOption);
 }

 @Test
 public void testClearValues() {
 Option option = new Option("optionName", "description");
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 option.clearValues();
 List<String> values = option.getValuesList();
 assertEquals(0, values.size());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddValue() {
 Option option = new Option("optionName", "description");
 option.addValue("value");
 }
}