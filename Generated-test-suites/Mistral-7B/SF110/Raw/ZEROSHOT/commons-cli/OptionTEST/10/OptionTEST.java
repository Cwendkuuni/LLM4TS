package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstruction() {
 Option option = new Option("-o", "Option with argument", true);
 assertEquals("-o", option.getOpt());
 assertEquals("Option with argument", option.getDescription());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertEquals(1, option.getArgs());

 Option longOption = new Option("--long-option", "Long option with argument", true);
 assertEquals("--long-option", longOption.getOpt());
 assertEquals("Long option with argument", longOption.getDescription());
 assertTrue(longOption.hasArg());
 assertTrue(longOption.hasLongOpt());
 assertNull(longOption.getOpt());
 assertFalse(longOption.isRequired());
 assertEquals(1, longOption.getArgs());
 }

 @Test
 public void testOptionConstructionWithLongOpt() {
 Option option = new Option("-o", "--long-option", "Option with argument", true);
 assertEquals("-o", option.getOpt());
 assertEquals("--long-option", option.getLongOpt());
 assertEquals("Option with argument", option.getDescription());
 assertTrue(option.hasArg());
 assertTrue(option.hasLongOpt());
 assertFalse(option.isRequired());
 assertEquals(1, option.getArgs());
 }

 @Test
 public void testOptionConstructionWithoutArg() {
 Option option = new Option("-o", "Option without argument", false);
 assertEquals("-o", option.getOpt());
 assertEquals("Option without argument", option.getDescription());
 assertFalse(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertNull(option.getLongOpt());
 assertFalse(option.isRequired());
 assertEquals(0, option.getArgs());
 }

 @Test
 public void testOptionConstructionWithLongOptAndArg() {
 Option option = new Option("-o", "--long-option", "Option with argument", false, "argName");
 assertEquals("-o", option.getOpt());
 assertEquals("--long-option", option.getLongOpt());
 assertEquals("Option with argument", option.getDescription());
 assertTrue(option.hasArg());
 assertTrue(option.hasLongOpt());
 assertFalse(option.isRequired());
 assertEquals("argName", option.getArgName());
 assertEquals(1, option.getArgs());
 }

 @Test
 public void testOptionGetters() {
 Option option = new Option("-o", "Option with argument", true);
 assertEquals("-o", option.getKey());
 assertEquals(Character.forDigit(0, 256), option.getId());
 assertNull(option.getType());
 assertNull(option.getValuesList());

 option.setType(String.class);
 assertEquals(String.class, option.getType());

 List values = Arrays.asList("value1", "value2");
 option.setValues(values);
 assertEquals(values, option.getValuesList());
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));

 option.clearValues();
 assertNull(option.getValue());
 assertNull(option.getValue(0));
 assertNull(option.getValuesList());
 }

 @Test
 public void testOptionSetters() {
 Option option = new Option("-o", "Option with argument", true);
 option.setDescription("New option description");
 assertEquals("New option description", option.getDescription());

 option.setArgName("newArgName");
 assertEquals("newArgName", option.getArgName());

 option.setOptionalArg(false);
 assertFalse(option.hasOptionalArg());

 option.setRequired(true);
 assertTrue(option.isRequired());

 option.setNumberOfArgs(2);
 assertEquals(2, option.getArgs());
 }
}