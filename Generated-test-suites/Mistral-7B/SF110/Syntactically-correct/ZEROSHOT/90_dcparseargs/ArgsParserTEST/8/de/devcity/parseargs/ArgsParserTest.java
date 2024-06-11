package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;

 @Before
 public void setUp() {
 argsParser = new ArgsParser(new String[] {"arg1", "-a", "arg3", "--longArg", "-b=valueB", "--longArg=valueLongArg", "arg4"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(7, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 String[] expected = {"arg1", "-a", "arg3", "--longArg", "-b=valueB", "--longArg=valueLongArg", "arg4"};
 assertArrayEquals(expected, argsParser.getArgs());
 }

 @Test
 public void testParseSwitchArgumentShort() {
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("-a");
 assertEquals(1, switchArgument.getIndex());
 assertEquals("-a", switchArgument.getKey());
 assertTrue(switchArgument.isPresent());
 argsParser.innerArgs[switchArgument.getIndex()] = null;
 }

 @Test
 public void testParseSwitchArgumentLong() {
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("--longArg");
 assertEquals(4, switchArgument.getIndex());
 assertEquals("--longArg", switchArgument.getKey());
 assertTrue(switchArgument.isPresent());
 argsParser.innerArgs[switchArgument.getIndex()] = null;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseSwitchArgumentNonExistent() {
 argsParser.parseSwitchArgument("--nonExistent");
 }

 @Test
 public void testParseParameterArgumentShort() {
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("-a");
 assertEquals(1, parameterArgument.getIndex());
 assertEquals("-a", parameterArgument.getKey());
 assertNotNull(parameterArgument.getValue());
 argsParser.innerArgs[parameterArgument.getIndex()] = null;
 }

 @Test
 public void testParseParameterArgumentShortWithValue() {
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("-b=valueB");
 assertEquals(3, parameterArgument.getIndex());
 assertEquals("-b", parameterArgument.getKey());
 assertEquals("valueB", parameterArgument.getValue());
 argsParser.innerArgs[parameterArgument.getIndex()] = null;
 argsParser.innerArgs[parameterArgument.getIndex() + 1] = null;
 }

 @Test
 public void testParseParameterArgumentLong() {
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("--longArg");
 assertEquals(4, parameterArgument.getIndex());
 assertEquals("--longArg", parameterArgument.getKey());
 assertNull(parameterArgument.getValue());
 argsParser.innerArgs[parameterArgument.getIndex()] = null;
 }

 @Test
 public void testParseParameterArgumentLongWithValue() {
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("--longArg=valueLongArg");
 assertEquals(4, parameterArgument.getIndex());
 assertEquals("--longArg", parameterArgument.getKey());
 assertEquals("valueLongArg", parameterArgument.getValue());
 argsParser.innerArgs[parameterArgument.getIndex()] = null;
 argsParser.innerArgs[parameterArgument.getIndex() + 1] = null;
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentNonExistentShort() {
 argsParser.parseParameterArgument("-c");
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentNonExistentLong() {
 argsParser.parseParameterArgument("--nonExistent");
 }

 @Test
 public void testParseStringArguments() {
 List<StringArgument> expected = new ArrayList<>();
 expected.add(new StringArgument(6, "arg4"));
 argsParser.innerArgs[6] = null;
 assertEquals(expected, argsParser.parseStringArguments());
 }

}