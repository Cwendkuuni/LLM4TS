package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionCreationUnspecified() {
 Option option = new Option("-o", "Option test");
 assertEquals("-o", option.getOpt());
 assertEquals("Option test", option.getDescription());
 assertEquals(Option.UNINITIALIZED, option.getArgs());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationOptional() {
 Option option = new Option("-o", "Option test", true, "Optional argument test");
 assertEquals("-o", option.getOpt());
 assertEquals("Optional argument test", option.getDescription());
 assertEquals(1, option.getArgs());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationRequired() {
 Option option = new Option("-r", "Required argument test", false, "Required argument test");
 assertEquals("-r", option.getOpt());
 assertEquals("Required argument test", option.getDescription());
 assertEquals(0, option.getArgs());
 assertFalse(option.hasOptionalArg());
 assertTrue(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationLongOpt() {
 Option option = new Option("--long-opt", "Long option test", false, "Long option test");
 assertEquals("--long-opt", option.getOpt());
 assertEquals("Long option test", option.getDescription());
 assertEquals(0, option.getArgs());
 assertFalse(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationLongOptWithArg() {
 Option option = new Option("--long-opt", "Long option test with arg", true, "Long option test with arg");
 assertEquals("--long-opt", option.getOpt());
 assertEquals("Long option test with arg", option.getDescription());
 assertEquals(1, option.getArgs());
 assertTrue(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationLongOptWithOptionalArg() {
 Option option = new Option("--long-opt", "Long option test with optional arg", true, "Long option test with optional arg");
 assertEquals("--long-opt", option.getOpt());
 assertEquals("Long option test with optional arg", option.getDescription());
 assertEquals(1, option.getArgs());
 assertTrue(option.hasOptionalArg());
 assertTrue(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithValueSeparator() {
 Option option = new Option("--sep-opt", "Option test with value separator", false, "Option test with value separator");
 option.setValueSeparator(':');
 assertEquals("--sep-opt", option.getOpt());
 assertEquals("Option test with value separator", option.getDescription());
 assertEquals(0, option.getArgs());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertTrue(option.hasValueSeparator());
 assertEquals(':', option.getValueSeparator());
 }

 @Test
 public void testAddValue() {
 Option option = new Option("--sep-opt", "Option test with value separator", false, "Option test with value separator");
 option.setValueSeparator(':');
 option.addValue("value1");
 option.addValue("value2");
 List<String>values = option.getValues();
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test(expected = UnsupportedOperationException.class)
 public void testAddValueUnsupported() {
 Option option = new Option("-o", "Option test", false, "Option test");
 option.addValue("value1");
 }
}