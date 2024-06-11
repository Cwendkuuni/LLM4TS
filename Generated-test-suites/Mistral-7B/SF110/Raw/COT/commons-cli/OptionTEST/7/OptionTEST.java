package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstructorWithNullOpt() {
 Option option = new Option(null, "test", false, "Test Option");
 assertNotNull(option);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructorWithInvalidOpt() {
 Option option = new Option("", "test", false, "Test Option");
 }

 @Test
 public void testOptionConstructorWithOptionalArg() {
 Option option = new Option("t", "test", true, "Test Option with optional argument");
 assertEquals("t", option.getOpt());
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testOptionConstructorWithLongOpt() {
 Option option = new Option("t", "test-long-opt", true, "Test Option with long opt");
 assertEquals("test-long-opt", option.getLongOpt());
 assertNull(option.getOpt());
 }

 @Test
 public void testOptionConstructorWithHasArg() {
 Option option = new Option("t", "test", false, "Test Option with arg", "arg");
 assertEquals("arg", option.getArgName());
 }

 @Test
 public void testOptionConstructorWithHasAndNoArgs() {
 Option option = new Option("t", "test", false, "Test Option with arg");
 option.setArgs(2);
 assertFalse(option.hasOptionalArg());
 assertEquals(2, option.getArgs());
 }

 @Test
 public void testOptionConstructorWithDescription() {
 Option option = new Option("t", "test", false, "Test Option with description");
 assertEquals("Test Option with description", option.getDescription());
 }

 @Test
 public void testOptionConstructorWithType() {
 Option option = new Option("t", "test", false, "Test Option with type", Integer.class);
 assertEquals(Integer.class, option.getType());
 }

 @Test
 public void testGetId() {
 Option option = new Option("t", "test", false, "Test Option");
 assertEquals('t', option.getId());
 }

 @Test
 public void testGetKey() {
 Option option = new Option("t", "test", false, "Test Option");
 assertEquals("test", option.getKey());
 }

 @Test
 public void testGetOpt() {
 Option option = new Option("t", "test", false, "Test Option");
 assertEquals("t", option.getOpt());
 }

 @Test
 public void testGetLongOpt() {
 Option option = new Option("t", null, false, "Test Option", "test-long-opt");
 assertEquals("test-long-opt", option.getLongOpt());
 }

 @Test
 public void testHasOptionalArg() {
 Option option = new Option("t", "test", true, "Test Option with optional argument");
 assertTrue(option.hasOptionalArg());
 }

 @Test
 public void testHasLongOpt() {
 Option option = new Option("t", "test", false, "Test Option", "test-long-opt");
 assertTrue(option.hasLongOpt());
 }

 @Test
 public void testHasArg() {
 Option option = new Option("t", "test", false, "Test Option");
 assertFalse(option.hasArg());
 }

 @Test
 public void testHasArgs() {
 Option option = new Option("t", "test", false, "Test Option with args");
 option.setArgs(3);
 assertTrue(option.hasArgs());
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueForProcessingWithNoArgs() {
 Option option = new Option("t", "test", false, "Test Option");
 option.addValueForProcessing("value");
 }

 @Test
 public void testProcessValue() {
 Option option = new Option("t", "test", false, "Test Option", null, 2, ',');
 option.processValue("value1,value2");
 assertEquals(2, option.values.size());
 assertEquals("value1", option.values.get(0));
 assertEquals("value2", option.values.get(1));
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueForProcessingWithTooManyArgs() {
 Option option = new Option("t", "test", false, "Test Option", null, 2, ',');
 option.processValue("value1,value2,value3");
 }

 @Test
 public void testAddValue() {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value");
 assertEquals(1, option.values.size());
 assertEquals("value", option.values.get(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testGetValueWithIndexOutOfBounds() {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value1");
 option.addValue("value2");
 option.addValue("value3");
 option.getValue(2);
 }

 @Test
 public void testGetValue() {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value1");
 assertEquals("value1", option.getValue());
 }

 @Test
 public void testGetValueWithDefaultValue() {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value1");
 assertEquals("value1", option.getValue(null));
 }

 @Test
 public void testGetValues() {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value1");
 option.addValue("value2");
 String[] values = option.getValues();
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 }

 @Test
 public void testGetValuesList() {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value1");
 option.addValue("value2");
 List<String> valuesList = option.getValuesList();
 assertEquals(2, valuesList.size());
 assertEquals("value1", valuesList.get(0));
 assertEquals("value2", valuesList.get(1));
 }

 @Test
 public void testToString() {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value1");
 option.addValue("value2");
 assertEquals("[ option: t :: Test Option :: [value1, value2] ]", option.toString());
 }

 @Test
 public void testEquals() {
 Option option1 = new Option("t", "test", false, "Test Option", null, 1);
 option1.addValue("value1");
 option1.addValue("value2");
 Option option2 = new Option("t", "test", false, "Test Option", null, 1);
 option2.addValue("value1");
 option2.addValue("value2");
 Option option3 = new Option("t", "test", false, "Test Option", null, 1);
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testHashCode() {
 Option option1 = new Option("t", "test", false, "Test Option", null, 1);
 option1.addValue("value1");
 option1.addValue("value2");
 Option option2 = new Option("t", "test", false, "Test Option", null, 1);
 option2.addValue("value1");
 option2.addValue("value2");
 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 assertEquals(hashCode1, hashCode2);
 }

 @Test
 public void testClone() throws CloneNotSupportedException {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value1");
 option.addValue("value2");
 Option clonedOption = (Option) option.clone();
 assertEquals(option.getId(), clonedOption.getId());
 assertEquals(option.getOpt(), clonedOption.getOpt());
 assertEquals(option.getLongOpt(), clonedOption.getLongOpt());
 assertEquals(option.getDescription(), clonedOption.getDescription());
 assertEquals(option.isRequired(), clonedOption.isRequired());
 assertEquals(option.hasOptionalArg(), clonedOption.hasOptionalArg());
 assertEquals(option.hasArg(), clonedOption.hasArg());
 assertEquals(option.hasArgs(), clonedOption.hasArgs());
 assertEquals(option.getType(), clonedOption.getType());
 assertEquals(option.values, clonedOption.values);
 }

 @Test
 public void testClearValues() {
 Option option = new Option("t", "test", false, "Test Option", null, 1);
 option.addValue("value1");
 option.clearValues();
 assertFalse(option.hasValues());
 }
}