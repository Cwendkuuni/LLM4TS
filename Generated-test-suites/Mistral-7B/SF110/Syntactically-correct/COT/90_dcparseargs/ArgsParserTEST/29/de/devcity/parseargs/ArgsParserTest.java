package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.*;

import org.junit.Test;

import de.devcity.parseargs.*;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private String[] args = new String[]{"arg1", "-a", "val1", "--longarg", "val2", "--longarg2", "val3", ""};
 private ArgsParser parser;

 @Test
 public void testGetArgsCount() {
 ArgsParser argsParser = new ArgsParser(args);
 assertEquals(args.length, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 ArgsParser argsParser = new ArgsParser(args);
 assertArrayEquals(args, argsParser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 ArgsParser argsParser = new ArgsParser();
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 ArgsParser argsParser = new ArgsParser(args);
 assertEquals(args.length - 3, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 ArgsParser argsParser = new ArgsParser(args);
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("-a");
 assertEquals(-1, switchArgument.getIndex());
 assertEquals("a", switchArgument.getKey());
 assertEquals(false, switchArgument.isFound());

 switchArgument = argsParser.parseSwitchArgument("--longarg");
 assertEquals(1, switchArgument.getIndex());
 assertEquals("longarg", switchArgument.getKey());
 assertEquals(true, switchArgument.isFound());
 }

 @Test
 public void testParseSwitchArgumentNotFound() {
 ArgsParser argsParser = new ArgsParser(args);
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("--notfound");
 assertEquals(-1, switchArgument.getIndex());
 assertEquals("notfound", switchArgument.getKey());
 assertEquals(false, switchArgument.isFound());
 }

 @Test
 public void testParseParameterArgument() {
 ArgsParser argsParser = new ArgsParser(args);
 ParameterArgument paramArgument = argsParser.parseParameterArgument("-a");
 assertEquals(-1, paramArgument.getIndex());
 assertNull(paramArgument.getValue());

 paramArgument = argsParser.parseParameterArgument("--longarg=val1");
 assertEquals(3, paramArgument.getIndex());
 assertEquals("val1", paramArgument.getValue());
 }

 @Test
 public void testParseParameterArgumentNotFound() {
 ArgsParser argsParser = new ArgsParser(args);
 ParameterArgument paramArgument = argsParser.parseParameterArgument("--notfound");
 assertEquals(-1, paramArgument.getIndex());
 assertNull(paramArgument.getValue());
 }

 @Test
 public void testParseStringArgument() {
 ArgsParser argsParser = new ArgsParser(args);
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(3, stringArguments.size());
 StringArgument stringArgument = stringArguments.get(0);
 assertEquals(2, stringArgument.getIndex());
 assertEquals("val1", stringArgument.getValue());
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseStringArgumentEmpty() {
 ArgsParser argsParser = new ArgsParser(new String[0]);
 argsParser.parseStringArgument();
 }

 @Test
 public void testParseStringArgumentMissingValue() {
 ArgsParser argsParser = new ArgsParser(new String[]{"arg1", "-a", "--longarg", ""});
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(2, stringArguments.size());
 StringArgument stringArgument = stringArguments.get(1);
 assertEquals(3, stringArgument.getIndex());
 assertEquals(null, stringArgument.getValue());
 }

}