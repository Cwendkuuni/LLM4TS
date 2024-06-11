package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;

public class ArgsParserTest {

 private String[] args = new String[] { "-a", "--long-a", "-b", "--long-b=value", "1", "--switch", "--long-switch", "two", "--long-param=three", "--long-param2=four" };

 @Test
 public void testConstructor() {
 ArgsParser parser = new ArgsParser(args);
 assertEquals(args.length, parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 ArgsParser parser = new ArgsParser(args);
 assertArrayEquals(args, parser.getArgs());
 }

 @Test
 public void testSetArgs() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(args);
 assertEquals(args.length, parser.getArgsCount());
 assertArrayEquals(args, parser.getArgs());
 }

 @Test
 public void testGetArgsLeftCount() {
 ArgsParser parser = new ArgsParser(args);
 assertEquals(8, parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 ArgsParser parser = new ArgsParser(args);
 SwitchArgument switchArg = parser.parseSwitchArgument("-long-a");
 assertTrue(switchArg.isSet());
 assertEquals(0, switchArg.getIndex());
 switchArg = parser.parseSwitchArgument("--switch");
 assertTrue(switchArg.isSet());
 assertEquals(5, switchArg.getIndex());
 switchArg = parser.parseSwitchArgument("not-found");
 assertFalse(switchArg.isSet());
 assertEquals(-1, switchArg.getIndex());
 }

 @Test
 public void testParseStringArgument() {
 ArgsParser parser = new ArgsParser(args);
 List<StringArgument> stringArgs = parser.parseStringArgument();
 assertEquals(6, stringArgs.size());
 StringArgument stringArg = stringArgs.get(0);
 assertEquals(1, stringArg.getIndex());
 assertEquals("1", stringArg.getValue());
 stringArg = stringArgs.get(1);
 assertEquals(3, stringArg.getIndex());
 assertEquals("two", stringArg.getValue());
 stringArg = stringArgs.get(2);
 assertEquals(7, stringArg.getIndex());
 assertEquals("three", stringArg.getValue());
 stringArg = stringArgs.get(3);
 assertEquals(8, stringArg.getIndex());
 assertEquals("four", stringArg.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testParseSwitchArgumentWithNullArgs() {
 ArgsParser parser = new ArgsParser();
 parser.setArgs(null);
 parser.parseSwitchArgument("arg");
 }

 @Test
 public void testParseParameterArgument() {
 ArgsParser parser = new ArgsParser(args);
 ParameterArgument paramArg = parser.parseParameterArgument("--long-param");
 assertEquals(3, paramArg.getIndex());
 assertEquals("three", paramArg.getValue());
 paramArg = parser.parseParameterArgument("--long-param2");
 assertEquals(8, paramArg.getIndex());
 assertEquals("four", paramArg.getValue());
 paramArg = parser.parseParameterArgument("--long-param");
 assertNull(paramArg);
 }

}