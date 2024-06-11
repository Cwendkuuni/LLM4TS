package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private String[] testArgs = new String[]{"-a", "--test", "-b=123", "--long-arg-key", "arg5", "--long-arg-key=value", "--switch"};

 @Test
 public void testConstructor() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 assertEquals(testArgs.length, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 assertArrayEquals(testArgs, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 ArgsParser argsParser = new ArgsParser();
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 assertEquals(5, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 ArgsParser argsParser = new ArgsParser(testArgs);

 SwitchArgument switchArg1 = argsParser.parseSwitchArgument("a");
 assertEquals(0, switchArg1.getIndex());
 assertTrue(switchArg1.isSwitchedOn());

 SwitchArgument switchArg2 = argsParser.parseSwitchArgument("test");
 assertEquals(1, switchArg2.getIndex());
 assertTrue(switchArg2.isSwitchedOn());

 SwitchArgument switchArg3 = argsParser.parseSwitchArgument("switch");
 assertEquals(6, switchArg3.getIndex());
 assertTrue(switchArg3.isSwitchedOn());

 SwitchArgument switchArg4 = argsParser.parseSwitchArgument("not_present");
 assertEquals(-1, switchArg4.getIndex());
 }

 @Test
 public void testParseParameterArgument() {
 ArgsParser argsParser = new ArgsParser(testArgs);

 ParameterArgument paramArg1 = argsParser.parseParameterArgument("b");
 assertEquals(2, paramArg1.getIndex());
 assertEquals("123", paramArg1.getValue());

 ParameterArgument paramArg2 = argsParser.parseParameterArgument("longArgKey");
 assertEquals(3, paramArg2.getIndex());
 assertEquals("value", paramArg2.getValue());

 ParameterArgument paramArg3 = argsParser.parseParameterArgument("not_present");
 assertEquals(-1, paramArg3.getIndex());
 }

 @Test
 public void testParseStringArguments() {
 ArgsParser argsParser = new ArgsParser(testArgs);
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 List<StringArgument> expectedStringArguments = new ArrayList<>();
 expectedStringArguments.add(new StringArgument(4, "arg5"));
 expectedStringArguments.add(new StringArgument(7, "value"));
 assertEquals(expectedStringArguments, stringArguments);
 }

}