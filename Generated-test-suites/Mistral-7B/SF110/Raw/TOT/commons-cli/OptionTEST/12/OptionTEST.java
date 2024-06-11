package org.apache.commons.cli.test;

import org.apache.commons.cli.Option;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionTest {

 @Test
 public void testOptionCreationWithNoArguments() {
 Option option = new Option("a", "optionA", false, "Description for option A");
 assertEquals("-a", option.getOpt());
 assertEquals("optionA", option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getValue());
 assertEquals("Description for option A", option.getDescription());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithOneArgument() {
 Option option = new Option("b", "optionB", true, "Description for option B");
 assertEquals("-b", option.getOpt());
 assertEquals("optionB", option.getLongOpt());
 assertTrue(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getValue());
 assertEquals("Description for option B", option.getDescription());
 assertTrue(option.isRequired());
 }

 @Test
 public void testOptionCreationWithOneArgumentAndOptional() {
 Option option = new Option("c", "optionC", true, "Description for option C", "defaultValue");
 assertEquals("-c", option.getOpt());
 assertEquals("optionC", option.getLongOpt());
 assertTrue(option.hasArg());
 assertTrue(option.hasOptionalArg());
 assertEquals("defaultValue", option.getValue(null));
 assertEquals("Description for option C", option.getDescription());
 assertFalse(option.isRequired());
 }

 @Test
 public void testOptionCreationWithMultipleArguments() {
 Option option = new Option("d", null, false, "Description for option D", 2);
 assertEquals("-d", option.getOpt());
 assertEquals(null, option.getLongOpt());
 assertFalse(option.hasArg());
 assertFalse(option.hasOptionalArg());
 assertNull(option.getValue());
 assertEquals("Description for option D", option.getDescription());
 assertFalse(option.isRequired());
 assertFalse(option.hasArgs());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionCreationWithInvalidArgumentCount() {
 Option option = new Option("e", null, false, "Description for option E", 0);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testOptionCreationWithInvalidOptionCharacter() {
 Option option = new Option("\u0000", null, false, "Description for option E");
 }
}