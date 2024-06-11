package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionCreation() {
 Option option = new Option("opt", "option description");
 assertEquals("opt", option.getOpt());
 assertEquals("option description", option.getDescription());
 assertNull(option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArgName());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionCreationWithLongOpt() {
 Option option = new Option("opt", "long opt description", true, "lopt");
 assertEquals("opt", option.getOpt());
 assertEquals("long opt description", option.getDescription());
 assertEquals("lopt", option.getLongOpt());
 assertTrue(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArgName());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionCreationWithArg() {
 Option option = new Option("opt", "option description", true, null, "argName");
 assertEquals("opt", option.getOpt());
 assertEquals("option description", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.isRequired());
 assertTrue(option.hasOptionalArg());
 assertEquals("argName", option.getArgName());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArgName());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 assertNull(option.getValue());
 assertNull(option.getValues());
 }

 @Test
 public void testOptionCreationWithArgAndValues() {
 Option option = new Option("opt", "option description", true, null, "argName", new String[]{"value1", "value2"});
 assertEquals("opt", option.getOpt());
 assertEquals("option description", option.getDescription());
 assertNull(option.getLongOpt());
 assertTrue(option.isRequired());
 assertTrue(option.hasOptionalArg());
 assertEquals("argName", option.getArgName());
 assertFalse(option.hasLongOpt());
 assertFalse(option.hasArgName());
 assertFalse(option.hasArgs());
 assertFalse(option.hasValueSeparator());
 assertEquals("value1", option.getValue());
 String[] expectedValues = {"value1", "value2"};
 assertArrayEquals(expectedValues, option.getValues().toArray(new String[option.getValues().size()]));
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionCreationWithInvalidOption() {
 Option option = new Option("opt", "option description", true, null, null);
 fail("IllegalArgumentException should have been thrown.");
 }

 @Test
 public void testOptionGettersAndSetters() {
 Option option = new Option("opt", "option description");
 option.setLongOpt("lopt");
 option.setDescription("updated description");
 option.setRequired(true);
 option.setOptionalArg(false);
 option.setArgName("argName");
 option.setArgs(1);
 option.setValueSeparator(';');
 option.addValue("value1");
 option.addValue("value2");
 assertEquals("opt", option.getOpt());
 assertEquals("lopt", option.getLongOpt());
 assertEquals("updated description", option.getDescription());
 assertTrue(option.isRequired());
 assertFalse(option.hasOptionalArg());
 assertEquals("argName", option.getArgName());
 assertEquals(1, option.getArgs());
 assertEquals(';', option.getValueSeparator());
 assertEquals("value1", option.getValue());
 String[] expectedValues = {"value1", "value2"};
 assertArrayEquals(expectedValues, option.getValues().toArray(new String[option.getValues().size()]));
 }

 @Test
 public void testOptionEqualsAndHashcode() {
 Option option1 = new Option("opt1", "desc1");
 Option option2 = new Option("opt1", "desc1");
 Option option3 = new Option("opt2", "desc2");
 assertTrue(option1.equals(option2));
 assertTrue(option1.hashCode() == option2.hashCode());
 assertFalse(option1.equals(option3));
 assertFalse(option1.hashCode() == option3.hashCode());
 }

 @Test
 public void testOptionToString() {
 Option option = new Option("opt", "option description");
 assertEquals("[ option: opt :: option description ]", option.toString());
 }
}