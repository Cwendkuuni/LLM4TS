package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstructionWithNoArg() {
 Option option = new Option("-o", "Option without argument");
 assertEquals("-o", option.getOpt());
 assertEquals("Option without argument", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionConstructionWithOneArg() {
 Option option = new Option("-o", true, "Option with argument");
 assertEquals("-o", option.getOpt());
 assertEquals("Option with argument", option.getDescription());
 assertNull(option.getLongOpt());
 assertEquals("arg", option.getArgName());
 assertTrue(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionConstructionWithLongOpt() {
 Option option = new Option("--long-o", "Option with long option");
 assertEquals("--long-o", option.getOpt());
 assertEquals("Option with long option", option.getDescription());
 assertEquals("--long-o", option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionConstructionWithArgAndLongOpt() {
 Option option = new Option("--long-o", true, "Option with long option and argument");
 assertEquals("--long-o", option.getOpt());
 assertEquals("Option with long option and argument", option.getDescription());
 assertEquals("--long-o", option.getLongOpt());
 assertEquals("arg", option.getArgName());
 assertTrue(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasNoValues());
 }

 @Test
 public void testOptionGetters() {
 Option option = new Option("-o", "Option with value and list", true, "Value or list");
 assertEquals("-o", option.getOpt());
 assertEquals("Option with value and list", option.getDescription());
 assertNull(option.getLongOpt());
 assertEquals("arg", option.getArgName());
 assertTrue(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertNull(option.getValue());
 assertNull(option.getValues());
 assertFalse(option.hasNoValues());

 option.setLongOpt("--long-o");
 assertEquals("--long-o", option.getLongOpt());

 option.setArgName("argname");
 assertEquals("argname", option.getArgName());

 option.setOptionalArg(true);
 assertTrue(option.hasOptionalArg());

 option.setRequired(true);
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionSetters() {
 Option option = new Option("-o", "Option with value and list", true, "Value or list");
 option.setType(String.class);
 assertEquals(String.class, option.getType());

 List<String> values = new ArrayList<>();
 values.add("value1");
 values.add("value2");
 option.setValues(values);
 assertEquals(values, option.getValuesList());
 assertEquals("value1", option.getValue(0));
 assertEquals("value2", option.getValue(1));
 assertEquals(2, option.getValues().size());

 option.setValueSeparator(';');
 assertEquals(';', option.getValueSeparator());

 option.setArgs(3);
 assertEquals(3, option.getArgs());
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("-o", "Option with value and list", true, "Value or list");
 Option option2 = new Option("-o", "Option with value and list", true, "Value or list");
 assertTrue(option1.equals(option2));

 Option option3 = new Option("-p", "Option with value and list", true, "Value or list");
 assertFalse(option1.equals(option3));

 Option option4 = new Option("-o", "Option with value and list", false, "Value or list");
 assertFalse(option1.equals(option4));

 Option option5 = new Option("-o", "Option with value and list", true, "Value or list", ';');
 option1.setValueSeparator(';');
 assertTrue(option1.equals(option5));
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("-o", "Option with value and list", true, "Value or list");
 Option option2 = new Option("-o", "Option with value and list", true, "Value or list");
 assertEquals(option1.hashCode(), option2.hashCode());

 Option option3 = new Option("-p", "Option with value and list", true, "Value or list");
 assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("-o", "", true, "Option with value and list", String[].class);
 String expected = "[ option: -o :: Option with value and list :: [ARG...] ]";
 assertEquals(expected, option.toString());
 }
}