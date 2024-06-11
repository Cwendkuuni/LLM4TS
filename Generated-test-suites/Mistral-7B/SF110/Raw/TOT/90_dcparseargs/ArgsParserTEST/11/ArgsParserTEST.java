package de.devcity.parseargs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

 private String[] testArgs = new String[] { "-a", "--long-key=value", "-b", "--long-key2=value2", "--long-key3", "--switch", "arg1", "arg2", "arg3", "arg4", "--string-arg=value" };

 @Test
 public void testGetArgsCount() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 assertEquals(testArgs.length, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 String[] args = argsParser.getArgs();
 assertArrayEquals(testArgs, args);
 }

 @Test
 public void testSetArgs() {
 ArgsParser argsParser = new ArgsParser();
 argsParser.setArgs(testArgs);
 ArgsParser parsedArgsParser = new ArgsParser(argsParser.getArgs());

 // Test getArgsCount()
 assertEquals(testArgs.length, parsedArgsParser.getArgsCount());

 // Test parseSwitchArgument()
 SwitchArgument switchArgument = parsedArgsParser.parseSwitchArgument("--long-key");
 assertEquals(1, switchArgument.getIndex());
 assertEquals("--long-key", switchArgument.getKey());
 assertTrue(switchArgument.isLong());

 switchArgument = parsedArgsParser.parseSwitchArgument("--switch");
 assertEquals(6, switchArgument.getIndex());
 assertTrue(switchArgument.isLong());

 // Test parseParameterArgument()
 ParameterArgument parameterArgument = parsedArgsParser.parseParameterArgument("--long-key2");
 assertEquals(2, parameterArgument.getIndex());
 assertEquals("--long-key2", parameterArgument.getKey());
 assertEquals("value2", parameterArgument.getValue());

 // Test parseStringArgument()
 List<StringArgument> stringArguments = parsedArgsParser.parseStringArgument();
 assertEquals(1, stringArguments.size());
 StringArgument stringArgument = stringArguments.get(0);
 assertEquals(7, stringArgument.getIndex());
 assertEquals("--string-arg", stringArgument.getKey());
 assertEquals("value", stringArgument.getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 ArgsParser argsParser = new ArgsParser();
 argsParser.setArgs(null);
 }

 @Test
 public void testParseSwitchArgumentMissingKey() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("--missing-key");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isLong());
 assertNull(switchArgument.getKey());
 }

 @Test
 public void testParseSwitchArgumentMissingIndicator() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("a");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isLong());
 assertNull(switchArgument.getKey());
 }

 @Test
 public void testParseParameterArgumentMissingKey() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("a");
 assertEquals(-1, parameterArgument.getIndex());
 assertNull(parameterArgument.getKey());
 assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentMissingValue() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("--long-key2");
 assertEquals(2, parameterArgument.getIndex());
 assertEquals("--long-key2", parameterArgument.getKey());
 assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseStringArgumentMissingKey() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(0, stringArguments.size());
 }
}