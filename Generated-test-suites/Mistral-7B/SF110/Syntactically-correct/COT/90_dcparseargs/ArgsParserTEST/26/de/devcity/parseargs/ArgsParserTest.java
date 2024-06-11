package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private String[] testArgs = new String[]{"arg1", "-a", "--longArg", "arg3", "--longArg2", "-b", "arg6", "--longArg3"};
 private ArgsParser argsParser;

 @Before
 public void setUp() {
 argsParser = new ArgsParser(testArgs);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(argsParser.getArgsCount(), testArgs.length);
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(argsParser.getArgs(), testArgs);
 }

 @Test
 public void testSetArgs() {
 String[] testArgs2 = new String[]{"arg1", "-a", "--longArg", "arg3", "--longArg2", null, "--longArg3"};
 argsParser.setArgs(testArgs2);
 assertEquals(argsParser.getArgsCount(), testArgs2.length);
 assertNull(argsParser.getArgs()[argsParser.getArgs().length - 1]);
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(argsParser.getArgsLeftCount(), 2);
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArg = argsParser.parseSwitchArgument("-a");
 assertEquals(switchArg.getIndex(), 1);
 assertEquals(switchArg.getKey(), "a");
 assertTrue(switchArg.isLong());

 switchArg = argsParser.parseSwitchArgument("--longArg");
 assertEquals(switchArg.getIndex(), 3);
 assertEquals(switchArg.getKey(), "longArg");
 assertTrue(switchArg.isLong());

 switchArg = argsParser.parseSwitchArgument("--nonExistentArgument");
 assertEquals(switchArg.getIndex(), -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseSwitchArgumentNullKey() {
 argsParser.parseSwitchArgument(null);
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument paramArg = argsParser.parseParameterArgument("a");
 assertEquals(paramArg.getIndex(), 1);
 assertEquals(paramArg.getKey(), "a");
 assertNull(paramArg.getValue());

 paramArg = argsParser.parseParameterArgument("--longArg2");
 assertEquals(paramArg.getIndex(), 5);
 assertEquals(paramArg.getKey(), "longArg2");
 assertEquals(paramArg.getValue(), "");

 paramArg = argsParser.parseParameterArgument("--longArg3");
 assertEquals(paramArg.getIndex(), 7);
 assertEquals(paramArg.getKey(), "longArg3");
 assertEquals(paramArg.getValue(), "");

 paramArg = argsParser.parseParameterArgument("--nonExistentArgument");
 assertEquals(paramArg.getIndex(), -1);
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentNullKey() {
 argsParser.parseParameterArgument(null);
 }

 @Test
 public void testParseParameterArgumentWithValue() {
 argsParser = new ArgsParser(new String[]{"arg1", "--argValue", "arg3", "--longArg2=value2", "arg4", "--longArg3=value3"});
 ParameterArgument paramArg = argsParser.parseParameterArgument("argValue");
 assertEquals(paramArg.getIndex(), 1);
 assertEquals(paramArg.getKey(), "argValue");
 assertEquals(paramArg.getValue(), "value2");
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseParameterArgumentOutOfBounds() {
 argsParser = new ArgsParser(new String[]{"arg1", "--argValue", "arg3", "--longArg2=value2"});
 argsParser.parseParameterArgument("arg5");
 }

 @Test
 public void testParseStringArguments() {
 List<StringArgument> stringArgs = argsParser.parseStringArgument();
 assertEquals(stringArgs.size(), 6);
 assertEquals(stringArgs.get(0).getIndex(), 0);
 assertEquals(stringArgs.get(0).getValue(), "arg1");
 assertEquals(stringArgs.get(1).getIndex(), 1);
 assertEquals(stringArgs.get(1).getValue(), "");
 assertEquals(stringArgs.get(2).getIndex(), 3);
 assertEquals(stringArgs.get(2).getValue(), "");
 assertEquals(stringArgs.get(3).getIndex(), 4);
 assertEquals(stringArgs.get(3).getValue(), "arg4");
 assertEquals(stringArgs.get(4).getIndex(), 5);
 assertEquals(stringArgs.get(4).getValue(), "");
 assertEquals(stringArgs.get(5).getIndex(), 6);
 assertEquals(stringArgs.get(5).getValue(), "");
 }

 @Test
 public void testParseStringArgumentsNullValue() {
 List<StringArgument> stringArgs = argsParser.parseStringArgument();
 assertEquals(stringArgs.size(), 6);
 assertEquals(stringArgs.get(0).getIndex(), 0);
 assertEquals(stringArgs.get(0).getValue(), "arg1");
 assertEquals(stringArgs.get(1).getIndex(), 1);
 assertEquals(stringArgs.get(1).getValue(), null);
 assertEquals(stringArgs.get(2).getIndex(), 3);
 assertEquals(stringArgs.get(2).getValue(), null);
 assertEquals(stringArgs.get(3).getIndex(), 4);
 assertEquals(stringArgs.get(3).getValue(), "arg4");
 assertEquals(stringArgs.get(4).getIndex(), 5);
 assertEquals(stringArgs.get(4).getValue(), null);
 assertEquals(stringArgs.get(5).getIndex(), 6);
 assertEquals(stringArgs.get(5).getValue(), null);
 }

}