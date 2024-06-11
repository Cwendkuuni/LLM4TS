package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionCreation() {
 Option option = new Option("-o", "Output option with no arguments", false);
 assertEquals("-o", option.getOpt());
 assertEquals(false, option.hasArg());
 assertEquals("Output option with no arguments", option.getDescription());
 assertEquals(null, option.getLongOpt());
 assertEquals(null, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals(null, option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(false, option.hasOptionalArg());
 assertEquals(false, option.hasLongOpt());
 assertEquals(false, option.hasArgName());
 assertEquals(false, option.hasArgs());
 assertEquals(0, option.getArgs());
 assertEquals(false, option.hasValueSeparator());
 assertNull(option.getValue());
 assertFalse(option.hasNoValues());
 assertEquals(true, option.equals(option));
 assertFalse(option.equals(new Object()));
 }

 @Test
 public void testOptionCreationWithLongOpt() {
 Option option = new Option("--output", "Output option with no arguments", false);
 assertEquals("--output", option.getLongOpt());
 assertEquals(false, option.hasArg());
 assertEquals("Output option with no arguments", option.getDescription());
 assertEquals("-o", option.getOpt());
 assertEquals(null, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals(null, option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(true, option.hasOptionalArg());
 assertEquals(true, option.hasLongOpt());
 assertEquals(false, option.hasArgName());
 assertEquals(false, option.hasArgs());
 assertEquals(0, option.getArgs());
 assertEquals(false, option.hasValueSeparator());
 assertNull(option.getValue());
 assertFalse(option.hasNoValues());
 assertEquals(true, option.equals(option));
 assertFalse(option.equals(new Object()));
 }

 @Test
 public void testOptionCreationWithArg() {
 Option option = new Option("-i", "Input option with an argument", true);
 assertEquals("-i", option.getOpt());
 assertEquals(true, option.hasArg());
 assertEquals("Input option with an argument", option.getDescription());
 assertEquals(null, option.getLongOpt());
 assertEquals(null, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals("arg", option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(false, option.hasOptionalArg());
 assertEquals(false, option.hasLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals(1, option.getArgs());
 assertEquals(false, option.hasValueSeparator());
 assertNull(option.getValue());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionCreationWithLongOptAndArg() {
 Option option = new Option("--input", "Input option with an argument", true);
 assertEquals("--input", option.getLongOpt());
 assertEquals(true, option.hasArg());
 assertEquals("Input option with an argument", option.getDescription());
 assertEquals("-i", option.getOpt());
 assertEquals(null, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals("arg", option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(true, option.hasOptionalArg());
 assertEquals(true, option.hasLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals(1, option.getArgs());
 assertEquals(false, option.hasValueSeparator());
 assertNull(option.getValue());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionCreationWithArgAndType() {
 Option option = new Option("-i", "Input option with an argument of type Integer", true, Integer.class);
 assertEquals("-i", option.getOpt());
 assertEquals(true, option.hasArg());
 assertEquals("Input option with an argument of type Integer", option.getDescription());
 assertEquals(null, option.getLongOpt());
 assertEquals(Integer.class, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals("arg", option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(false, option.hasOptionalArg());
 assertEquals(false, option.hasLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals(1, option.getArgs());
 assertEquals(false, option.hasValueSeparator());
 assertNull(option.getValue());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionCreationWithOptionalArg() {
 Option option = new Option("-a", "Optional argument option", true, null);
 assertEquals("-a", option.getOpt());
 assertEquals(true, option.hasArg());
 assertEquals("Optional argument option", option.getDescription());
 assertEquals(null, option.getLongOpt());
 assertEquals(null, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals("arg", option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(true, option.hasOptionalArg());
 assertEquals(false, option.hasLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals(1, option.getArgs());
 assertEquals(false, option.hasValueSeparator());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithOptionalArgAndLongOpt() {
 Option option = new Option("--arg", "Optional argument option", true, null);
 assertEquals("--arg", option.getLongOpt());
 assertEquals(true, option.hasArg());
 assertEquals("Optional argument option", option.getDescription());
 assertEquals("-a", option.getOpt());
 assertEquals(null, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals("arg", option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(true, option.hasOptionalArg());
 assertEquals(true, option.hasLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals(1, option.getArgs());
 assertEquals(false, option.hasValueSeparator());
 assertTrue(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithValueSeparator() {
 Option option = new Option("-s", "Option with a value separator", true, null, ',');
 assertEquals("-s", option.getOpt());
 assertEquals(true, option.hasArg());
 assertEquals("Option with a value separator", option.getDescription());
 assertEquals(null, option.getLongOpt());
 assertEquals(null, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals("arg", option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(false, option.hasOptionalArg());
 assertEquals(false, option.hasLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals(1, option.getArgs());
 assertEquals(',', option.getValueSeparator());
 assertEquals(false, option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithValues() {
 Option option = new Option("-l", "Option with values", true, null, ',');
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 assertEquals("-l", option.getOpt());
 assertEquals(true, option.hasArg());
 assertEquals("Option with values", option.getDescription());
 assertEquals(null, option.getLongOpt());
 assertEquals(null, option.getType());
 assertEquals(false, option.isRequired());
 assertEquals("arg", option.getArgName());
 assertEquals(Character.MIN_VALUE, option.getId());
 assertEquals(false, option.hasOptionalArg());
 assertEquals(false, option.hasLongOpt());
 assertEquals(true, option.hasArgName());
 assertEquals(1, option.getArgs());
 assertEquals(',', option.getValueSeparator());
 assertEquals(true, option.hasValueSeparator());
 assertEquals(2, option.getValues().size());
 assertEquals("value1", option.getValues().get(0));
 assertEquals("value2", option.getValues().get(1));
 }

 @Test(expected = RuntimeException.class)
 public void testOptionCreationWithInvalidArgs() {
 Option option = new Option("-x", "Invalid option", 5);
 }
}