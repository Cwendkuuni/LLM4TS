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
import java.util.Arrays;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstructionWithNoArgs() throws IllegalArgumentException {
 Option option = new Option("opt", "description");
 assertEquals("opt", option.getOpt());
 assertEquals("description", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertNull(option.getLongOpt());
 assertNull(option.getArgName());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 }

 @Test
 public void testOptionConstructionWithOneArg() throws IllegalArgumentException {
 Option option = new Option("opt", "description", true, "arg");
 assertEquals("opt", option.getOpt());
 assertEquals("description", option.getDescription());
 assertEquals("arg", option.getArgName());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertNull(option.getLongOpt());
 assertEquals(Arrays.asList("arg"), option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 }

 @Test
 public void testOptionConstructionWithOneArgAndLongOpt() throws IllegalArgumentException {
 Option option = new Option("opt", "description", true, "arg", "long-opt");
 assertEquals("opt", option.getOpt());
 assertEquals("description", option.getDescription());
 assertEquals("arg", option.getArgName());
 assertTrue(option.hasArg());
 assertTrue(option.hasLongOpt());
 assertEquals("long-opt", option.getLongOpt());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertEquals(Arrays.asList("arg"), option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 }

 @Test
 public void testOptionConstructionWithMultipleArgs() throws IllegalArgumentException {
 Option option = new Option("opt", "description", -2, null);
 assertEquals("opt", option.getOpt());
 assertEquals("description", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertNotNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 }

 @Test
 public void testOptionGettersAndSetters() {
 Option option = new Option("opt", "description");
 option.setDescription("updated-description");
 assertEquals("updated-description", option.getDescription());
 option.setArgName("updated-arg-name");
 assertEquals("updated-arg-name", option.getArgName());
 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());
 option.setOptionalArg(false);
 assertFalse(option.hasOptionalArg());
 option.setRequired(true);
 assertTrue(option.isRequired());
 option.setRequired(false);
 assertFalse(option.isRequired());
 option.setLongOpt("updated-long-opt");
 assertEquals("updated-long-opt", option.getLongOpt());
 OptionType optionType = new OptionType("OptionType");
 option.setType(optionType);
 assertSame(optionType, option.getType());
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("opt1", "description1");
 Option option2 = new Option("opt1", "description1");
 Option option3 = new Option("opt2", "description2");
 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("opt1", "description1");
 Option option2 = new Option("opt1", "description1");
 Option option3 = new Option("opt2", "description2");
 assertEquals(option1.hashCode(), option1.hashCode());
 assertEquals(option1.hashCode(), option2.hashCode());
 assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testOptionClone() throws CloneNotSupportedException {
 Option option = new Option("opt", "description");
 Option clonedOption = option.clone();
 assertEquals(option.getOpt(), clonedOption.getOpt());
 assertEquals(option.getDescription(), clonedOption.getDescription());
 assertFalse(option.getValues().equals(clonedOption.getValues()));
 assertTrue(option.getValues().getClass().equals(clonedOption.getValues().getClass()));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionInvalidConstructionWithNoArgAndNoLongOpt() throws IllegalArgumentException {
 new Option("opt", "description");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionInvalidConstructionWithNoArgAndHasLongOpt() throws IllegalArgumentException {
 new Option("opt", "description", true, "arg", null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionInvalidConstructionWithLongOptAndNoArg() throws IllegalArgumentException {
 new Option("opt", "description", null, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionInvalidConstructionWithNegativeNumberOfArgs() throws IllegalArgumentException {
 new Option("opt", "description", -3, null);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testAddValueForProcessingWithNoArgs() {
 Option option = new Option("opt", "description");
 option.addValueForProcessing("value");
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueForProcessingWithMoreArgsThanAllowed() {
 Option option = new Option("opt", "description", 1, null);
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 }

 @Test
 public void testAddValue() throws RuntimeException {
 throw new UnsupportedOperationException("The testAddValue method is not intended for client use. The addValueForProcessing method should be used instead.");
 }

 @Test(expected = RuntimeException.class)
 public void testAddValueWithoutAddValueForProcessing() {
 Option option = new Option("opt", "description");
 option.addValue("value");
 }

 @Test
 public void testGetValue() {
 Option option = new Option("opt", "description");
 option.addValue("value1");
 option.addValue("value2");
 assertEquals("value1", option.getValue());
 }

 @Test
 public void testGetValueWithDefaultValue() {
 Option option = new Option("opt", "description");
 option.addValue("value1");
 assertEquals("value1", option.getValue("default-value"));
 }

 @Test
 public void testGetValues() {
 Option option = new Option("opt", "description");
 option.addValue("value1");
 option.addValue("value2");
 assertEquals(Arrays.asList("value1", "value2"), option.getValues());
 }

 @Test
 public void testGetValuesList() {
 Option option = new Option("opt", "description");
 option.addValue("value1");
 option.addValue("value2");
 assertEquals(option.getValuesList(), Arrays.asList("value1", "value2"));
 }

 @Test
 public void testClearValues() {
 Option option = new Option("opt", "description");
 option.addValue("value1");
 option.clearValues();
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertNull(option.getValuesList());
 }
}