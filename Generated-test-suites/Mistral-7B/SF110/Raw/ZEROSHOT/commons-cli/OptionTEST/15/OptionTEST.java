package org.apache.commons.cli.test;

import static org.junit.Assert.*;

import org.apache.commons.cli.*;
import java.util.*;

public class OptionTest {

 @org.junit.Test
 public void testOptionConstruction() {
 Option opt = new Option("t", "test-option", true, "Test option");
 assertEquals("t", opt.getOpt());
 assertEquals("test-option", opt.getLongOpt());
 assertTrue(opt.isRequired());
 assertFalse(opt.hasArg());
 assertNull(opt.getDescription());
 assertEquals("Test option", opt.getDescription());

 opt = new Option("t", "test-option", false, "Test option");
 assertEquals("t", opt.getOpt());
 assertEquals("test-option", opt.getLongOpt());
 assertFalse(opt.isRequired());
 assertFalse(opt.hasArg());
 assertNull(opt.getDescription());
 assertEquals("Test option", opt.getDescription());

 opt = new Option("t", "test-option", false, "Test option", "test-arg");
 assertEquals("t", opt.getOpt());
 assertEquals("test-option", opt.getLongOpt());
 assertFalse(opt.isRequired());
 assertTrue(opt.hasArg());
 assertEquals("test-arg", opt.getArgName());
 assertNull(opt.getDescription());
 assertEquals("Test option", opt.getDescription());
 }

 @org.junit.Test
 public void testOptionId() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertEquals('t', opt.getId());
 }

 @org.junit.Test
 public void testOptionKey() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertEquals("test-option", opt.getKey());
 }

 @org.junit.Test
 public void testOptionGetType() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertNull(opt.getType());

 opt = new Option('t', "test-option", false, "Test option", Integer.TYPE);
 assertEquals(Integer.TYPE, opt.getType());
 }

 @org.junit.Test
 public void testOptionSetType() {
 Option opt = new Option('t', "test-option", false, "Test option");
 Object type = new Object();
 opt.setType(type);
 assertEquals(type, opt.getType());
 }

 @org.junit.Test
 public void testOptionHasLongOpt() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertFalse(opt.hasLongOpt());

 opt = new Option('t', "test-option", false, "Test option", "long-test-option");
 assertTrue(opt.hasLongOpt());
 }

 @org.junit.Test
 public void testOptionHasArg() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertFalse(opt.hasArg());

 opt = new Option('t', "test-option", true, "Test option");
 assertTrue(opt.hasArg());
 }

 @org.junit.Test
 public void testOptionHasOptionalArg() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertFalse(opt.hasOptionalArg());

 opt = new Option('t', "test-option", true, "Test option");
 assertTrue(opt.hasOptionalArg());
 }

 @org.junit.Test
 public void testOptionHasDescription() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertNull(opt.getDescription());

 opt = new Option('t', "test-option", false, "Test option", "Test description");
 assertNotNull(opt.getDescription());
 assertEquals("Test description", opt.getDescription());
 }

 @org.junit.Test
 public void testOptionSetDescription() {
 Option opt = new Option('t', "test-option", false, "Test option");
 String description = "New test description";
 opt.setDescription(description);
 assertEquals(description, opt.getDescription());
 }

 @org.junit.Test
 public void testOptionRequired() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertFalse(opt.isRequired());

 opt.setRequired(true);
 assertTrue(opt.isRequired());
 }

 @org.junit.Test
 public void testOptionArgName() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertNull(opt.getArgName());

 opt = new Option('t', "test-option", false, "Test option", "test-arg");
 assertEquals("test-arg", opt.getArgName());
 }

 @org.junit.Test
 public void testOptionSetArgName() {
 Option opt = new Option('t', "test-option", false, "Test option");
 String argName = "New arg name";
 opt.setArgName(argName);
 assertEquals(argName, opt.getArgName());
 }

 @org.junit.Test
 public void testOptionHasArgs() {
 Option opt = new Option('t', "test-option", false, "Test option");
 assertFalse(opt.hasArgs());

 opt = new Option('t', "test-option", true, "Test option", "test-arg1", "test-arg2");
 assertTrue(opt.hasArgs());
 }

 @org.junit.Test
 public void testOptionSetArgs() {
 Option opt = new Option('t', "test-option", false, "Test option");
 opt.setArgs(1);
 assertEquals(1, opt.getArgs());
 }

 @org.junit.Test(expected = RuntimeException.class)
 public void testAddValueWithNoArgs() {
 Option opt = new Option('t', "test-option", false, "Test option");
 opt.addValue("value");
 fail("Adding value to an option with no args should throw a RuntimeException");
 }

 @org.junit.Test
 public void testAddValueWithOneArg() {
 Option opt = new Option('t', "test-option", true, "Test option", "test-arg");
 opt.addValue("value");
 assertEquals("value", opt.getValue());
 }

 @org.junit.Test
 public void testAddValueWithMultipleValues() {
 Option opt = new Option('t', "test-option", true, "Test option", "test-arg");
 opt.setValueSeparator(';');
 opt.addValue("value1");
 opt.addValue("value2");
 List values = opt.getValuesList();
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @org.junit.Test
 public void testToString() {
 Option opt = new Option('t', "test-option", true, "Test option", "test-arg");
 assertEquals("[ option: t :: Test option :: [ARG] ]", opt.toString());
 }

 @org.junit.Test
 public void testEquals() {
 Option opt1 = new Option('t', "test-option", true, "Test option", "test-arg");
 Option opt2 = new Option('t', "test-option", true, "Test option", "test-arg");
 assertTrue(opt1.equals(opt2));

 opt1 = new Option('t', "test-option", true, "Test option", "test-arg");
 opt2 = new Option('T', "test-option", true, "Test option", "test-arg");
 assertFalse(opt1.equals(opt2));

 opt1 = new Option('t', "test-option", true, "Test option", "test-arg");
 opt2 = new Option('t', "test-option", true, "Test option2", "test-arg");
 assertFalse(opt1.equals(opt2));
 }
}