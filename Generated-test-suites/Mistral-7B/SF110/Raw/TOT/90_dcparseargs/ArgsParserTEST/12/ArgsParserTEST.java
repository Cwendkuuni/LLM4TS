package de.devcity.parseargs;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class ArgsParserTest {

 private String[] args = null;
 private ArgsParser parser;

 @Test
 public void testNullArgs() {
 ArgsParser testParser = new ArgsParser(null);
 assertThrows(NullPointerException.class, testParser::getArgs);
 }

 @Test
 public void testEmptyArgs() {
 args = new String[0];
 parser = new ArgsParser(args);
 assertEquals(0, parser.getArgsCount());
 }

 @Test
 public void testParseSwitchArgumentShort() {
 args = new String[] { "-s" };
 parser = new ArgsParser(args);
 SwitchArgument switchArgument = parser.parseSwitchArgument("s");
 assertEquals(0, switchArgument.getIndex());
 assertTrue(switchArgument.isSwitch());
 }

 @Test
 public void testParseSwitchArgumentLong() {
 args = new String[] { "--switch" };
 parser = new ArgsParser(args);
 SwitchArgument switchArgument = parser.parseSwitchArgument("switch");
 assertEquals(0, switchArgument.getIndex());
 assertTrue(switchArgument.isSwitch());
 }

 @Test
 public void testParseSwitchArgumentNotFoundShort() {
 args = new String[] { "-a", "-b" };
 parser = new ArgsParser(args);
 SwitchArgument switchArgument = parser.parseSwitchArgument("c");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isSwitch());
 }

 @Test
 public void testParseSwitchArgumentNotFoundLong() {
 args = new String[] { "--a", "--b" };
 parser = new ArgsParser(args);
 SwitchArgument switchArgument = parser.parseSwitchArgument("c");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isSwitch());
 }

 @Test
 public void testParseParameterArgumentShort() {
 args = new String[] { "-p", "arg" };
 parser = new ArgsParser(args);
 ParameterArgument parameterArgument = parser.parseParameterArgument("p");
 assertEquals(0, parameterArgument.getIndex());
 assertEquals("arg", parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentLong() {
 args = new String[] { "--param", "arg" };
 parser = new ArgsParser(args);
 ParameterArgument parameterArgument = parser.parseParameterArgument("param");
 assertEquals(0, parameterArgument.getIndex());
 assertEquals("arg", parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentNotFoundShort() {
 args = new String[] { "-a", "-b" };
 parser = new ArgsParser(args);
 ParameterArgument parameterArgument = parser.parseParameterArgument("p");
 assertNull(parameterArgument);
 }

 @Test
 public void testParseParameterArgumentNotFoundLong() {
 args = new String[] { "--a", "--b" };
 parser = new ArgsParser(args);
 ParameterArgument parameterArgument = parser.parseParameterArgument("p");
 assertNull(parameterArgument);
 }

 @Test
 public void testParseMultipleSwitchArgumentsShort() {
 args = new String[] { "-a", "-b", "-c" };
 parser = new ArgsParser(args);
 List<SwitchArgument> switchArguments = parser.parseStringArgument();
 assertEquals(3, switchArguments.size());
 assertTrue(switchArguments.get(0).isSwitch());
 assertTrue(switchArguments.get(1).isSwitch());
 assertTrue(switchArguments.get(2).isSwitch());
 }

 @Test
 public void testParseMultipleParameterArgumentsShort() {
 args = new String[] { "-p1", "arg1", "-p2", "arg2" };
 parser = new ArgsParser(args);
 List<ParameterArgument> parameterArguments = parser.parseStringArgument();
 assertEquals(2, parameterArguments.size());
 assertEquals("arg1", parameterArguments.get(0).getValue());
 assertEquals("arg2", parameterArguments.get(1).getValue());
 }

 @Test
 public void testParseMultipleStringArguments() {
 args = new String[] { "-s1", "arg1", "-s2", "arg2" };
 parser = new ArgsParser(args);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(2, stringArguments.size());
 assertEquals("arg1", stringArguments.get(0).getValue());
 assertEquals("arg2", stringArguments.get(1).getValue());
 }

 @Test
 public void testParseStringArgumentEmpty() {
 args = new String[] { "-s" };
 parser = new ArgsParser(args);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(0, stringArguments.size());
 }
}