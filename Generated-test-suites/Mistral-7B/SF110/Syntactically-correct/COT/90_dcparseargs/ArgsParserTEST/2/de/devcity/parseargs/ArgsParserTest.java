package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;

 @Before
 public void setup() {
 argsParser = new ArgsParser(new String[]{"test", "-a", "1", "--long-arg", "-a", "2"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(6, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 String[] args = argsParser.getArgs();
 assertEquals(6, args.length);
 assertArrayEquals(new String[]{"test", "-a", "1", "--long-arg", "-a", "2"}, args);
 }

 @Test
 public void testSetArgs() {
 String[] newArgs = new String[]{"newTest", "-b", "3"};
 argsParser.setArgs(newArgs);
 assertEquals(3, argsParser.getArgsCount());
 assertArrayEquals(new String[]{"newTest", "-b", "3"}, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(2, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("-a");
 assertEquals(2, switchArgument.getIndex());
 assertEquals("-a", switchArgument.getKey());
 assertTrue(switchArgument.isLong());

 switchArgument = argsParser.parseSwitchArgument("--long-arg");
 assertEquals(3, switchArgument.getIndex());
 assertEquals("--long-arg", switchArgument.getKey());
 assertTrue(switchArgument.isLong());

 switchArgument = argsParser.parseSwitchArgument("--nonExistentArg");
 assertEquals(-1, switchArgument.getIndex());
 assertNull(switchArgument.getKey());
 assertFalse(switchArgument.isLong());
 }

 @Test
 public void testParseSwitchArgumentLongKeyMissing() {
 String[] args = new String[]{"--long-arg", "value"};
 ArgsParser argsParserLongKeyMissing = new ArgsParser(args);
 SwitchArgument switchArgument = argsParserLongKeyMissing.parseSwitchArgument("nonExistentKey");
 assertEquals(-1, switchArgument.getIndex());
 assertNull(switchArgument.getKey());
 assertFalse(switchArgument.isLong());
 }

 @Test
 public void testParseSwitchArgumentShortKeyMissing() {
 String[] args = new String[]{"-a", "value"};
 ArgsParser argsParserShortKeyMissing = new ArgsParser(args);
 SwitchArgument switchArgument = argsParserShortKeyMissing.parseSwitchArgument("nonExistentKey");
 assertEquals(-1, switchArgument.getIndex());
 assertNull(switchArgument.getKey());
 assertFalse(switchArgument.isLong());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("a");
 assertEquals(2, parameterArgument.getIndex());
 assertEquals("1", parameterArgument.getValue());

 parameterArgument = argsParser.parseParameterArgument("--long-arg");
 assertEquals(3, parameterArgument.getIndex());
 assertEquals("value", parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentLongKeyMissingValue() {
 String[] args = new String[]{"--long-arg"};
 ArgsParser argsParserLongKeyMissingValue = new ArgsParser(args);
 ParameterArgument parameterArgument = argsParserLongKeyMissingValue.parseParameterArgument("nonExistentKey");
 assertEquals(-1, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentShortKeyMissingValue() {
 String[] args = new String[]{"-a"};
 ArgsParser argsParserShortKeyMissingValue = new ArgsParser(args);
 ParameterArgument parameterArgument = argsParserShortKeyMissingValue.parseParameterArgument("nonExistentKey");
 assertEquals(-1, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentShortKeyValueMissing() {
 String[] args = new String[]{"-a"};
 ArgsParser argsParserShortKeyValueMissing = new ArgsParser(args);
 ParameterArgument parameterArgument = argsParserShortKeyValueMissing.parseParameterArgument("a");
 assertEquals(1, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentLongKeyValueMissing() {
 String[] args = new String[]{"--long-arg"};
 ArgsParser argsParserLongKeyValueMissing = new ArgsParser(args);
 ParameterArgument parameterArgument = argsParserLongKeyValueMissing.parseParameterArgument("--long-arg");
 assertEquals(3, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(4, stringArguments.size());
 assertEquals("test", stringArguments.get(0).getValue());
 assertEquals("1", stringArguments.get(1).getValue());
 assertEquals("value", stringArguments.get(2).getValue());
 assertNull(stringArguments.get(3).getValue());
 }

 @Test
 public void testParseStringArgumentEmptyList() {
 ArgsParser argsParserEmptyList = new ArgsParser(new String[0]);
 List<StringArgument> stringArguments = argsParserEmptyList.parseStringArgument();
 assertTrue(stringArguments.isEmpty());
 }

}