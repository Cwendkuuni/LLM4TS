package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstructionWithNoArguments() {
 Option option = new Option("opt", "Description");
 assertEquals("opt", option.getOpt());
 assertEquals("Description", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertNull(option.getType());
 }

 @Test
 public void testOptionConstructionWithOptionalSingleArgument() {
 Option option = new Option("opt", "Description", true, "arg");
 assertEquals("opt", option.getOpt());
 assertEquals("Description", option.getDescription());
 assertTrue(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertNull(option.getType());
 }

 @Test
 public void testOptionConstructionWithOptionalLongOption() {
 Option option = new Option("opt", "Description", true, "long-opt");
 assertEquals("opt", option.getOpt());
 assertEquals("Description", option.getDescription());
 assertFalse(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertFalse(option.isRequired());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertNull(option.getType());
 }

 @Test
 public void testOptionConstructionWithRequiredSingleArgument() {
 Option option = new Option("opt", "Description", false, "arg");
 assertEquals("opt", option.getOpt());
 assertEquals("Description", option.getDescription());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertTrue(option.isRequired());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertNull(option.getType());
 }

 @Test
 public void testOptionConstructionWithRequiredLongOption() {
 Option option = new Option("opt", "Description", false, "long-opt");
 assertEquals("opt", option.getOpt());
 assertEquals("Description", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertTrue(option.isRequired());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertNull(option.getType());
 }

 @Test
 public void testOptionConstructionWithValues() {
 Option option = new Option("opt", "Description", false, "arg", new String[]{"value1", "value2"});
 assertEquals("opt", option.getOpt());
 assertEquals("Description", option.getDescription());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertTrue(option.isRequired());
 assertEquals("value1", option.getValue());
 List<String> values = option.getValues();
 assertEquals(2, values.size());
 assertSame("value1", values.get(0));
 assertSame("value2", values.get(1));
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("opt1", "Description1");
 Option option2 = new Option("opt2", "Description2");
 Option option3 = new Option("opt1", "Description1");
 Option option4 = new Option("opt1", "Description1");
 Option option5 = new Option("opt1", "Description2");
 Option option6 = new Option("opt1", "Description1", null, null);
 Option option7 = new Option("opt1", "Description1", null, null);
 assertEquals(option1, option3);
 assertEquals(option1, option4);
 assertNotSame(option1, option2);
 assertNotSame(option1, option5);
 assertNotSame(option1, option6);
 assertNotSame(option1, option7);
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("opt1", "Description1");
 Option option2 = new Option("opt2", "Description2");
 int hashCode1 = option1.hashCode();
 int hashCode2 = option2.hashCode();
 assertEquals(hashCode1, option1.hashCode());
 assertNotEquals(hashCode1, hashCode2);
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("opt", "Description", false, "arg", new String[]{"value1", "value2"});
 String expected = "[ option: opt :: Description :: [arg] :: :: value1, value2 ]";
 assertEquals(expected, option.toString());
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("opt", "Description", false, "arg", new String[]{"value1", "value2"});
 Option clonedOption = option.clone();
 List<String> clonedValues = clonedOption.getValuesList();
 assertEquals(option.getValuesList(), clonedOption.getValuesList());
 assertNotSame(option.getValuesList(), clonedValues);
 }
}