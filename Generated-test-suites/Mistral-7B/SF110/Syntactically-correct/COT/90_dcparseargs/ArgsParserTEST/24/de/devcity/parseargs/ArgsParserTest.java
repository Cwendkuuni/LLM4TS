package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.StringArgument;

public class ArgsParserTest {

 private ArgsParser parser;

 @Before
 public void setup() {
 this.parser = new ArgsParser(new String[]{"arg1", "--switchArg", "arg2", "--longSwitchArg=value", "--param=value"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(5, parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(new String[]{"arg1", "--switchArg", "arg2", "--longSwitchArg=value", "--param=value"}, parser.getArgs());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArg1 = parser.parseSwitchArgument("switchArg");
 assertEquals(1, switchArg1.getIndex());
 assertEquals("switchArg", switchArg1.getKey());
 assertTrue(switchArg1.isLong());

 SwitchArgument switchArg2 = parser.parseSwitchArgument("longSwitchArg");
 assertEquals(3, switchArg2.getIndex());
 assertEquals("longSwitchArg", switchArg2.getKey());
 assertTrue(switchArg2.isLong());

 SwitchArgument switchArg3 = parser.parseSwitchArgument("nonExistent");
 assertEquals(-1, switchArg3.getIndex());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument paramArg1 = parser.parseParameterArgument("param");
 assertEquals(4, paramArg1.getIndex());
 assertEquals("param", paramArg1.getKey());
 assertEquals("value", paramArg1.getValue());

 ParameterArgument paramArg2 = parser.parseParameterArgument("nonExistent");
 assertEquals(-1, paramArg2.getIndex());
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArgs = parser.parseStringArgument();
 assertEquals(3, stringArgs.size());
 assertEquals("arg1", stringArgs.get(0).getValue());
 assertEquals("--longSwitchArg=value", stringArgs.get(1).getValue());
 assertEquals("arg2", stringArgs.get(2).getValue());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 parser = new ArgsParser(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(3, parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgumentWithLongKeyAndIndexMismatch() {
 String[] args = new String[]{"arg1", "--param=value", "--switchArg", "--longSwitchArg=value"};
 ArgsParser parser2 = new ArgsParser(args);
 SwitchArgument switchArg = parser2.parseSwitchArgument("longParam");
 assertEquals(-1, switchArg.getIndex());
 }

 @Test
 public void testParseParameterArgumentWithValueMissing() {
 String[] args = new String[]{"arg1", "--param"};
 ArgsParser parser2 = new ArgsParser(args);
 ParameterArgument paramArg = parser2.parseParameterArgument("param");
 assertNull(paramArg);
 }
}