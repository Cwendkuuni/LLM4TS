package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

public class OptionTest {

 @Test
 public void testOptionCreation() {
 Option option = new Option("-o", "Option option", true);
 assertEquals("-o", option.getOpt());
 assertEquals("Option option", option.getDescription());
 assertTrue(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithLongOpt() {
 Option option = new Option("--longOption", "Long option", false);
 assertEquals("--longOption", option.getOpt());
 assertEquals("Long option", option.getDescription());
 assertFalse(option.hasArg());
 assertTrue(option.isRequired());
 assertEquals("--longOption", option.getKey());
 assertNull(option.getOpt());
 assertEquals("--longOption", option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithArgName() {
 Option option = new Option("--option", "Option option", false, "newArgName");
 assertEquals("--option", option.getOpt());
 assertEquals("Option option", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 assertEquals("--option", option.getKey());
 assertNull(option.getOpt());
 assertEquals("--option", option.getLongOpt());
 assertNull(option.getValue());
 assertNotNull(option.getArgName());
 assertEquals("newArgName", option.getArgName());
 assertNull(option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 }

 @Test
 public void testOptionCreationWithValueSeparator() {
 Option option = new Option("--option", "Option option", false, null, ',');
 assertEquals("--option", option.getOpt());
 assertEquals("Option option", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 assertEquals("--option", option.getKey());
 assertNull(option.getOpt());
 assertEquals("--option", option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValuesList());
 assertTrue(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionCreationWithArg() {
 Option option = new Option("-o", "Option option", false, "newArgName");
 assertEquals("-o", option.getOpt());
 assertEquals("Option option", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertEquals("newArgName", option.getArgName());
 assertNull(option.getValue());
 assertNull(option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 }

 @Test
 public void testOptionCreationWithArgs() {
 Option option = new Option("-o", "Option option", 2, "newArgName1 newArgName2");
 assertEquals("-o", option.getOpt());
 assertEquals("Option option", option.getDescription());
 assertTrue(option.hasArgs());
 assertFalse(option.isRequired());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNotNull(option.getArgName());
 assertEquals("newArgName1 newArgName2", option.getArgName());
 assertNull(option.getValuesList());
 assertFalse(option.hasValueSeparator());
 }

 @Test
 public void testOptionCreationWithValues() {
 Option option = new Option("-o", "Option option", false, null, ' ', new ArrayList<String>() {{
 add("Value1");
 add("Value2");
 add("Value3");
 }});
 assertEquals("-o", option.getOpt());
 assertEquals("Option option", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 assertEquals("Value1 Value2 Value3", option.getValues().toString());
 }

 @Test
 public void testOptionEquals() {
 Option option1 = new Option("-o", "Option option", false, null);
 Option option2 = new Option("-o", "Option option", false, null);
 Option option3 = new Option("-o", "Option option2", false, null);
 assertTrue(option1.equals(option1));
 assertTrue(option1.equals(option2));
 assertFalse(option1.equals(option3));
 }

 @Test
 public void testOptionHashCode() {
 Option option1 = new Option("-o", "Option option", false, null);
 Option option2 = new Option("-o", "Option option", false, null);
 Option option3 = new Option("-o", "Option option2", false, null);
 assertEquals(option1.hashCode(), option2.hashCode());
 assertNotEquals(option1.hashCode(), option3.hashCode());
 }

 @Test
 public void testOptionClone() throws CloneNotSupportedException {
 Option option = new Option("-o", "Option option", false, null);
 Option clonedOption = (Option)option.clone();
 assertEquals(option.getOpt(), clonedOption.getOpt());
 assertEquals(option.getDescription(), clonedOption.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasLongOpt());
 assertNull(option.getLongOpt());
 assertNull(option.getValue());
 assertNull(option.getValuesList());
 assertFalse(option.hasValueSeparator());
 assertFalse(option.hasArgs());
 assertFalse(option.hasArgName());
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("-o", "Option option", false, null);
 assertEquals("[ option: -o :: Option option ]", option.toString());
 }
}