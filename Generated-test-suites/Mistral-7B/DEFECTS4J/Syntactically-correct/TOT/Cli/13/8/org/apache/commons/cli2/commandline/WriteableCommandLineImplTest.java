package org.apache.commons.cli2.commandline;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {

 private WriteableCommandLineImpl commandLine;

 @Before
 public void setUp() {
 commandLine = new WriteableCommandLineImpl(new Option("a", "arg", false, "An argument"), new ArrayList());
 }

 @Test
 public void testAddOption() {
 Option option = new Option("o", "opt", true, "An option with a value");
 commandLine.addOption(option);
 assertTrue(commandLine.hasOption(option));
 }

 @Test
 public void testAddValue() {
 commandLine.addValue(commandLine.getOption("a"), "value1");
 List values = commandLine.getValues(commandLine.getOption("a"), null);
 assertEquals(1, values.size());
 assertEquals("value1", values.get(0));
 }

 @Test
 public void testAddSwitch() {
 commandLine.addSwitch(commandLine.getOption("a"), true);
 assertTrue(commandLine.getSwitch(commandLine.getOption("a"), false));
 }

 @Test
 public void testHasOption() {
 assertFalse(commandLine.hasOption(new Option("x", "xopt", false, "An unknown option")));
 }

 @Test
 public void testGetOption() {
 Option option = commandLine.getOption("o");
 assertEquals("opt", option.getLongOpt());
 assertFalse(option.isValueSeparated());
 assertFalse(option.isRequired());
 }

 @Test
 public void testGetValues() {
 commandLine.addValue(commandLine.getOption("a"), "value1");
 commandLine.addValue(commandLine.getOption("a"), "value2");
 List values = commandLine.getValues(commandLine.getOption("a"), null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testGetSwitch() {
 commandLine.addSwitch(commandLine.getOption("a"), true);
 assertTrue(commandLine.getSwitch(commandLine.getOption("a"), false));
 }

 @Test
 public void testAddProperty() {
 commandLine.addProperty("property", "value");
 assertEquals("value", commandLine.getProperty("property", "defaultValue"));
 }

 @Test
 public void testAddOptionWithValueList() {
 Option option = new Option("l", "list", true, "An option with a list of values");
 commandLine.addOption(option);
 commandLine.addValue(option, "value1");
 commandLine.addValue(option, "value2");
 List values = commandLine.getValues(option, null);
 assertEquals(2, values.size());
 assertEquals("value1", values.get(0));
 assertEquals("value2", values.get(1));
 }

 @Test
 public void testAddOptionWithValueAndSwitch() {
 Option option = new Option("b", "bool", false, "An option with a boolean value and a switch");
 commandLine.addOption(option);
 commandLine.addValue(option, "true");
 commandLine.addSwitch(option, true);
 assertTrue(commandLine.getSwitch(option, false));
 }

 @Test
 public void testToString() {
 assertEquals("arg value1 value2", commandLine.toString());
 }
}