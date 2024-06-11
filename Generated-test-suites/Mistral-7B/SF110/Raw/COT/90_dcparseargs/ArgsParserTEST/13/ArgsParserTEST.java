package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;
 private String[] args;

 @Before
 public void init() {
 argsParser = new ArgsParser();
 args = new String[]{"arg1", "--key1", "value1", "--key2=value2", "--key3", "-key4=value4"};
 argsParser.setArgs(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(6, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(new String[]{"arg1", "--key1", "value1", "--key2=value2", "--key3", "-key4=value4"}, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(4, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgumentShort() {
 SwitchArgument switchArg = argsParser.parseSwitchArgument("k");
 assertEquals(-1, switchArg.getIndex());
 assertFalse(switchArg.isFound());
 }

 @Test
 public void testParseSwitchArgumentLong() {
 SwitchArgument switchArg = argsParser.parseSwitchArgument("--key");
 assertEquals(2, switchArg.getIndex());
 assertTrue(switchArg.isFound());
 }

 @Test
 public void testParseSwitchArgumentNonExisting() {
 SwitchArgument switchArg = argsParser.parseSwitchArgument("--nonExisting");
 assertEquals(-1, switchArg.getIndex());
 assertFalse(switchArg.isFound());
 }

 @Test
 public void testParseParameterArgumentShort() {
 ParameterArgument paramArg = argsParser.parseParameterArgument("k");
 assertEquals(-1, paramArg.getIndex());
 assertNull(paramArg.getValue());
 }

 @Test
 public void testParseParameterArgumentLong() {
 ParameterArgument paramArg = argsParser.parseParameterArgument("--key=value");
 assertEquals(3, paramArg.getIndex());
 assertEquals("value", paramArg.getValue());
 }

 @Test
 public void testParseParameterArgumentNonExisting() {
 ParameterArgument paramArg = argsParser.parseParameterArgument("--nonExisting");
 assertEquals(-1, paramArg.getIndex());
 assertNull(paramArg.getValue());
 }

 @Test
 public void testParseParameterArgumentValueEmpty() {
 ParameterArgument paramArg = argsParser.parseParameterArgument("--key=");
 assertEquals(3, paramArg.getIndex());
 assertNull(paramArg.getValue());
 }

 @Test
 public void testParseParameterArgumentValueMultiple() {
 ParameterArgument paramArg = argsParser.parseParameterArgument("--key=value1 value2");
 assertEquals(3, paramArg.getIndex());
 assertEquals("value1 value2", paramArg.getValue());
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArgs = argsParser.parseStringArgument();
 assertEquals(4, stringArgs.size());
 assertEquals("arg1", stringArgs.get(0).getValue());
 assertEquals("value1", stringArgs.get(1).getValue());
 assertEquals("value2", stringArgs.get(2).getValue());
 assertEquals("value4", stringArgs.get(3).getValue());
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseStringArgumentOutOfBounds() {
 argsParser = new ArgsParser(new String[0]);
 List<StringArgument> stringArgs = argsParser.parseStringArgument();
}

}