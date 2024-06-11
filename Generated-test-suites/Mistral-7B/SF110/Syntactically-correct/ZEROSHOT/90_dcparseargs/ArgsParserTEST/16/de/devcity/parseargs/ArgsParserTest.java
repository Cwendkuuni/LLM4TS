package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.Arguments;
import de.devcity.parseargs.args.*;
import de.devcity.parseargs.ArgsParser;

public class ArgsParserTest {

 private String[] testArgs = new String[]{"-a", "--arg1=test", "-b", "--arg2=test2", "arg3", "--arg3=test3"};

 @Test
 public void testArgsParser() {
 ArgsParser parser = new ArgsParser(testArgs);
 ArgsParser parserNull = new ArgsParser(null);

 assertEquals(5, parser.getArgsCount());
 assertEquals(5, parser.getArgsLeftCount());

 try {
 new ArgsParser(new String[0]);
 fail("Should not be able to create ArgsParser with empty args array.");
 } catch (NullPointerException e) {
 // expected
 }

 try {
 new ArgsParser(null);
 fail("Should not be able to create ArgsParser with null args array.");
 } catch (NullPointerException e) {
 // expected
 }

 // test getArgs()
 String[] args = parser.getArgs();
 assertEquals(5, args.length);
 for (int i = 0; i < args.length; ++i) {
 assertEquals(testArgs[i], args[i]);
 }

 // test getArgsLeftCount()
 assertEquals(0, parser.getArgsLeftCount());

 // test parseSwitchArgument()
 SwitchArgument switchArgument = parser.parseSwitchArgument("-a");
 assertEquals(0, switchArgument.getIndex());
 assertFalse(switchArgument.isWithValue());

 switchArgument = parser.parseSwitchArgument("--arg1");
 assertEquals(1, switchArgument.getIndex());
 assertTrue(switchArgument.isWithValue());

 switchArgument = parser.parseSwitchArgument("--arg5");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isWithValue());

 // test parseParameterArgument()
 ParameterArgument parameterArgument = parser.parseParameterArgument("arg3");
 assertEquals(-1, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());

 parameterArgument = parser.parseParameterArgument("--arg3");
 assertEquals(4, parameterArgument.getIndex());
 assertEquals("test3", parameterArgument.getValue());

 parameterArgument = parser.parseParameterArgument("--arg4");
 assertEquals(-1, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());

 // test parseStringArgument()
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(2, stringArguments.size());

 StringArgument stringArgument1 = stringArguments.get(0);
 assertEquals(2, stringArgument1.getIndex());
 assertEquals("test", stringArgument1.getValue());

 StringArgument stringArgument2 = stringArguments.get(1);
 assertEquals(3, stringArgument2.getIndex());
 assertEquals("test2", stringArgument2.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseSwitchOrParameterArgumentWithWrongIndex() {
 ArgsParser parser = new ArgsParser(testArgs);
 parser.parseParameterArgument("--non_existent_arg");
 }

 @Test
 public void testParseStringArgumentWithEmptyList() {
 ArgsParser parser = new ArgsParser(new String[0]);
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(0, stringArguments.size());
 }

 @Test
 public void testParseStringArgumentWithEmptyArgument() {
 ArgsParser parser = new ArgsParser(new String[]{"", "--arg1=test"});
 List<StringArgument> stringArguments = parser.parseStringArgument();
 assertEquals(1, stringArguments.size());
 assertEquals("", stringArguments.get(0).getValue());
 }

 @Test
 public void testParseSwitchArgumentWithLongKey() {
 ArgsParser parser = new ArgsParser(new String[]{"--longKey"});
 SwitchArgument switchArgument = parser.parseSwitchArgument("--longKey");
 assertEquals(0, switchArgument.getIndex());
 assertTrue(switchArgument.isWithValue());
 }

 @Test
 public void testParseSwitchArgumentWithShortKey() {
 ArgsParser parser = new ArgsParser(new String[]{"-a", "--arg1=test"});
 SwitchArgument switchArgument = parser.parseSwitchArgument("-a");
 assertEquals(0, switchArgument.getIndex());
 assertFalse(switchArgument.isWithValue());
 }

 @Test
 public void testParseSwitchArgumentWithWrongShortKey() {
 ArgsParser parser = new ArgsParser(new String[]{"-a", "--arg1=test"});
 SwitchArgument switchArgument = parser.parseSwitchArgument("-b");
 assertEquals(-1, switchArgument.getIndex());
 assertFalse(switchArgument.isWithValue());
 }

 @Test
 public void testParseParameterArgumentWithLongKey() {
 ArgsParser parser = new ArgsParser(new String[]{"--arg1=test1", "--arg2=test2"});
 ParameterArgument parameterArgument = parser.parseParameterArgument("--arg1");
 assertEquals(0, parameterArgument.getIndex());
 assertEquals("test1", parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentWithShortKey() {
 ArgsParser parser = new ArgsParser(new String[]{"-a=test1", "-b=test2"});
 ParameterArgument parameterArgument = parser.parseParameterArgument("-a");
 assertEquals(0, parameterArgument.getIndex());
 assertEquals("test1", parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentWithWrongShortKey() {
 ArgsParser parser = new ArgsParser(new String[]{"-a=test1", "-b=test2"});
 ParameterArgument parameterArgument = parser.parseParameterArgument("-c");
 assertEquals(-1, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentWithMissingValue() {
 ArgsParser parser = new ArgsParser(new String[]{"--arg1", "--arg2"});
 ParameterArgument parameterArgument = parser.parseParameterArgument("--arg1");
 assertEquals(0, parameterArgument.getIndex());
 assertNull(parameterArgument.getValue());
 }
}