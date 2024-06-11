package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;

 @Before
 public void setup() {
 argsParser = new ArgsParser(new String[]{"arg1", "--key=value", "-key2", "--longKey=anotherValue"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(4, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(new String[]{"arg1", "--key=value", "-key2", "--longKey=anotherValue"}, argsParser.getArgs());
 }

 @Test
 public void testSetArgs() {
 argsParser = new ArgsParser(null);
 argsParser.setArgs(new String[]{"arg1", "--key=value", "-key2", "--longKey=anotherValue"});
 assertEquals(4, argsParser.getArgsCount());
 assertArrayEquals(new String[]{"arg1", "--key=value", "-key2", "--longKey=anotherValue"}, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser = new ArgsParser(null);
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(2, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 Argument argument = argsParser.parseSwitchArgument("key");
 assertTrue(argument instanceof SwitchArgument);
 assertEquals(1, argument.getIndex());
 assertEquals("key", argument.getKey());
 assertTrue(((SwitchArgument) argument).isLong());

 argument = argsParser.parseSwitchArgument("longKey");
 assertTrue(argument instanceof SwitchArgument);
 assertEquals(3, argument.getIndex());
 assertEquals("longKey", argument.getKey());
 assertTrue(((SwitchArgument) argument).isLong());

 argument = argsParser.parseSwitchArgument("notExistingKey");
 assertTrue(argument instanceof SwitchArgument);
 assertEquals(-1, argument.getIndex());
 assertNull(argument.getValue());
 assertFalse(((SwitchArgument) argument).isLong());
 }

 @Test
 public void testParseParameterArgument() {
 Argument argument = argsParser.parseParameterArgument("key");
 assertTrue(argument instanceof ParameterArgument);
 assertEquals(1, argument.getIndex());
 assertEquals("value", argument.getValue());
 assertEquals("key", argument.getKey());

 argument = argsParser.parseParameterArgument("longKey");
 assertTrue(argument instanceof ParameterArgument);
 assertEquals(3, argument.getIndex());
 assertEquals("anotherValue", argument.getValue());
 assertEquals("longKey", argument.getKey());

 argument = argsParser.parseParameterArgument("notExistingKey");
 assertNull(argument);

 argument = argsParser.parseParameterArgument("anotherKey");
 assertNull(argument);
 }

 @Test
 public void testParseStringArguments() {
 List<StringArgument> arguments = argsParser.parseStringArgument();
 assertEquals(2, arguments.size());
 assertEquals("value", arguments.get(0).getValue());
 assertEquals("anotherValue", arguments.get(1).getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseStringArgumentsEmptyList() {
 argsParser = new ArgsParser(new String[0]);
 argsParser.parseStringArgument();
 }
}