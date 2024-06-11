package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private static final String[] BASIC_ARGS = new String[]{"arg1", "arg2", "arg3"};
 private static final ArgsParser argsParser = new ArgsParser(BASIC_ARGS);

 @Test
 public void testGetArgsCount() {
     assertEquals(3, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
     assertArrayEquals(BASIC_ARGS, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNullArgs() {
     ArgsParser argsParserNull = new ArgsParser(null);
 }

 @Test
 public void testSetArgs() {
     ArgsParser argsParserSet = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
     argsParserSet.setArgs(BASIC_ARGS);
     assertArrayEquals(BASIC_ARGS, argsParserSet.getArgs());
 }

 @Test
 public void testGetArgsLeftCount() {
     assertEquals(3, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgumentShortKey() {
     final String key = "a";
     final SwitchArgument switchArgument = argsParser.parseSwitchArgument(key);
     assertEquals(0, switchArgument.getIndex());
     assertFalse(switchArgument.isLongKey());
 }

 @Test
 public void testParseSwitchArgumentLongKey() {
     final String key = "long-key";
     final SwitchArgument switchArgument = argsParser.parseSwitchArgument(key);
     assertEquals(-1, switchArgument.getIndex());
     assertTrue(switchArgument.isLongKey());
 }

 @Test
 public void testParseSwitchArgumentNotFound() {
     final String key = "not-found";
     final SwitchArgument switchArgument = argsParser.parseSwitchArgument(key);
     assertEquals(-1, switchArgument.getIndex());
     assertFalse(switchArgument.isLongKey());
 }

 @Test
 public void testParseParameterArgumentShortKey() {
     final String key = "a";
     final ParameterArgument parameterArgument = argsParser.parseParameterArgument(key);
     assertEquals(-1, parameterArgument.getIndex());
     assertFalse(parameterArgument.isLongKey());
     assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentLongKey() {
     final String key = "long-key";
     final String value = "value";
     final int index = 2;
     final ArgsParser argsParserWithValue = new ArgsParser(new String[]{"--long-key", "value", "arg3"});
     final ParameterArgument parameterArgument = argsParserWithValue.parseParameterArgument(key);
     assertEquals(index, parameterArgument.getIndex());
     assertTrue(parameterArgument.isLongKey());
     assertEquals("value", parameterArgument.getValue());
 }

 @Test(expected = IllegalStateException.class)
 public void testParseParameterArgumentMissingValue() {
     final String key = "long-key";
     final ArgsParser argsParserWithKey = new ArgsParser(new String[]{"--long-key", "arg3"});
     argsParserWithKey.parseParameterArgument(key);
 }

 @Test
 public void testParseParameterArgumentNotFound() {
     final String key = "not-found";
     final ArgsParser argsParserWithKey = new ArgsParser(BASIC_ARGS);
     final ParameterArgument parameterArgument = argsParserWithKey.parseParameterArgument(key);
     assertEquals(-1, parameterArgument.getIndex());
     assertFalse(parameterArgument.isLongKey());
     assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseStringArgument() {
     final ArgsParser argsParserStringArg = new ArgsParser(new String[]{"arg1", "-a", "arg3", "-b", "value"});
     final List<StringArgument> stringArguments = argsParserStringArg.parseStringArgument();
     assertEquals(2, stringArguments.size());
     assertEquals("a", stringArguments.get(0).getKey());
     assertEquals("value", stringArguments.get(1).getValue());
 }

 @Test
 public void testParseStringArgumentEmptyArgs() {
     final ArgsParser argsParserEmpty = new ArgsParser(new String[]{});
     final List<StringArgument> stringArguments = argsParserEmpty.parseStringArgument();
     assertEquals(0, stringArguments.size());
 }

 @Test
 public void testParseStringArgumentNoStringArgs() {
     final ArgsParser argsParserNoString = new ArgsParser(new String[]{"arg1", "arg2", "arg3"});
     final List<StringArgument> stringArguments = argsParserNoString.parseStringArgument();
     assertEquals(0, stringArguments.size());
 }
}