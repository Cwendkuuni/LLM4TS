package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.Arguments.SwitchArgument;
import de.devcity.parseargs.Arguments.StringArgument;
import de.devcity.parseargs.Arguments.ParameterArgument;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest {

 private String[] args = new String[]{"-a", "--long-arg", "1", "--switch", "-b", "2", "--long-arg2=test", "--switch2"};
 private ArgsParser parser;

 @Before
 public void setUp() {
 parser = new ArgsParser(args);
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(args.length, parser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 assertArrayEquals(args, parser.getArgs());
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(3, parser.getArgsLeftCount());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 parser.setArgs(null);
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArg1 = parser.parseSwitchArgument("-switch");
 assertEquals(-1, switchArg1.getIndex());
 assertFalse(switchArg1.isSet());

 SwitchArgument switchArg2 = parser.parseSwitchArgument("--switch2");
 assertEquals(5, switchArg2.getIndex());
 assertTrue(switchArg2.isSet());
 }

 @Test
 public void testParseSwitchArgumentNotFound() {
 SwitchArgument switchArg = parser.parseSwitchArgument("--notfound");
 assertEquals(-1, switchArg.getIndex());
 assertFalse(switchArg.isSet());
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument paramArg1 = parser.parseParameterArgument("---long-arg");
 assertEquals(-1, paramArg1.getIndex());
 assertNull(paramArg1.getValue());

 ParameterArgument paramArg2 = parser.parseParameterArgument("--long-arg=value");
 assertEquals(1, paramArg2.getIndex());
 assertEquals("value", paramArg2.getValue());

 ParameterArgument paramArg3 = parser.parseParameterArgument("--long-arg2");
 assertEquals(3, paramArg3.getIndex());
 assertEquals(null, paramArg3.getValue());
 }

 @Test
 public void testParseParameterArgumentNotFound() {
 ParameterArgument paramArg = parser.parseParameterArgument("--notfound");
 assertEquals(-1, paramArg.getIndex());
 assertNull(paramArg.getValue());
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArgs = parser.parseStringArgument();
 List<StringArgument> expectedArgs = new ArrayList<>();
 expectedArgs.add(new StringArgument(1, "1"));
 expectedArgs.add(new StringArgument(5, "2"));
 expectedArgs.add(new StringArgument(6, "test"));
 assertEquals(expectedArgs, stringArgs);
 }
}