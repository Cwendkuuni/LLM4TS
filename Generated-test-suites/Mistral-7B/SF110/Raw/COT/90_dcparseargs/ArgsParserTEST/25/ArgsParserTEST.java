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
 public void setUp() {
 argsParser = new ArgsParser();
 args = new String[]{"-a", "--long-arg", "1", "--switch", "value", "-b", "--long-arg2", "2"};
 argsParser.setArgs(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(args.length, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(args, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsWithNull() {
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(5, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgumentWithShortKey() {
 final int index = argsParser.parseSwitchArgument("a").getIndex();
 assertEquals(0, index);
 }

 @Test
 public void testParseSwitchArgumentWithLongKey() {
 final int index = argsParser.parseSwitchArgument("long-arg").getIndex();
 assertEquals(1, index);
 }

 @Test
 public void testParseSwitchArgumentWhenKeyNotFound() {
 final SwitchArgument switchArgument = argsParser.parseSwitchArgument("notExist");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isPresent());
 }

 @Test
 public void testParseParameterArgumentWithShortKey() {
 final int index = argsParser.parseParameterArgument("a").getIndex();
 assertEquals(0, index);
 }

 @Test
 public void testParseParameterArgumentWithLongKey() {
 final int index = argsParser.parseParameterArgument("long-arg").getIndex();
 assertEquals(1, index);
 }

 @Test
 public void testParseParameterArgumentWhenKeyNotFound() {
 final ParameterArgument parameterArgument = argsParser.parseParameterArgument("notExist");
 assertEquals(-1, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentWithValue() {
 final int index = argsParser.parseParameterArgument("a").getIndex();
 final String value = argsParser.parseParameterArgument("a").getValue();
 assertEquals(index, 0);
 assertEquals("1", value);
 }

 @Test
 public void testParseParameterArgumentWithMultipleValues() {
 final int index = argsParser.parseParameterArgument("long-arg2").getIndex();
 final String value = argsParser.parseParameterArgument("long-arg2").getValue();
 assertEquals(index, 6);
 assertEquals("2", value);
 }

 @Test
 public void testParseStringArgument() {
 final List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(5, stringArguments.size());
 for (int i = 0; i < stringArguments.size(); ++i) {
 StringArgument stringArgument = stringArguments.get(i);
 assertEquals(args[stringArgument.getIndex()], stringArgument.getValue());
 }
 }

 @Test(expected = ArrayIndexOutOfBoundsException.class)
 public void testParseSwitchArgumentWhenOutOfBounds() {
 argsParser.parseSwitchArgument("notExist");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseParameterArgumentWhenOutOfBounds() {
 argsParser.parseParameterArgument("notExist");
 }

 @Test
 public void testParseStringArgumentWhenOutOfArgs() {
 argsParser.setArgs(new String[0]);
 argsParser.parseStringArgument();
 assertTrue(argsParser.getArgsLeftCount() > 0);
 }
}