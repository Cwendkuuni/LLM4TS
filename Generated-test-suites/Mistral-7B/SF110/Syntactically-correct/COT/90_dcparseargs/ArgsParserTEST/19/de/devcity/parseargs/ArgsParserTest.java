package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;
import de.devcity.parseargs.arguments.ParameterArgument;

public class ArgsParserTest {

 private ArgsParser parser;

 @Before
 public void setUp() {
 this.parser = new ArgsParser(new String[]{"arg1", "--key SwitchArg", "--longKey LongArg", "-k Value", "otherArg"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(5, this.parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(new String[]{"arg1", "--key", "--longKey", "-k", "otherArg"}, this.parser.getArgs());
 }

 @Test
 public void testSetArgs() {
 String[] args = new String[]{"arg1", "arg2", "arg3"};
 this.parser.setArgs(args);
 assertArrayEquals(args, this.parser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 this.parser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(3, this.parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArg = this.parser.parseSwitchArgument("key");
 assertEquals(1, switchArg.getIndex());
 assertEquals("key", switchArg.getKey());
 assertTrue(switchArg.isSwitch());

 switchArg = this.parser.parseSwitchArgument("LongArg");
 assertEquals(2, switchArg.getIndex());
 assertEquals("LongArg", switchArg.getKey());
 assertTrue(switchArg.isSwitch());

 switchArg = this.parser.parseSwitchArgument("noKey");
 assertEquals(-1, switchArg.getIndex());
 assertNull(switchArg.getKey());
 assertFalse(switchArg.isSwitch());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument paramArg = this.parser.parseParameterArgument("k");
 assertEquals(2, paramArg.getIndex());
 assertEquals("Value", paramArg.getValue());

 paramArg = this.parser.parseParameterArgument("noKey");
 assertEquals(-1, paramArg.getIndex());
 assertNull(paramArg);
 }

 @Test
 public void testParseStringArguments() {
 List<StringArgument> stringArgs = this.parser.parseStringArgument();
 assertEquals(2, stringArgs.size());
 assertEquals(1, stringArgs.get(0).getIndex());
 assertEquals("arg1", stringArgs.get(0).getValue());
 assertEquals(3, stringArgs.get(1).getIndex());
 assertEquals("otherArg", stringArgs.get(1).getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseSwitchArgumentWithNoArgument() {
 this.parser = new ArgsParser(new String[0]);
 this.parser.parseSwitchArgument("key");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentWithNoValue() {
 this.parser = new ArgsParser(new String[]{"arg1", "--k"});
 this.parser.parseParameterArgument("k");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentWithDuplicateKey() {
 this.parser = new ArgsParser(new String[]{"arg1", "--k", "--k", "arg3"});
 this.parser.parseParameterArgument("k");
 this.parser.parseParameterArgument("k");
 }
}