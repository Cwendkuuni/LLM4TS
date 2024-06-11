package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private String[] testArgs = new String[]{"-a", "--long-argument", "-b=1", "--long-argument2=test", "--switch"};

 @Test
 public void testSetArgs() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 ArgsParser expectedArgsParser = new ArgsParser();
 expectedArgsParser.setArgs(testArgs);
 assertEquals(expectedArgsParser, argsParser);
 }

 @Test
 public void testGetArgsCount() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 assertEquals(testArgs.length, argsParser.getArgsCount());
 }

 @Test
 public void testParseSwitchArgument() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 SwitchArgument switchArgument1 = argsParser.parseSwitchArgument("-a");
 assertEquals(-1, switchArgument1.getIndex());
 assertEquals("-a", switchArgument1.getKey());
 assertTrue(switchArgument1.isPresent());

 SwitchArgument switchArgument2 = argsParser.parseSwitchArgument("--long-argument");
 assertEquals(0, switchArgument2.getIndex());
 assertEquals("--long-argument", switchArgument2.getKey());
 assertTrue(switchArgument2.isPresent());

 SwitchArgument switchArgument3 = argsParser.parseSwitchArgument("--switch");
 assertEquals(5, switchArgument3.getIndex());
 assertEquals("--switch", switchArgument3.getKey());
 assertTrue(switchArgument3.isPresent());

 SwitchArgument switchArgument4 = argsParser.parseSwitchArgument("--non-exist");
 assertEquals(-1, switchArgument4.getIndex());
 assertEquals("--non-exist", switchArgument4.getKey());
 assertFalse(switchArgument4.isPresent());
 }

 @Test
 public void testParseParameterArgument() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 ParameterArgument parameterArgument1 = argsParser.parseParameterArgument("--long-argument");
 assertEquals(-1, parameterArgument1.getIndex());
 assertEquals("--long-argument", parameterArgument1.getKey());
 assertNull(parameterArgument1.getValue());

 ParameterArgument parameterArgument2 = argsParser.parseParameterArgument("--long-argument2");
 assertEquals(1, parameterArgument2.getIndex());
 assertEquals("--long-argument2", parameterArgument2.getKey());
 assertEquals("test", parameterArgument2.getValue());

 ParameterArgument parameterArgument3 = argsParser.parseParameterArgument("--non-exist");
 assertEquals(-1, parameterArgument3.getIndex());
 assertEquals("--non-exist", parameterArgument3.getKey());
 assertNull(parameterArgument3.getValue());

 ArgsParser argsParser2 = new ArgsParser(new String[]{"--long-argument", "value"});
 ParameterArgument parameterArgument4 = argsParser2.parseParameterArgument("--long-argument");
 assertEquals(0, parameterArgument4.getIndex());
 assertEquals("--long-argument", parameterArgument4.getKey());
 assertEquals("value", parameterArgument4.getValue());
 }

 @Test
 public void testParseStringArgument() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 List<StringArgument> expectedStringArguments = new ArrayList<>();
 expectedStringArguments.add(new StringArgument(2, "--long-argument2=test"));
 expectedStringArguments.add(new StringArgument(4, "--switch"));
 assertEquals(expectedStringArguments, stringArguments);
 }

 @Test(expected = NullPointerException.class)
 public void testParseSwitchArgumentNullArgs() {
 ArgsParser argsParser = new ArgsParser(null);
 }
}