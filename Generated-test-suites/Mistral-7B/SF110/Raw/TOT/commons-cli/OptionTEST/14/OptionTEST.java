package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionWithNoArgs() {
 Option option = new Option("a", "option-a", false, "This is option A");
 assertEquals("option-a", option.getOpt());
 assertEquals("Option A", option.getDescription());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionWithOneRequiredArg() {
 Option option = new Option("b", "option-b", true, "Option B requires an argument");
 assertTrue(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertEquals("Option B requires an argument", option.getDescription());
 }

 @Test
 public void testOptionWithOneOptionalArg() {
 Option option = new Option("c", "option-c", false, "Option C can take an argument", 1);
 assertTrue(option.hasOptionalArg());
 assertEquals("Option C can take an argument", option.getDescription());
 assertFalse(option.isRequired());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionWithInvalidArgNumber() {
 Option option = new Option("d", "option-d", false, "Option D should not have -2 number of args");
 try {
 option.setArgs(-2);
 }
 catch (IllegalArgumentException iae) {
 assertEquals("Invalid number of arguments for option: option-d", iae.getMessage());
 }
 }

 @Test
 public void testOptionWithValues() {
 Option option = new Option("e", "option-e", false, "Option E can take multiple values", -2);
 option.setValueSeparator(':');
 option.addValueForProcessing("value1");
 option.addValueForProcessing("value2");
 List<? extends String> values = option.getValuesList();
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testOptionEquality() {
 Option option1 = new Option("a", null, false, "Option A");
 Option option2 = new Option("a", null, false, "Option A");
 assertTrue(option1.equals(option2));
 assertTrue(option1.hashCode() == option2.hashCode());
 }

 @Test
 public void testOptionClone() throws CloneNotSupportedException {
 Option option = new Option("f", "option-f", false, "Option F");
 Option clonedOption = (Option)option.clone();
 assertNotSame(option, clonedOption);
 assertEquals(option, clonedOption);
 }
}