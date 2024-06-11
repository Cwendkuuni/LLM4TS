package org.apache.commons.cli.test;

import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

 @Test
 public void testOptionBuilder() {
 OptionBuilder ob = new OptionBuilder();

 // Test default options
 Option optionDefault = ob.create('D');
 assertEquals("Default Option", optionDefault.getLongOpt());
 assertFalse(optionDefault.isRequired());
 assertNull(optionDefault.getArgName());
 assertNull(optionDefault.getType());
 assertEquals(Option.UNINITIALIZED, optionDefault.getArgs());
 assertFalse(optionDefault.hasOptionalArg());
 assertNull(optionDefault.getValueSeparator());

 // Test setting options
 Option optionHasArg = OptionBuilder.hasArg().create('A');
 assertEquals("Option with Argument", optionHasArg.getLongOpt());
 assertTrue(optionHasArg.isRequired());
 assertEquals("arg", optionHasArg.getArgName());
 assertNull(optionHasArg.getType());
 assertEquals(1, optionHasArg.getArgs());
 assertFalse(optionHasArg.hasOptionalArg());
 assertNull(optionHasArg.getValueSeparator());

 Option optionHasOptionalArg = OptionBuilder.hasOptionalArg().create('O');
 assertEquals("Option with Optional Argument", optionHasOptionalArg.getLongOpt());
 assertFalse(optionHasOptionalArg.isRequired());
 assertEquals("arg", optionHasOptionalArg.getArgName());
 assertNull(optionHasOptionalArg.getType());
 assertEquals(1, optionHasOptionalArg.getArgs());
 assertTrue(optionHasOptionalArg.hasOptionalArg());
 assertNull(optionHasOptionalArg.getValueSeparator());

 Option optionHasValueSeparator = OptionBuilder.withValueSeparator(':').create('V');
 assertEquals("Option with Value Separator", optionHasValueSeparator.getLongOpt());
 assertFalse(optionHasValueSeparator.isRequired());
 assertEquals("arg", optionHasValueSeparator.getArgName());
 assertNull(optionHasValueSeparator.getType());
 assertEquals(1, optionHasValueSeparator.getArgs());
 assertFalse(optionHasValueSeparator.hasOptionalArg());
 assertEquals(':', optionHasValueSeparator.getValueSeparator());

 Option optionWithType = OptionBuilder.withType(Integer.TYPE).create('T');
 assertEquals("Option with Type", optionWithType.getLongOpt());
 assertFalse(optionWithType.isRequired());
 assertEquals("arg", optionWithType.getArgName());
 assertSame(Integer.TYPE, optionWithType.getType());
 assertEquals(1, optionWithType.getArgs());
 assertFalse(optionWithType.hasOptionalArg());
 assertNull(optionWithType.getValueSeparator());

 Option optionIsRequired = OptionBuilder.isRequired().create('R');
 assertEquals("Option Required", optionIsRequired.getLongOpt());
 assertTrue(optionIsRequired.isRequired());
 assertEquals("arg", optionIsRequired.getArgName());
 assertNull(optionIsRequired.getType());
 assertEquals(1, optionIsRequired.getArgs());
 assertFalse(optionIsRequired.hasOptionalArg());
 assertNull(optionIsRequired.getValueSeparator());

 Option optionHasArgs = OptionBuilder.hasArgs().create('S');
 assertEquals("Option with Args", optionHasArgs.getLongOpt());
 assertFalse(optionHasArgs.isRequired());
 assertEquals("args", optionHasArgs.getArgName());
 assertEquals(Option.UNLIMITED_VALUES, optionHasArgs.getArgs());
 assertFalse(optionHasArgs.hasOptionalArg());
 assertNull(optionHasArgs.getValueSeparator());

 Option optionHasArgsWithOneArg = OptionBuilder.hasArgs(1).create('X');
 assertEquals("Option with 1 Arg", optionHasArgsWithOneArg.getLongOpt());
 assertFalse(optionHasArgsWithOneArg.isRequired());
 assertEquals("arg", optionHasArgsWithOneArg.getArgName());
 assertEquals(1, optionHasArgsWithOneArg.getArgs());
 assertFalse(optionHasArgsWithOneArg.hasOptionalArg());
 assertNull(optionHasArgsWithOneArg.getValueSeparator());

 Option optionHasOptionalArgs = OptionBuilder.hasOptionalArgs().create('Y');
 assertEquals("Option with Optional Args", optionHasOptionalArgs.getLongOpt());
 assertFalse(optionHasOptionalArgs.isRequired());
 assertEquals("args", optionHasOptionalArgs.getArgName());
 assertEquals(Option.UNLIMITED_VALUES, optionHasOptionalArgs.getArgs());
 assertTrue(optionHasOptionalArgs.hasOptionalArg());
 assertNull(optionHasOptionalArgs.getValueSeparator());

 Option optionHasOptionalArgsWithNum = OptionBuilder.hasOptionalArgs(2).create('Z');
 assertEquals("Option with 2 Optional Args", optionHasOptionalArgsWithNum.getLongOpt());
 assertFalse(optionHasOptionalArgsWithNum.isRequired());
 assertEquals("args", optionHasOptionalArgsWithNum.getArgName());
 assertEquals(2, optionHasOptionalArgsWithNum.getArgs());
 assertTrue(optionHasOptionalArgsWithNum.hasOptionalArg());
 assertNull(optionHasOptionalArgsWithNum.getValueSeparator());
 }
}