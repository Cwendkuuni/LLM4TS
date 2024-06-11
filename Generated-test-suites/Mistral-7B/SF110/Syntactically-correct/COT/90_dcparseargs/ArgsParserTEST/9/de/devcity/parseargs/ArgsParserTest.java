package de.devcity.parseargs.test;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import de.devcity.parseargs.ArgsParser;
import de.devcity.parseargs.arguments.*;

public class ArgsParserTest {

 private ArgsParser argsParser;

 @Before
 public void setup() {
 argsParser = new ArgsParser(new String[] {"arg1", "--key=value", "--longKey", "arg3", "-arg4=value"});
 }

 @Test
 public void testGetArgsCount() {
 assertEquals(5, argsParser.getArgsCount());
 }

 @Test
 public void testGetArgs() {
 String[] args = argsParser.getArgs();
 assertArrayEquals(new String[] {"arg1", "--key=value", "--longKey", "arg3", "-arg4=value"}, args);
 }

 @Test
 public void testSetArgs() {
 argsParser.setArgs(new String[] {"arg1", "--key=value", "--longKey", "arg3", "-arg4=value"});
 String[] args = argsParser.getArgs();
 assertArrayEquals(new String[] {"arg1", "--key=value", "--longKey", "arg3", "-arg4=value"}, args);
 }

 @Test(expected = NullPointerException.class)
 public void testSetArgsNull() {
 argsParser.setArgs(null);
 }

 @Test
 public void testGetArgsLeftCount() {
 assertEquals(2, argsParser.getArgsLeftCount());
 }

 @Test
 public void testParseSwitchArgument() {
 SwitchArgument switchArgument = argsParser.parseSwitchArgument("key");
 assertEquals("key", switchArgument.getKey());
 assertEquals(1, switchArgument.getIndex());
 assertEquals(true, switchArgument.isLong());
 argsParser.parseSwitchArgument("longKey");
 switchArgument = argsParser.parseSwitchArgument("key");
 assertEquals(-1, switchArgument.getIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseSwitchArgumentMissing() {
 argsParser.parseSwitchArgument("nonexistentKey");
 }

 @Test
 public void testParseParameterArgument() {
 ParameterArgument parameterArgument = argsParser.parseParameterArgument("key");
 assertEquals("value", parameterArgument.getValue());
 assertEquals(1, parameterArgument.getIndex());
 argsParser.parseParameterArgument("arg4");
 parameterArgument = argsParser.parseParameterArgument("key");
 assertEquals(-1, parameterArgument.getIndex());
 }

 @Test(expected = IllegalArgumentException.class)
 public void testParseParameterArgumentMissing() {
 argsParser.parseParameterArgument("nonexistentKey");
 }

 @Test
 public void testParseStringArgument() {
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(2, stringArguments.size());
 StringArgument stringArgument = stringArguments.get(0);
 assertEquals("arg1", stringArgument.getValue());
 assertEquals(0, stringArgument.getIndex());
 stringArguments = argsParser.parseStringArgument();
 assertEquals(1, stringArguments.size());
 stringArgument = stringArguments.get(0);
 assertEquals("arg3", stringArgument.getValue());
 assertEquals(2, stringArgument.getIndex());
 }

 @Test
 public void testParseStringArgumentEmpty() {
 List<StringArgument> stringArguments = argsParser.parseStringArgument();
 assertEquals(0, stringArguments.size());
 }
}