package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstruction() {
 Option option = new Option("-o", "Option with no arguments.");
 assertEquals("-o", option.getOpt());
 assertEquals(null, option.getLongOpt());
 assertEquals("Option with no arguments.", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 assertNull(option.getType());
 assertFalse(option.hasValueSeparator());
 assertNull(option.getValue());
 assertFalse(option.hasLongOpt());
 assertEquals(0, option.getArgs());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 assertEquals("arg", option.getArgName());

 Option optionWithArg = new Option("-a", "Option with one argument.", true);
 assertEquals("-a", optionWithArg.getOpt());
 assertEquals("--arg", optionWithArg.getLongOpt());
 assertEquals("Option with one argument.", optionWithArg.getDescription());
 assertTrue(optionWithArg.hasArg());
 assertTrue(optionWithArg.hasOptionalArg());
 assertFalse(optionWithArg.isRequired());
 assertNull(optionWithArg.getType());
 assertFalse(optionWithArg.hasValueSeparator());
 assertNull(optionWithArg.getValue());
 assertFalse(optionWithArg.hasLongOpt());
 assertEquals(1, optionWithArg.getArgs());
 assertFalse(optionWithArg.hasArgs());
 assertFalse(optionWithArg.hasArgName());
 assertEquals("arg", optionWithArg.getArgName());

 Option optionWithLongOpt = new Option("--log-level", "Option with long opt.", false);
 assertEquals("-log-level", optionWithLongOpt.getOpt());
 assertEquals("--log-level", optionWithLongOpt.getLongOpt());
 assertEquals("Option with long opt.", optionWithLongOpt.getDescription());
 assertTrue(optionWithLongOpt.hasArg());
 assertFalse(optionWithLongOpt.hasOptionalArg());
 assertFalse(optionWithLongOpt.isRequired());
 assertNull(optionWithLongOpt.getType());
 assertFalse(optionWithLongOpt.hasValueSeparator());
 assertNull(optionWithLongOpt.getValue());
 assertTrue(optionWithLongOpt.hasLongOpt());
 assertEquals(0, optionWithLongOpt.getArgs());
 assertFalse(optionWithLongOpt.hasArgs());
 assertFalse(optionWithLongOpt.hasArgName());
 assertEquals("arg", optionWithLongOpt.getArgName());
 }

 @Test
 public void testOptionProperties() {
 Option option = new Option("-o", "Option with no arguments.");
 option.setLongOpt("--option-long");
 assertEquals("-o", option.getOpt());
 assertEquals("--option-long", option.getLongOpt());
 option.setDescription("Updated option description.");
 assertEquals("Option with no arguments.", option.getDescription());
 assertEquals("Updated option description.", option.getDescription());
 option.setRequired(true);
 assertFalse(option.isRequired());
 assertTrue(option.isRequired());
 option.setOptionalArg(true);
 assertFalse(option.hasOptionalArg());
 assertTrue(option.hasOptionalArg());
 option.setArgName("argName");
 assertFalse(option.hasArgName());
 assertEquals("argName", option.getArgName());
 option.setArgs(2);
 assertEquals(0, option.getArgs());
 assertEquals(2, option.getArgs());
 option.clearValues();
 assertNull(option.getValue());
 option.addValue("value1");
 assertEquals("value1", option.getValue());
 option.addValue("value2");
 String[] values = option.getValues();
 assertEquals(2, values.length);
 assertEquals("value1", values[0]);
 assertEquals("value2", values[1]);
 List valuesList = option.getValuesList();
 assertEquals(2, valuesList.size());
 assertEquals("value1", valuesList.get(0));
 assertEquals("value2", valuesList.get(1));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionValidation() {
 Option option = new Option("", "Option with empty opt.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionValidationLongOptEmpty() {
 Option option = new Option("--", "Option with empty long opt.");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionValidationArgNameEmpty() {
 Option option = new Option("--arg", "", "Option with empty arg name.");
 }
}