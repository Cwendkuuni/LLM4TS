package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.Arguments;
import de.devcity.parseargs.argsparser.ArgsParser;
import de.devcity.parseargs.arguments.SwitchArgument;
import de.devcity.parseargs.arguments.ParameterArgument;
import de.devcity.parseargs.arguments.StringArgument;

public class ArgsParserTest {

 private String[] args;
 private ArgsParser parser;

 @Test
 public void testArgsParser_EmptyArgs() {
 args = new String[0];
 parser = new ArgsParser(args);
 assertEquals(0, parser.getArgsCount());
 }

 @Test
 public void testArgsParser_ValidArgs() {
 args = new String[]{"arg1", "--key1", "--key2=value2", "-k3", "--key4=value4"};
 parser = new ArgsParser(args);
 assertEquals(args.length, parser.getArgsCount());
 assertEquals(args.clone(), parser.getArgs());
 }

 @Test(expected = NullPointerException.class)
 public void testArgsParser_NullArgs() {
 args = null;
 parser = new ArgsParser();
 parser.setArgs(args);
 fail("Expected NullPointerException");
 }

 @Test
 public void testGetArgsLeftCount() {
 args = new String[]{"arg1", "--key1", "--key2", "--key3", null, "--key4", null, "--key5"};
 parser = new ArgsParser(args);
 assertEquals(5, parser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument_ValidShortKey() {
 args = new String[]{"--switchKey1"};
 parser = new ArgsParser(args);
 SwitchArgument result = parser.parseSwitchArgument("switchKey1");
 assertEquals(0, result.getIndex());
 assertEquals("switchKey1", result.getKey());
 assertTrue(result.isPresent());
 }

 @Test
 public void testParseSwitchArgument_ValidLongKey() {
 args = new String[]{"--key1", "--key2"};
 parser = new ArgsParser(args);
 SwitchArgument result = parser.parseSwitchArgument("key2");
 assertEquals(1, result.getIndex());
 assertEquals("key2", result.getKey());
 assertTrue(result.isPresent());
 }

 @Test
 public void testParseSwitchArgument_InvalidShortKey() {
 args = new String[]{"--key1", "--key2"};
 parser = new ArgsParser(args);
 SwitchArgument result = parser.parseSwitchArgument("key3");
 assertEquals(-1, result.getIndex());
 assertFalse(result.isPresent());
 }

 @Test
 public void testParseSwitchArgument_InvalidLongKey() {
 args = new String[]{"--key1", "--key2"};
 parser = new ArgsParser(args);
 SwitchArgument result = parser.parseSwitchArgument("key3");
 assertEquals(-1, result.getIndex());
 assertFalse(result.isPresent());
 }

 @Test
 public void testParseParameterArgument_ValidShortKey() {
 args = new String[]{"--key1=value1", "--key2=value2"};
 parser = new ArgsParser(args);
 ParameterArgument result = parser.parseParameterArgument("key1");
 assertEquals(0, result.getIndex());
 assertEquals("value1", result.getValue());
 assertEquals("key1", result.getKey());
 }

 @Test
 public void testParseParameterArgument_ValidLongKey() {
 args = new String[]{"--key1=value1", "--key2=value2"};
 parser = new ArgsParser(args);
 ParameterArgument result = parser.parseParameterArgument("key2");
 assertEquals(1, result.getIndex());
 assertEquals("value2", result.getValue());
 assertEquals("key2", result.getKey());
 }

 @Test
 public void testParseParameterArgument_InvalidShortKey() {
 args = new String[]{"--key1=value1", "--key2=value2"};
 parser = new ArgsParser(args);
 ParameterArgument result = parser.parseParameterArgument("key3");
 assertNull(result);
 }

 @Test
 public void testParseParameterArgument_InvalidLongKey() {
 args = new String[]{"--key1=value1", "--key2=value2"};
 parser = new ArgsParser(args);
 ParameterArgument result = parser.parseParameterArgument("key3");
 assertNull(result);
 }

 @Test
 public void testParseStringArgument() {
 args = new String[]{"--stringArg1=value1", "--stringArg2=value2"};
 parser = new ArgsParser(args);
 List<StringArgument> result = parser.parseStringArgument();
 List<StringArgument> expected = new ArrayList<>();
 expected.add(new StringArgument(0, "value1"));
 expected.add(new StringArgument(1, "value2"));
 assertEquals(expected, result);
 }
}