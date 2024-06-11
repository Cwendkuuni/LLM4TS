package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionConstruction() {
 Option option = new Option("opt", "Option description");
 assertEquals("opt", option.getOpt());
 assertEquals("Option description", option.getDescription());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructionWithInvalidOptionName() {
 Option option = new Option("", "Option description"); // empty opt
 fail("Should throw IllegalArgumentException");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionConstructionWithNullDescription() {
 Option option = new Option("opt", null);
 fail("Should throw IllegalArgumentException");
 }

 @Test
 public void testOptionConstructionWithHasArg() {
 Option option = new Option("opt", false, "Option description");
 assertEquals("opt", option.getOpt());
 assertEquals("Option description", option.getDescription());
 assertEquals(-1, option.getArgs());
 }

 @Test
 public void testOptionConstructionWithNoArg() {
 Option option = new Option("opt", true, "Option description");
 assertEquals("opt", option.getOpt());
 assertEquals("Option description", option.getDescription());
 assertEquals(0, option.getArgs());
 }

 @Test
 public void testOptionConstructionWithLongOpt() {
 Option option = new Option("opt", "long-opt", false, "Option description");
 assertEquals("opt", option.getOpt());
 assertEquals("long-opt", option.getLongOpt());
 assertEquals("Option description", option.getDescription());
 }

 @Test
 public void testOptionConstructionWithHasArgAndLongOpt() {
 Option option = new Option("opt", "long-opt", true, "Option description");
 assertEquals("opt", option.getOpt());
 assertEquals("long-opt", option.getLongOpt());
 assertEquals(1, option.getArgs());
 }

 @Test
 public void testOptionConstructionWithValues() {
 Option option = new Option("opt", false, "Option description", new String[]{"value1", "value2"});
 assertEquals("opt", option.getOpt());
 assertEquals("Option description", option.getDescription());
 assertEquals(2, option.getArgs());
 List<String> values = option.getValuesList();
 assertEquals(2, values.size());
 assertTrue(values.contains("value1"));
 assertTrue(values.contains("value2"));
 }

 @Test
 public void testOptionConstructionWithValueSeparator() {
 Option option = new Option("opt", false, "Option description", new String[]{"value1"}, ',');
 assertEquals("opt", option.getOpt());
 assertEquals("Option description", option.getDescription());
 assertEquals(1, option.getArgs());
 List<String> values = option.getValuesList();
 assertEquals(1, values.size());
 assertTrue(values.contains("value1"));
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("opt1", false, "Option 1 description", new String[]{"value1"});
 Option option2 = new Option("opt1", false, "Option 1 description", new String[]{"value1"});
 assertEquals(option1, option2);
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("opt1", false, "Option 1 description", new String[]{"value1"});
 Option option2 = new Option("opt2", false, "Option 2 description", new String[]{"value2"});
 assertNotSame(option1.hashCode(), option2.hashCode());
 }

 @Test
 public void testOptionClone() {
 Option option = new Option("opt", false, "Option description", new String[]{"value1"});
 Option clonedOption = option.clone();
 assertEquals(option, clonedOption);
 }

 @Test
 public void testOptionClearValues() {
 Option option = new Option("opt", false, "Option description", new String[]{"value1"});
 option.clearValues();
 assertEquals(0, option.getValuesList().size());
 }
}