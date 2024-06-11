package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private String[] testArgs;
 private ArgsParser argsParser;

 @Before
 public void setUp() {
 testArgs = new String[]{"-a", "--parameter=value", "-b", "--switch", "--long-parameter=anotherValue", "test"};
 argsParser = new ArgsParser(testArgs);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(testArgs.length, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(testArgs, argsParser.getArgs());
 }

 @Test
 public void testSetArgs() {
 String[] tempArgs = new String[]{"temp1", "temp2", "temp3"};
 argsParser.setArgs(tempArgs);
 assertArrayEquals(tempArgs, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 int expected = 3;
 assertEquals(expected, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("--switch");
 assertTrue(switchArgument.isPresent());
 assertEquals(1, switchArgument.getIndex());

 switchArgument = argsParser.parseSwitchArgument("--long-parameter");
 assertTrue(switchArgument.isPresent());
 assertEquals(4, switchArgument.getIndex());

 switchArgument = argsParser.parseSwitchArgument("--not-present");
 assertFalse(switchArgument.isPresent());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("--parameter");
 assertTrue(parameterArgument.isPresent());
 assertEquals("value", parameterArgument.getValue());
 assertEquals(1, parameterArgument.getIndex());

 parameterArgument = argsParser.parseParameterArgument("--long-parameter");
 assertTrue(parameterArgument.isPresent());
 assertEquals("anotherValue", parameterArgument.getValue());
 assertEquals(4, parameterArgument.getIndex());

 parameterArgument = argsParser.parseParameterArgument("--not-present");
 assertFalse(parameterArgument.isPresent());
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(2, stringArguments.size());
 StringArgument stringArgument1 = stringArguments.get(0);
 assertEquals("a", stringArgument1.getValue());
 assertEquals(0, stringArgument1.getIndex());

 StringArgument stringArgument2 = stringArguments.get(1);
 assertEquals("test", stringArgument2.getValue());
 assertEquals(5, stringArgument2.getIndex());
 }
}