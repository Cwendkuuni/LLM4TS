package de.devcity.parseargs;

import static org.junit.Assert.*;

import org.junit.Test;

import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private String[] testArgs = new String[]{"-a", "--long-argument-key=value", "-b=value2", "--long-argument-key2=value3", "-c", "--", "arg1", "arg2"};
 private ArgsParser argsParser;

 @Test
 public void testGetArgsCount() {
 argsParser = new ArgsParser(testArgs);
 assertEquals(testArgs.length, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 argsParser = new ArgsParser(testArgs);
 assertArrayEquals(testArgs, argsParser.getArgs());
 }

 @Test
 public void testSetArgs() {
 argsParser = new ArgsParser();
 String[] newArgs = new String[]{"newArg1", "newArg2"};
 argsParser.setArgs(newArgs);
 assertEquals(newArgs.length, argsParser.getArgsCount());
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser = new ArgsParser();
 argsParser.setArgs(null);
 }

 @Test
 public void testParseSwitchArgument() {
 argsParser = new ArgsParser(testArgs);
 assertEquals(0, argsParser.parseSwitchArgument("a").getIndex());
 assertEquals("a", argsParser.parseSwitchArgument("a").getKey());
 assertEquals(true, argsParser.parseSwitchArgument("a").isLong());
 argsParser.parseSwitchArgument("a");
 assertNull(argsParser.parseSwitchArgument("a"));

 assertEquals(1, argsParser.parseSwitchArgument("long-argument-key").getIndex());
 assertEquals("long-argument-key", argsParser.parseSwitchArgument("long-argument-key").getKey());
 assertEquals(true, argsParser.parseSwitchArgument("long-argument-key").isLong());
 argsParser.parseSwitchArgument("--long-argument-key=value");
 assertNull(argsParser.parseSwitchArgument("long-argument-key"));
 }

 @Test(expected = SwitchArgument.InvalidKeyException.class)
 public void testParseSwitchArgumentInvalidKey() {
 argsParser = new ArgsParser(testArgs);
 argsParser.parseSwitchArgument("invalidKey");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseSwitchArgumentIndexOutOfBounds() {
 argsParser = new ArgsParser(testArgs);
 argsParser.parseSwitchArgument("--nonexistentKey");
 }

 @Test
 public void testParseParameterArgument() {
 argsParser = new ArgsParser(testArgs);
 assertEquals(1, argsParser.parseParameterArgument("b").getIndex());
 assertEquals("b", argsParser.parseParameterArgument("b").getKey());
 assertEquals("value2", argsParser.parseParameterArgument("b").getValue());
 argsParser.parseParameterArgument("b");
 assertNull(argsParser.parseParameterArgument("b"));

 assertEquals(3, argsParser.parseParameterArgument("long-argument-key2").getIndex());
 assertEquals("long-argument-key2", argsParser.parseParameterArgument("long-argument-key2").getKey());
 assertEquals("value3", argsParser.parseParameterArgument("long-argument-key2").getValue());
 argsParser.parseParameterArgument("--long-argument-key2=value3");
 assertNull(argsParser.parseParameterArgument("long-argument-key2"));
 }

 @Test(expected = ParameterArgument.InvalidKeyException.class)
 public void testParseParameterArgumentInvalidKey() {
 argsParser = new ArgsParser(testArgs);
 argsParser.parseParameterArgument("invalidKey");
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testParseParameterArgumentIndexOutOfBounds() {
 argsParser = new ArgsParser(testArgs);
 argsParser.parseParameterArgument("--nonexistentKey=value");
 }

 @Test
 public void testParseStringArgument() {
 argsParser = new ArgsParser(testArgs);
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(2, stringArguments.size());
 assertEquals("arg1", stringArguments.get(0).getKey());
 assertEquals("arg2", stringArguments.get(1).getKey());
 }

 @Test
 public void testParseStringArgumentEmpty() {
 argsParser = new ArgsParser(new String[]{"--"});
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertTrue(stringArguments.isEmpty());
 }
}