package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;

 @Before
 public void setup() {
 argsParser = new ArgsParser(new String[]{"arg1", "-a", "arg3", "-b=value", "--argC"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(4, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 String[] expected = new String[]{"arg1", "-a", "arg3", "-b=value", "--argC"};
 assertArrayEquals(expected, argsParser.getArgs());
 }

 @Test
 public void testSetArgs() {
 argsParser = new ArgsParser(null);
 argsParser.setArgs(new String[]{"arg1", "-a", "arg3", "-b=value", "--argC"});
 String[] expected2 = new String[]{"arg1", "-a", "arg3", "-b=value", "--argC"};
 assertArrayEquals(expected2, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(3, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("-a");
 assertEquals(1, switchArgument.getIndex());
 assertEquals("-a", switchArgument.getKey());
 assertTrue(switchArgument.isLong());

 switchArgument = argsParser.parseSwitchArgument("--argC");
 assertEquals(4, switchArgument.getIndex());
 assertEquals("--argC", switchArgument.getKey());
 assertTrue(switchArgument.isLong());

 switchArgument = argsParser.parseSwitchArgument("--nonExistingArg");
 assertEquals(-1, switchArgument.getIndex());
 assertEquals("nonExistingArg", switchArgument.getKey());
 assertFalse(switchArgument.isLong());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("-a");
 assertEquals(1, parameterArgument.getIndex());
 assertEquals("-a", parameterArgument.getKey());
 assertNull(parameterArgument.getValue());

 parameterArgument = argsParser.parseParameterArgument("--b=value");
 assertEquals(3, parameterArgument.getIndex());
 assertEquals("--b", parameterArgument.getKey());
 assertEquals("value", parameterArgument.getValue());

 parameterArgument = argsParser.parseParameterArgument("--nonExistingArg");
 assertEquals(-1, parameterArgument.getIndex());
 assertNull(parameterArgument);
 }

 @Test
 public void testParseStringArguments() {
 List<StringArgument> expected = new ArrayList<>();
 expected.add(new StringArgument(1, "arg1"));
 expected.add(new StringArgument(2, "-a"));
 expected.add(new StringArgument(3, "arg3"));
 expected.add(new StringArgument(4, "--argC"));
 assertEquals(expected, argsParser.parseStringArgument());
 }

 @Test
 public void testParseStringArgumentsNoArguments() {
 List<StringArgument> expected = new ArrayList<>();
 assertEquals(expected, argsParser.parseStringArgument());
 }

 @Test
 public void testParseStringArgumentsWithOnlySwitchArguments() {
 argsParser = new ArgsParser(new String[]{"--argA", "--argB"});
 List<StringArgument> expected = new ArrayList<>();
 assertEquals(expected, argsParser.parseStringArgument());
 }

 @Test
 public void testParseStringArgumentsWithOnlyParameterArguments() {
 argsParser = new ArgsParser(new String[]{"-a=value1", "-b=value2"});
 List<StringArgument> expected = new ArrayList<>();
 expected.add(new StringArgument(1, "-a=value1"));
 expected.add(new StringArgument(2, "-b=value2"));
 assertEquals(expected, argsParser.parseStringArgument());
 }

 @Test
 public void testParseStringArgumentsWithMixedArguments() {
 argsParser = new ArgsParser(new String[]{"arg1", "-a", "arg3", "-b=value", "--argC", "--argD=value2"});
 List<StringArgument> expected = new ArrayList<>();
 expected.add(new StringArgument(0, "arg1"));
 expected.add(new StringArgument(2, "-a"));
 expected.add(new StringArgument(4, "--argC"));
 expected.add(new StringArgument(5, "--argD=value2"));
 expected.add(new StringArgument(3, "arg3"));
 expected.add(new StringArgument(1, "-b=value"));
 assertEquals(expected, argsParser.parseStringArgument());
 }
}