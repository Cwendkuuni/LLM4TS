package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;

 @Test
 public void testConstructors() {
 argsParser = new ArgsParser();
 assertNull(argsParser.getArgs());

 String[] args = new String[]{"arg1", "arg2", "arg3"};
 argsParser = new ArgsParser(args);
 assertArrayEquals(args, argsParser.getArgs());
 }

 @Test
 public void testGettersAndSetters() {
 String[] args = new String[]{"arg1", "arg2", "arg3"};
 argsParser = new ArgsParser(args);
 assertEquals(3, argsParser.getArgsCount());
 assertEquals(args, argsParser.getArgs());

 argsParser.setArgs(new String[]{ "arg4", "arg5" });
 assertArrayEquals(new String[]{"arg4", "arg5"}, argsParser.getArgs());
 }

 @Test
 public void testGetArgsLeftCount() {
 String[] args = new String[]{"--arg1=value1", "-arg2", "--arg3=value3", "-arg4"};
 argsParser = new ArgsParser(args);
 assertEquals(2, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 String[] args = new String[]{"arg1", "--arg2", "--arg3=value3", "-arg4"};
 argsParser = new ArgsParser(args);

 SwitchArgument arg2 = argsParser.parseSwitchArgument("arg2");
 assertEquals(1, arg2.getIndex());
 assertNull(arg2.getValue());
 assertTrue(arg2.isLong());

 SwitchArgument arg3 = argsParser.parseSwitchArgument("arg3");
 assertEquals(2, arg3.getIndex());
 assertNull(arg3.getValue());
 assertTrue(arg3.isLong());

 SwitchArgument nonExistentArg = argsParser.parseSwitchArgument("arg5");
 assertEquals(-1, nonExistentArg.getIndex());
 assertNull(nonExistentArg.getValue());
 assertFalse(nonExistentArg.isLong());
 }

 @Test
 public void testParseParameterArgument() {
 String[] args = new String[]{"arg1", "--arg2=value2", "--arg3=value3", "-arg4=value4"};
 argsParser = new ArgsParser(args);

 ParameterArgument arg2 = argsParser.parseParameterArgument("arg2");
 assertEquals(1, arg2.getIndex());
 assertEquals("value2", arg2.getValue());
 assertFalse(arg2.isLong());

 ParameterArgument arg3 = argsParser.parseParameterArgument("arg3");
 assertEquals(2, arg3.getIndex());
 assertEquals("value3", arg3.getValue());
 assertFalse(arg3.isLong());

 ParameterArgument arg4 = argsParser.parseParameterArgument("arg4");
 assertEquals(3, arg4.getIndex());
 assertEquals("value4", arg4.getValue());
 assertFalse(arg4.isLong());
 }

 @Test
 public void testParseStringArgument() {
 String[] args = new String[]{"arg1", "--arg2=value2", "--arg3=value3", "-arg4=value4"};
 argsParser = new ArgsParser(args);

 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(2, stringArguments.size());
 StringArgument arg2 = stringArguments.get(0);
 assertEquals(1, arg2.getIndex());
 assertEquals("value2", arg2.getValue());

 StringArgument arg4 = stringArguments.get(1);
 assertEquals(3, arg4.getIndex());
 assertEquals("value4", arg4.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 String[] args = null;
 argsParser = new ArgsParser();
 argsParser.setArgs(args);
 fail("Expected NullPointerException");
 }
}