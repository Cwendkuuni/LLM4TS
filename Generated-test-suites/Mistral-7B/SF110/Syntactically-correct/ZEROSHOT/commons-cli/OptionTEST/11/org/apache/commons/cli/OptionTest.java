package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionCreation() {
 Option option = new Option("-o", "option description");
 assertEquals("-o", option.getOpt());
 assertEquals("option description", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getType());
 assertEquals(-1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithLongOpt() {
 Option option = new Option("--long-opt", "long option description");
 assertEquals("--long-opt", option.getOpt());
 assertEquals("long option description", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertTrue(option.hasLongOpt());
 assertEquals("--long-opt", option.getLongOpt());
 assertNull(option.getType());
 assertEquals(-1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithArg() {
 Option option = new Option("-a", "option description", true);
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getType());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithOptionalArg() {
 Option option = new Option("-b", "optional argument option description", true);
 assertTrue(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getType());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithValueSeparator() {
 Option option = new Option("-c", "value separator option description", true, ',');
 assertTrue(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertTrue(option.hasValueSeparator());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getType());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithLongOptAndArg() {
 Option option = new Option("--long-opt-arg", "long option with argument description", true);
 assertTrue(option.hasArg());
 assertTrue(option.hasLongOpt());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertEquals("--long-opt-arg", option.getLongOpt());
 assertNull(option.getType());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithLongOptAndValueSeparator() {
 Option option = new Option("--long-opt-sep", "long option with value separator description", true, ':');
 assertTrue(option.hasArg());
 assertTrue(option.hasLongOpt());
 assertTrue(option.hasValueSeparator());
 assertNull(option.getType());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithType() {
 Option option = new Option("-d", "option with type description", true, String.class);
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasLongOpt());
 assertNotNull(option.getType());
 assertEquals(String.class, option.getType());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithValue() {
 Option option = new Option("-e", "option with value description", true, "test");
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasLongOpt());
 assertNotNull(option.getType());
 assertEquals("test", option.getValue());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());
 }

 @Test
 public void testOptionCreationWithValues() {
 Option option = new Option("-f", "option with values description", true, ',');
 assertTrue(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertTrue(option.hasValueSeparator());
 assertFalse(option.hasLongOpt());
 assertNull(option.getType());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());

 List values = new ArrayList();
 values.add("value1");
 values.add("value2");
 option.values = values;

 assertEquals("value1,value2", option.getValue());
 assertEquals(1, option.getArgs());
 assertEquals(2, option.values.size());
 }

 @Test
 public void testOptionCreationWithValuesLongOpt() {
 Option option = new Option("--long-opt-values", "long option with values description", true, ',');
 assertTrue(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertTrue(option.hasValueSeparator());
 assertTrue(option.hasLongOpt());
 assertEquals("--long-opt-values", option.getLongOpt());
 assertNull(option.getType());
 assertEquals(1, option.getArgs());
 assertEquals(0, option.values.size());

 List values = new ArrayList();
 values.add("value1");
 values.add("value2");
 option.values = values;

 assertEquals("value1,value2", option.getValue());
 assertEquals(1, option.getArgs());
 assertEquals(2, option.values.size());
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("-a", "option description");
 Option option2 = new Option("-a", "option description");
 assertTrue(option1.equals(option2));
 assertEquals(option1.hashCode(), option2.hashCode());

 Option option3 = new Option("-b", "option description");
 assertFalse(option1.equals(option3));
 assertFalse(option3.equals(option1));
 assertNotSame(option1, option3);

 Option option4 = new Option("-a", "other option description");
 assertFalse(option1.equals(option4));
 assertFalse(option4.equals(option1));
 assertNotSame(option1, option4);
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("-o", "option description");
 Option clonedOption = (Option)option.clone();
 assertEquals(option.getOpt(), clonedOption.getOpt());
 assertEquals(option.getDescription(), clonedOption.getDescription());
 assertTrue(option.values.isEmpty());
 assertTrue(clonedOption.values.isEmpty());
 assertSame(option, clonedOption);
 }

 @Test
 public void testOptionClearValues() {
 Option option = new Option("-o", "option description");
 List values = new ArrayList();
 option.values = values;
 option.clearValues();
 assertTrue(option.values.isEmpty());
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testOptionAddValue() {
 Option option = new Option("-o", "option description");
 option.addValue("value");
 }
}